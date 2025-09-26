package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class ExcelReader {
    
    public static List<String> getUrlsFromExcel(String filePath) throws IOException {
        List<String> urls = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0);  //link should be in first column only 
                
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String url = cell.getStringCellValue().trim();
                    if (!url.isEmpty()) {
                        urls.add(url);
                    }
                }
            }
        }

        return urls;
    }
}
