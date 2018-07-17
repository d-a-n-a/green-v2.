package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.util.Date;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.ProductImageTask;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.enumerations.TipAnunt;

public class AdForProduct extends AppCompatActivity implements OnMapReadyCallback {

    ImageView productPhoto;
    SupportMapFragment mapFragment;
    Advertisement adv;
    Intent intent;
    TextView denumireProdus;
    TextView descriereProdus;
    TextView categorie;
    TextView valabilitate;
    TextView locatie;
    TextView detalii;
    TextView status;
    TextView zile;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_for_product);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        denumireProdus = (TextView)findViewById(R.id.productName);
        descriereProdus = (TextView)findViewById(R.id.productDescription);
        categorie = (TextView)findViewById(R.id.category);
        valabilitate  = (TextView)findViewById(R.id.dataValabilitate);
        locatie = (TextView)findViewById(R.id.productLocation);
        detalii = (TextView)findViewById(R.id.productDetails);
        status = (TextView)findViewById(R.id.statusType);
        zile = (TextView)findViewById(R.id.daysSincePosted);
        imageView = (ImageView)findViewById(R.id.userProfileImage);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.locationMap);
        mapFragment.getMapAsync(this);


        intent = getIntent();
        if(intent != null)
             adv = (Advertisement)intent.getSerializableExtra("selectedAd");
        productPhoto = (ImageView)findViewById(R.id.backgroundLayout);

        if(adv.getTip().equals(TipAnunt.cerere.toString())){
            productPhoto.setPadding(30,50,30,30);
            productPhoto.setScaleType(ImageView.ScaleType.CENTER);
          }

        ProductImageTask productImageTask = new ProductImageTask(productPhoto, getApplicationContext());
        productImageTask.execute(GeneralConstants.Url+ adv.getProdus().getUrl());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                intent.putExtra("username", adv.getUser().getUsername());
                startActivity(intent);
            }
        });

        denumireProdus.setText(adv.getProdus().getDenumireProdus());
        descriereProdus.setText(adv.getProdus().getDetaliiAnunt());
        categorie.setText(adv.getProdus().getCategorie().getDenumire());
        valabilitate.setText(adv.getProdus().getValabilitate());
        locatie.setText(adv.getUser().getLocatie().getStrada());
        detalii.setText(adv.getDescriereProdus());
        status.setText(adv.getStatusAnunt().getTip());
        ((TextView)findViewById(R.id.usernameanunt)).setText(adv.getUser().getUsername());
        ((TextView)findViewById(R.id.usernameanunt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                intent.putExtra("username", adv.getUser().getUsername());
                startActivity(intent);
            }
        });
        String dataIntroducerii = adv.getDataPostarii().trim();
        Log.i("introducere", dataIntroducerii);
        long days=0;
        try {

            Date oldDate = GeneralConstants.SDF.parse(dataIntroducerii);
            Log.i("oldDate", oldDate.toString());

            Date currentDate = new Date();
            Log.i("curent", currentDate.toString());
            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = hours / 24;

            Log.i("days", ""+days);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        zile.setText("anunt postat acum "+days + " zile");

        ((Button)findViewById(R.id.btnTakeProduct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{adv.getUser().getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contactare anunt: "+ adv.getProdus().getDenumireProdus());
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(),"Aplicatia Gmail nu este instalata pe dispozitiv.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(adv.getUser().getLocatie().getLatitudine(), adv.getUser().getLocatie().getLongitudine());
        googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(adv.getUser().getLocatie().getStrada())
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
