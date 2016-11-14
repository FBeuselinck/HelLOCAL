package be.howest.nmct.hellocal;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private String Name;
    private String City;
    private String Country;
    private String Price;
    private String UserId;
    private String Transport;
    private String Active;
    private String Culture;
    private String CityIcon;
    private String SmthElse;


    private TextView textViewName;
    private TextView textViewCity;
    private TextView textViewCountry;
    private RatingBar ratingBar;
    private TextView textViewReviews;
    private TextView textViewPrice;

    private Button btnBook;
    private Button btnContact;


    private Image iconActive;
    private Image iconCulture;
    private Image iconCity;
    private Image iconElse;

    private ImageView ImageView1;
    private ImageView ImageView2;
    private ImageView ImageView3;
    private ImageView ImageView4;
    private ImageView ImageView5;
    private ImageView ImageView6;
    private ImageView ImageView7;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Name = intent.getStringExtra("Name");
        City = intent.getStringExtra("City");
        Country = intent.getStringExtra("Country");
        Price = intent.getStringExtra("Price");
        UserId = intent.getStringExtra("UserId");

        Transport = intent.getStringExtra("Transport");
        Active = intent.getStringExtra("Active");
        CityIcon = intent.getStringExtra("City2");
        Culture = intent.getStringExtra("Culture");
        SmthElse = intent.getStringExtra("SmthElse");


        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textViewReviews = (TextView) findViewById(R.id.textViewReviews);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);

        btnBook = (Button) findViewById(R.id.btnBook);
        btnContact = (Button) findViewById(R.id.btnContact);

        ImageView1 = (ImageView) findViewById(R.id.imageIcon1);
        ImageView2 = (ImageView) findViewById(R.id.imageIcon2);
        ImageView3 = (ImageView) findViewById(R.id.imageIcon3);
        ImageView4 = (ImageView) findViewById(R.id.imageIcon4);
        ImageView5 = (ImageView) findViewById(R.id.imageIcon5);
        ImageView6 = (ImageView) findViewById(R.id.imageIcon6);
        ImageView7 = (ImageView) findViewById(R.id.imageIcon7);


//        Toast.makeText(getApplicationContext(), UserId,
//                Toast.LENGTH_LONG).show();


        textViewName.setText(Name);
        textViewCity.setText(City);
        textViewCountry.setText(Country);
        textViewPrice.setText(Price);
        btnBook.setText("BOOK " + Name.toUpperCase());
        btnContact.setText("CONTACT " + Name.toUpperCase());

        if(Active.equals("True")&&CityIcon.equals("false")&&Culture.equals("false")&&SmthElse.equals("false")){
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("True")&&Culture.equals("false")&&SmthElse.equals("false")){
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setImageResource(R.drawable.city);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("True")&&Culture.equals("True")&&SmthElse.equals("false")){
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setImageResource(R.drawable.city);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("false")&&Culture.equals("True")&&SmthElse.equals("false")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("false")&&Culture.equals("false")&&SmthElse.equals("True")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(CityIcon.equals("True")&&Active.equals("false")&&Culture.equals("false")&&SmthElse.equals("false")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(CityIcon.equals("True")&&Active.equals("false")&&Culture.equals("True")&&SmthElse.equals("false")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setVisibility(View.GONE);
        }else if(CityIcon.equals("True")&&Active.equals("false")&&Culture.equals("True")&&SmthElse.equals("True")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setImageResource(R.drawable.smthelse);
        }else if(Culture.equals("True")&&Active.equals("false")&&CityIcon.equals("false")&&SmthElse.equals("false")){
            ImageView3.setImageResource(R.drawable.culture);
            ImageView2.setVisibility(View.GONE);
            ImageView1.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Culture.equals("True")&&Active.equals("false")&&CityIcon.equals("false")&&SmthElse.equals("True")){
            ImageView3.setImageResource(R.drawable.culture);
            ImageView2.setVisibility(View.GONE);
            ImageView1.setVisibility(View.GONE);
            ImageView4.setImageResource(R.drawable.smthelse);
        }else if(SmthElse.equals("True")&&Active.equals("false")&&Culture.equals("false")&&CityIcon.equals("false")){
            ImageView4.setImageResource(R.drawable.smthelse);
            ImageView2.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView1.setVisibility(View.GONE);
        }else{
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setImageResource(R.drawable.city);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setImageResource(R.drawable.smthelse);
        }


//
//        if(Culture.equals("True")&&Active.equals("false")&&City.equals("false")&&SmthElse.equals("false")){
//            ImageView3.setImageResource(R.drawable.culture);
//            ImageView2.setVisibility(View.GONE);
//            ImageView1.setVisibility(View.GONE);
//            ImageView4.setVisibility(View.GONE);
//        }
//
//        if(SmthElse.equals("True")&&Active.equals("false")&&Culture.equals("false")&&City.equals("false")){
//            ImageView4.setImageResource(R.drawable.smthelse);
//            ImageView2.setVisibility(View.GONE);
//            ImageView3.setVisibility(View.GONE);
//            ImageView1.setVisibility(View.GONE);
//        }

        if(Transport.equals("Guides transport")){
            ImageView5.setImageResource(R.drawable.guidetransport);
        }else if(Transport.equals("Own transport")){
            ImageView5.setImageResource(R.drawable.owntransport);
        }else if(Transport.equals("No transport")){
            ImageView5.setImageResource(R.drawable.notransport);
        }









    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }





}
