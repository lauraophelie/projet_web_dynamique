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

     public ModelView() {}
     public ModelView(String view) {
          this.setView(view);
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
}