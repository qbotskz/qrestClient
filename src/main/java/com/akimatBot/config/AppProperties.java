package com.akimatBot.config;

import com.akimatBot.entity.custom.Food;
import lombok.Getter;

import java.io.InputStream;
import java.util.Properties;

@Getter
public class AppProperties {
    public static final Properties properties;

    static {
        properties = new Properties();

        try {
            ClassLoader classLoader = AppProperties.class.getClassLoader();
            InputStream applicationPropertiesStream = classLoader.getResourceAsStream("application.properties");
            properties.load(applicationPropertiesStream);
        } catch (Exception e) {
            // process the exception
        }
    }
}
