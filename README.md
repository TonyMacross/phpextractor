# PHP Extractor

A comprehensive Java-based tool for analyzing PHP repositories and generating detailed HTML reports about various PHP code elements with externalized HTML templates.

## Overview

PHP Extractor is a Maven-based Java 11 application that scans PHP repositories and extracts important code elements such as database queries, file inclusions, function definitions, class structures, and more. It generates separate HTML reports for each category of findings using externalized HTML templates for easy customization.

## Features

The tool analyzes PHP files and generates reports for:

- **File Inventory** - Complete list of all PHP files in the repository
- **SQL Queries** - Database queries using `db_query()` with table detection
- **Require Statements** - All `require()` calls and their locations
- **Include Statements** - All `include()` calls and their locations
- **CURL Calls** - Authentication and API calls using `curl_init`, `curl_setopt`, `curl_exec`
- **Function Definitions** - All user-defined functions
- **Class Definitions** - All class declarations
- **Trait Usage** - All `use` statements for traits
- **Class Inheritance** - All `extends` relationships
- **Interface Implementations** - All `implements` declarations
- **Interface Definitions** - All interface declarations

## New Template System

### Externalized HTML Templates
- HTML templates are now stored in `src/main/resources/html-templates.properties`
- Easy customization without modifying Java code
- Variable substitution using `${variableName}` syntax
- Centralized template management

### Template Structure
The templates are organized as follows:
- `html.header` and `html.footer` - Common page structure
- `report.{category}.title` - Page titles
- `report.{category}.heading` - Section headings with statistics
- `report.{category}.table.header` - Table headers
- `report.{category}.table.row` - Table row templates
- `report.{category}.table.footer` - Table closing tags

### Customizing Templates

To customize the HTML output:

1. **Modify styles**: Edit the CSS in the `html.header` template
2. **Change layout**: Modify the table structure templates
3. **Add new fields**: Update row templates and add corresponding variables
4. **Customize branding**: Update headers and footers

Example template customization:
```properties
# Custom heading with company branding
report.files.heading=<h1><img src="logo.png"/> PHP Files Inventory</h1>\n\
<div class="stats">Total files found: <strong>${count}</strong></div>
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone or download the project
2. Navigate to the project directory
3. Build the project:

```bash
mvn clean package
```

This will create an executable JAR file in the `target` directory.

## Usage

### Basic Usage

```bash
java -jar target/PHPExtractor-1.0.0.jar <repository_path> [output_directory]
```

### Parameters

- `repository_path` (required): Path to the PHP repository folder to analyze
- `output_directory` (optional): Directory where HTML reports will be generated (default: `./reports`)

### Examples

```bash
# Analyze a PHP project in the current directory
java -jar target/PHPExtractor-1.0.0.jar ./my-php-project

# Analyze a PHP project and save reports to a specific directory
java -jar target/PHPExtractor-1.0.0.jar /path/to/php/project /path/to/output/reports

# Analyze using the Maven exec plugin
mvn exec:java -Dexec.mainClass="net.gcae.utils.PHPExtractor" -Dexec.args="./my-php-project"
```

## Generated Reports

The tool generates up to 11 different HTML reports, each focusing on a specific aspect of the PHP codebase:

| Report File | Description |
|-------------|-------------|
| `phpFilesReport.html` | Inventory of all PHP files |
| `phpQueriesReport.html` | SQL queries with table detection |
| `phpRequiresReport.html` | Require statements |
| `phpIncludesReport.html` | Include statements |
| `phpCallsReport.html` | CURL API calls |
| `phpFunctionsReport.html` | Function definitions |
| `phpClassesReport.html` | Class definitions |
| `phpInheritsFromTritReport.html` | Trait usage |
| `phpInheritFromClassReport.html` | Class inheritance |
| `phpImplementsReport.html` | Interface implementations |
| `phpInterfaceReport.html` | Interface definitions |

**Note**: Only reports with actual findings are generated. If no content is found for a specific category, the corresponding report will be skipped and a notification will appear in the log and console.

## Project Structure

```
PHPExtractor/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── net/
│   │   │       └── gcae/
│   │   │           └── utils/
│   │   │               ├── PHPExtractor.java          # Main class
│   │   │               ├── analyzer/
│   │   │               │   └── PHPAnalyzer.java       # Core analysis engine
│   │   │               ├── model/                     # Data models
│   │   │               │   └── *.java
│   │   │               └── reporter/
│   │   │                   ├── HTMLReporter.java      # HTML report generator
│   │   │                   └── TemplateManager.java   # Template management
│   │   └── resources/
│   │       ├── logback.xml                           # Logging configuration
│   │       └── html-templates.properties             # HTML templates
│   └── test/
│       └── java/                                     # Unit tests (future)
├── target/                                           # Generated files
└── logs/                                            # Application logs
```

## Template Management

### TemplateManager Class
The `TemplateManager` class provides:
- Centralized template loading from properties files
- Variable substitution with validation
- HTML escaping for security
- Template existence validation
- Support for custom template sources

### Key Methods
```java
// Get template with variable replacement
String html = templateManager.getTemplate("report.files.heading", 
    "count", "150");

