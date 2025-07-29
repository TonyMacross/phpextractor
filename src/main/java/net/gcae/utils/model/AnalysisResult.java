package net.gcae.utils.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for all analysis results
 */
public class AnalysisResult {
    
    private List<PHPFile> phpFiles;
    private List<SQLQuery> queries;
    private List<RequireStatement> requires;
    private List<IncludeStatement> includes;
    private List<CurlCall> calls;
    private List<PHPFunction> functions;
    private List<PHPClass> classes;
    private List<TraitUse> traitUses;
    private List<ClassExtend> classExtends;
    private List<InterfaceImplementation> implementations;
    private List<PHPInterface> interfaces;
    
    public AnalysisResult() {
        this.phpFiles = new ArrayList<>();
        this.queries = new ArrayList<>();
        this.requires = new ArrayList<>();
        this.includes = new ArrayList<>();
        this.calls = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.traitUses = new ArrayList<>();
        this.classExtends = new ArrayList<>();
        this.implementations = new ArrayList<>();
        this.interfaces = new ArrayList<>();
    }
    
    // PHP Files
    public List<PHPFile> getPhpFiles() { return phpFiles; }
    public void addPhpFile(PHPFile phpFile) { this.phpFiles.add(phpFile); }
    
    // SQL Queries
    public List<SQLQuery> getQueries() { return queries; }
    public void addQuery(SQLQuery query) { this.queries.add(query); }
    
    // Requires
    public List<RequireStatement> getRequires() { return requires; }
    public void addRequire(RequireStatement require) { this.requires.add(require); }
    
    // Includes
    public List<IncludeStatement> getIncludes() { return includes; }
    public void addInclude(IncludeStatement include) { this.includes.add(include); }
    
    // CURL Calls
    public List<CurlCall> getCalls() { return calls; }
    public void addCall(CurlCall call) { this.calls.add(call); }
    
    // Functions
    public List<PHPFunction> getFunctions() { return functions; }
    public void addFunction(PHPFunction function) { this.functions.add(function); }
    
    // Classes
    public List<PHPClass> getClasses() { return classes; }
    public void addClass(PHPClass phpClass) { this.classes.add(phpClass); }
    
    // Trait Uses
    public List<TraitUse> getTraitUses() { return traitUses; }
    public void addTraitUse(TraitUse traitUse) { this.traitUses.add(traitUse); }
    
    // Class Extends
    public List<ClassExtend> getClassExtends() { return classExtends; }
    public void addClassExtend(ClassExtend classExtend) { this.classExtends.add(classExtend); }
    
    // Interface Implementations
    public List<InterfaceImplementation> getImplements() { return implementations; }
    public void addImplementation(InterfaceImplementation implementation) { this.implementations.add(implementation); }
    
    // Interfaces
    public List<PHPInterface> getInterfaces() { return interfaces; }
    public void addInterface(PHPInterface phpInterface) { this.interfaces.add(phpInterface); }
}