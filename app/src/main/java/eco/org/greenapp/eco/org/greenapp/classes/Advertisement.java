package eco.org.greenapp.eco.org.greenapp.classes;

/**
 * Created by danan on 4/1/2018.
 */

public class Advertisement {
    private int id;
    private String dataPostarii;
    private String tip;
    private String username;
    private String denumireProdus;
    private String statusAnunt;
//imaginea!!!!
 public Advertisement(){}
    public int getId() {
        return id;
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
}
