package be.howest.nmct.hellocal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SearchFragment extends Fragment {

    private Button buttonSearch;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        buttonSearch = (Button) view.findViewById(R.id.btnSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });

        // Inflate the layout for this fragment
        return  view;

    }

    private void Search(){
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = new InfoFragment();
        ft.replace(R.id.fragmentContainer, fragment, "NewFragmentTag");
        ft.commit();
    }
}
