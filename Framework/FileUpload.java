package etu1885;

public class FileUpload {

    String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    String path;
    
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    byte [] bytes;

    public byte [] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public FileUpload() {}

    public FileUpload(String path, byte[] bytes) {
        this.setPath(path);
        this.setBytes(bytes);
    }
    
    public FileUpload(String name, String path, byte[] bytes) {
        this.setName(name);
        this.setPath(path);
        this.setBytes(bytes);
    }
}