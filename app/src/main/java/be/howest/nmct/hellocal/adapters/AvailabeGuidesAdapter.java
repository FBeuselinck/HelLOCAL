package be.howest.nmct.hellocal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


import be.howest.nmct.hellocal.ItemClickSupport;
import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.models.AvaiableGuides;

/**
 * Created by Simon on 25/11/2016.
 */

public class AvailabeGuidesAdapter extends RecyclerView.Adapter<AvailabeGuidesAdapter.MyViewHolder> {

    private List<AvaiableGuides> guidesList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewNaam);
            date = (TextView) view.findViewById(R.id.textViewTime);

        }
    }


    public AvailabeGuidesAdapter(List<AvaiableGuides> guidesList) {
        this.guidesList = guidesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_booking, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AvaiableGuides guide = guidesList.get(position);
        holder.name.setText(guide.getName());
        holder.date.setText(guide.getDateFrom());

    }

    @Override
    public int getItemCount() {
        return guidesList.size();
    }

}
