package net.gcae.utils.model;

/**
 * Represents an SQL query found in PHP code
 */
public class SQLQuery {
    private String queryPreview;
    private String tableName;
    private String filePath;
    private String functionName;
    
    public SQLQuery(String queryPreview, String tableName, String filePath, String functionName) {
        this.queryPreview = queryPreview;
        this.tableName = tableName;
        this.filePath = filePath;
        this.functionName = functionName;
    }
    
    // Getters
    public String getQueryPreview() { return queryPreview; }
    public String getTableName() { return tableName; }
    public String getFilePath() { return filePath; }
    public String getFunctionName() { return functionName; }
}