# PHP Code Extractor

A comprehensive Java application designed to analyze PHP projects and generate detailed Excel reports containing code inventories and metrics. This tool performs recursive analysis of entire project directories, including all subdirectories, to provide complete code audit capabilities.

## Overview

The PHP Code Extractor is a Maven-based Java application that recursively scans PHP project directories to extract and analyze various code elements, generating professional Excel reports with detailed inventories and statistics. This tool is particularly useful for code audits, legacy system analysis, project documentation, and migration planning.

## Features

### Code Analysis Capabilities

- **Recursive Directory Analysis**: Automatically scans all subdirectories and files
- **File Inventory**: Analyzes PHP, HTML, JavaScript, and CSS files with metadata
- **SQL Query Detection**: Identifies database queries with complexity analysis and data source detection
- **Dependency Tracking**: Maps require and include statements with file locations
- **CURL Operations**: Detects external API calls, authentication patterns, and ColdFusion integration
- **Function Analysis**: Catalogs function definitions with complexity metrics based on line count
- **Class Structure**: Maps class definitions, inheritance relationships, and method counts
- **Interface Mapping**: Identifies interface definitions and implementations
- **Trait Usage**: Tracks trait usage and inheritance patterns
- **Smart File Filtering**: Automatically excludes irrelevant directories and binary files

### Enhanced Analysis Features

- **SQL Complexity Assessment**: Analyzes query complexity based on length, JOINs, and subqueries
- **Function Complexity Metrics**: Evaluates function complexity using line count analysis
- **Data Source Detection**: Identifies MySQLi, PDO, and legacy MySQL connections
- **Encoding Handling**: Robust file reading with multiple encoding fallbacks
- **Binary File Protection**: Automatically skips binary files to prevent parsing errors

### Report Generation

The application generates a comprehensive Excel workbook with up to 12 sheets:

1. **ITx Inventory Overview** - Summary statistics and project metrics
2. **phpFilesReport** - Complete file inventory with size, type, and line count
3. **phpQueriesReport** - SQL queries with complexity analysis and table mapping
4. **phpRequiresReport** - File require statements and their locations
5. **phpIncludesReport** - File include statements and their locations
6. **phpCallsReport** - CURL calls and external API interactions for ColdFusion integration
7. **phpFunctionReport** - Function definitions with complexity metrics and parameters
8. **phpClassesReport** - Class definitions with inheritance and interface information
9. **phpInheritTritReport** - Trait usage patterns and relationships
10. **phpInheritClassReport** - Class inheritance relationships and hierarchies
11. **phpImplementReport** - Interface implementations and relationships
12. **phpInterfacesReport** - Interface definitions with method counts

### Smart Directory Filtering

The analyzer automatically excludes common non-source directories:
- Version control: `.git/`, `.svn/`
- Dependencies: `node_modules/`, `vendor/`
- Build artifacts: `target/`, `build/`, `dist/`
- Logs and temporary files: `logs/`, hidden files (starting with `.`)

## System Requirements

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6.0 or higher
- Minimum 512 MB RAM (1 GB recommended for large projects)
- Sufficient disk space for generated reports

## Installation and Setup

### 1. Clone or Download the Project

```bash
git clone <repository-url>
cd PHPExtractor
```

### 2. Build the Application

```bash
mvn clean package
```

This will create an executable JAR file in the `target` directory: `PHPExtractor-1.0.0.jar`

### 3. Project Structure

```
PHPExtractor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── net/gcae/utils/extractor/
│   │   │       ├── PHPExtractorMain.java
│   │   │       ├── analyzer/
│   │   │       │   └── PHPAnalyzer.java
│   │   │       ├── model/
│   │   │       │   ├── AnalysisResult.java
│   │   │       │   └── [Model Classes]
│   │   │       └── report/
│   │   │           └── ExcelReportGenerator.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
├── target/
├── logs/
├── pom.xml
└── README.md
```

## Usage

### Basic Usage

