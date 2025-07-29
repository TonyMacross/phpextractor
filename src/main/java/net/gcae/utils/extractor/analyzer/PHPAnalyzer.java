package net.gcae.utils.extractor.analyzer;

import net.gcae.utils.extractor.model.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Analyzes PHP projects to extract code elements
 */
public class PHPAnalyzer {
    
    private static final Logger logger = LoggerFactory.getLogger(PHPAnalyzer.class);
    
    // File extensions to analyze
    private static final Set<String> VALID_EXTENSIONS = Set.of(".php", ".html", ".htm", ".js", ".css");
    
    // Regex patterns for different code elements
    private static final Pattern SQL_PATTERN = Pattern.compile(
        "(?i)(SELECT|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER)\\s+.*?(?=;|$|\\)|\\?|'|\"|\\s*$)", 
        Pattern.MULTILINE | Pattern.DOTALL
    );
    
    private static final Pattern REQUIRE_PATTERN = Pattern.compile(
        "(?i)require(?:_once)?\\s*\\(\\s*[\"']([^\"']+)[\"']\\s*\\)"
    );
    
    private static final Pattern INCLUDE_PATTERN = Pattern.compile(
        "(?i)include(?:_once)?\\s*\\(\\s*[\"']([^\"']+)[\"']\\s*\\)"
    );
    
    private static final Pattern CURL_INIT_PATTERN = Pattern.compile(
        "(?i)curl_init\\s*\\(([^)]*)\\)"
    );
    
    private static final Pattern CURL_SETOPT_PATTERN = Pattern.compile(
        "(?i)curl_setopt\\s*\\([^,]+,\\s*([^,]+),\\s*([^)]+)\\)"
    );
    
