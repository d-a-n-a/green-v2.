package eco.org.greenapp.eco.org.greenapp.classes;

/**
 * Created by danan on 4/16/2018.
 */

public class UserWithReview extends User {
    private float medieReviews;



    public float getMedieReviews() {
        return medieReviews;
    }

    public void setMedieReviews(float medieReviews) {
        this.medieReviews = medieReviews;
    }

    @Override
    public String toString() {
        return "UserFiltru{" +
                " medieReviews=" + medieReviews +
                '}';
    }
}
