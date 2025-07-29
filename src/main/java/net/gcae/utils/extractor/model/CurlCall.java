package net.gcae.utils.extractor.model;

/**
 * Represents a CURL call found in the PHP code
 */
public class CurlCall {
    private String callType;
    private String fileLocation;
    private String target;
    private String purpose;
    
    public CurlCall(String callType, String fileLocation, String target, String purpose) {
        this.callType = callType;
        this.fileLocation = fileLocation;
        this.target = target;
        this.purpose = purpose;
    }
    
    // Getters and Setters
    public String getCallType() { return callType; }
    public void setCallType(String callType) { this.callType = callType; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
}