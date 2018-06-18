package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import exceptions.ExportException;
import models.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import play.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class ExportService {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private final static Logger.ALogger logger = Logger.of("exports-service");

    public static File generateFile(ArrayNode reports, User user) throws ExportException {
        String[] columns = user.getReportColumns();
        Workbook workbook = new XSSFWorkbook();
        // Create a Sheet
        Sheet sheet = workbook.createSheet("Report");
        //Header font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.SKY_BLUE.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create Header Row
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(user.getHeaderForReport());
        titleCell.setCellStyle(headerCellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0,1,0,columns.length));

        Row headersRow = sheet.createRow(2);

        CellStyle columnsCellStyle = workbook.createCellStyle();
        Font columnsFont = workbook.createFont();
        columnsFont.setBold(true);
        columnsFont.setFontHeightInPoints((short) 14);
        columnsFont.setColor(IndexedColors.BLACK1.getIndex());
        columnsCellStyle.setFont(columnsFont);
        columnsCellStyle.setAlignment(HorizontalAlignment.LEFT);
        columnsCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        columnsCellStyle.setBorderTop(BorderStyle.MEDIUM);
        columnsCellStyle.setBorderRight(BorderStyle.MEDIUM);
        columnsCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        // Create column headers
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headersRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(columnsCellStyle);
        }
        int startingRow = 3;
        CellStyle valueCellStyle = workbook.createCellStyle();
        valueCellStyle.setAlignment(HorizontalAlignment.LEFT);
        valueCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        valueCellStyle.setBorderTop(BorderStyle.MEDIUM);
        valueCellStyle.setBorderRight(BorderStyle.MEDIUM);
        valueCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        for(JsonNode report : reports){
            Row row = sheet.createRow(startingRow);
            Iterator<JsonNode> it = report.iterator();
            int actualCellNum = 0;
            while(it.hasNext()){
                Cell cell = row.createCell(actualCellNum);
                cell.setCellStyle(valueCellStyle);
                JsonNode actualNode = it.next();
                if(columns[actualCellNum].equals("Día")){
                    try {
                        cell.setCellValue(formatter.format(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(actualNode.asText())));
                    }catch(Exception e){
                        throw new ExportException("No se pudo darle formato a la fecha indicada");
                    }
                }else if(columns[actualCellNum].equals("Comercio")) {
                    cell.setCellValue(actualNode.get("businessName").asText());
                }else{
                    cell.setCellValue(actualNode.asText());
                }
                actualCellNum++;
            }
            startingRow++;
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        File file;
        try {
            // Write the output to a file
            file = new File("reporte-HoyComo.xlsx");
            if (file.exists()){
                file.delete();
            }else{
                file.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();
        }catch(Exception e){
            logger.error("Error intentando escribir archivo xlsx", e);
            throw new ExportException("Error intentando exportar reporte");
        }
        return file;

    }
}
