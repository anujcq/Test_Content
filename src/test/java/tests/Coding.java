package tests;

import java.time.Duration;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Coding {
    private WebDriver driver;
    private XSSFSheet sheet;
    private int rowNum;

    public Coding(WebDriver driver, XSSFSheet sheet,int rowNum){
        this.driver = driver;
        this.sheet = sheet;
        this.rowNum = rowNum;
    }
    public void codingSolve() throws Exception{
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String url=driver.getCurrentUrl();
        String QuestID = url.substring(url.lastIndexOf("/") + 1);

        String qName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='txtQuesTitle']"))).getAttribute("value");
        String qScore = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='score']"))).getAttribute("value");
        String qKeywords="";
        String qDescription = "";
        String allowedLanguages ="";

        try{
            List<WebElement> tags = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='ant-select-selection-overflow']//span/span[@class='ant-tag ant-tag-default css-f9u17k']")));
                //div[@class='ant-select-selection-overflow']//span/span[@class='ant-tag ant-tag-default css-dev-only-do-not-override-1ykofdx'] testing side
            
            for (int i = 0; i < tags.size(); i++) {
                if (i == tags.size() - 1) {
                    qKeywords += tags.get(i).getText(); // last item → no comma
                } else {
                    qKeywords += tags.get(i).getText() + ", ";
                }
            }
            
        }catch(Exception e){
            System.out.println("No keywords");
        }

        try{
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@id='txtQues']//div[@class='ql-editor']/*[self::p or self::pre]")));
                List<WebElement> descElements = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@id='txtQues']//div[@class='ql-editor']/*[self::p or self::pre]")));
    
                StringBuilder qDescriptionBuilder = new StringBuilder();
                for (WebElement el : descElements) {
                    qDescriptionBuilder.append(el.getText()).append("\n");
                }
                qDescription = qDescriptionBuilder.toString().trim();
        }catch(Exception e){
            System.out.println("No description");
        }

        List<WebElement> allLanguages = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("(//div[@class=\"ant-tabs-nav-list\"])[2]/div[@data-node-key]/div")));

        for (int i = 0; i < allLanguages.size(); i++) {
                if (i == allLanguages.size() - 1) {
                    allowedLanguages += allLanguages.get(i).getText(); // last item → no comma
                } else {
                    allowedLanguages += allLanguages.get(i).getText() + ", ";
                }
            }
        
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue("https://assess.testpad.chitkara.edu.in/questions/preview/" + QuestID);
        row.createCell(1).setCellValue(QuestID);
        row.createCell(2).setCellValue("Coding");
        row.createCell(3).setCellValue(qName);
        row.createCell(4).setCellValue(qDescription);
        row.createCell(5).setCellValue(qScore);
        row.createCell(6).setCellValue(qKeywords);
        row.createCell(14).setCellValue(allowedLanguages);

        driver.switchTo().defaultContent();
        driver.close();
    }
}
