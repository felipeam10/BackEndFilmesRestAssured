package support.utils;

import io.restassured.RestAssured;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseUrlSetup {

    private static Properties properties = new Properties();

    public static void loadProperties(String propertiesFileName) {
        try (InputStream input = BaseUrlSetup.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + propertiesFileName);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration", ex);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String setupBaseUrl(String propertiesFileName, String key) {
        loadProperties(propertiesFileName);
        String baseUrl = properties.getProperty(key);
        if (baseUrl != null) {
            return baseUrl;
        } else {
            throw new RuntimeException(key + " not found in the properties file");
        }
    }
}