package net.gcae.utils.model;

/**
 * Represents a require statement
 */
public class RequireStatement {
    private String requiredFile;
    private String filePath;
    private String functionName;
    
    public RequireStatement(String requiredFile, String filePath, String functionName) {
        this.requiredFile = requiredFile;
        this.filePath = filePath;
        this.functionName = functionName;
    }
    
    // Getters
    public String getRequiredFile() { return requiredFile; }
    public String getFilePath() { return filePath; }
    public String getFunctionName() { return functionName; }
}