```bash
java -jar target/PHPExtractor-1.0.0.jar <php-project-path> [output-directory]
```

### Parameters

- `<php-project-path>`: Required. Path to the PHP project directory to analyze (will be analyzed recursively)
- `[output-directory]`: Optional. Directory where the Excel report will be saved (defaults to current directory)

### Examples

```bash
# Analyze a PHP project recursively and save report in current directory
java -jar target/PHPExtractor-1.0.0.jar /path/to/php/project

# Analyze a PHP project and save report in specific directory
java -jar target/PHPExtractor-1.0.0.jar /path/to/php/project /path/to/reports

# Analyze current directory PHP project with all subdirectories
java -jar target/PHPExtractor-1.0.0.jar . ./reports

# Real-world example
java -jar target/PHPExtractor-1.0.0.jar /Users/antonio.sanchez/workarea/repos/php-shopping-cart ./reports
```

### Output

The application generates an Excel file named `IT_CodeAnalysis_[timestamp].xlsx` containing detailed analysis results.

**Sample Output:**
```
Analysis completed successfully!
Report generated: ./reports/IT_CodeAnalysis_20250729_003321.xlsx

Summary:
- Files analyzed: 45
- SQL queries found: 23
- Functions found: 67
- Classes found: 12
```

## Analysis Details

### File Types Supported

- `.php` - PHP source files (primary analysis target)
- `.html`, `.htm` - HTML template files
- `.js` - JavaScript files
- `.css` - CSS stylesheets

### SQL Query Analysis

The tool detects various SQL patterns including:
- **Basic Operations**: SELECT, INSERT, UPDATE, DELETE statements
- **DDL Operations**: CREATE, DROP, ALTER statements
- **Complex Queries**: JOINs, subqueries, unions
- **Data Source Detection**: MySQLi, PDO, legacy MySQL functions
- **Complexity Assessment**: Based on query length, JOIN count, and subquery complexity

**SQL Query Format**: `QueryName | DB Table | File:Line | Data Source | SQL Query | Complexity`

### CURL Detection for ColdFusion Integration

Identifies three main CURL operations for ColdFusion authentication and calls:
- `curl_init()` - Session initialization with target URLs
- `curl_setopt()` - Configuration settings including authentication
- `curl_exec()` - Execution calls

### Complexity Metrics

**Function Complexity** (based on line count):
- **Low**: ≤ 20 lines - Simple functions with minimal logic
- **Medium**: 21-50 lines - Moderate complexity with conditional logic
- **High**: > 50 lines - Complex functions requiring refactoring consideration

**Query Complexity** (based on multiple factors):
- **Low**: Simple queries < 200 characters, no JOINs
- **Medium**: Moderate queries 200-500 characters, 1-2 JOINs, or simple subqueries
- **High**: Complex queries > 500 characters, 3+ JOINs, or multiple subqueries

### PHP Language Features Detected

- **Object-Oriented Programming**: Classes, interfaces, traits, inheritance
- **Namespaces**: Use statements and namespace declarations
- **File Dependencies**: require/include statements with paths
- **Function Definitions**: Parameters, complexity analysis
- **Class Relationships**: Extends, implements relationships

## Logging and Monitoring

The application uses SLF4J with Logback for comprehensive logging:

### Log Destinations
- **Console Output**: Real-time progress and status updates
- **File Logging**: Detailed logs saved to `logs/php-extractor.log`
- **Log Rotation**: Automatic rotation by date and size (10MB max)
- **Log Retention**: 30 days of historical logs

### Log Levels

- **INFO**: General application flow, file counts, and completion statistics
- **DEBUG**: Detailed analysis progress, file-by-file processing (for troubleshooting)
- **WARN**: Non-critical issues (e.g., unreadable files, encoding problems)
- **ERROR**: Critical errors that prevent completion

### Sample Log Output

