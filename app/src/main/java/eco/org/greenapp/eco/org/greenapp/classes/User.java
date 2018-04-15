package eco.org.greenapp.eco.org.greenapp.classes;

/**
 * Created by danan on 4/2/2018.
 */

public class User {
    private String nume;
    private String prenume;
    private String username;
    private String locatie;
    private String dataInregistrarii;
    private String biografie;
    private String email;

    //fotografie! PLUS REVIEW
    //dar trebui e sa ii iau si tranzactiile si anunturile!! -  pe astea le fac din shared preferences mai bine cu emailul!

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

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
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
}
