package net.gcae.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.gcae.utils.analyzer.PHPAnalyzer;
import net.gcae.utils.model.AnalysisResult;
import net.gcae.utils.reporter.HTMLReporter;

/**
 * PHP Extractor - Analyzes PHP files and generates HTML reports
 * Main class for the PHP code analysis tool
 */
public class PHPExtractor {
    
    private static final Logger logger = LoggerFactory.getLogger(PHPExtractor.class);
    
    public static void main(String[] args) {
        logger.info("Starting PHP Extractor...");
        
        if (args.length < 1) {
            System.err.println("Usage: java -jar PHPExtractor.jar <repository_path> [output_directory]");
            System.err.println("  repository_path: Path to the PHP repository folder to analyze");
            System.err.println("  output_directory: Optional output directory for reports (default: ./reports)");
            System.exit(1);
        }
        
        String repositoryPath = args[0];
        String outputDirectory = args.length > 1 ? args[1] : "./reports";
        
        try {
            // Validate input directory
            Path repoPath = Paths.get(repositoryPath);
            if (!Files.exists(repoPath) || !Files.isDirectory(repoPath)) {
                logger.error("Repository path does not exist or is not a directory: {}", repositoryPath);
                System.exit(1);
            }
            
            // Create output directory if it doesn't exist
            Path outputPath = Paths.get(outputDirectory);
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                logger.info("Created output directory: {}", outputDirectory);
            }
            
            // Initialize analyzer and reporter
            PHPAnalyzer analyzer = new PHPAnalyzer();
            HTMLReporter reporter = new HTMLReporter(outputDirectory);
            
            // Analyze PHP files
            logger.info("Starting analysis of repository: {}", repositoryPath);
            AnalysisResult result = analyzer.analyzeRepository(repositoryPath);
            
            // Generate reports
            logger.info("Generating HTML reports...");
            reporter.generateReports(result);
            
            // Print summary
            printSummary(result);
            
            logger.info("PHP Extractor completed successfully. Reports generated in: {}", outputDirectory);
            
        } catch (Exception e) {
            logger.error("Error during extraction process", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void printSummary(AnalysisResult result) {
        System.out.println("\n=== PHP Extractor Summary ===");
        System.out.println("Files analyzed: " + result.getPhpFiles().size());
        System.out.println("SQL queries found: " + result.getQueries().size());
        System.out.println("Requires found: " + result.getRequires().size());
        System.out.println("Includes found: " + result.getIncludes().size());
        System.out.println("CURL calls found: " + result.getCalls().size());
        System.out.println("Functions found: " + result.getFunctions().size());
        System.out.println("Classes found: " + result.getClasses().size());
        System.out.println("Trait uses found: " + result.getTraitUses().size());
        System.out.println("Class extends found: " + result.getClassExtends().size());
        System.out.println("Interface implementations found: " + result.getImplements().size());
        System.out.println("Interfaces found: " + result.getInterfaces().size());
        System.out.println("==============================\n");
    }
}