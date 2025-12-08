package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.OcrVoteData;
import com.ruoyi.system.domain.vo.OwnerInfo;
import com.ruoyi.system.domain.vo.VoteItem;
import com.ruoyi.system.service.IOcrVoteProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * OCR投票数据处理服务实现类
 * 
 * @author ruoyi
 */
@Service
public class OcrVoteProcessorServiceImpl implements IOcrVoteProcessorService {
    
    private static final Logger log = LoggerFactory.getLogger(OcrVoteProcessorServiceImpl.class);
    
    // 同意选项识别模式
    private static final Set<String> AGREE_PATTERNS = new HashSet<>(Arrays.asList("✔", "√", "check", "勾", "对", "是", "同意", "v", "A"));
    
    // 反对选项识别模式
    private static final Set<String> OPPOSE_PATTERNS = new HashSet<>(Arrays.asList("✗", "×", "x", "打叉", "叉", "错", "否", "反对", "B"));
    
    // 弃权选项识别模式
    private static final Set<String> ABSTAIN_PATTERNS = new HashSet<>(Arrays.asList("o", "0", "口", "圆", "空", "弃权", "○", "空白", "C"));
    
    // 从多选项识别模式
    private static final Set<String> FOLLOW_MAJORITY_PATTERNS = new HashSet<>(Arrays.asList("D", "从多", "随大流", "随众"));
    
    @Override
    public OcrVoteData processOcrResponse(String ocrResponse) {
        try {

            JSONObject response = JSON.parseObject(ocrResponse);
            JSONArray tableDetections = response.getJSONArray("TableDetections");

            if (tableDetections == null || tableDetections.size() < 6) {
                log.warn("OCR响应中TableDetections数量不足，期望至少6个表格");
                return createFailedResult("OCR识别结果格式不正确");
            }

            // 提取业主基础信息（通常在第3个表格，索引为2）
            OwnerInfo ownerInfo = extractOwnerInfo(tableDetections.getJSONObject(2));
            
            // 提取表决事项信息，优先从索引3提取，如果为空则尝试索引5
            List<VoteItem> voteItems = extractVoteItems(tableDetections.getJSONObject(3));
            if (voteItems.isEmpty() && tableDetections.size() > 5) { // 检查索引5是否存在
                voteItems = extractVoteItems(tableDetections.getJSONObject(5));
            }
            
            return new OcrVoteData(ownerInfo, voteItems);
            
        } catch (Exception e) {
            log.error("处理OCR响应失败", e);
            return createFailedResult("OCR数据解析失败：" + e.getMessage());
        }
    }
    
    /**
     * 从表格单元格中提取业主信息
     */
    private OwnerInfo extractOwnerInfo(JSONObject ownerTable) {
        JSONArray cells = ownerTable.getJSONArray("Cells");
        List<CellInfo> sortedCells = sortCellsByPosition(cells);
        
        // 使用更灵活的方式提取信息
        OwnerInfo.Builder builder = OwnerInfo.builder();
        
        String ownerName = findValueForLabel(sortedCells, "业主姓名", "姓名");
        builder.ownerName(ownerName);
        
        String buildingAreaStr = findValueForLabel(sortedCells, "专有部分建筑面积", "建筑面积");
        builder.buildingArea(parseDouble(buildingAreaStr));
        
        String roomNumber = findValueForLabel(sortedCells, "房号");
        builder.roomNumber(roomNumber);
        
        String phoneNumber = findValueForLabel(sortedCells, "电话");
        builder.phoneNumber(cleanPhoneNumber(phoneNumber));
        
        String agentName = findValueForLabel(sortedCells, "代理人姓名", "代理人");
        builder.agentName(agentName);
        
        String agentPhone = findValueForLabel(sortedCells, "代理人联系电话", "代理人电话");
        builder.agentPhone(cleanPhoneNumber(agentPhone));
        
        return builder.build();
    }
    
