package net.gcae.utils.extractor.model;

/**
 * Represents a function definition
 */
public class FunctionDefinition {
    private String functionName;
    private String fileLocation;
    private int lineCount;
    private String complexity;
    private String parameters;
    
    public FunctionDefinition(String functionName, String fileLocation, int lineCount, String complexity, String parameters) {
        this.functionName = functionName;
        this.fileLocation = fileLocation;
        this.lineCount = lineCount;
        this.complexity = complexity;
        this.parameters = parameters;
    }
    
    // Getters and Setters
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public int getLineCount() { return lineCount; }
    public void setLineCount(int lineCount) { this.lineCount = lineCount; }
    
    public String getComplexity() { return complexity; }
    public void setComplexity(String complexity) { this.complexity = complexity; }
    
    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }
}