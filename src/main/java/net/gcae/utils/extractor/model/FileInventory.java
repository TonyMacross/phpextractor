
package net.gcae.utils.extractor.model;

/**
 * Represents a file in the PHP project inventory
 */
public class FileInventory {
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private int lineCount;
    
    public FileInventory(String fileName, String filePath, String fileType, long fileSize, int lineCount) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.lineCount = lineCount;
    }
    
    // Getters and Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public int getLineCount() { return lineCount; }
    public void setLineCount(int lineCount) { this.lineCount = lineCount; }
}