package net.gcae.utils.model;

/**
 * Represents a PHP interface definition found in the code
 */
public class PHPInterface {
    private String interfaceName;
    private String filePath;
    
    /**
     * Constructor for PHPInterface
     * 
     * @param interfaceName The name of the interface
     * @param filePath The file path where the interface was defined
     */
    public PHPInterface(String interfaceName, String filePath) {
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
        return "PHPInterface{" +
               "interfaceName='" + interfaceName + '\'' +
               ", filePath='" + filePath + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PHPInterface that = (PHPInterface) obj;
        return interfaceName.equals(that.interfaceName) &&
               filePath.equals(that.filePath);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(interfaceName, filePath);
    }
}