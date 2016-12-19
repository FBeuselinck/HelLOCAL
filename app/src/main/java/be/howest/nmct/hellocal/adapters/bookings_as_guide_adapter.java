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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import be.howest.nmct.hellocal.ChatActivity;
import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.BookingRequests;
import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.ProfileDetails;

/**
 * Created by Simon on 12/12/2016.
 */

public class bookings_as_guide_adapter extends RecyclerView.Adapter<bookings_as_guide_adapter.MyViewHolder>  {

    private List<AvaiableGuides> guidesList;
    private List<BookingRequests> reqsList;
    private List<ProfileDetails> profile;

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


    public bookings_as_guide_adapter(List<BookingRequests> reqsList, List<ProfileDetails> profile , List<AvaiableGuides> guidesList, Context context) {
        this.guidesList = guidesList;
        this.profile = profile;
        this.reqsList = reqsList;
        this.context = context;
    }

    @Override
    public bookings_as_guide_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookings_as_guide, parent, false);

        return new bookings_as_guide_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(bookings_as_guide_adapter.MyViewHolder holder, int position) {
        final AvaiableGuides guide = guidesList.get(position);
        final BookingRequests req = reqsList.get(position);
        final ProfileDetails prof = profile.get(position);



        holder.location.setText(guide.getLocation());
        holder.date.setText(req.getDate());

        if(req.getConfirmed()){
            holder.confirmed.setImageResource(R.drawable.checkedtrue);
            holder.btnConfirm.setText("CONTACT");
        }else{
            holder.confirmed.setImageResource(R.drawable.checked);
            holder.btnConfirm.setText("ANSWER");
        }

        if(req.getConfirmed()){

            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Contact

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(Const.EXTRA_DATA, (Serializable) prof);
                    context.startActivity(intent);


                }
            });

        }else{

            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    alertDialogBuilder.setTitle("Do you want to confirm this booking?");

                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //confirm booking

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    alertDialogBuilder.setTitle("Are you really sure you want to confirm this booking?")
                                            .setMessage("You can't undo this!");

                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    //confirm booking


                                                    ValueEventListener postListener = new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                                                            List Keys = new ArrayList(td.keySet());


                                                            for (int i = 0; i < Keys.size(); i++) {

//                                                                Map<String, Object> ts = (HashMap<String, Object>) values.get(i);
//                                                                List<Object> list = new ArrayList<>(ts.values());

                                                                String currentKey = Keys.get(i).toString();

                                                                Boolean checkTrue = false;
//
//                                                                for (int q = 0; q < ListAvailableGuidesIds.size(); q++) {
//                                                                    if (ListAvailableGuidesIds.get(q).equals(currentKey)) {
//                                                                        checkTrue = true;
//                                                                    }
//                                                                }

                                                               
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }

                                                    };
                                                    DatabaseReference myRef = database.getReference("bookingRequests");
                                                    myRef.addListenerForSingleValueEvent(postListener);

                                                }})




                                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }
                            })
                            .setNegativeButton("Decline",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // decline booking

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    alertDialogBuilder.setTitle("Are you really sure you want to decline this booking?")
                                            .setMessage("You can't undo this!");

                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    //decline booking
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
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                    
                }
            });

        }


        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Booking details")
                .setMessage("Booking with " + prof.getName() + "\r\n" +
                            "on " + req.getDate() + " for " + guide.getPrice() + "/h");

                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Contact",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //contact guide
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
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
