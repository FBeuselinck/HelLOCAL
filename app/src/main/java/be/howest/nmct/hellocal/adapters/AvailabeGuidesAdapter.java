package be.howest.nmct.hellocal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


import be.howest.nmct.hellocal.ItemClickSupport;
import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.models.AvaiableGuides;

/**
 * Created by Simon on 25/11/2016.
 */

public class AvailabeGuidesAdapter extends RecyclerView.Adapter<AvailabeGuidesAdapter.MyViewHolder> {

    private List<AvaiableGuides> guidesList;
    private List<String> bookingIds;

    Context context;


    FirebaseDatabase database = FirebaseDatabase.getInstance();



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location, date;
        public Button btnRemove;

        public MyViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.textViewLocation);
            date = (TextView) view.findViewById(R.id.textViewTime);
            btnRemove = (Button) view.findViewById(R.id.btnClose);

        }
    }


    public AvailabeGuidesAdapter(List<AvaiableGuides> guidesList, List<String> bookingIds, Context context) {
        this.guidesList = guidesList;
        this.bookingIds = bookingIds;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_your_available_bookings, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AvaiableGuides guide = guidesList.get(position);
        final String bookingId = bookingIds.get(position);
        holder.location.setText(guide.getLocation());
        holder.date.setText("From " + guide.getDateFrom() + " till " + guide.getDateTill());
        holder.date.setTag(R.id.bookingId, bookingId );


        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Are you really sure you want to delete this booking?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                DatabaseReference myRef = database.getReference("avaiableGuides").child(bookingId);
                                myRef.removeValue();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return guidesList.size();
    }

}
