package net.gcae.utils.extractor.model;

/**
 * Represents inheritance information (traits and class inheritance)
 */
public class InheritanceInfo {
    private String childName;
    private String parentName;
    private String fileLocation;
    private String inheritanceType;
    
    public InheritanceInfo(String childName, String parentName, String fileLocation, String inheritanceType) {
        this.childName = childName;
        this.parentName = parentName;
        this.fileLocation = fileLocation;
        this.inheritanceType = inheritanceType;
    }
    
    // Getters and Setters
    public String getChildName() { return childName; }
    public void setChildName(String childName) { this.childName = childName; }
    
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public String getInheritanceType() { return inheritanceType; }
    public void setInheritanceType(String inheritanceType) { this.inheritanceType = inheritanceType; }
}