    /**
     * 在排序后的单元格列表中查找特定标签的值
     */
    private String findValueForLabel(List<CellInfo> cells, String... labelKeywords) {
        for (int i = 0; i < cells.size(); i++) {
            String cleanLabel = cleanText(cells.get(i).getText());
            for (String keyword : labelKeywords) {
                if (cleanLabel.contains(keyword)) {
                    // 假设值在标签的右侧或下方，且在同一行或紧邻的下一行
                    // 这里简化为查找同一行右侧的第一个非空单元格
                    for (int j = i + 1; j < cells.size(); j++) {
                        // 检查是否在同一行（或非常接近的行）
                        if (Math.abs(cells.get(i).getRowTl() - cells.get(j).getRowTl()) < 10) {
                            String value = cleanText(cells.get(j).getText());
                            if (!StringUtils.isEmpty(value)) {
                                return value;
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
    
    /**
     * 从表格中提取投票信息
     */
    private List<VoteItem> extractVoteItems(JSONObject voteTable) {
        JSONArray cells = voteTable.getJSONArray("Cells");
        List<CellInfo> sortedCells = sortCellsByPosition(cells);
        List<VoteItem> voteItems = new ArrayList<>();
        
        // 假设前几个单元格是标题，实际投票项从某个位置开始
        // 这里需要更智能地识别投票项的起始位置
        // 简单起见，我们假设第一个文本内容为空的单元格，或者行号显著增加的单元格，标志着新的一行的开始
        
        // 通常第一行是表头，第二行开始是具体事项。但为了鲁棒性，我们按行处理。
        // 将单元格按行分组
        Map<Integer, List<CellInfo>> rows = new TreeMap<>();
        for (CellInfo cell : sortedCells) {
            // 使用单元格的Y轴起始坐标作为行标识，但考虑到OCR可能不精确，可以对Y轴进行分组
            int rowKey = (cell.getRowTl() / 10) * 10; // 粗略分组到10像素的行区间
            rows.computeIfAbsent(rowKey, k -> new ArrayList<>()).add(cell);
        }
        
        boolean firstRowSkipped = false; // 用于跳过表头
        for (Map.Entry<Integer, List<CellInfo>> entry : rows.entrySet()) {
            List<CellInfo> rowCells = entry.getValue();
            if (rowCells.isEmpty()) continue;
            
            // 对行内单元格按X轴排序
            rowCells.sort(Comparator.comparing(CellInfo::getColTl));
            
            if (!firstRowSkipped) { // 跳过第一个被识别为“行”的表头
                firstRowSkipped = true;
                continue;
            }
            
            String topicTitle = "";
            String voteSymbol = "";
            
            // 尝试在这一行中找到主题和投票符号
            if (rowCells.size() >= 2) {
                topicTitle = cleanText(rowCells.get(0).getText()); // 假设第一个是主题
                voteSymbol = cleanText(rowCells.get(1).getText()); // 假设第二个是投票符号
            } else if (rowCells.size() == 1) {
                topicTitle = cleanText(rowCells.get(0).getText());
            }
            
            if (!StringUtils.isEmpty(topicTitle)) { // 避免添加空主题
                // 智能识别投票选项
                VoteItem.VoteOption option = recognizeVoteOption(voteSymbol);
                
                voteItems.add(VoteItem.builder()
                        .topicTitle(topicTitle)
                        .originalSymbol(voteSymbol)
                        .voteOption(option)
                        .confidence(calculateConfidence(voteSymbol, option))
                        .build());
            }
        }
        
        return voteItems;
    }
    
    /**
     * 智能识别投票选项，处理OCR识别错误
     */
    private VoteItem.VoteOption recognizeVoteOption(String symbol) {
        if (StringUtils.isEmpty(symbol)) {
            return VoteItem.VoteOption.ABSTAIN; // 空白默认弃权
        }
        
        String cleanSymbol = symbol.trim().toLowerCase();
        
        // 同意选项识别
        if (isPatternMatch(cleanSymbol, AGREE_PATTERNS)) {
            return VoteItem.VoteOption.AGREE;
        }
        
        // 反对选项识别
        if (isPatternMatch(cleanSymbol, OPPOSE_PATTERNS)) {
            return VoteItem.VoteOption.OPPOSE;
        }
        
        // 弃权选项识别模式
        if (isPatternMatch(cleanSymbol, ABSTAIN_PATTERNS)) {
            return VoteItem.VoteOption.ABSTAIN;
        }
        
        // 从多选项识别
        if (isPatternMatch(cleanSymbol, FOLLOW_MAJORITY_PATTERNS)) {
            return VoteItem.VoteOption.FOLLOW_MAJORITY;
        }
        
        // 无法识别默认弃权
        return VoteItem.VoteOption.ABSTAIN;
    }
    
    /**
     * 模式匹配检查
     */
    private boolean isPatternMatch(String symbol, Set<String> patterns) {
        return patterns.stream().anyMatch(pattern -> 
            symbol.contains(pattern) || levenshteinDistance(symbol, pattern) <= 1);
    }
    
    /**
     * 计算识别置信度
     */
    private Double calculateConfidence(String symbol, VoteItem.VoteOption option) {
        if (StringUtils.isEmpty(symbol)) {
            return 0.5; // 空白符号置信度为50%
        }
        
        String cleanSymbol = symbol.trim().toLowerCase();
        Set<String> patterns;
        
        switch (option) {
            case AGREE:
                patterns = AGREE_PATTERNS;
                break;
            case OPPOSE:
                patterns = OPPOSE_PATTERNS;
                break;
            case ABSTAIN:
                patterns = ABSTAIN_PATTERNS;
                break;
            case FOLLOW_MAJORITY:
                patterns = FOLLOW_MAJORITY_PATTERNS;
                break;
            default:
                return 0.0;
        }
        
        // 精确匹配置信度最高
        if (patterns.contains(cleanSymbol)) {
            return 1.0;
        }
        
        // 模糊匹配置信度中等
        boolean fuzzyMatch = patterns.stream()
                .anyMatch(pattern -> levenshteinDistance(cleanSymbol, pattern) <= 1);
        if (fuzzyMatch) {
            return 0.8;
        }
        
        // 默认弃权置信度较低
        return 0.3;
    }
    
    /**
     * 按照表格位置排序单元格
     */
    private List<CellInfo> sortCellsByPosition(JSONArray cells) {
        List<CellInfo> cellList = new ArrayList<>();
        
        for (int i = 0; i < cells.size(); i++) {
            JSONObject cell = cells.getJSONObject(i);
            cellList.add(new CellInfo(
                cell.getString("Text"),
                cell.getInteger("RowTl"),
                cell.getInteger("RowBr"),
                cell.getInteger("ColTl"),
                cell.getInteger("ColBr")
            ));
        }
        
        // 按位置排序：先行后列
        cellList.sort((a, b) -> {
            // 首先比较行的起始位置，如果相近则认为是同一行
            if (Math.abs(a.getRowTl() - b.getRowTl()) < 10) { 
                return Integer.compare(a.getColTl(), b.getColTl());
            }
            return Integer.compare(a.getRowTl(), b.getRowTl());
        });
        
        return cellList;
    }
    
    /**
     * 清理文本数据
     */
    private String cleanText(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        
        return text.trim()
                .replaceAll("\\s+", " ")  // 多个空白字符合并为一个空格
                .replaceAll("[\\r\\n]", "") // 移除换行符
                .replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9\\(\\)（）\\-_√×✔✗OoXx]", ""); // 保留中文、英文、数字和特定符号
    }
    
    /**
     * 清理电话号码
     */
    private String cleanPhoneNumber(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "";
        }
        
        // 只保留数字
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        
        // 验证手机号格式
        if (cleanPhone.length() == 11 && cleanPhone.startsWith("1")) {
            return cleanPhone;
        }
        
        return phone; // 返回原始数据供人工确认
    }
    
    /**
     * 解析Double值
     */
    private Double parseDouble(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        
        try {
            // 提取数字部分
            String numStr = value.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numStr);
        } catch (NumberFormatException e) {
            log.warn("无法解析数字：{}", value);
            return null;
        }
    }
    
    /**
     * 计算编辑距离
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * 创建失败结果
     */
    private OcrVoteData createFailedResult(String message) {
        OcrVoteData result = new OcrVoteData();
        result.setStatus(OcrVoteData.ProcessStatus.FAILED);
        result.setWarnings(Arrays.asList(message));
        return result;
    }
    
    /**
     * 单元格信息内部类
     */
    private static class CellInfo {
        private String text;
        private Integer rowTl;
        private Integer rowBr;
        private Integer colTl;
        private Integer colBr;
        
        public CellInfo(String text, Integer rowTl, Integer rowBr, Integer colTl, Integer colBr) {
            this.text = text;
            this.rowTl = rowTl;
            this.rowBr = rowBr;
            this.colTl = colTl;
            this.colBr = colBr;
        }
        
        // Getters
        public String getText() { return text; }
        public Integer getRowTl() { return rowTl; }
        public Integer getRowBr() { return rowBr; }
        public Integer getColTl() { return colTl; }
        public Integer getColBr() { return colBr; }
    }
}