package tests;

import config.ConfigReader;

import utils.ExcelReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

public class Login {
    
    public static void main(String args[]){

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        ConfigReader.loadProperties();
        String baseURL =  ConfigReader.get("baseUrl");
        String userName = ConfigReader.get("username");
        String password = ConfigReader.get("password");
        String filePath = ConfigReader.get("excelPath");
        
        try{
            driver.get(baseURL);
            // driver.manage().window().maximize();

            driver.switchTo().frame("loginIframe");
            driver.findElement(By.xpath("//input[@id='email']")).sendKeys(userName);
            driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
            driver.findElement(By.xpath("//button[@id='submit']")).click();
            
            List<String> urls=ExcelReader.getUrlsFromExcel(filePath);
            urls.remove(0);
            // System.out.println(urls);

            TestExecutor testExecutor = new TestExecutor(driver , urls);
            testExecutor.executeTests();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            // driver.quit();   // close all browser window 
        }

    }
}