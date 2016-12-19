package be.howest.nmct.hellocal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

import be.howest.nmct.hellocal.ChatActivity;
import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.BookingRequests;
import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.ProfileDetails;

/**
 * Created by Simon on 19/12/2016.
 */

public class bookings_as_tourist_adapter  extends RecyclerView.Adapter<bookings_as_tourist_adapter.MyViewHolder> {

    private List<AvaiableGuides> guidesList;
    private List<BookingRequests> reqsList;
    private List<ProfileDetails> profile;
    private List<String> keys;

    Context context;


    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location, date;
        public Button btnConfirm;
        public ImageView confirmed;

        public MyViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.textViewLocation);
            date = (TextView) view.findViewById(R.id.textViewTime);
            btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
            confirmed = (ImageView) view.findViewById(R.id.imageConfirmed);
        }
    }


    public bookings_as_tourist_adapter(List<BookingRequests> reqsList, List<ProfileDetails> profile , List<AvaiableGuides> guidesList, List<String> keys, Context context) {
        this.guidesList = guidesList;
        this.profile = profile;
        this.reqsList = reqsList;
        this.keys = keys;
        this.context = context;
    }

    @Override
    public bookings_as_tourist_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookings_as_normal, parent, false);

        return new bookings_as_tourist_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(bookings_as_tourist_adapter.MyViewHolder holder, int position) {
        final AvaiableGuides guide = guidesList.get(position);
        final BookingRequests req = reqsList.get(position);
        final ProfileDetails prof = profile.get(position);



        holder.location.setText(guide.getLocation());
        holder.date.setText(req.getDate());

        if(req.getConfirmed()){
            holder.confirmed.setImageResource(R.drawable.checkedtrue);
        }else{
            holder.confirmed.setImageResource(R.drawable.checked);
        }


            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Contact

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(Const.EXTRA_DATA, (Serializable) prof);
                    context.startActivity(intent);


                }
            });




        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(!req.getConfirmed()) {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    alertDialogBuilder.setTitle("Pending").setMessage("No official booking is made until the guide has confirmed the booking. Please wait.");

                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Contact", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra(Const.EXTRA_DATA, (Serializable) prof);
                                    context.startActivity(intent);

                                }
                            })
                            .setNegativeButton("Cancel booking", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    alertDialogBuilder.setTitle("Are you really sure you want to cancel this booking?")
                                            .setMessage("You can't undo this!");

                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //cancel booking
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }else{


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    alertDialogBuilder.setTitle("Confirmed").setMessage("Your booking is confirmed and will set place on "+ req.getDate() + "\r\n with "+
                    prof.getName() + " for " + guide.getPrice() + "/h");

                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Contact", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra(Const.EXTRA_DATA, (Serializable) prof);
                                    context.startActivity(intent);

                                }
                            })
                            .setNegativeButton("Cancel booking", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();


                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return guidesList.size();
    }


}
