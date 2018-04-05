package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.Serializable;

/**
 * Created by danan on 4/5/2018.
 */

public class Transaction implements Serializable {
    private int idTranzactie;
    private String locatie;
    private String data;
    private String ora;
    private int idAnunt;
    private String status;
    private String expeditor;
    private String destinatar;
    private String denumire;

    public int getIdTranzactie() {
        return idTranzactie;
    }

    public void setIdTranzactie(int idTranzactie) {
        this.idTranzactie = idTranzactie;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public int getIdAnunt() {
        return idAnunt;
    }

    public void setIdAnunt(int idAnunt) {
        this.idAnunt = idAnunt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpeditor() {
        return expeditor;
    }

    public void setExpeditor(String expeditor) {
        this.expeditor = expeditor;
    }

    public String getDestinatar() {
        return destinatar;
    }

    public void setDestinatar(String destinatar) {
        this.destinatar = destinatar;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
