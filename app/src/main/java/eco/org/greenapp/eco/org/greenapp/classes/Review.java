package eco.org.greenapp.eco.org.greenapp.classes;

/**
 * Created by danan on 4/2/2018.
 */

public class Review {
    private String user;
    private String data_adaugare;
    private int nota;
    private String continut;
    private String urlProfil;
    public Review(){

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData_adaugare() {
        return data_adaugare;
    }

    public void setData_adaugare(String data_adaugare) {
        this.data_adaugare = data_adaugare;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public String getUrlProfil() {
        return urlProfil;
    }

    public void setUrlProfil(String urlProfil) {
        this.urlProfil = urlProfil;
    }
}
