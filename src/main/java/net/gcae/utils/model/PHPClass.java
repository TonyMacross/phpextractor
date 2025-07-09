package net.gcae.utils.model;

/**
 * Represents a PHP class definition found in the code
 */
public class PHPClass {
    private String className;
    private String filePath;
    
    /**
     * Constructor for PHPClass
     * 
     * @param className The name of the class
     * @param filePath The file path where the class was defined
     */
    public PHPClass(String className, String filePath) {
        this.className = className;
        this.filePath = filePath;
    }
    
    // Getters
    public String getClassName() { return className; }
    public String getFilePath() { return filePath; }
    
    // Setters
    public void setClassName(String className) { this.className = className; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    @Override
    public String toString() {
        return "PHPClass{" +
               "className='" + className + '\'' +
               ", filePath='" + filePath + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PHPClass phpClass = (PHPClass) obj;
        return className.equals(phpClass.className) &&
               filePath.equals(phpClass.filePath);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(className, filePath);
    }
}