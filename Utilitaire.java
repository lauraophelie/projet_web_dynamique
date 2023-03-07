package etu1885.framework;

import java.net.URI;

public class Utilitaire {
    public String getUrlValues(String url) {
        
        URI uri = URI.create(url);
        String uriPath = uri.getPath();
        String [] values = uriPath.split("/");
        String value = "";
        
        for (int i = 2; i < values.length - 1; i++) {
            value += values[i] + "/";
        }
        value += values[values.length - 1];
        
        return value;
    }
}
