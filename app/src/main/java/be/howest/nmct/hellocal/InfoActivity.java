package be.howest.nmct.hellocal;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private String Name;
    private String City;
    private String Country;
    private String Price;

    private TextView textViewName;
    private TextView textViewCity;
    private TextView textViewCountry;
    private RatingBar ratingBar;
    private TextView textViewReviews;
    private TextView textViewPrice;


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

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textViewReviews = (TextView) findViewById(R.id.textViewReviews);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);


        textViewName.setText(Name);
        textViewCity.setText(City);
        textViewCountry.setText(Country);
        textViewPrice.setText("€ "+ Price);


    }
}
