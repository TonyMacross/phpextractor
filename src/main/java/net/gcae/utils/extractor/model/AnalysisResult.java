package net.gcae.utils.extractor.model;

import java.util.List;

/**
 * Contains the complete analysis result of a PHP project
 */
public class AnalysisResult {
    
    private List<FileInventory> files;
    private List<SQLQuery> sqlQueries;
    private List<FileReference> requires;
    private List<FileReference> includes;
    private List<CurlCall> curlCalls;
    private List<FunctionDefinition> functions;
    private List<ClassDefinition> classes;
    private List<InheritanceInfo> traits;
    private List<InheritanceInfo> classInheritances;
    private List<InterfaceImplementation> implementations;
    private List<InterfaceDefinition> interfaces;
    
    // Constructors
    public AnalysisResult() {}
    
    public AnalysisResult(List<FileInventory> files, List<SQLQuery> sqlQueries, 
                         List<FileReference> requires, List<FileReference> includes,
                         List<CurlCall> curlCalls, List<FunctionDefinition> functions,
                         List<ClassDefinition> classes, List<InheritanceInfo> traits,
                         List<InheritanceInfo> classInheritances, List<InterfaceImplementation> implementations,
                         List<InterfaceDefinition> interfaces) {
        this.files = files;
        this.sqlQueries = sqlQueries;
        this.requires = requires;
        this.includes = includes;
        this.curlCalls = curlCalls;
        this.functions = functions;
        this.classes = classes;
        this.traits = traits;
        this.classInheritances = classInheritances;
        this.implementations = implementations;
        this.interfaces = interfaces;
    }
    
    // Getters and Setters
    public List<FileInventory> getFiles() { return files; }
    public void setFiles(List<FileInventory> files) { this.files = files; }
    
    public List<SQLQuery> getSqlQueries() { return sqlQueries; }
    public void setSqlQueries(List<SQLQuery> sqlQueries) { this.sqlQueries = sqlQueries; }
    
    public List<FileReference> getRequires() { return requires; }
    public void setRequires(List<FileReference> requires) { this.requires = requires; }
    
    public List<FileReference> getIncludes() { return includes; }
    public void setIncludes(List<FileReference> includes) { this.includes = includes; }
    
    public List<CurlCall> getCurlCalls() { return curlCalls; }
    public void setCurlCalls(List<CurlCall> curlCalls) { this.curlCalls = curlCalls; }
    
    public List<FunctionDefinition> getFunctions() { return functions; }
    public void setFunctions(List<FunctionDefinition> functions) { this.functions = functions; }
    
    public List<ClassDefinition> getClasses() { return classes; }
    public void setClasses(List<ClassDefinition> classes) { this.classes = classes; }
    
    public List<InheritanceInfo> getTraits() { return traits; }
    public void setTraits(List<InheritanceInfo> traits) { this.traits = traits; }
    
    public List<InheritanceInfo> getClassInheritances() { return classInheritances; }
    public void setClassInheritances(List<InheritanceInfo> classInheritances) { this.classInheritances = classInheritances; }
    
    public List<InterfaceImplementation> getImplementations() { return implementations; }
    public void setImplementations(List<InterfaceImplementation> implementations) { this.implementations = implementations; }
    
    public List<InterfaceDefinition> getInterfaces() { return interfaces; }
    public void setInterfaces(List<InterfaceDefinition> interfaces) { this.interfaces = interfaces; }
    
    // Utility methods
    public boolean hasData() {
        return (files != null && !files.isEmpty()) ||
               (sqlQueries != null && !sqlQueries.isEmpty()) ||
               (requires != null && !requires.isEmpty()) ||
               (includes != null && !includes.isEmpty()) ||
               (curlCalls != null && !curlCalls.isEmpty()) ||
               (functions != null && !functions.isEmpty()) ||
               (classes != null && !classes.isEmpty()) ||
               (traits != null && !traits.isEmpty()) ||
               (classInheritances != null && !classInheritances.isEmpty()) ||
               (implementations != null && !implementations.isEmpty()) ||
               (interfaces != null && !interfaces.isEmpty());
    }
}