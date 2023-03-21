package test;
import etu1885.URLs;
import java.util.List;

public class Emp {
    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    String prenom;

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @URLs(url="emp-save")
    public List<Emp> findAll() {
        return null;
    }
}
