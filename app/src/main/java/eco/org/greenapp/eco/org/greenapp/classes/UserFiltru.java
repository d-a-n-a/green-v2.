package eco.org.greenapp.eco.org.greenapp.classes;

/**
 * Created by danan on 4/16/2018.
 */

public class UserFiltru extends User {
    private String urlProfil;
    private float medieReviews;

    public String getUrlProfil() {
        return urlProfil;
    }

    public void setUrlProfil(String urlProfil) {
        this.urlProfil = urlProfil;
    }

    public float getMedieReviews() {
        return medieReviews;
    }

    public void setMedieReviews(float medieReviews) {
        this.medieReviews = medieReviews;
    }

    @Override
    public String toString() {
        return "UserFiltru{" +
                "urlProfil='" + urlProfil + '\'' +
                ", medieReviews=" + medieReviews +
                '}';
    }
}
