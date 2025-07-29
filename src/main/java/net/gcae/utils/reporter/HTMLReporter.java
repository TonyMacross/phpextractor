package net.gcae.utils.reporter;

import net.gcae.utils.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Generates HTML reports for PHP analysis results using externalized templates
 */
public class HTMLReporter {
    
    private static final Logger logger = LoggerFactory.getLogger(HTMLReporter.class);
    private final String outputDirectory;
    private final Properties htmlTemplates;
    
    public HTMLReporter(String outputDirectory) throws IOException {
        this.outputDirectory = outputDirectory;
        this.htmlTemplates = loadTemplates();
    }
    
    /**
     * Loads HTML templates from properties file
     */
    private Properties loadTemplates() throws IOException {
        Properties templates = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("html-templates.properties")) {
            if (inputStream == null) {
                throw new IOException("Cannot find html-templates.properties in classpath");
            }
            templates.load(inputStream);
            logger.info("Loaded {} HTML templates", templates.size());
        }
        return templates;
    }
    
    /**
     * Generates all HTML reports based on analysis results
     */
    public void generateReports(AnalysisResult result) throws IOException {
        logger.info("Generating HTML reports using externalized templates...");
        
        generateFilesReport(result);
        generateQueriesReport(result);
        generateRequiresReport(result);
        generateIncludesReport(result);
        generateCallsReport(result);
        generateFunctionsReport(result);
        generateClassesReport(result);
        generateTraitUsesReport(result);
        generateClassExtendsReport(result);
        generateImplementsReport(result);
        generateInterfacesReport(result);
        
        logger.info("All HTML reports generated successfully");
    }
    
    /**
     * Generates PHP Files inventory report
     */
    private void generateFilesReport(AnalysisResult result) throws IOException {
        if (result.getPhpFiles().isEmpty()) {
            logger.warn("No PHP files found - skipping phpFilesReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        // Header
        String title = getTemplate("report.files.title");
        html.append(getHtmlHeader(title));
        
        // Content
        html.append(replaceVariables(getTemplate("report.files.heading"), 
            "count", String.valueOf(result.getPhpFiles().size())));
        
        // Table
        html.append(getTemplate("report.files.table.header"));
        
        for (PHPFile file : result.getPhpFiles()) {
            String row = getTemplate("report.files.table.row");
            row = replaceVariables(row,
                "fileName", escapeHtml(file.getFileName()),
                "relativePath", escapeHtml(file.getRelativePath()),
                "fileSize", String.valueOf(file.getFileSize()));
            html.append(row);
        }
        
        html.append(getTemplate("report.files.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpFilesReport.html", html.toString());
        logger.info("Generated phpFilesReport.html with {} files", result.getPhpFiles().size());
    }
    
    /**
     * Generates SQL Queries report
     */
    private void generateQueriesReport(AnalysisResult result) throws IOException {
        if (result.getQueries().isEmpty()) {
            logger.warn("No SQL queries found - skipping phpQueriesReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.queries.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.queries.heading"), 
            "count", String.valueOf(result.getQueries().size())));
        
        html.append(getTemplate("report.queries.table.header"));
        
        for (SQLQuery query : result.getQueries()) {
            String row = getTemplate("report.queries.table.row");
            row = replaceVariables(row,
                "queryPreview", escapeHtml(truncateText(query.getQueryPreview(), 100)),
                "tableName", escapeHtml(query.getTableName()),
                "filePath", escapeHtml(query.getFilePath()),
                "functionName", escapeHtml(query.getFunctionName()));
            html.append(row);
        }
        
        html.append(getTemplate("report.queries.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpQueriesReport.html", html.toString());
        logger.info("Generated phpQueriesReport.html with {} queries", result.getQueries().size());
    }
    
    /**
     * Generates Requires report
     */
    private void generateRequiresReport(AnalysisResult result) throws IOException {
        if (result.getRequires().isEmpty()) {
            logger.warn("No require statements found - skipping phpRequiresReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.requires.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.requires.heading"), 
            "count", String.valueOf(result.getRequires().size())));
        
        html.append(getTemplate("report.requires.table.header"));
        
        for (RequireStatement require : result.getRequires()) {
            String row = getTemplate("report.requires.table.row");
            row = replaceVariables(row,
                "requiredFile", escapeHtml(require.getRequiredFile()),
                "filePath", escapeHtml(require.getFilePath()),
                "functionName", escapeHtml(require.getFunctionName()));
            html.append(row);
        }
        
        html.append(getTemplate("report.requires.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpRequiresReport.html", html.toString());
        logger.info("Generated phpRequiresReport.html with {} require statements", result.getRequires().size());
    }
    
    /**
     * Generates Includes report
     */
    private void generateIncludesReport(AnalysisResult result) throws IOException {
        if (result.getIncludes().isEmpty()) {
            logger.warn("No include statements found - skipping phpIncludesReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.includes.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.includes.heading"), 
            "count", String.valueOf(result.getIncludes().size())));
        
        html.append(getTemplate("report.includes.table.header"));
        
        for (IncludeStatement include : result.getIncludes()) {
            String row = getTemplate("report.includes.table.row");
            row = replaceVariables(row,
                "includedFile", escapeHtml(include.getIncludedFile()),
                "filePath", escapeHtml(include.getFilePath()),
                "functionName", escapeHtml(include.getFunctionName()));
            html.append(row);
        }
        
        html.append(getTemplate("report.includes.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpIncludesReport.html", html.toString());
        logger.info("Generated phpIncludesReport.html with {} include statements", result.getIncludes().size());
    }
    
    /**
     * Generates CURL Calls report
     */
    private void generateCallsReport(AnalysisResult result) throws IOException {
        if (result.getCalls().isEmpty()) {
            logger.warn("No CURL calls found - skipping phpCallsReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.calls.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.calls.heading"), 
            "count", String.valueOf(result.getCalls().size())));
        
        html.append(getTemplate("report.calls.table.header"));
        
        for (CurlCall call : result.getCalls()) {
            String row = getTemplate("report.calls.table.row");
            row = replaceVariables(row,
                "callType", escapeHtml(call.getCallType()),
                "filePath", escapeHtml(call.getFilePath()),
                "functionName", escapeHtml(call.getFunctionName()));
            html.append(row);
        }
        
        html.append(getTemplate("report.calls.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpCallsReport.html", html.toString());
        logger.info("Generated phpCallsReport.html with {} CURL calls", result.getCalls().size());
    }
    
    /**
     * Generates Functions report
     */
    private void generateFunctionsReport(AnalysisResult result) throws IOException {
        if (result.getFunctions().isEmpty()) {
            logger.warn("No functions found - skipping phpFunctionsReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.functions.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.functions.heading"), 
            "count", String.valueOf(result.getFunctions().size())));
        
        html.append(getTemplate("report.functions.table.header"));
        
        for (PHPFunction function : result.getFunctions()) {
            String row = getTemplate("report.functions.table.row");
            row = replaceVariables(row,
                "functionName", escapeHtml(function.getFunctionName()),
                "filePath", escapeHtml(function.getFilePath()));
            html.append(row);
        }
        
        html.append(getTemplate("report.functions.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpFunctionsReport.html", html.toString());
        logger.info("Generated phpFunctionsReport.html with {} functions", result.getFunctions().size());
    }
    
    /**
     * Generates Classes report
     */
    private void generateClassesReport(AnalysisResult result) throws IOException {
        if (result.getClasses().isEmpty()) {
            logger.warn("No classes found - skipping phpClassesReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.classes.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.classes.heading"), 
            "count", String.valueOf(result.getClasses().size())));
        
        html.append(getTemplate("report.classes.table.header"));
        
        for (PHPClass phpClass : result.getClasses()) {
            String row = getTemplate("report.classes.table.row");
            row = replaceVariables(row,
                "className", escapeHtml(phpClass.getClassName()),
                "filePath", escapeHtml(phpClass.getFilePath()));
            html.append(row);
        }
        
        html.append(getTemplate("report.classes.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpClassesReport.html", html.toString());
        logger.info("Generated phpClassesReport.html with {} classes", result.getClasses().size());
    }
    
    /**
     * Generates Trait Uses report
     */
    private void generateTraitUsesReport(AnalysisResult result) throws IOException {
        if (result.getTraitUses().isEmpty()) {
            logger.warn("No trait uses found - skipping phpInheritsFromTritReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.traituses.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.traituses.heading"), 
            "count", String.valueOf(result.getTraitUses().size())));
        
        html.append(getTemplate("report.traituses.table.header"));
        
        for (TraitUse traitUse : result.getTraitUses()) {
            String row = getTemplate("report.traituses.table.row");
            row = replaceVariables(row,
                "traitName", escapeHtml(traitUse.getTraitName()),
                "filePath", escapeHtml(traitUse.getFilePath()));
            html.append(row);
        }
        
        html.append(getTemplate("report.traituses.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpInheritsFromTritReport.html", html.toString());
        logger.info("Generated phpInheritsFromTritReport.html with {} trait uses", result.getTraitUses().size());
    }
    
    /**
     * Generates Class Extends report
     */
    private void generateClassExtendsReport(AnalysisResult result) throws IOException {
        if (result.getClassExtends().isEmpty()) {
            logger.warn("No class extends found - skipping phpInheritFromClassReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.classextends.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.classextends.heading"), 
            "count", String.valueOf(result.getClassExtends().size())));
        
        html.append(getTemplate("report.classextends.table.header"));
        
        for (ClassExtend classExtend : result.getClassExtends()) {
            String row = getTemplate("report.classextends.table.row");
            row = replaceVariables(row,
                "parentClass", escapeHtml(classExtend.getParentClass()),
                "filePath", escapeHtml(classExtend.getFilePath()));
            html.append(row);
        }
        
        html.append(getTemplate("report.classextends.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpInheritFromClassReport.html", html.toString());
        logger.info("Generated phpInheritFromClassReport.html with {} class extends", result.getClassExtends().size());
    }
    
    /**
     * Generates Interface Implementations report
     */
    private void generateImplementsReport(AnalysisResult result) throws IOException {
        if (result.getImplements().isEmpty()) {
            logger.warn("No interface implementations found - skipping phpImplementsReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.implements.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.implements.heading"), 
            "count", String.valueOf(result.getImplements().size())));
        
        html.append(getTemplate("report.implements.table.header"));
        
        for (InterfaceImplementation impl : result.getImplements()) {
            String row = getTemplate("report.implements.table.row");
            row = replaceVariables(row,
                "interfaceName", escapeHtml(impl.getInterfaceName()),
                "filePath", escapeHtml(impl.getFilePath()));
            html.append(row);
        }
        
        html.append(getTemplate("report.implements.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpImplementsReport.html", html.toString());
        logger.info("Generated phpImplementsReport.html with {} interface implementations", result.getImplements().size());
    }
    
    /**
     * Generates Interfaces report
     */
    private void generateInterfacesReport(AnalysisResult result) throws IOException {
        if (result.getInterfaces().isEmpty()) {
            logger.warn("No interfaces found - skipping phpInterfaceReport.html");
            return;
        }
        
        StringBuilder html = new StringBuilder();
        
        String title = getTemplate("report.interfaces.title");
        html.append(getHtmlHeader(title));
        
        html.append(replaceVariables(getTemplate("report.interfaces.heading"), 
            "count", String.valueOf(result.getInterfaces().size())));
        
        html.append(getTemplate("report.interfaces.table.header"));
        
        for (PHPInterface phpInterface : result.getInterfaces()) {
            String row = getTemplate("report.interfaces.table.row");
            row = replaceVariables(row,
                "interfaceName", escapeHtml(phpInterface.getInterfaceName()),
                "filePath", escapeHtml(phpInterface.getFilePath()));
            html.append(row);
        }
        
        html.append(getTemplate("report.interfaces.table.footer"));
        html.append(getHtmlFooter());
        
        writeHtmlFile("phpInterfaceReport.html", html.toString());
        logger.info("Generated phpInterfaceReport.html with {} interfaces", result.getInterfaces().size());
    }
    
    /**
     * Gets template from properties
     */
    private String getTemplate(String key) {
        String template = htmlTemplates.getProperty(key);
        if (template == null) {
            logger.error("Template not found for key: {}", key);
            return "";
        }
        return template;
    }
    
    /**
     * Generates HTML header using template
     */
    private String getHtmlHeader(String title) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        
        String header = getTemplate("html.header");
        return replaceVariables(header, 
            "title", escapeHtml(title), 
            "timestamp", timestamp);
    }
    
    /**
     * Generates HTML footer using template
     */
    private String getHtmlFooter() {
        return getTemplate("html.footer");
    }
    
    /**
     * Replaces variables in template string
     * Variables are in the format ${variableName}
     */
    private String replaceVariables(String template, String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even number of arguments");
        }
        
        String result = template;
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            result = result.replace("${" + key + "}", value != null ? value : "");
        }
        
        return result;
    }
    
    /**
     * Writes HTML content to file
     */
    private void writeHtmlFile(String fileName, String content) throws IOException {
        Path filePath = Paths.get(outputDirectory, fileName);
        Files.write(filePath, content.getBytes("UTF-8"));
    }
    
    /**
     * Escapes HTML special characters
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    /**
     * Truncates text to specified length
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
}