package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.Serializable;

/**
 * Created by danan on 4/1/2018.
 */

public class Advertisement implements Serializable{
    private int id;
    private String dataPostarii;
    private String tip;
    private String descriereProdus;
    private Status statusAnunt;
    private User user;
    private Produs produs;
    private float distanta;

    public Advertisement(){}
    public int getId() {
        return id;
    }

    public String getDescriereProdus() {
        return descriereProdus;
    }

    public void setDescriereProdus(String descriereProdus) {
        this.descriereProdus = descriereProdus;
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


    public Status getStatusAnunt() {
        return statusAnunt;
    }

    public void setStatusAnunt(Status statusAnunt) {
        this.statusAnunt = statusAnunt;
    }

    public float getDistanta() {
        return distanta;
    }

    public void setDistanta(float distanta) {
        this.distanta = distanta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Produs getProdus() {
        return produs;
    }

    public void setProdus(Produs produs) {
        this.produs = produs;
    }
}
