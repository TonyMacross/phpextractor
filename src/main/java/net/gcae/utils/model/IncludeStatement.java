package net.gcae.utils.model;

/**
 * Represents an include statement
 */
public class IncludeStatement {
    private String includedFile;
    private String filePath;
    private String functionName;
    
    public IncludeStatement(String includedFile, String filePath, String functionName) {
        this.includedFile = includedFile;
        this.filePath = filePath;
        this.functionName = functionName;
    }
    
    // Getters
    public String getIncludedFile() { return includedFile; }
    public String getFilePath() { return filePath; }
    public String getFunctionName() { return functionName; }
}