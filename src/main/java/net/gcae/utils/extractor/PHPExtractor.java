package net.gcae.utils.extractor;

import net.gcae.utils.extractor.analyzer.PHPAnalyzer;
import net.gcae.utils.extractor.model.AnalysisResult;
import net.gcae.utils.extractor.report.ExcelReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main class for PHP Code Extractor application
 */
public class PHPExtractor {
    
    private static final Logger logger = LoggerFactory.getLogger(PHPExtractor.class);
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar PHPExtractor.jar <php-project-path> [output-path]");
            System.err.println("Example: java -jar PHPExtractor.jar /path/to/php/project ./reports");
            System.exit(1);
        }
        
        String phpProjectPath = args[0];
        String outputPath = args.length > 1 ? args[1] : ".";
        
        try {
            logger.info("Starting PHP Code Analysis for: {}", phpProjectPath);
            logger.info("Output directory: {}", outputPath);
            
            // Validate input directory
            File projectDir = new File(phpProjectPath);
            if (!projectDir.exists() || !projectDir.isDirectory()) {
                logger.error("Invalid project path: {} - Directory does not exist or is not a directory", phpProjectPath);
                System.err.println("Error: Invalid project path - " + phpProjectPath);
                System.exit(1);
            }
            
            // Validate/create output directory
            File outputDir = new File(outputPath);
            if (!outputDir.exists()) {
                boolean created = outputDir.mkdirs();
                if (!created) {
                    logger.error("Could not create output directory: {}", outputPath);
                    System.err.println("Error: Could not create output directory - " + outputPath);
                    System.exit(1);
                }
                logger.info("Created output directory: {}", outputPath);
            }
            
            // Create analyzer and perform analysis
            PHPAnalyzer analyzer = new PHPAnalyzer();
            AnalysisResult result = analyzer.analyzeProject(projectDir);
            
            // Check if any data was found
            if (!result.hasData()) {
                logger.warn("No analyzable data found in project directory: {}", phpProjectPath);
                System.out.println("Warning: No PHP files or analyzable content found in the specified directory.");
            }
            
            // Generate Excel report
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportFileName = String.format("IT_CodeAnalysis_%s.xlsx", timestamp);
            String reportPath = new File(outputDir, reportFileName).getAbsolutePath();
            
            ExcelReportGenerator reportGenerator = new ExcelReportGenerator();
            reportGenerator.generateReport(result, reportPath);
            
            logger.info("Analysis completed successfully. Report generated: {}", reportPath);
            System.out.println("Analysis completed successfully!");
            System.out.println("Report generated: " + reportPath);
            System.out.println("\nSummary:");
            System.out.println("- Files analyzed: " + (result.getFiles() != null ? result.getFiles().size() : 0));
            System.out.println("- SQL queries found: " + (result.getSqlQueries() != null ? result.getSqlQueries().size() : 0));
            System.out.println("- Functions found: " + (result.getFunctions() != null ? result.getFunctions().size() : 0));
            System.out.println("- Classes found: " + (result.getClasses() != null ? result.getClasses().size() : 0));
            
        } catch (Exception e) {
            logger.error("Error during analysis: {}", e.getMessage(), e);
            System.err.println("Error during analysis: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}