package framework.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {

    public static String getRandomAlphanumericStringWithGap(int min, int max){
        return RandomStringUtils.randomAlphanumeric(min, max);
    }

    public static String getRandomAlphanumericString(int count){
        return RandomStringUtils.randomAlphanumeric(count);
    }

    public static String getRandomAlphabeticString(int count){
        return RandomStringUtils.randomAlphabetic(count);
    }

}