```
00:33:21.306 [main] INFO n.gcae.utils.extractor.PHPExtractor - Starting PHP Code Analysis for: /path/to/project
00:33:21.312 [main] INFO n.g.u.extractor.analyzer.PHPAnalyzer - Starting analysis of project: /path/to/project
00:33:21.315 [main] INFO n.g.u.extractor.analyzer.PHPAnalyzer - Found 45 valid files to analyze
00:33:21.357 [main] INFO n.g.u.extractor.analyzer.PHPAnalyzer - Analysis completed. Found 45 files, 23 SQL queries, 67 functions, 12 classes
00:33:21.360 [main] INFO n.g.u.e.report.ExcelReportGenerator - Generating Excel report: ./IT_CodeAnalysis_20250729_003321.xlsx
```

## Error Handling and Resilience

The application includes robust error handling for common scenarios:

### File Processing
- **Invalid file paths**: Validates input directories before processing
- **Encoding issues**: Attempts UTF-8, then ISO-8859-1, then skips problematic files
- **Binary files**: Automatically detects and skips binary files
- **Permission issues**: Handles read permission errors gracefully

### Memory and Performance
- **Large projects**: Efficiently processes projects with 10,000+ files
- **Memory management**: Streaming file processing to handle large codebases
- **Progress tracking**: Real-time feedback on analysis progress

### Partial Failures
- **Individual file failures**: Continues analysis even if specific files fail
- **Missing directories**: Creates output directories automatically
- **Incomplete data**: Generates reports with available data, logs missing elements

## Performance Considerations

### Resource Usage
- **Memory Usage**: Approximately 50-100 MB for typical projects (1,000-5,000 files)
- **Processing Speed**: ~100-500 files per second (depending on file size and complexity)
- **Large Projects**: Successfully tested with projects containing 10,000+ files
- **Output Size**: Excel files typically 1-10 MB depending on project complexity

### Optimization Features
- **Smart filtering**: Excludes non-source directories automatically
- **Streaming processing**: Files processed individually to optimize memory usage
- **Efficient regex**: Optimized pattern matching for code element detection

## Troubleshooting

### Common Issues and Solutions

1. **"Invalid project path" Error**
   ```bash
   Error: Invalid project path - /path/does/not/exist
   ```
   - **Solution**: Ensure the provided path exists and is a directory
   - **Check**: File permissions for read access on the directory

2. **"MalformedInputException" Warnings**
   ```
   WARN Could not read file with UTF-8, trying with ISO-8859-1
   ```
   - **Status**: Normal behavior for binary files or files with special encoding
   - **Action**: Files are automatically handled with encoding fallback or skipped

3. **No Files Found**
   ```
   Warning: No PHP files or analyzable content found in the specified directory.
   ```
   - **Check**: Ensure directory contains `.php`, `.html`, `.js`, or `.css` files
   - **Verify**: Files are not in excluded directories (`.git/`, `vendor/`, etc.)

4. **Out of Memory Errors**
   ```bash
   java.lang.OutOfMemoryError: Java heap space
   ```
   - **Solution**: Increase JVM heap size:
   ```bash
   java -Xmx2g -jar target/PHPExtractor-1.0.0.jar /path/to/project
   ```

5. **Excel Report Not Generated**
   - **Check**: Output directory permissions
   - **Verify**: Sufficient disk space
   - **Review**: Log files for specific error messages

### Debug Mode

Enable detailed logging for troubleshooting:

```bash
# Enable debug logging
java -Dlogback.configurationFile=src/main/resources/logback.xml -jar target/PHPExtractor-1.0.0.jar <path>

# Or set logging level via system property
java -Droot.level=DEBUG -jar target/PHPExtractor-1.0.0.jar <path>
```

### Validation Steps

Before reporting issues, verify:

1. **Java Version**: `java -version` (requires JDK 11+)
2. **File Permissions**: Read access to source directory
3. **Directory Structure**: Contains expected file types
4. **Available Space**: Sufficient disk space for reports
5. **Log Analysis**: Review `logs/php-extractor.log` for detailed error information

## Dependencies and Compatibility

### Core Dependencies (Updated)

