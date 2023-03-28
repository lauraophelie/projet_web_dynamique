package etu1885.framework;

public class Mapping {
    String className;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Mapping() {}
    public Mapping(String className, String method) {
        this.setClassName(className);
        this.setMethod(method);
    }
}
