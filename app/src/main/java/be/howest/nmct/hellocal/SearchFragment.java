package be.howest.nmct.hellocal;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import be.howest.nmct.hellocal.adapters.PlacesAutoCompleteAdapter;


public class SearchFragment extends Fragment  {

    private TextView textViewseekbar;

    private Spinner spinnerCountry;
//    private EditText editTextLocation;
    private EditText editTextDate;
    private Spinner spinnerPeople;
    private Spinner spinnerTransport;
    private Spinner spinnerType;
    private Spinner spinnerLanguage;
    private SeekBar rangeSeekbar;
    private String seekbarValue;

    private String Location = "";


    private Button buttonSearch;

    Calendar myCalendar = Calendar.getInstance();

    private GoogleApiClient mGoogleApiClient;

    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private AutoCompleteTextView myLocation;





    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        spinnerCountry = (Spinner) view.findViewById(R.id.SpinnerCountry);
//        editTextLocation = (EditText) view.findViewById(R.id.EditTextLocation);
        editTextDate = (EditText) view.findViewById(R.id.editTextDate);
        spinnerPeople = (Spinner) view.findViewById(R.id.spinnerPeople);
        spinnerTransport = (Spinner) view.findViewById(R.id.spinnerTransport);
        spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
        spinnerLanguage = (Spinner) view.findViewById(R.id.spinnerLanguage);
        rangeSeekbar = (SeekBar) view.findViewById(R.id.rangeSeekbar);

        textViewseekbar = (TextView) view.findViewById(R.id.textViewseekbar) ;




        buttonSearch = (Button) view.findViewById(R.id.btnSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.rangeSeekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewseekbar.setText("€ " + String.valueOf(progress));
                seekbarValue = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        myLocation = (AutoCompleteTextView) view.findViewById(R.id.myLocation);
        mPlacesAdapter = new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1,
                mGoogleApiClient,null, null);
        myLocation.setOnItemClickListener(mAutocompleteClickListener);
        myLocation.setAdapter(mPlacesAdapter);


        // Inflate the layout for this fragment
        return  view;

    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            Location = place.getName().toString();
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

       

        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }



    private void Search(){

        if(editTextDate.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(getActivity(), ListActivity.class);
            intent.putExtra("Location",Location);
            intent.putExtra("Country",spinnerCountry.getSelectedItem().toString());
            intent.putExtra("Date",editTextDate.getText().toString().trim());
            intent.putExtra("People",spinnerPeople.getSelectedItem().toString());
            intent.putExtra("Transport",spinnerTransport.getSelectedItem().toString());
            intent.putExtra("Type",spinnerType.getSelectedItem().toString());
            intent.putExtra("Language",spinnerLanguage.getSelectedItem().toString());
            intent.putExtra("Price",seekbarValue);
            startActivity(intent);

        }

    }
}
