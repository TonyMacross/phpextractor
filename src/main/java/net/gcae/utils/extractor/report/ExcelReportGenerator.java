package net.gcae.utils.extractor.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.gcae.utils.extractor.model.AnalysisResult;
import net.gcae.utils.extractor.model.ClassDefinition;
import net.gcae.utils.extractor.model.CurlCall;
import net.gcae.utils.extractor.model.FileInventory;
import net.gcae.utils.extractor.model.FileReference;
import net.gcae.utils.extractor.model.FunctionDefinition;
import net.gcae.utils.extractor.model.InheritanceInfo;
import net.gcae.utils.extractor.model.InterfaceDefinition;
import net.gcae.utils.extractor.model.InterfaceImplementation;
import net.gcae.utils.extractor.model.SQLQuery;

/**
 * Generates Excel reports from analysis results
 */
public class ExcelReportGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelReportGenerator.class);
    
    // Sheet names
    private static final String OVERVIEW_SHEET = "ITx Inventory Overview";
    private static final String FILES_SHEET = "phpFilesReport";
    private static final String QUERIES_SHEET = "phpQueriesReport";
    private static final String REQUIRES_SHEET = "phpRequiresReport";
    private static final String INCLUDES_SHEET = "phpIncludesReport";
    private static final String CALLS_SHEET = "phpCallsReport";
    private static final String FUNCTIONS_SHEET = "phpFunctionReport";
    private static final String CLASSES_SHEET = "phpClassesReport";
    private static final String TRAITS_SHEET = "phpInheritTritReport";
    private static final String INHERITANCE_SHEET = "phpInheritClassReport";
    private static final String IMPLEMENTS_SHEET = "phpImplementReport";
    private static final String INTERFACES_SHEET = "phpInterfacesReport";
    
    public void generateReport(AnalysisResult result, String outputPath) throws IOException {
        logger.info("Generating Excel report: {}", outputPath);
        
        // Ensure output directory exists
        File outputFile = new File(outputPath);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
            logger.debug("Created output directory: {}", parentDir.getAbsolutePath());
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            
            // Always create overview sheet first
            createOverviewSheet(workbook, result, headerStyle, dataStyle);
            
            // Create individual sheets only if data exists
            if (hasData(result.getFiles())) {
                createFilesSheet(workbook, result.getFiles(), headerStyle, dataStyle);
                logger.debug("Created files sheet with {} entries", result.getFiles().size());
            } else {
                logger.info("No files data found, skipping {} sheet", FILES_SHEET);
            }
            
            if (hasData(result.getSqlQueries())) {
                createQueriesSheet(workbook, result.getSqlQueries(), headerStyle, dataStyle);
                logger.debug("Created queries sheet with {} entries", result.getSqlQueries().size());
            } else {
                logger.info("No SQL queries data found, skipping {} sheet", QUERIES_SHEET);
            }
            
            if (hasData(result.getRequires())) {
                createRequiresSheet(workbook, result.getRequires(), headerStyle, dataStyle);
                logger.debug("Created requires sheet with {} entries", result.getRequires().size());
            } else {
                logger.info("No requires data found, skipping {} sheet", REQUIRES_SHEET);
            }
            
            if (hasData(result.getIncludes())) {
                createIncludesSheet(workbook, result.getIncludes(), headerStyle, dataStyle);
                logger.debug("Created includes sheet with {} entries", result.getIncludes().size());
            } else {
                logger.info("No includes data found, skipping {} sheet", INCLUDES_SHEET);
            }
            
            if (hasData(result.getCurlCalls())) {
                createCallsSheet(workbook, result.getCurlCalls(), headerStyle, dataStyle);
                logger.debug("Created calls sheet with {} entries", result.getCurlCalls().size());
            } else {
                logger.info("No CURL calls data found, skipping {} sheet", CALLS_SHEET);
            }
            
            if (hasData(result.getFunctions())) {
                createFunctionsSheet(workbook, result.getFunctions(), headerStyle, dataStyle);
                logger.debug("Created functions sheet with {} entries", result.getFunctions().size());
            } else {
                logger.info("No functions data found, skipping {} sheet", FUNCTIONS_SHEET);
            }
            
            if (hasData(result.getClasses())) {
                createClassesSheet(workbook, result.getClasses(), headerStyle, dataStyle);
                logger.debug("Created classes sheet with {} entries", result.getClasses().size());
            } else {
                logger.info("No classes data found, skipping {} sheet", CLASSES_SHEET);
            }
            
            if (hasData(result.getTraits())) {
                createTraitsSheet(workbook, result.getTraits(), headerStyle, dataStyle);
                logger.debug("Created traits sheet with {} entries", result.getTraits().size());
            } else {
                logger.info("No traits data found, skipping {} sheet", TRAITS_SHEET);
            }
            
            if (hasData(result.getClassInheritances())) {
                createInheritanceSheet(workbook, result.getClassInheritances(), headerStyle, dataStyle);
                logger.debug("Created inheritance sheet with {} entries", result.getClassInheritances().size());
            } else {
                logger.info("No class inheritance data found, skipping {} sheet", INHERITANCE_SHEET);
            }
            
            if (hasData(result.getImplementations())) {
                createImplementsSheet(workbook, result.getImplementations(), headerStyle, dataStyle);
                logger.debug("Created implementations sheet with {} entries", result.getImplementations().size());
            } else {
                logger.info("No interface implementations data found, skipping {} sheet", IMPLEMENTS_SHEET);
            }
            
            if (hasData(result.getInterfaces())) {
                createInterfacesSheet(workbook, result.getInterfaces(), headerStyle, dataStyle);
                logger.debug("Created interfaces sheet with {} entries", result.getInterfaces().size());
            } else {
                logger.info("No interfaces data found, skipping {} sheet", INTERFACES_SHEET);
            }
            
            // Write the workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
                logger.info("Excel report written successfully to: {}", outputPath);
            }
            
            logger.info("Excel report generated successfully with {} sheets", workbook.getNumberOfSheets());
        } catch (Exception e) {
            logger.error("Error generating Excel report: {}", e.getMessage(), e);
            throw new IOException("Failed to generate Excel report: " + e.getMessage(), e);
        }
    }
    
    private boolean hasData(List<?> list) {
        return list != null && !list.isEmpty();
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private void createOverviewSheet(Workbook workbook, AnalysisResult result, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(OVERVIEW_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Category", "Count", "Description"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        rowNum = addOverviewRow(sheet, rowNum, "Total Files", safeSize(result.getFiles()), "PHP, HTML, JS, CSS files analyzed", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "SQL Queries", safeSize(result.getSqlQueries()), "Database queries found in code", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Require Statements", safeSize(result.getRequires()), "File require statements", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Include Statements", safeSize(result.getIncludes()), "File include statements", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "CURL Calls", safeSize(result.getCurlCalls()), "CURL operations found", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Functions", safeSize(result.getFunctions()), "Function definitions", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Classes", safeSize(result.getClasses()), "Class definitions", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Traits Usage", safeSize(result.getTraits()), "Trait usage statements", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Class Inheritance", safeSize(result.getClassInheritances()), "Class extends relationships", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Interface Implementations", safeSize(result.getImplementations()), "Interface implementations", dataStyle);
        rowNum = addOverviewRow(sheet, rowNum, "Interface Definitions", safeSize(result.getInterfaces()), "Interface definitions", dataStyle);
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private int addOverviewRow(Sheet sheet, int rowNum, String category, int count, String description, CellStyle dataStyle) {
        Row row = sheet.createRow(rowNum);
        
        Cell categoryCell = row.createCell(0);
        categoryCell.setCellValue(category);
        categoryCell.setCellStyle(dataStyle);
        
        Cell countCell = row.createCell(1);
        countCell.setCellValue(count);
        countCell.setCellStyle(dataStyle);
        
        Cell descCell = row.createCell(2);
        descCell.setCellValue(description);
        descCell.setCellStyle(dataStyle);
        
        return rowNum + 1;
    }
    
    private int safeSize(List<?> list) {
        return list != null ? list.size() : 0;
    }
    
    private void createFilesSheet(Workbook workbook, List<FileInventory> files, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(FILES_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"File Name", "File Path", "File Type", "File Size (bytes)", "Line Count"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (FileInventory file : files) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(file.getFileName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(file.getFilePath());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(file.getFileType());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(file.getFileSize());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(file.getLineCount());
            cell4.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createQueriesSheet(Workbook workbook, List<SQLQuery> queries, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(QUERIES_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Query Name", "DB Table", "File:Line", "Data Source", "SQL Query", "Complexity"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (SQLQuery query : queries) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(query.getQueryName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(query.getDbTable());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(query.getFileLocation());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(query.getDataSource());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(query.getSqlQuery());
            cell4.setCellStyle(dataStyle);
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(query.getComplexity());
            cell5.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createRequiresSheet(Workbook workbook, List<FileReference> requires, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(REQUIRES_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Required File", "File Location", "Reference Type"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (FileReference require : requires) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(require.getReferencedFile());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(require.getFileLocation());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(require.getReferenceType());
            cell2.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createIncludesSheet(Workbook workbook, List<FileReference> includes, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(INCLUDES_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Included File", "File Location", "Reference Type"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (FileReference include : includes) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(include.getReferencedFile());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(include.getFileLocation());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(include.getReferenceType());
            cell2.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createCallsSheet(Workbook workbook, List<CurlCall> calls, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(CALLS_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Call Type", "File Location", "Target", "Purpose"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (CurlCall call : calls) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(call.getCallType());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(call.getFileLocation());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(call.getTarget());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(call.getPurpose());
            cell3.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createFunctionsSheet(Workbook workbook, List<FunctionDefinition> functions, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(FUNCTIONS_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Function Name", "File Location", "Line Count", "Complexity", "Parameters"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (FunctionDefinition function : functions) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(function.getFunctionName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(function.getFileLocation());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(function.getLineCount());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(function.getComplexity());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(function.getParameters());
            cell4.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createClassesSheet(Workbook workbook, List<ClassDefinition> classes, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(CLASSES_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Class Name", "File Location", "Method Count", "Parent Class", "Interfaces"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (ClassDefinition clazz : classes) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(clazz.getClassName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(clazz.getFileLocation());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(clazz.getMethodCount());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(clazz.getParentClass());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(clazz.getInterfaces());
            cell4.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createTraitsSheet(Workbook workbook, List<InheritanceInfo> traits, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(TRAITS_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Child Name", "Trait Name", "File Location", "Inheritance Type"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (InheritanceInfo trait : traits) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(trait.getChildName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(trait.getParentName());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(trait.getFileLocation());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(trait.getInheritanceType());
            cell3.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createInheritanceSheet(Workbook workbook, List<InheritanceInfo> inheritances, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(INHERITANCE_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Child Class", "Parent Class", "File Location", "Inheritance Type"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (InheritanceInfo inheritance : inheritances) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(inheritance.getChildName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(inheritance.getParentName());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(inheritance.getFileLocation());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(inheritance.getInheritanceType());
            cell3.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createImplementsSheet(Workbook workbook, List<InterfaceImplementation> implementations, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(IMPLEMENTS_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Class Name", "Interface Name", "File Location"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (InterfaceImplementation implementation : implementations) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(implementation.getClassName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(implementation.getInterfaceName());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(implementation.getFileLocation());
            cell2.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createInterfacesSheet(Workbook workbook, List<InterfaceDefinition> interfaces, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(INTERFACES_SHEET);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Interface Name", "File Location", "Method Count", "Extends Interface"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (InterfaceDefinition interfaceDefinition : interfaces) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(interfaceDefinition.getInterfaceName());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(interfaceDefinition.getFileLocation());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(interfaceDefinition.getMethodCount());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(interfaceDefinition.getExtendsInterface());
            cell3.setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}