package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.Serializable;

/**
 * Created by danan on 4/25/2018.
 */

public class Produs implements Serializable {

    private String denumireProdus;
    private String valabilitate;
    private Categorie categorie;
    private String url;
    private String detaliiAnunt;//asta e descriere din anunturi table; ba nu, asta cred ca e de fapt detalii din produs


    public Produs(){

    }

    public String getDenumireProdus() {
        return denumireProdus;
    }

    public void setDenumireProdus(String denumireProdus) {
        this.denumireProdus = denumireProdus;
    }

    public String getValabilitate() {
        return valabilitate;
    }

    public void setValabilitate(String valabilitate) {
        this.valabilitate = valabilitate;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetaliiAnunt() {
        return detaliiAnunt;
    }

    public void setDetaliiAnunt(String detaliiAnunt) {
        this.detaliiAnunt = detaliiAnunt;
    }
}
