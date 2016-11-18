package be.howest.nmct.hellocal;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class SearchFragment extends Fragment {

    private TextView textViewseekbar;

    private Spinner spinnerCountry;
    private EditText editTextLocation;
    private EditText editTextDate;
    private Spinner spinnerPeople;
    private Spinner spinnerTransport;
    private Spinner spinnerType;
    private Spinner spinnerLanguage;
    private SeekBar rangeSeekbar;
    private String seekbarValue;


    private Button buttonSearch;

    Calendar myCalendar = Calendar.getInstance();



    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        spinnerCountry = (Spinner) view.findViewById(R.id.SpinnerCountry);
        editTextLocation = (EditText) view.findViewById(R.id.EditTextLocation);
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

        // Inflate the layout for this fragment
        return  view;

    }




    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }



    private void Search(){
//        final FragmentTransaction ft = getFragmentManager().beginTransaction();
//        Fragment fragment = new InfoFragment();
//        ft.replace(R.id.fragmentContainer, fragment, "NewFragmentTag");
//        ft.commit();


        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra("Location",editTextLocation.getText().toString().trim());
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
