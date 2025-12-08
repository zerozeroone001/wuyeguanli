package com.ruoyi.system.utils;

import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class Utils {
    public static String createOwnerNo(){
        return RandomStringUtils.randomAlphanumeric(10);

    }
}
