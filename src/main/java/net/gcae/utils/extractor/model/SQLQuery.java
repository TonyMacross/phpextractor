package net.gcae.utils.extractor.model;

/**
 * Represents an SQL query found in the PHP code
 */
public class SQLQuery {
    private String queryName;
    private String dbTable;
    private String fileLocation;
    private String dataSource;
    private String sqlQuery;
    private String complexity;
    
    public SQLQuery(String queryName, String dbTable, String fileLocation, String dataSource, String sqlQuery, String complexity) {
        this.queryName = queryName;
        this.dbTable = dbTable;
        this.fileLocation = fileLocation;
        this.dataSource = dataSource;
        this.sqlQuery = sqlQuery;
        this.complexity = complexity;
    }
    
    // Getters and Setters
    public String getQueryName() { return queryName; }
    public void setQueryName(String queryName) { this.queryName = queryName; }
    
    public String getDbTable() { return dbTable; }
    public void setDbTable(String dbTable) { this.dbTable = dbTable; }
    
    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }
    
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    
    public String getSqlQuery() { return sqlQuery; }
    public void setSqlQuery(String sqlQuery) { this.sqlQuery = sqlQuery; }
    
    public String getComplexity() { return complexity; }
    public void setComplexity(String complexity) { this.complexity = complexity; }
}