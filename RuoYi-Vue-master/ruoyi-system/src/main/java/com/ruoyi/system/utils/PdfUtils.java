package com.ruoyi.system.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.ruoyi.system.domain.vo.MeetingVoteReportVO;
import com.ruoyi.system.domain.vo.VoteResultVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * PDF生成工具类
 * 使用iText 5.5.13.3生成专业的PDF报告
 *
 * @author ruoyi
 * @date 2025-11-27
 */
public class PdfUtils {

    private static final String FONT_PATH = "STSong-Light";
    private static final String FONT_ENCODING = "UniGB-UCS2-H";

    /**
     * 生成投票统计报告PDF
     *
     * @param reportData 会议报告数据
     * @return PDF字节数组
     * @throws DocumentException
     * @throws IOException
     */
    public static byte[] generateVoteReportPdf(MeetingVoteReportVO reportData)
            throws DocumentException, IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            // 添加页眉页脚
            writer.setPageEvent(new PdfPageHelper(reportData.getMeetingTitle()));

            document.open();

            // 创建中文字体
            BaseFont bfChinese = BaseFont.createFont(FONT_PATH, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(bfChinese, 18, Font.BOLD);
            Font headingFont = new Font(bfChinese, 14, Font.BOLD);
            Font normalFont = new Font(bfChinese, 12, Font.NORMAL);
            Font smallFont = new Font(bfChinese, 10, Font.NORMAL);

            // 1. 标题
            Paragraph title = new Paragraph(reportData.getMeetingTitle() + " - 表决结果报告", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // 2. 会议基本信息
            document.add(createMeetingInfoTable(reportData, headingFont, normalFont));
            document.add(new Paragraph("\n"));

            // 3. 整体参与率统计
            document.add(createParticipationSummary(reportData, headingFont, normalFont));
            document.add(new Paragraph("\n"));

            // 4. 表决结果统计
            Paragraph resultTitle = new Paragraph("表决结果统计", headingFont);
            resultTitle.setSpacingBefore(10);
            resultTitle.setSpacingAfter(10);
            document.add(resultTitle);

            // 5. 遍历每个议题
            List<VoteResultVO> voteResults = reportData.getTopicResults();
            for (int i = 0; i < voteResults.size(); i++) {
                VoteResultVO result = voteResults.get(i);

                // 议题标题
                Paragraph topicTitle = new Paragraph("议题 " + (i + 1) + ": " + result.getTopicTitle(), headingFont);
                topicTitle.setSpacingBefore(15);
                topicTitle.setSpacingAfter(10);
                document.add(topicTitle);

                // 议题统计表格
                document.add(createVoteResultTable(result, normalFont, smallFont));

                // 表决结论
                String conclusion = result.getIsPassed() ? "通过" : "未通过";
                Font conclusionFont = new Font(bfChinese, 14, Font.BOLD);
                conclusionFont.setColor(result.getIsPassed() ? BaseColor.GREEN : BaseColor.RED);
                Paragraph conclusionPara = new Paragraph("表决结论: " + conclusion, conclusionFont);
                conclusionPara.setSpacingBefore(10);
                conclusionPara.setSpacingAfter(15);
                document.add(conclusionPara);

                // 添加分隔线
                if (i < voteResults.size() - 1) {
                    LineSeparator line = new LineSeparator();
                    line.setLineColor(BaseColor.LIGHT_GRAY);
                    document.add(new Chunk(line));
                }
            }

            // 5. 报告说明
            document.add(new Paragraph("\n"));
            Paragraph note = new Paragraph("说明: ", headingFont);
            note.setSpacingBefore(20);
            document.add(note);

            Paragraph noteContent = new Paragraph(
                    "1. 表决通过标准: 同意人数占总人数的比例 > 50% 且 同意面积占总面积的比例 > 50%\n" +
                            "2. 参与率 = 实际参与人数(面积) / 总人数(总面积) × 100%\n" +
                            "3. 本报告由系统自动生成，数据统计截止至报告生成时间。",
                    smallFont
            );
            noteContent.setSpacingAfter(10);
            document.add(noteContent);

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return baos.toByteArray();
    }

    /**
     * 创建会议信息表格
     */
    private static PdfPTable createMeetingInfoTable(MeetingVoteReportVO reportData, Font headingFont, Font normalFont)
            throws DocumentException {

        Paragraph heading = new Paragraph("会议基本信息", headingFont);
        heading.setSpacingBefore(10);
        heading.setSpacingAfter(10);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setWidths(new int[]{1, 3});

        // 设置表格样式
        PdfPCell cell;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // 小区名称
        cell = new PdfPCell(new Phrase("小区名称", normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
        table.addCell(new Phrase(reportData.getCommunityName() != null ? reportData.getCommunityName() : "-", normalFont));

        // 会议标题
        cell = new PdfPCell(new Phrase("会议标题", normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
        table.addCell(new Phrase(reportData.getMeetingTitle(), normalFont));

        // 会议时间
        cell = new PdfPCell(new Phrase("会议时间", normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
        String meetingTime = reportData.getMeetingTime() != null ? sdf.format(reportData.getMeetingTime()) : "-";
        table.addCell(new Phrase(meetingTime, normalFont));

        // 会议地点
        cell = new PdfPCell(new Phrase("会议地点", normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
        table.addCell(new Phrase(reportData.getMeetingLocation() != null ? reportData.getMeetingLocation() : "-", normalFont));

        // 投票时间段
        cell = new PdfPCell(new Phrase("投票时间段", normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
        String voteStartTime = reportData.getVoteStartTime() != null ? sdf.format(reportData.getVoteStartTime()) : "-";
        String voteEndTime = reportData.getVoteEndTime() != null ? sdf.format(reportData.getVoteEndTime()) : "-";
        table.addCell(new Phrase(voteStartTime + " 至 " + voteEndTime, normalFont));

        return table;
    }

    /**
     * 创建整体参与率统计表格
     */
    private static PdfPTable createParticipationSummary(MeetingVoteReportVO reportData, Font headingFont, Font normalFont)
            throws DocumentException {

        Paragraph heading = new Paragraph("整体参与率统计", headingFont);
        heading.setSpacingBefore(10);
        heading.setSpacingAfter(10);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setWidths(new int[]{2, 2, 2, 2});

        // 表头
        String[] headers = {"统计项", "人数", "面积(㎡)", "参与率"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, normalFont));
            cell.setBackgroundColor(new BaseColor(70, 130, 180));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            Font whiteFont = new Font(normalFont);
            whiteFont.setColor(BaseColor.WHITE);
            cell.setPhrase(new Phrase(header, whiteFont));
            table.addCell(cell);
        }

        // 总数统计
        addParticipationRow(table, "应参与总数",
                String.valueOf(reportData.getTotalOwners()),
                String.format("%.2f", reportData.getTotalArea()),
                "-", normalFont);

        // 实际参与
        addParticipationRow(table, "实际参与(人数)",
                String.valueOf(reportData.getParticipatedOwners()),
                String.format("%.2f", reportData.getParticipatedArea()),
                String.format("%.2f%%", reportData.getOwnerParticipationRate()), normalFont);

        // 面积参与率
        addParticipationRow(table, "实际参与(面积)",
                "-",
                "-",
                String.format("%.2f%%", reportData.getAreaParticipationRate()), normalFont);

        return table;
    }

    /**
     * 添加参与率表格行
     */
    private static void addParticipationRow(PdfPTable table, String label, String people, String area,
                                             String rate, Font font) {
        PdfPCell cell;

        cell = new PdfPCell(new Phrase(label, font));
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(people, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(area, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(rate, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    /**
     * 创建表决结果表格
     */
    private static PdfPTable createVoteResultTable(VoteResultVO result, Font normalFont, Font smallFont)
            throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);
        table.setSpacingAfter(5);
        table.setWidths(new int[]{2, 1, 2, 2, 2});

        // 表头
        String[] headers = {"统计项", "人数", "面积(㎡)", "人数占比", "面积占比"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, normalFont));
            cell.setBackgroundColor(new BaseColor(70, 130, 180));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            Font whiteFont = new Font(normalFont);
            whiteFont.setColor(BaseColor.WHITE);
            cell.setPhrase(new Phrase(header, whiteFont));
            table.addCell(cell);
        }

        // 总数统计
        addTableRow(table, "总人数/总面积",
                String.valueOf(result.getTotalPeople()),
                String.format("%.2f", result.getTotalArea()),
                "-", "-", normalFont);

        // 参与统计
        double peopleRate = result.getPeopleParticipationRate() != null ?
                result.getPeopleParticipationRate().doubleValue() : 0.0;
        double areaRate = result.getAreaParticipationRate() != null ?
                result.getAreaParticipationRate().doubleValue() : 0.0;

        addTableRow(table, "参与",
                String.valueOf(result.getActualPeople()),
                String.format("%.2f", result.getActualArea()),
                String.format("%.2f%%", peopleRate),
                String.format("%.2f%%", areaRate), normalFont);

        // 同意票
        double agreePeopleRate = calculateRate(result.getAgreePeople(), result.getActualPeople());
        double agreeAreaRate = calculateRate(result.getAgreeArea().doubleValue(), result.getActualArea().doubleValue());

        addTableRow(table, "同意",
                String.valueOf(result.getAgreePeople()),
                String.format("%.2f", result.getAgreeArea()),
                String.format("%.2f%%", agreePeopleRate),
                String.format("%.2f%%", agreeAreaRate), normalFont);

        // 反对票
        double opposePeopleRate = calculateRate(result.getOpposePeople(), result.getActualPeople());
        double opposeAreaRate = calculateRate(result.getOpposeArea().doubleValue(), result.getActualArea().doubleValue());

        addTableRow(table, "反对",
                String.valueOf(result.getOpposePeople()),
                String.format("%.2f", result.getOpposeArea()),
                String.format("%.2f%%", opposePeopleRate),
                String.format("%.2f%%", opposeAreaRate), normalFont);

        // 弃权票
        double abstainPeopleRate = calculateRate(result.getAbstainPeople(), result.getActualPeople());
        double abstainAreaRate = calculateRate(result.getAbstainArea().doubleValue(), result.getActualArea().doubleValue());

        addTableRow(table, "弃权",
                String.valueOf(result.getAbstainPeople()),
                String.format("%.2f", result.getAbstainArea()),
                String.format("%.2f%%", abstainPeopleRate),
                String.format("%.2f%%", abstainAreaRate), normalFont);

        // 未投票
        double notVotedPeopleRate = calculateRate(result.getNotVotedPeople(), result.getTotalPeople());
        double notVotedAreaRate = calculateRate(result.getNotVotedArea().doubleValue(), result.getTotalArea().doubleValue());

        addTableRow(table, "未投票",
                String.valueOf(result.getNotVotedPeople()),
                String.format("%.2f", result.getNotVotedArea()),
                String.format("%.2f%%", notVotedPeopleRate),
                String.format("%.2f%%", notVotedAreaRate), normalFont);

        return table;
    }

    /**
     * 添加表格行
     */
    private static void addTableRow(PdfPTable table, String label, String people, String area,
                                     String peopleRate, String areaRate, Font font) {
        PdfPCell cell;

        cell = new PdfPCell(new Phrase(label, font));
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(people, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(area, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(peopleRate, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(areaRate, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    /**
     * 计算百分比
     */
    private static double calculateRate(double part, double total) {
        if (total == 0) {
            return 0.0;
        }
        return (part / total) * 100.0;
    }

    /**
     * PDF页面事件处理器 - 添加页眉页脚
     */
    static class PdfPageHelper extends PdfPageEventHelper {
        private String title;

        public PdfPageHelper(String title) {
            this.title = title;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                BaseFont bfChinese = BaseFont.createFont(FONT_PATH, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
                Font footerFont = new Font(bfChinese, 8, Font.NORMAL, BaseColor.GRAY);

                // 页脚 - 页码
                PdfPTable footer = new PdfPTable(3);
                footer.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
                footer.setWidths(new int[]{1, 1, 1});

                // 左侧 - 生成时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                PdfPCell leftCell = new PdfPCell(new Phrase("生成时间: " + sdf.format(new java.util.Date()), footerFont));
                leftCell.setBorder(Rectangle.NO_BORDER);
                leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                footer.addCell(leftCell);

                // 中间 - 空白
                PdfPCell centerCell = new PdfPCell(new Phrase("", footerFont));
                centerCell.setBorder(Rectangle.NO_BORDER);
                footer.addCell(centerCell);

                // 右侧 - 页码
                PdfPCell rightCell = new PdfPCell(new Phrase("第 " + writer.getPageNumber() + " 页", footerFont));
                rightCell.setBorder(Rectangle.NO_BORDER);
                rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                footer.addCell(rightCell);

                footer.writeSelectedRows(0, -1,
                        document.leftMargin(),
                        document.bottomMargin() - 10,
                        writer.getDirectContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
