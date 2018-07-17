package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.Serializable;

/**
 * Created by danan on 4/2/2018.
 */

public class User implements Serializable {
    private String nume;
    private String prenume;
    private String username;
    //private String locatie;
    private Locatie locatie;
    private String dataInregistrarii;
    private String biografie;
    private String email;
    private String url;

    public User(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public String getDataInregistrarii() {
        return dataInregistrarii;
    }

    public void setDataInregistrarii(String dataInregistrarii) {
        this.dataInregistrarii = dataInregistrarii;
    }

    public String getBiografie() {
        return biografie;
    }

    public void setBiografie(String biografie) {
        this.biografie = biografie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
