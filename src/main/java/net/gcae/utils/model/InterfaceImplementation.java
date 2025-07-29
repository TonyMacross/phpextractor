package net.gcae.utils.model;

/**
 * Represents an interface implementation found in PHP code
 */
public class InterfaceImplementation {
    private String interfaceName;
    private String filePath;
    
    /**
     * Constructor for InterfaceImplementation
     * 
     * @param interfaceName The name of the interface being implemented
     * @param filePath The file path where the implementation was found
     */
    public InterfaceImplementation(String interfaceName, String filePath) {
        this.interfaceName = interfaceName;
        this.filePath = filePath;
    }
    
    // Getters
    public String getInterfaceName() { return interfaceName; }
    public String getFilePath() { return filePath; }
    
    // Setters
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    @Override
    public String toString() {
        return "InterfaceImplementation{" +
               "interfaceName='" + interfaceName + '\'' +
               ", filePath='" + filePath + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        InterfaceImplementation that = (InterfaceImplementation) obj;
        return interfaceName.equals(that.interfaceName) &&
               filePath.equals(that.filePath);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(interfaceName, filePath);
    }
}