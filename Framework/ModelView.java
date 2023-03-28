package etu1885.framework;

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
}