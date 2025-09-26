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

public class MCQ {
    private WebDriver driver;
    private XSSFSheet sheet;
    private int rowNum;

    public MCQ(WebDriver driver , XSSFSheet sheet,int rowNum){
        this.driver = driver;
        this.sheet = sheet;
        this.rowNum = rowNum;
    }
    public void mcqSolve(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        Row row = sheet.createRow(rowNum);

        String url=driver.getCurrentUrl();
        String QuestID = url.substring(url.lastIndexOf("/") + 1);

        String qName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='txtQuesTitle']"))).getAttribute("value");
        String qScore = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='score']"))).getAttribute("value");
        String qKeywords="";
        String qDescription = "";

        try{
            List<WebElement> tags = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='ant-select-selection-overflow']//span/span[@class='ant-tag ant-tag-default css-f9u17k']")));
            
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

        List<WebElement> allOptionElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//textarea[@rows='2']")));
     
        int i=7;
        for (WebElement option : allOptionElements) {
            String lang ="";
            lang = option.getText().trim();
            
            if(!lang.isEmpty()){
                row.createCell(i).setCellValue(lang);
            }
            i++;
        }

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));

        int optionNumber = -1;
        for (int j = 0; j < checkboxes.size(); j++) {
            if (checkboxes.get(j).isSelected()) {
                optionNumber = j + 1;  // because list is 0-based, but options start at 1
                break;
            }
        }
        
        row.createCell(0).setCellValue("https://assess.testpad.chitkara.edu.in/questions/preview/" + QuestID);
        row.createCell(1).setCellValue(QuestID);
        row.createCell(2).setCellValue("MCQ");
        row.createCell(3).setCellValue(qName);
        row.createCell(4).setCellValue(qDescription);
        row.createCell(5).setCellValue(qScore);
        row.createCell(6).setCellValue(qKeywords);
        row.createCell(13).setCellValue(optionNumber);
    

        driver.switchTo().defaultContent();
        driver.close();
    }
}
