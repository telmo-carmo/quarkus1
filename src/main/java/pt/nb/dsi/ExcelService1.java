package pt.nb.dsi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExcelService1 {
    private static final Logger logger = Logger.getLogger(ExcelService1.class);

    Object[][] sampleContent = { { "LastName", "FirstName", "BusinessPhone", "City", "ZipPostal" },
            { "Bedecs", "Anna", "(123)555-0100", "Seattle", 98052 },
            { "Gratacos Solsona", "Antonio", "(123)555-0100", "Boston", 98112 },
            { "Axen", "Thomas", "(123)555-0100", "Los Angeles", 98052 },
            { "Lee", "Christina", "(123)555-0100", "New York", 98052 },
            { "O’Donnell", "Martin", "(123)555-0100", "Minneapolis", 98012 } };


    private void generateContent(XSSFWorkbook workbook ,String sheetName) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        logger.info("Generating Excel content");
        //DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
         

        for (Object[] content : sampleContent) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : content) {
                Cell cell = row.createCell(colNum++);
                //sheet.autoSizeColumn(colNum);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
                cell.setCellStyle(style);
            }
        }
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            generateContent(workbook,"Persons");
            workbook.write(bos);
            bos.close();
            workbook.close();
            logger.warn("Retrieving Excel as a Byte array");
        } catch (IOException e) {
            logger.error(e);
        }
        return bos.toByteArray();
    }
}
