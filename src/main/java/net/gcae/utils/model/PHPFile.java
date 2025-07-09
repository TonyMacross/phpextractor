package net.gcae.utils.model;

/**
 * Represents a PHP file in the repository
 */
public class PHPFile {
    private String fileName;
    private String relativePath;
    private long fileSize;
    
    public PHPFile(String fileName, String relativePath, long fileSize) {
        this.fileName = fileName;
        this.relativePath = relativePath;
        this.fileSize = fileSize;
    }
    
    // Getters
    public String getFileName() { return fileName; }
    public String getRelativePath() { return relativePath; }
    public long getFileSize() { return fileSize; }
}
