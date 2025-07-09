package net.gcae.utils.model;

/**
 * Represents a PHP function definition found in the code
 */
public class PHPFunction {
    private String functionName;
    private String filePath;
    
    /**
     * Constructor for PHPFunction
     * 
     * @param functionName The name of the function
     * @param filePath The file path where the function was defined
     */
    public PHPFunction(String functionName, String filePath) {
        this.functionName = functionName;
        this.filePath = filePath;
    }
    
    // Getters
    public String getFunctionName() { return functionName; }
    public String getFilePath() { return filePath; }
    
    // Setters
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    @Override
    public String toString() {
        return "PHPFunction{" +
               "functionName='" + functionName + '\'' +
               ", filePath='" + filePath + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PHPFunction that = (PHPFunction) obj;
        return functionName.equals(that.functionName) &&
               filePath.equals(that.filePath);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(functionName, filePath);
    }
}