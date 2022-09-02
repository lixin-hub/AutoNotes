package com.bully.myplugin;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    static Properties properties = null;

    // 创建并加载配置文件
    public static void loadProperties() {
        properties = new Properties();
        InputStream in = Util.class.getClassLoader().getResourceAsStream("settings.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSetting(String key) {
        if (properties == null) {
            loadProperties();
        }
        if (properties == null) {
            return null;
        }
        return properties.getProperty(key, "-1");
    }


    public static String getStringSetting(String key) {
        return getSetting(key);
    }

    public static Integer getIntegerSetting(String key) {
        return Integer.parseInt(Objects.requireNonNull(getSetting(key)));
    }

    public static Boolean geBooleanSetting(String key) {
        return Boolean.parseBoolean(getSetting(key));
    }

    public static boolean isContainChinese(String str)  {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\、|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    @Test
    public void test() {
        System.out.println(isContainChinese("sdrg、"+""));
    }
}