    private static final Pattern CURL_EXEC_PATTERN = Pattern.compile(
        "(?i)curl_exec\\s*\\(([^)]+)\\)"
    );
    
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "(?i)function\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]*)\\)\\s*\\{"
    );
    
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "(?i)class\\s+([a-zA-Z_][a-zA-Z0-9_]*)(?:\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*))?(?:\\s+implements\\s+([^{]+))?\\s*\\{"
    );
    
    private static final Pattern USE_PATTERN = Pattern.compile(
        "(?i)use\\s+([a-zA-Z_][a-zA-Z0-9_\\\\]*)(?:\\s+as\\s+([a-zA-Z_][a-zA-Z0-9_]*))?\\s*;"
    );
    
    private static final Pattern EXTENDS_PATTERN = Pattern.compile(
        "(?i)class\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*)"
    );
    
    private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile(
        "(?i)class\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+implements\\s+([^{]+)"
    );
    
    private static final Pattern INTERFACE_PATTERN = Pattern.compile(
        "(?i)interface\\s+([a-zA-Z_][a-zA-Z0-9_]*)(?:\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*))?\\s*\\{"
    );
    
    public AnalysisResult analyzeProject(File projectDir) throws IOException {
        logger.info("Starting analysis of project: {}", projectDir.getAbsolutePath());
        
        List<FileInventory> files = new ArrayList<>();
        List<SQLQuery> sqlQueries = new ArrayList<>();
        List<FileReference> requires = new ArrayList<>();
        List<FileReference> includes = new ArrayList<>();
        List<CurlCall> curlCalls = new ArrayList<>();
        List<FunctionDefinition> functions = new ArrayList<>();
        List<ClassDefinition> classes = new ArrayList<>();
        List<InheritanceInfo> traits = new ArrayList<>();
        List<InheritanceInfo> classInheritances = new ArrayList<>();
        List<InterfaceImplementation> implementations = new ArrayList<>();
        List<InterfaceDefinition> interfaces = new ArrayList<>();
        
        // Use Files.walk for recursive directory traversal
        try (Stream<Path> paths = Files.walk(projectDir.toPath())) {
            List<Path> validFiles = paths
                .filter(Files::isRegularFile)
                .filter(this::isValidFile)
                .collect(java.util.stream.Collectors.toList());
                
            logger.info("Found {} valid files to analyze", validFiles.size());
            
            for (Path path : validFiles) {
                try {
                    logger.debug("Analyzing file: {}", path);
                    analyzeFile(path, files, sqlQueries, requires, includes, curlCalls, 
                              functions, classes, traits, classInheritances, implementations, interfaces);
                } catch (Exception e) {
                    logger.warn("Error analyzing file {}: {}", path, e.getMessage());
                }
            }
        }
        
        logger.info("Analysis completed. Found {} files, {} SQL queries, {} functions, {} classes", 
                   files.size(), sqlQueries.size(), functions.size(), classes.size());
        
        return new AnalysisResult(files, sqlQueries, requires, includes, curlCalls, 
                                functions, classes, traits, classInheritances, implementations, interfaces);
    }
    
    private boolean isValidFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        
        // Skip hidden files and directories
        if (fileName.startsWith(".")) {
            return false;
        }
        
        // Skip common non-source directories
        String pathStr = path.toString().toLowerCase();
        if (pathStr.contains("/.git/") || 
            pathStr.contains("/node_modules/") || 
            pathStr.contains("/vendor/") ||
            pathStr.contains("/.svn/") ||
            pathStr.contains("/target/") ||
            pathStr.contains("/build/") ||
            pathStr.contains("/dist/") ||
            pathStr.contains("/logs/")) {
            return false;
        }
        
        // Check for valid extensions
        boolean isValid = VALID_EXTENSIONS.stream().anyMatch(fileName::endsWith);
        
        if (isValid) {
            logger.debug("Valid file found: {}", path);
        }
        
        return isValid;
    }
    
    private void analyzeFile(Path filePath, List<FileInventory> files, List<SQLQuery> sqlQueries,
                           List<FileReference> requires, List<FileReference> includes,
                           List<CurlCall> curlCalls, List<FunctionDefinition> functions,
                           List<ClassDefinition> classes, List<InheritanceInfo> traits,
                           List<InheritanceInfo> classInheritances, List<InterfaceImplementation> implementations,
                           List<InterfaceDefinition> interfaces) throws IOException {
        
        logger.debug("Processing file: {}", filePath);
        
        String content;
        try {
            content = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.debug("UTF-8 failed for {}, trying with ISO-8859-1", filePath);
            try {
                content = FileUtils.readFileToString(filePath.toFile(), StandardCharsets.ISO_8859_1);
            } catch (Exception e2) {
                logger.warn("Could not read file {} with any encoding: {}", filePath, e2.getMessage());
                return; // Skip this file
            }
        }
        
        String fileName = filePath.getFileName().toString();
        String fileType = getFileType(fileName);
        long fileSize = Files.size(filePath);
        int lineCount = (int) content.lines().count();
        
        files.add(new FileInventory(fileName, filePath.toString(), fileType, fileSize, lineCount));
        logger.debug("Added file: {} (type: {}, size: {} bytes, lines: {})", fileName, fileType, fileSize, lineCount);
        
        // Only analyze PHP files for code elements
        if (fileType.equals("PHP")) {
            logger.debug("Analyzing PHP content for: {}", fileName);
            analyzePHPContent(content, filePath.toString(), sqlQueries, requires, includes, 
                            curlCalls, functions, classes, traits, classInheritances, implementations, interfaces);
        }
    }
    
    private String getFileType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "php": return "PHP";
            case "html":
            case "htm": return "HTML";
            case "js": return "JavaScript";
            case "css": return "CSS";
            default: return "Other";
        }
    }
    
    private void analyzePHPContent(String content, String filePath, List<SQLQuery> sqlQueries,
                                 List<FileReference> requires, List<FileReference> includes,
                                 List<CurlCall> curlCalls, List<FunctionDefinition> functions,
                                 List<ClassDefinition> classes, List<InheritanceInfo> traits,
                                 List<InheritanceInfo> classInheritances, List<InterfaceImplementation> implementations,
                                 List<InterfaceDefinition> interfaces) {
        
        String[] lines = content.split("\\r?\\n");
        
        // Analyze SQL queries
        analyzeSQLQueries(content, filePath, lines, sqlQueries);
        
        // Analyze requires
        analyzeRequires(content, filePath, lines, requires);
        
        // Analyze includes
        analyzeIncludes(content, filePath, lines, includes);
        
        // Analyze CURL calls
        analyzeCurlCalls(content, filePath, lines, curlCalls);
        
        // Analyze functions
        analyzeFunctions(content, filePath, lines, functions);
        
        // Analyze classes
        analyzeClasses(content, filePath, lines, classes);
        
        // Analyze traits (use statements)
        analyzeTraits(content, filePath, lines, traits);
        
        // Analyze class inheritance
        analyzeClassInheritance(content, filePath, lines, classInheritances);
        
        // Analyze interface implementations
        analyzeImplementations(content, filePath, lines, implementations);
        
        // Analyze interface definitions
        analyzeInterfaces(content, filePath, lines, interfaces);
    }
    
    private void analyzeSQLQueries(String content, String filePath, String[] lines, List<SQLQuery> sqlQueries) {
        Matcher matcher = SQL_PATTERN.matcher(content);
        int queryCount = 0;
        
        while (matcher.find()) {
            queryCount++;
            String query = matcher.group().trim();
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            
            String queryName = "Query_" + queryCount;
            String dbTable = extractTableName(query);
            String fileLocation = filePath + ":" + lineNumber;
            String dataSource = detectDataSource(content);
            String complexity = calculateQueryComplexity(query);
            
            sqlQueries.add(new SQLQuery(queryName, dbTable, fileLocation, dataSource, query, complexity));
        }
    }
    
    private void analyzeRequires(String content, String filePath, String[] lines, List<FileReference> requires) {
        Matcher matcher = REQUIRE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String requiredFile = matcher.group(1);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            requires.add(new FileReference(requiredFile, fileLocation, "require"));
        }
    }
    
    private void analyzeIncludes(String content, String filePath, String[] lines, List<FileReference> includes) {
        Matcher matcher = INCLUDE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String includedFile = matcher.group(1);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            includes.add(new FileReference(includedFile, fileLocation, "include"));
        }
    }
    
    private void analyzeCurlCalls(String content, String filePath, String[] lines, List<CurlCall> curlCalls) {
        // Analyze curl_init
        Matcher initMatcher = CURL_INIT_PATTERN.matcher(content);
        while (initMatcher.find()) {
            int lineNumber = getLineNumber(content, initMatcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            String target = initMatcher.group(1).trim();
            
            curlCalls.add(new CurlCall("curl_init", fileLocation, target, "Initialize CURL session"));
        }
        
        // Analyze curl_setopt
        Matcher setoptMatcher = CURL_SETOPT_PATTERN.matcher(content);
        while (setoptMatcher.find()) {
            int lineNumber = getLineNumber(content, setoptMatcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            String option = setoptMatcher.group(1).trim();
            String value = setoptMatcher.group(2).trim();
            
            curlCalls.add(new CurlCall("curl_setopt", fileLocation, option + "=" + value, "Set CURL option"));
        }
        
        // Analyze curl_exec
        Matcher execMatcher = CURL_EXEC_PATTERN.matcher(content);
        while (execMatcher.find()) {
            int lineNumber = getLineNumber(content, execMatcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            String handle = execMatcher.group(1).trim();
            
            curlCalls.add(new CurlCall("curl_exec", fileLocation, handle, "Execute CURL session"));
        }
    }
    
    private void analyzeFunctions(String content, String filePath, String[] lines, List<FunctionDefinition> functions) {
        Matcher matcher = FUNCTION_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String functionName = matcher.group(1);
            String parameters = matcher.group(2);
            int startLine = getLineNumber(content, matcher.start(), lines);
            
            // Calculate function length by finding matching braces
            int functionLength = calculateFunctionLength(content, matcher.start());
            String complexity = calculateFunctionComplexity(functionLength);
            String fileLocation = filePath + ":" + startLine;
            
            functions.add(new FunctionDefinition(functionName, fileLocation, functionLength, complexity, parameters));
        }
    }
    
    private void analyzeClasses(String content, String filePath, String[] lines, List<ClassDefinition> classes) {
        Matcher matcher = CLASS_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String className = matcher.group(1);
            String parentClass = matcher.group(2);
            String interfaces = matcher.group(3);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            // Count methods in class
            int methodCount = countMethodsInClass(content, matcher.start());
            
            classes.add(new ClassDefinition(className, fileLocation, methodCount, 
                                          parentClass != null ? parentClass : "", 
                                          interfaces != null ? interfaces.trim() : ""));
        }
    }
    
    private void analyzeTraits(String content, String filePath, String[] lines, List<InheritanceInfo> traits) {
        Matcher matcher = USE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String traitName = matcher.group(1);
            String alias = matcher.group(2);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            traits.add(new InheritanceInfo("Current Class", traitName, fileLocation, "use trait"));
        }
    }
    
    private void analyzeClassInheritance(String content, String filePath, String[] lines, List<InheritanceInfo> classInheritances) {
        Matcher matcher = EXTENDS_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String childClass = matcher.group(1);
            String parentClass = matcher.group(2);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            classInheritances.add(new InheritanceInfo(childClass, parentClass, fileLocation, "extends"));
        }
    }
    
    private void analyzeImplementations(String content, String filePath, String[] lines, List<InterfaceImplementation> implementations) {
        Matcher matcher = IMPLEMENTS_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String className = matcher.group(1);
            String interfaceList = matcher.group(2);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            // Split multiple interfaces
            String[] interfaces = interfaceList.split(",");
            for (String interfaceName : interfaces) {
                implementations.add(new InterfaceImplementation(className, interfaceName.trim(), fileLocation));
            }
        }
    }
    
    private void analyzeInterfaces(String content, String filePath, String[] lines, List<InterfaceDefinition> interfaces) {
        Matcher matcher = INTERFACE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String interfaceName = matcher.group(1);
            String extendsInterface = matcher.group(2);
            int lineNumber = getLineNumber(content, matcher.start(), lines);
            String fileLocation = filePath + ":" + lineNumber;
            
            // Count methods in interface
            int methodCount = countMethodsInInterface(content, matcher.start());
            
            interfaces.add(new InterfaceDefinition(interfaceName, fileLocation, methodCount, 
                                                 extendsInterface != null ? extendsInterface : ""));
        }
    }
    
    // Helper methods
    private int getLineNumber(String content, int position, String[] lines) {
        String beforePosition = content.substring(0, position);
        return beforePosition.split("\\r?\\n").length;
    }
    
    private String extractTableName(String query) {
        // Simple table name extraction - can be enhanced
        String[] keywords = {"FROM", "INTO", "UPDATE", "TABLE"};
        String upperQuery = query.toUpperCase();
        
        for (String keyword : keywords) {
            int index = upperQuery.indexOf(keyword);
            if (index != -1) {
                String afterKeyword = query.substring(index + keyword.length()).trim();
                String[] words = afterKeyword.split("\\s+");
                if (words.length > 0) {
                    return words[0].replaceAll("[^a-zA-Z0-9_]", "");
                }
            }
        }
        return "Unknown";
    }
    
    private String detectDataSource(String content) {
        if (content.contains("mysqli_") || content.contains("new mysqli")) {
            return "MySQLi";
        } else if (content.contains("PDO")) {
            return "PDO";
        } else if (content.contains("mysql_")) {
            return "MySQL (deprecated)";
        } else {
            return "Unknown";
        }
    }
    
    private String calculateQueryComplexity(String query) {
        int length = query.length();
        int joinCount = query.toUpperCase().split("JOIN").length - 1;
        int subqueryCount = query.split("\\(\\s*SELECT").length - 1;
        
        if (length > 500 || joinCount > 3 || subqueryCount > 2) {
            return "High";
        } else if (length > 200 || joinCount > 1 || subqueryCount > 0) {
            return "Medium";
        } else {
            return "Low";
        }
    }
    
    private int calculateFunctionLength(String content, int startPos) {
        int braceCount = 0;
        int lineCount = 0;
        boolean inFunction = false;
        
        for (int i = startPos; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{') {
                braceCount++;
                inFunction = true;
            } else if (c == '}') {
                braceCount--;
                if (inFunction && braceCount == 0) {
                    break;
                }
            } else if (c == '\n' && inFunction) {
                lineCount++;
            }
        }
        return lineCount;
    }
    
    private String calculateFunctionComplexity(int lineCount) {
        if (lineCount > 50) {
            return "High";
        } else if (lineCount > 20) {
            return "Medium";
        } else {
            return "Low";
        }
    }
    
    private int countMethodsInClass(String content, int classStartPos) {
        // Find class end and count function keywords within
        int braceCount = 0;
        boolean inClass = false;
        int methodCount = 0;
        String classContent = "";
        
        for (int i = classStartPos; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{') {
                braceCount++;
                inClass = true;
            } else if (c == '}') {
                braceCount--;
                if (inClass && braceCount == 0) {
                    classContent = content.substring(classStartPos, i);
                    break;
                }
            }
        }
        
        if (!classContent.isEmpty()) {
            Matcher methodMatcher = Pattern.compile("(?i)function\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*\\(").matcher(classContent);
            while (methodMatcher.find()) {
                methodCount++;
            }
        }
        
        return methodCount;
    }
    
    private int countMethodsInInterface(String content, int interfaceStartPos) {
        return countMethodsInClass(content, interfaceStartPos); // Same logic
    }
}