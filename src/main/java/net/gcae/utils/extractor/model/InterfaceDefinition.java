package net.gcae.utils.extractor.model;

/**
 * Represents interface definition
 */
public class InterfaceDefinition {
    private String interfaceName;
    private String fileLocation;
    private int methodCount;
    private String extendsInterface;
    
    public InterfaceDefinition(String interfaceName, String fileLocation, int methodCount, String extendsInterface) {
        this.interfaceName = interfaceName;
        this.fileLocation = fileLocation;
        this.methodCount = methodCount;
        this.extendsInterface = extendsInterface;
    }
    
    // Getters and Setters
    public String getInterfaceName() { return interfaceName; }
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public int getMethodCount() { return methodCount; }
    public void setMethodCount(int methodCount) { this.methodCount = methodCount; }
    
    public String getExtendsInterface() { return extendsInterface; }
    public void setExtendsInterface(String extendsInterface) { this.extendsInterface = extendsInterface; }
}