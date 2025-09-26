package tests;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;

public class TestExecutor {
    private WebDriver driver;
    private List<String> urls;
   
    public TestExecutor(WebDriver driver, List<String> urls) {
        this.driver=driver;
        this.urls = urls;
    }

    public void executeTests() throws Exception{
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        //workbook creation
        XSSFWorkbook workbook = new XSSFWorkbook();
        int i=1;
        for(String url:urls){
            try{

                driver.navigate().to(url);
               
                // String sheetName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2"))).getText();
                String sheetName = "Sheet" + i++;

                XSSFSheet sheet = workbook.createSheet(sheetName);

                SectionProcessor sectionProcessor = new SectionProcessor(driver, sheet, url);
                sectionProcessor.processSections();
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Issue in Test " + url + "\n");
                
            }
        }
        
        FileOutputStream fos = new FileOutputStream("TestContentReport.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
