package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Review;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/2/2018.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    private List<Review> listaReviews;
    private int idLayout;

    public ReviewAdapter(@NonNull Context context, int resource, List<Review> reviews) {
        super(context, resource, reviews);
        this.idLayout = resource;
        this.listaReviews = reviews;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.idLayout,parent, false);

        TextView reviewer = (TextView)view.findViewById(R.id.reviewerUsername);
        TextView date = (TextView)view.findViewById(R.id.dateOfReview);
        TextView continut = (TextView)view.findViewById(R.id.reviewContent);
        TextView nota = (TextView)view.findViewById(R.id.reviewScore);
        ImageView img = (ImageView)view.findViewById(R.id.reviewerAvatar);


        Review review = this.listaReviews.get(position);
        reviewer.setText(review.getUser().getUsername());
        date.setText(review.getData_adaugare());
        continut.setText(review.getContinut());
        nota.setText(""+review.getNota()+"/5");

        if(!review.getUser().getUrl().isEmpty() && !(review.getUser().getUrl()==null))
        {
            GetImageTask getImageTask = new GetImageTask(img, getContext());
            getImageTask.execute(GeneralConstants.Url+review.getUser().getUrl());
        }

        return view;
    }
}
