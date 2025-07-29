package net.gcae.utils.analyzer;

import net.gcae.utils.model.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Core PHP file analyzer that extracts different elements from PHP code
 */
public class PHPAnalyzer {
    
    private static final Logger logger = LoggerFactory.getLogger(PHPAnalyzer.class);
    
    // Regex patterns for different PHP elements
    private static final Pattern QUERY_PATTERN = Pattern.compile(
        "db_query\\s*\\(\\s*[\"']([^\"']+)[\"']", 
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );
    
    private static final Pattern REQUIRE_PATTERN = Pattern.compile(
        "require\\s*\\(\\s*[\"']([^\"']+)[\"']\\s*\\)", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern INCLUDE_PATTERN = Pattern.compile(
        "include\\s*\\(\\s*[\"']([^\"']+)[\"']\\s*\\)", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CURL_INIT_PATTERN = Pattern.compile(
        "curl_init\\s*\\(", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CURL_SETOPT_PATTERN = Pattern.compile(
        "curl_setopt\\s*\\(", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CURL_EXEC_PATTERN = Pattern.compile(
        "curl_exec\\s*\\(", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "function\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "class\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?:extends\\s+[a-zA-Z_][a-zA-Z0-9_]*)?(?:\\s+implements\\s+[a-zA-Z_][a-zA-Z0-9_,\\s]*)?\\s*\\{", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern USE_PATTERN = Pattern.compile(
        "use\\s+([a-zA-Z_][a-zA-Z0-9_\\\\]*)(?:\\s+as\\s+[a-zA-Z_][a-zA-Z0-9_]*)?\\s*;", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern EXTENDS_PATTERN = Pattern.compile(
        "class\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*)", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile(
        "implements\\s+([a-zA-Z_][a-zA-Z0-9_,\\s]*)", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern INTERFACE_PATTERN = Pattern.compile(
        "interface\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?:extends\\s+[a-zA-Z_][a-zA-Z0-9_,\\s]*)?\\s*\\{", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern TABLE_PATTERN = Pattern.compile(
        "(?:FROM|JOIN|UPDATE|INSERT\\s+INTO|DELETE\\s+FROM)\\s+([a-zA-Z_][a-zA-Z0-9_]*)", 
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * Analyzes a PHP repository and returns comprehensive analysis results
     */
    public AnalysisResult analyzeRepository(String repositoryPath) throws IOException {
        logger.info("Starting repository analysis: {}", repositoryPath);
        
        AnalysisResult result = new AnalysisResult();
        
        // Find all PHP files
        List<File> phpFiles = findPhpFiles(repositoryPath);
        logger.info("Found {} PHP files to analyze", phpFiles.size());
        
        // Analyze each PHP file
        for (File phpFile : phpFiles) {
            try {
                analyzeFile(phpFile, result, repositoryPath);
            } catch (Exception e) {
                logger.error("Error analyzing file: {}", phpFile.getPath(), e);
            }
        }
        
        return result;
    }
    
    /**
     * Finds all PHP files in the repository
     */
    private List<File> findPhpFiles(String repositoryPath) throws IOException {
        List<File> phpFiles = new ArrayList<>();
        
        try (Stream<Path> paths = Files.walk(Paths.get(repositoryPath))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().toLowerCase().endsWith(".php"))
                 .forEach(path -> phpFiles.add(path.toFile()));
        }
        
        return phpFiles;
    }
    
    /**
     * Analyzes a single PHP file
     */
    private void analyzeFile(File file, AnalysisResult result, String repositoryPath) throws IOException {
        logger.debug("Analyzing file: {}", file.getPath());
        
        // Read file content with proper encoding handling
        String content = readFileContent(file);
        if (content == null) {
            logger.warn("Skipping file due to encoding issues: {}", file.getPath());
            return;
        }
        
        String relativePath = getRelativePath(file.getPath(), repositoryPath);
        
        // Add file to inventory
        PHPFile phpFile = new PHPFile(file.getName(), relativePath, file.length());
        result.addPhpFile(phpFile);
        
        // Extract different elements
        extractQueries(content, relativePath, result);
        extractRequires(content, relativePath, result);
        extractIncludes(content, relativePath, result);
        extractCurlCalls(content, relativePath, result);
        extractFunctions(content, relativePath, result);
        extractClasses(content, relativePath, result);
        extractTraitUses(content, relativePath, result);
        extractClassExtends(content, relativePath, result);
        extractImplements(content, relativePath, result);
        extractInterfaces(content, relativePath, result);
    }
    
    /**
     * Reads file content with proper encoding handling
     */
    private String readFileContent(File file) {
        try {
            // Try UTF-8 first
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                // Fallback to ISO-8859-1
                return FileUtils.readFileToString(file, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                logger.error("Cannot read file with any encoding: {}", file.getPath(), ex);
                return null;
            }
        }
    }
    
    /**
     * Gets relative path from repository root
     */
    private String getRelativePath(String fullPath, String repositoryPath) {
        return fullPath.substring(repositoryPath.length()).replace("\\", "/");
    }
    
    /**
     * Extracts SQL queries from PHP code
     */
    private void extractQueries(String content, String filePath, AnalysisResult result) {
        Matcher matcher = QUERY_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String queryPreview = matcher.group(1).trim();
            String tableName = extractTableName(queryPreview);
            
            // Find the function containing this query
            String functionName = findContainingFunction(content, matcher.start());
            
            SQLQuery query = new SQLQuery(queryPreview, tableName, filePath, functionName);
            result.addQuery(query);
        }
    }
    
    /**
     * Extracts table name from SQL query
     */
    private String extractTableName(String query) {
        Matcher matcher = TABLE_PATTERN.matcher(query);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }
    
    /**
     * Finds the function containing a specific position in the code
     */
    private String findContainingFunction(String content, int position) {
        String beforePosition = content.substring(0, position);
        
        // Find the last function declaration before this position
        Pattern funcPattern = Pattern.compile("function\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(", Pattern.CASE_INSENSITIVE);
        Matcher matcher = funcPattern.matcher(beforePosition);
        
        String lastFunction = "global";
        while (matcher.find()) {
            lastFunction = matcher.group(1);
        }
        
        return lastFunction;
    }
    
    /**
     * Extracts require statements
     */
    private void extractRequires(String content, String filePath, AnalysisResult result) {
        Matcher matcher = REQUIRE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String requiredFile = matcher.group(1).trim();
            String functionName = findContainingFunction(content, matcher.start());
            
            RequireStatement requireStmt = new RequireStatement(requiredFile, filePath, functionName);
            result.addRequire(requireStmt);
        }
    }
    
    /**
     * Extracts include statements
     */
    private void extractIncludes(String content, String filePath, AnalysisResult result) {
        Matcher matcher = INCLUDE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String includedFile = matcher.group(1).trim();
            String functionName = findContainingFunction(content, matcher.start());
            
            IncludeStatement includeStmt = new IncludeStatement(includedFile, filePath, functionName);
            result.addInclude(includeStmt);
        }
    }
    
    /**
     * Extracts CURL calls
     */
    private void extractCurlCalls(String content, String filePath, AnalysisResult result) {
        extractCurlCall(content, filePath, result, CURL_INIT_PATTERN, "curl_init");
        extractCurlCall(content, filePath, result, CURL_SETOPT_PATTERN, "curl_setopt");
        extractCurlCall(content, filePath, result, CURL_EXEC_PATTERN, "curl_exec");
    }
    
    private void extractCurlCall(String content, String filePath, AnalysisResult result, Pattern pattern, String callType) {
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String functionName = findContainingFunction(content, matcher.start());
            
            CurlCall curlCall = new CurlCall(callType, filePath, functionName);
            result.addCall(curlCall);
        }
    }
    
    /**
     * Extracts function definitions
     */
    private void extractFunctions(String content, String filePath, AnalysisResult result) {
        Matcher matcher = FUNCTION_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String functionName = matcher.group(1).trim();
            
            PHPFunction function = new PHPFunction(functionName, filePath);
            result.addFunction(function);
        }
    }
    
    /**
     * Extracts class definitions
     */
    private void extractClasses(String content, String filePath, AnalysisResult result) {
        Matcher matcher = CLASS_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String className = matcher.group(1).trim();
            
            PHPClass phpClass = new PHPClass(className, filePath);
            result.addClass(phpClass);
        }
    }
    
    /**
     * Extracts trait uses
     */
    private void extractTraitUses(String content, String filePath, AnalysisResult result) {
        Matcher matcher = USE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String traitName = matcher.group(1).trim();
            
            TraitUse traitUse = new TraitUse(traitName, filePath);
            result.addTraitUse(traitUse);
        }
    }
    
    /**
     * Extracts class extends
     */
    private void extractClassExtends(String content, String filePath, AnalysisResult result) {
        Matcher matcher = EXTENDS_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String parentClass = matcher.group(1).trim();
            
            ClassExtend classExtend = new ClassExtend(parentClass, filePath);
            result.addClassExtend(classExtend);
        }
    }
    
    /**
     * Extracts interface implementations
     */
    private void extractImplements(String content, String filePath, AnalysisResult result) {
        Matcher matcher = IMPLEMENTS_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String interfaces = matcher.group(1).trim();
            String[] interfaceArray = interfaces.split(",");
            
            for (String interfaceName : interfaceArray) {
                InterfaceImplementation impl = new InterfaceImplementation(interfaceName.trim(), filePath);
                result.addImplementation(impl);
            }
        }
    }
    
    /**
     * Extracts interface definitions
     */
    private void extractInterfaces(String content, String filePath, AnalysisResult result) {
        Matcher matcher = INTERFACE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String interfaceName = matcher.group(1).trim();
            
            PHPInterface phpInterface = new PHPInterface(interfaceName, filePath);
            result.addInterface(phpInterface);
        }
    }
}