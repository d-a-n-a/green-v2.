package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.Serializable;

/**
 * Created by danan on 4/1/2018.
 */

public class Advertisement implements Serializable{
    private int id;
    private String dataPostarii;
    //nu am durata in zile, dar oricum in aplicatie nu fac nimic cu ea
    //sau as putea sa o pun pentru ca oricum calculez cu cate zile in urma s-au postat anunturile
    private String tip;
    private String descriereProdus;//care e de fapt descriere de la anunt
    private Status statusAnunt;

    private User user;
   // private String username;
   // private String email;
   // private String locatieUser;
   // private float latitudine;
   //  private float longitudine;

    //private String statusAnunt;

    private Produs produs;
    /*private String denumireProdus;
    private String valabilitate;
    private String categorie;//pe asta o gasesc in produs

    private String url;

    private String detaliiAnunt;//asta e descriere din anunturi table; ba nu, asta cred ca e de fapt detalii din produs*/



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
