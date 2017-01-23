package be.howest.nmct.hellocal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<String> keys;
    private List<AvaiableGuides> ListUserGuides = new ArrayList<>();

    private DatabaseReference mDatabaseReference;


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

            mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        }
    }


    public bookings_as_guide_adapter(List<BookingRequests> reqsList, List<ProfileDetails> profile , List<AvaiableGuides> guidesList, List<String> keys, Context context) {
        this.guidesList = guidesList;
        this.profile = profile;
        this.reqsList = reqsList;
        this.keys = keys;
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
        final String uid = keys.get(position);



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

                                                    final DatabaseReference myRef = database.getReference("bookingRequests");

                                                    ValueEventListener postListener = new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                                                            List<String> list = new ArrayList<>(td.keySet());


                                                            for(int i = 0; i< list.size(); i++){
                                                                if(list.get(i).equals(uid)){
                                                                    HashMap<String, Object> result = new HashMap<>();
                                                                    result.put("confirmed", true);
                                                                    myRef.child(uid).updateChildren(result);
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }

                                                    };
                                                    myRef.addValueEventListener(postListener);


                                                    removeDate(req.getDate(),req.getGuideId());


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
                                                    //TODO -> decline booking
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
                        .setNeutralButton("Save to calander", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar beginTime = Calendar.getInstance();
                                String year = req.getDate().substring(6,10);
                                String month = req.getDate().substring(3,5);
                                String day = req.getDate().substring(0,2);

                                beginTime.set(Integer.parseInt(year),Integer.parseInt(month) -1, Integer.parseInt(day));
                                Calendar endTime = Calendar.getInstance();
                                endTime.set(Integer.parseInt(year),Integer.parseInt(month) -1, Integer.parseInt(day));
                                Intent intent = new Intent(Intent.ACTION_INSERT)
                                        .setData(CalendarContract.Events.CONTENT_URI)
                                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                                        .putExtra(CalendarContract.Events.TITLE, "Hellocal guide for " + prof.getName())
                                        .putExtra(CalendarContract.Events.DESCRIPTION, "You are guiding for " + prof.getName() + ". For a price of " + guide.getPrice() + "€/h.")
                                        .putExtra(CalendarContract.Events.EVENT_LOCATION, guide.getLocation())
                                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                                context.startActivity(intent);
                            }
                        })
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

    public void removeDate(final String date, final String guideId){

            // get all bookings where userId = guideId


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {


                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                List Keys = new ArrayList(td.keySet());

                List<Object> values = new ArrayList<>(td.values());

                for(int i = 0; i< values.size(); i++){

                    Map<String, Object> ts = (HashMap<String,Object>) values.get(i);
                    List<Object> list = new ArrayList<>(ts.values());

                    List keys = new ArrayList<>(td.keySet());
                    String getKey = keys.get(i).toString();

                    String userId = list.get(5).toString();
                    String dateFrom = list.get(8).toString();
                    String dateTill = list.get(1).toString();

                    if(userId.equals(guideId)){

                        Date checkDateFrom = new Date();
                        Date checkDateTill = new Date();
                        Date checkDate = new Date();


                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                             checkDateFrom = format.parse(dateFrom);
                             checkDateTill = format.parse(dateTill);
                             checkDate = format.parse(date);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        if(checkDate.compareTo(checkDateFrom) >= 0 && checkDate.compareTo(checkDateTill)<=0){

                            String name = list.get(9).toString();
                            String country = list.get(2).toString();
                            String location = list.get(4).toString();
                            String maxPeople = list.get(6).toString();
                            String price = list.get(0).toString();
                            String transport = list.get(11).toString();
                            String photoUri = list.get(3).toString();
                            Boolean canBeBooked = Boolean.parseBoolean(list.get(7).toString());

                            ArrayList<String> list2 = (ArrayList<String>) list.get(10);
                            ArrayList<String> type = new ArrayList<>();

                            for (int o = 0; o < list2.size(); o++) {
                                type.add(list2.get(o).toString());
                            }

                            AvaiableGuides guide = new AvaiableGuides(name, country, location, dateFrom, dateTill, maxPeople, price, type, transport, userId, photoUri, getKey, canBeBooked);
                            ListUserGuides.add(guide);

                        }



                    }

                }

                removeDates(date);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("avaiableGuides");
        myRef.addListenerForSingleValueEvent(postListener);



    }


    public void removeDates(String date){



        for(int i = 0 ; i<ListUserGuides.size(); i++){

            String dateFrom = ListUserGuides.get(i).getDateFrom();
            String dateTill = ListUserGuides.get(i).getDateTill();

            Date checkDateFrom = new Date();
            Date checkDateTill = new Date();
            Date checkDate = new Date();


            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                checkDateFrom = format.parse(dateFrom);
                checkDateTill = format.parse(dateTill);
                checkDate = format.parse(date);

            } catch (ParseException e) {
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(checkDate);
            cal.add(Calendar.DATE, -1);
            Date checkDateTill2 = cal.getTime();

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(checkDate);
            cal2.add(Calendar.DATE, 1);
            Date checkDateFrom2 = cal.getTime();

            String stringCheckDateFrom2 = "", stringCheckDateTill2 = "";


            try {
                stringCheckDateFrom2 = format.format(checkDateFrom2);
                stringCheckDateTill2 = format.format(checkDateFrom2);


            } catch (Exception e) {

            }


            String name = ListUserGuides.get(i).getName();
            String country = ListUserGuides.get(i).getCountry();
            String location = ListUserGuides.get(i).getLocation();
            String maxPeople = ListUserGuides.get(i).getMaxPeople();
            String price = ListUserGuides.get(i).getPrice();
            String transport = ListUserGuides.get(i).getTransport();
            String userId = ListUserGuides.get(i).getUserId();
            String photoUri = ListUserGuides.get(i).getPhotoUri();
            String key = ListUserGuides.get(i).getKey();
            Boolean canBeBooked = ListUserGuides.get(i).getCanBeBooked();


            ArrayList<String> list2 = (ArrayList<String>) ListUserGuides.get(i).getType();;
            ArrayList<String> type = new ArrayList<>();

            for (int o = 0; o < list2.size(); o++) {
                type.add(list2.get(o).toString());
            }

            AvaiableGuides guide1 = new AvaiableGuides(name, country, location, dateFrom, stringCheckDateTill2, maxPeople, price, type, transport, userId, photoUri, true);
            AvaiableGuides guide2 = new AvaiableGuides(name, country, location, stringCheckDateFrom2, dateTill, maxPeople, price, type, transport, userId, photoUri, true);




            mDatabaseReference.child("avaiableGuides").push().setValue(guide1);
            mDatabaseReference.child("avaiableGuides").push().setValue(guide2);

            HashMap<String, Object> result = new HashMap<>();
            result.put("canBeBooked", false);

            if(canBeBooked){
                mDatabaseReference.child("avaiableGuides").child(key).updateChildren(result);
            }

        }
    }







//        Toast.makeText(context, date + guideId, Toast.LENGTH_SHORT).show();





    @Override
    public int getItemCount() {
        return guidesList.size();
    }
}
