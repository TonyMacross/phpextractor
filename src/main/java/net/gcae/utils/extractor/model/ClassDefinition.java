package net.gcae.utils.extractor.model;

/**
 * Represents a class definition
 */
public class ClassDefinition {
    private String className;
    private String fileLocation;
    private int methodCount;
    private String parentClass;
    private String interfaces;
    
    public ClassDefinition(String className, String fileLocation, int methodCount, String parentClass, String interfaces) {
        this.className = className;
        this.fileLocation = fileLocation;
        this.methodCount = methodCount;
        this.parentClass = parentClass;
        this.interfaces = interfaces;
    }
    
    // Getters and Setters
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public int getMethodCount() { return methodCount; }
    public void setMethodCount(int methodCount) { this.methodCount = methodCount; }
    
    public String getParentClass() { return parentClass; }
    public void setParentClass(String parentClass) { this.parentClass = parentClass; }
    
    public String getInterfaces() { return interfaces; }
    public void setInterfaces(String interfaces) { this.interfaces = interfaces; }
}