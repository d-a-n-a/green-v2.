package eco.org.greenapp.eco.org.greenapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import java.io.InputStream;

import eco.org.greenapp.R;

/**
 * Created by danan on 4/7/2018.
 */

public class GetImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Context context;


    public GetImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
       //bmImage.setImageBitmap(result);
        //aici ar fi bine sa las ca mai sus si sa modific din xml sa fie circular
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.context.getResources(),result);
        roundedBitmapDrawable.setCircular(true);
        bmImage.setImageDrawable(roundedBitmapDrawable);
    }
}