- **Apache POI 5.2.4**: Excel file generation and manipulation
- **Commons IO 2.15.1**: Enhanced file operations and utilities (updated for POI compatibility)
- **SLF4J 2.0.9**: Structured logging framework
- **Logback 1.4.12**: Logging implementation with rotation support

### Development Dependencies

- **JUnit Jupiter 5.10.0**: Comprehensive unit testing framework
- **Maven Shade Plugin**: Creates executable# PHP Code Extractor

A comprehensive Java application designed to analyze PHP projects and generate detailed Excel reports containing code inventories and metrics.

## Overview

The PHP Code Extractor is a Maven-based Java application that scans PHP project directories to extract and analyze various code elements, generating professional Excel reports with detailed inventories and statistics. This tool is particularly useful for code audits, legacy system analysis, and project documentation.

## Features

### Code Analysis Capabilities

- **File Inventory**: Analyzes PHP, HTML, JavaScript, and CSS files
- **SQL Query Detection**: Identifies database queries with complexity analysis
- **Dependency Tracking**: Maps require and include statements
- **CURL Operations**: Detects external API calls and authentication patterns
- **Function Analysis**: Catalogs function definitions with complexity metrics
- **Class Structure**: Maps class definitions, inheritance, and relationships
- **Interface Mapping**: Identifies interface definitions and implementations
- **Trait Usage**: Tracks trait usage and inheritance patterns

### Report Generation

The application generates a comprehensive Excel workbook with up to 11 sheets:

1. **ITx Inventory Overview** - Summary statistics of all analyzed elements
2. **phpFilesReport** - Complete file inventory with metadata
3. **phpQueriesReport** - SQL queries with complexity analysis
4. **phpRequiresReport** - File require statements and locations
5. **phpIncludesReport** - File include statements and locations
6. **phpCallsReport** - CURL calls and external API interactions
7. **phpFunctionReport** - Function definitions with complexity metrics
8. **phpClassesReport** - Class definitions and relationships
9. **phpInheritTritReport** - Trait usage patterns
10. **phpInheritClassReport** - Class inheritance relationships
11. **phpImplementReport** - Interface implementations
12. **phpInterfacesReport** - Interface definitions

## System Requirements

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6.0 or higher
- Minimum 512 MB RAM (1 GB recommended for large projects)
- Sufficient disk space for generated reports

## Installation and Setup

### 1. Clone or Download the Project

```bash
git clone <repository-url>
cd PHPExtractor
```

### 2. Build the Application

```bash
mvn clean package
```

This will create an executable JAR file in the `target` directory: `PHPExtractor-1.0.0.jar`

### 3. Project Structure

```
PHPExtractor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── net/gcae/utils/extractor/
│   │   │       ├── PHPExtractorMain.java
│   │   │       ├── analyzer/
│   │   │       │   └── PHPAnalyzer.java
│   │   │       ├── model/
│   │   │       │   ├── AnalysisResult.java
│   │   │       │   └── [Model Classes]
│   │   │       └── report/
│   │   │           └── ExcelReportGenerator.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
├── target/
├── logs/
├── pom.xml
└── README.md
```

## Usage

### Basic Usage

```bash
java -jar target/PHPExtractor-1.0.0.jar <php-project-path> [output-directory]
```

### Parameters

- `<php-project-path>`: Required. Path to the PHP project directory to analyze
- `[output-directory]`: Optional. Directory where the Excel report will be saved (defaults to current directory)

### Examples

```bash
# Analyze a PHP project and save report in current directory
java -jar target/PHPExtractor-1.0.0.jar /path/to/php/project

# Analyze a PHP project and save report in specific directory
java -jar target/PHPExtractor-1.0.0.jar /path/to/php/project /path/to/reports

# Analyze current directory PHP project
java -jar target/PHPExtractor-1.0.0.jar .
```

### Output

The application generates an Excel file named `IT_CodeAnalysis_[timestamp].xlsx` containing detailed analysis results.

## Analysis Details

### File Types Supported

