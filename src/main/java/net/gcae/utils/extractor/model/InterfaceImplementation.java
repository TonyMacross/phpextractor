package net.gcae.utils.extractor.model;

/**
 * Represents interface implementation
 */
public class InterfaceImplementation {
    private String className;
    private String interfaceName;
    private String fileLocation;
    
    public InterfaceImplementation(String className, String interfaceName, String fileLocation) {
        this.className = className;
        this.interfaceName = interfaceName;
        this.fileLocation = fileLocation;
    }
    
    // Getters and Setters
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getInterfaceName() { return interfaceName; }
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
}