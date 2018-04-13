package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.Serializable;

/**
 * Created by danan on 4/1/2018.
 */

public class Advertisement implements Serializable{
    private int id;
    private String dataPostarii;
    private String tip;
    private String username;
    private String denumireProdus;
    private String statusAnunt;
    private String email;
    private String valabilitate;
    private String categorie;
    private String locatieUser;
    private String descriereProdus;
    private String detaliiAnunt;
    private String url;

 public Advertisement(){}
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValabilitate() {
        return valabilitate;
    }

    public void setValabilitate(String valabilitate) {
        this.valabilitate = valabilitate;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLocatieUser() {
        return locatieUser;
    }

    public void setLocatieUser(String locatieUser) {
        this.locatieUser = locatieUser;
    }

    public String getDescriereProdus() {
        return descriereProdus;
    }

    public void setDescriereProdus(String descriereProdus) {
        this.descriereProdus = descriereProdus;
    }

    public String getDetaliiAnunt() {
        return detaliiAnunt;
    }

    public void setDetaliiAnunt(String detaliiAnunt) {
        this.detaliiAnunt = detaliiAnunt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataPostarii() {
        return dataPostarii;
    }

    public void setDataPostarii(String dataPostarii) {
        this.dataPostarii = dataPostarii;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDenumireProdus() {
        return denumireProdus;
    }

    public void setDenumireProdus(String denumireProdus) {
        this.denumireProdus = denumireProdus;
    }

    public String getStatusAnunt() {
        return statusAnunt;
    }

    public void setStatusAnunt(String statusAnunt) {
        this.statusAnunt = statusAnunt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