- `.php` - PHP source files
- `.html`, `.htm` - HTML files
- `.js` - JavaScript files
- `.css` - CSS stylesheets

### SQL Query Analysis

The tool detects various SQL patterns including:
- SELECT, INSERT, UPDATE, DELETE statements
- CREATE, DROP, ALTER DDL statements
- Complex queries with JOINs and subqueries
- Complexity assessment based on query length and structure

### CURL Detection

Identifies three main CURL operations:
- `curl_init()` - Session initialization
- `curl_setopt()` - Configuration settings
- `curl_exec()` - Execution calls

### Complexity Metrics

**Function Complexity** (based on line count):
- Low: ≤ 20 lines
- Medium: 21-50 lines
- High: > 50 lines

**Query Complexity** (based on length, JOINs, subqueries):
- Low: Simple queries < 200 characters
- Medium: Moderate queries 200-500 characters or with JOINs
- High: Complex queries > 500 characters or multiple JOINs/subqueries

## Logging

The application uses SLF4J with Logback for comprehensive logging:

- **Console Output**: Real-time progress and status updates
- **File Logging**: Detailed logs saved to `logs/php-extractor.log`
- **Log Rotation**: Automatic rotation by date and size (10MB max)
- **Log Retention**: 30 days of historical logs

### Log Levels

- **INFO**: General application flow and statistics
- **DEBUG**: Detailed analysis progress (for troubleshooting)
- **WARN**: Non-critical issues (e.g., unreadable files)
- **ERROR**: Critical errors that prevent completion

## Error Handling

The application includes robust error handling for common scenarios:

- **Invalid file paths**: Validates input directories before processing
- **Encoding issues**: Attempts multiple character encodings for file reading
- **Binary files**: Automatically skips binary files to prevent parsing errors
- **Memory management**: Efficient processing for large codebases
- **Partial failures**: Continues analysis even if individual files fail

## Performance Considerations

- **Memory Usage**: Approximately 50-100 MB for typical projects
- **Processing Speed**: ~100-500 files per second (depending on file size)
- **Large Projects**: Tested with projects containing 10,000+ files
- **Output Size**: Excel files typically 1-10 MB depending on project complexity

## Troubleshooting

### Common Issues

1. **"Invalid project path" Error**
   - Ensure the provided path exists and is a directory
   - Check file permissions for read access

2. **"MalformedInputException" Warnings**
   - Normal for binary files; the application automatically handles these
   - Files are skipped and analysis continues

3. **Out of Memory Errors**
   - Increase JVM heap size: `java -Xmx2g -jar PHPExtractor-1.0.0.jar ...`

4. **Missing Sheets in Excel Report**
   - Normal behavior when no data is found for specific categories
   - Check logs for detailed information about what was analyzed

### Debug Mode

Enable detailed logging for troubleshooting:

```bash
java -Dlogback.configurationFile=src/main/resources/logback.xml -jar target/PHPExtractor-1.0.0.jar <path>
```

## Dependencies

### Core Dependencies

- **Apache POI 5.2.4**: Excel file generation and manipulation
- **Commons IO 2.11.0**: Enhanced file operations and utilities
- **SLF4J 2.0.9**: Structured logging framework
- **Logback 1.4.12**: Logging implementation

### Development Dependencies

- **JUnit Jupiter 5.10.0**: Unit testing framework

## Building from Source

### Prerequisites

- JDK 11 or higher
- Apache Maven 3.6.0+

### Build Commands

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package (creates executable JAR)
mvn package

# Create executable JAR with dependencies
mvn clean package shade:shade
```

## License

This project is distributed under the terms specified by the organization. Please refer to the license file for detailed information.

## Contributing

For contributions, bug reports, or feature requests, please follow the organization's contribution guidelines.

## Support

For technical support or questions:

1. Check the troubleshooting section above
2. Review log files for detailed error information
3. Contact the development team with specific error messages and log excerpts

## Version History

- **1.0.0**: Initial release with comprehensive PHP analysis capabilities