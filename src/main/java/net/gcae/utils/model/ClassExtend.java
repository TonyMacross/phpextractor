package net.gcae.utils.model;

/**
 * Represents a class extends relationship found in PHP code
 */
public class ClassExtend {
    private String parentClass;
    private String filePath;
    
    /**
     * Constructor for ClassExtend
     * 
     * @param parentClass The name of the parent class being extended
     * @param filePath The file path where the extends relationship was found
     */
    public ClassExtend(String parentClass, String filePath) {
        this.parentClass = parentClass;
        this.filePath = filePath;
    }
    
    // Getters
    public String getParentClass() { return parentClass; }
    public String getFilePath() { return filePath; }
    
    // Setters
    public void setParentClass(String parentClass) { this.parentClass = parentClass; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    @Override
    public String toString() {
        return "ClassExtend{" +
               "parentClass='" + parentClass + '\'' +
               ", filePath='" + filePath + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ClassExtend that = (ClassExtend) obj;
        return parentClass.equals(that.parentClass) &&
               filePath.equals(that.filePath);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(parentClass, filePath);
    }
}