package net.gcae.utils.model;

/**
 * Represents a CURL call found in PHP code
 * Used for tracking authentication and API calls
 */
public class CurlCall {
    private String callType;
    private String filePath;
    private String functionName;
    
    /**
     * Constructor for CurlCall
     * 
     * @param callType The type of CURL call (curl_init, curl_setopt, curl_exec)
     * @param filePath The file path where the call was found
     * @param functionName The function containing the call
     */
    public CurlCall(String callType, String filePath, String functionName) {
        this.callType = callType;
        this.filePath = filePath;
        this.functionName = functionName;
    }
    
    // Getters
    public String getCallType() { return callType; }
    public String getFilePath() { return filePath; }
    public String getFunctionName() { return functionName; }
    
    // Setters
    public void setCallType(String callType) { this.callType = callType; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    
    @Override
    public String toString() {
        return "CurlCall{" +
               "callType='" + callType + '\'' +
               ", filePath='" + filePath + '\'' +
               ", functionName='" + functionName + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CurlCall curlCall = (CurlCall) obj;
        return callType.equals(curlCall.callType) &&
               filePath.equals(curlCall.filePath) &&
               functionName.equals(curlCall.functionName);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(callType, filePath, functionName);
    }
}