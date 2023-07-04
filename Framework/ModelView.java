package etu1885.framework;

import java.util.HashMap;

public class ModelView {
     
     String view;

     public String getView() {
          return this.view;
     }

     public void setView(String view) {
          this.view = view;
     }

     boolean isJson;

     public boolean isJson() {
          return isJson;
     }

     public void setJson(boolean isJson) {
          this.isJson = isJson;
     }

     public ModelView() {}
     public ModelView(String view) {
          this.setView(view);
          this.setJson(false);
     }

     HashMap<String, Object> data = new HashMap<String, Object>();

     public HashMap<String, Object> getData() {
          return data;
     }

     public void setData(HashMap<String, Object> data) {
          this.data = data;
     }

     public void addItem(String key, Object value) {
          this.getData().put(key, value);
     }

     HashMap<String, Object> session = new HashMap<String, Object>();

     public HashMap<String, Object> getSession() {
          return session;
     }

     public void setSession(HashMap<String, Object> session) {
          this.session = session;
     }

     public void addSession(String name, Object value) {
          this.getSession().put(name, value);
     }
}