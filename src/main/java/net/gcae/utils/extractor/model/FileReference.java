package net.gcae.utils.extractor.model;

/**
 * Represents a file reference (require/include)
 */
public class FileReference {
    private String referencedFile;
    private String fileLocation;
    private String referenceType;
    
    public FileReference(String referencedFile, String fileLocation, String referenceType) {
        this.referencedFile = referencedFile;
        this.fileLocation = fileLocation;
        this.referenceType = referenceType;
    }
    
    // Getters and Setters
    public String getReferencedFile() { return referencedFile; }
    public void setReferencedFile(String referencedFile) { this.referencedFile = referencedFile; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
}