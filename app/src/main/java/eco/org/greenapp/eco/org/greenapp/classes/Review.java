package eco.org.greenapp.eco.org.greenapp.classes;

/**
 * Created by danan on 4/2/2018.
 */

public class Review {
    //private String user;
    private String data_adaugare;
    private float nota;
    private String continut;
    private User user;
   // private String urlProfil;
    public Review(){

    }

    public String getData_adaugare() {
        return data_adaugare;
    }

    public void setData_adaugare(String data_adaugare) {
        this.data_adaugare = data_adaugare;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
