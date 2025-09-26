package config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader{
    private static Properties properties = new Properties();

    public static void loadProperties(){
        try{
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
            properties.load(fileInputStream);

        }catch(IOException e){
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }
    public static String get(String key){
        return properties.getProperty(key);
    }

}