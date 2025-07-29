package net.gcae.utils.model;

/**
 * Represents a trait use statement found in PHP code
 */
public class TraitUse {
    private String traitName;
    private String filePath;
    
    /**
     * Constructor for TraitUse
     * 
     * @param traitName The name of the trait being used
     * @param filePath The file path where the trait use was found
     */
    public TraitUse(String traitName, String filePath) {
        this.traitName = traitName;
        this.filePath = filePath;
    }
    
    // Getters
    public String getTraitName() { return traitName; }
    public String getFilePath() { return filePath; }
    
    // Setters
    public void setTraitName(String traitName) { this.traitName = traitName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    @Override
    public String toString() {
        return "TraitUse{" +
               "traitName='" + traitName + '\'' +
               ", filePath='" + filePath + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TraitUse traitUse = (TraitUse) obj;
        return traitName.equals(traitUse.traitName) &&
               filePath.equals(traitUse.filePath);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(traitName, filePath);
    }
}