// Get template with automatic HTML escaping
String safeHtml = templateManager.getTemplateWithEscaping("report.files.row",
    "fileName", userInput);

// Validate required templates exist
boolean valid = templateManager.validateRequiredTemplates(
    "html.header", "html.footer", "report.files.title");
```

## Configuration

### Maven Configuration
- **Group ID**: `net.gcae.utils`
- **Artifact ID**: `PHPExtractor`
- **Main Class**: `net.gcae.utils.PHPExtractor`
- **Java Version**: 11
- **Encoding**: UTF-8

### Dependencies
- Apache Commons IO (file operations)
- Apache Commons Lang (string utilities)
- SLF4J + Logback (logging)
- Jackson (JSON processing)
- JUnit Jupiter (testing)

## Advanced Customization

### Creating Custom Templates

1. **Copy the default template file**:
   ```bash
   cp src/main/resources/html-templates.properties custom-templates.properties
   ```

2. **Modify templates as needed**:
   ```properties
   # Add custom CSS
   html.header=<!DOCTYPE html>\n\
   <html lang="en">\n\
   <head>\n\
       <link rel="stylesheet" href="custom.css">\n\
       <!-- ... rest of header -->
   
   # Custom table styling
   report.files.table.row=<tr class="custom-row">\n\
   <td class="file-name">${fileName}</td>\n\
   <!-- ... rest of row -->
   ```

3. **Use custom templates programmatically**:
   ```java
   Properties customProps = new Properties();
   customProps.load(new FileInputStream("custom-templates.properties"));
   TemplateManager customManager = new TemplateManager(customProps);
   HTMLReporter reporter = new HTMLReporter(outputDir, customManager);
   ```

### Template Variables

Available variables for each report type:

#### Common Variables
- `${title}` - Report title
- `${timestamp}` - Generation timestamp
- `${count}` - Item count

#### Files Report
- `${fileName}` - File name
- `${relativePath}` - Relative file path
- `${fileSize}` - File size in bytes

#### Queries Report
- `${queryPreview}` - SQL query preview
- `${tableName}` - Database table name
- `${filePath}` - File containing query
- `${functionName}` - Function containing query

#### Other Reports
Similar patterns with relevant field names for each PHP element type.

## Error Handling and Logging

### Template Loading
- Graceful fallback if templates are missing
- Detailed error logging for debugging
- Validation of required templates on startup

### Variable Substitution
- Safe handling of null values
- HTML escaping to prevent XSS
- Clear error messages for malformed templates

## Development

### Adding New Report Types

1. **Add templates to properties file**:
   ```properties
   report.newtype.title=New Element Type
   report.newtype.heading=<h1>New Elements</h1>
   report.newtype.table.header=<table><thead>...
   report.newtype.table.row=<tr><td>${field1}</td>...
   report.newtype.table.footer=</tbody></table>
   ```

2. **Create model class** in `net.gcae.utils.model`

3. **Add extraction logic** to `PHPAnalyzer.java`

4. **Add report generation method** to `HTMLReporter.java`:
   ```java
   private void generateNewTypeReport(AnalysisResult result) throws IOException {
       // Use templates with TemplateManager
       String title = templateManager.getTemplate("report.newtype.title");
       // ... implementation
   }
   ```

### Testing Templates

```bash
# Validate all templates load correctly
mvn test -Dtest=TemplateManagerTest

# Generate reports with test data
mvn exec:java -Dexec.args="./test-data ./test-output"
```

## Version History

- **v1.1.0** - Template System
  - Externalized HTML templates to properties file
  - Added TemplateManager for centralized template handling
  - Improved customization capabilities
  - Enhanced error handling for templates

- **v1.0.0** - Initial release
  - Basic PHP file analysis
  - HTML report generation
  - Support for all major PHP constructs
  - Encoding handling and error recovery
