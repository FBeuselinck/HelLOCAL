package be.howest.nmct.hellocal;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import be.howest.nmct.hellocal.contracts.SqliteContract;
import be.howest.nmct.hellocal.helpers.SqliteHelper;
import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.Message;
import be.howest.nmct.hellocal.models.ProfileDetails;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    DatabaseReference database;
    private static RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView noMessages;
    private ArrayList<String> mContactIds;
    private ArrayList<ProfileDetails> mProfiles;
    private ArrayList<Message> mMessages;
    private ProgressDialog progressDia;
    static Activity thisActivity = null;
    private SqliteHelper sqliteHelper;
    private Boolean mIsOnline;

    public static String userId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDia = ProgressDialog.show(getActivity(), null, getString(R.string.alert_loading));
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        View v =inflater.inflate(R.layout.fragment_inbox, container, false);
        database = FirebaseDatabase.getInstance().getReference();

        thisActivity = getActivity();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewInbox);
        mRecyclerView.setHasFixedSize(true);
        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        sqliteHelper = new SqliteHelper(v.getContext());


        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(v.getContext().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mIsOnline = true;
        } else {
            mIsOnline = false;
        }

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onResume(){
        if(mIsOnline) loadInbox();
        else loadDataFromSQL();
        super.onResume();
    }

    private void loadDataFromSQL() {
        getSqlListProfiles();
        try {
            getSqlListMessages();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dismissNoMessages();
        InboxRecycleViewAdapter adapter = new InboxRecycleViewAdapter(mProfiles);
        mRecyclerView.setAdapter(adapter);
        progressDia.dismiss();

    }
    private void getSqlListProfiles(){
        mProfiles = new ArrayList<>();

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String[] projection = {
                SqliteContract.ProfileDetailsMessages.COLUMN_AVAILABLE,
                SqliteContract.ProfileDetailsMessages.COLUMN_PROFILEID,
                SqliteContract.ProfileDetailsMessages.COLUMN_PHONENUMBER,
                SqliteContract.ProfileDetailsMessages.COLUMN_PHOTOURI,
                SqliteContract.ProfileDetailsMessages.COLUMN_NAME,
                SqliteContract.ProfileDetailsMessages.COLUMN_HOMETOWN,
                SqliteContract.ProfileDetailsMessages.COLUMN_DESCRIPTION,
                SqliteContract.ProfileDetailsMessages.COLUMN_BIRTHDATE,
                SqliteContract.ProfileDetailsMessages.COLUMN_GENDER,
        };


        Cursor c = db.query(
                SqliteContract.ProfileDetailsMessages.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        do {
            ProfileDetails pd = new ProfileDetails();
            pd.setPhoneNumber(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_PHONENUMBER)));
            pd.setProfileId(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_PROFILEID)));
            pd.setPhotoUri(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_PHOTOURI)));
            String s = c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_AVAILABLE));
            pd.setBirthDate(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_BIRTHDATE)));
            pd.setDescription(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_DESCRIPTION)));
            pd.setHomeTown(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_HOMETOWN)));
            pd.setName(c.getString(c.getColumnIndex(SqliteContract.ProfileDetailsMessages.COLUMN_NAME)));

            pd.setAvailable(s.equals("true"));

            mProfiles.add(pd);
        } while (c.moveToNext());

    }
    private void getSqlListMessages() throws ParseException {
        mMessages = new ArrayList<>();

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        String[] projection = {
                SqliteContract.Messages.COLUMN_STATUS,
                SqliteContract.Messages.COLUMN_SENDER,
                SqliteContract.Messages.COLUMN_RECIEVER,
                SqliteContract.Messages.COLUMN_MESSAGE,
                SqliteContract.Messages.COLUMN_DATE,
                SqliteContract.Messages.COLUMN_SENT
        };


        Cursor c = db.query(
                SqliteContract.Messages.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        do {
            Message msg = new Message();

            String s = c.getString(c.getColumnIndex(SqliteContract.Messages.COLUMN_DATE));
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
            msg.setDate((df.parse(s)));

            msg.setMsg(c.getString(c.getColumnIndex(SqliteContract.Messages.COLUMN_MESSAGE)));
            msg.setReceiver(c.getString(c.getColumnIndex(SqliteContract.Messages.COLUMN_RECIEVER)));
            msg.setSender(c.getString(c.getColumnIndex(SqliteContract.Messages.COLUMN_SENDER)));
            s = c.getString(c.getColumnIndex(SqliteContract.Messages.COLUMN_SENT));
            if(s.equals("1"))
                msg.setSent(true);
            else
            msg.setSent(false);

            msg.setStatus(c.getInt(c.getColumnIndex(SqliteContract.Messages.COLUMN_STATUS)));



           mMessages.add(msg);
        } while (c.moveToNext());

    }

    private void loadInbox(){
        mContactIds = new ArrayList<String>();
        mMessages = new ArrayList<>();
        database.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message msg = ds.getValue(Message.class);
                    if (msg.getSender().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        mMessages.add(msg);
                        if(!mContactIds.contains(msg.getReceiver())){
                            mContactIds.add(msg.getReceiver());
                            getContact(msg.getReceiver());
                        }
                    }
                    else if(msg.getReceiver().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        mMessages.add(msg);
                        if(!mContactIds.contains(msg.getSender())){
                            mContactIds.add(msg.getSender());
                            getContact(msg.getSender());
                        }
                    }
                }
                dismissNoMessages();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getContact(String contactId){
        mProfiles = new ArrayList<ProfileDetails>();
        database.child("profileDetails").child(contactId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetails profile = dataSnapshot.getValue(ProfileDetails.class);
                mProfiles.add(profile);

                InboxRecycleViewAdapter adapter = new InboxRecycleViewAdapter(mProfiles);
                mRecyclerView.setAdapter(adapter);

                progressDia.dismiss();

                SaveSQL();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SaveSQL(){
        //reset database
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        sqliteHelper.onUpgrade(db,0,2);

        SaveSQLProfiles(mProfiles);
        SaveSQLMessages(mMessages);
    }

    private void SaveSQLProfiles(ArrayList<ProfileDetails> list) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        for(int j = 0; j<list.size(); j++){
            ContentValues values = new ContentValues();
            ProfileDetails pd = list.get(j);
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_AVAILABLE, pd.getAvailable());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_BIRTHDATE, pd.getBirthDate());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_DESCRIPTION, pd.getDescription());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_GENDER, pd.getGender().toString());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_HOMETOWN, pd.getHomeTown());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_NAME, pd.getName());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_PHONENUMBER, pd.getPhoneNumber());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_PROFILEID, pd.getProfileId());
            values.put(SqliteContract.ProfileDetailsMessages.COLUMN_PHOTOURI, pd.getPhotoUri());

            db.insert(SqliteContract.ProfileDetailsMessages.TABLE_NAME, null, values);
        }
    }

    private void SaveSQLMessages(ArrayList<Message> list){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        for(int i = 0; i<list.size(); i++){
            ContentValues values = new ContentValues();
            Message msg = list.get(i);
            values.put(SqliteContract.Messages.COLUMN_DATE, msg.getDate().toString());
            values.put(SqliteContract.Messages.COLUMN_MESSAGE, msg.getMsg());
            values.put(SqliteContract.Messages.COLUMN_RECIEVER, msg.getReceiver());
            values.put(SqliteContract.Messages.COLUMN_SENDER, msg.getSender());
            values.put(SqliteContract.Messages.COLUMN_SENT, msg.getSent());
            values.put(SqliteContract.Messages.COLUMN_STATUS, msg.getStatus());

            db.insert(SqliteContract.Messages.TABLE_NAME, null, values);
        }
    }
    private void dismissNoMessages(){
        noMessages = (TextView) getActivity().findViewById(R.id.noMessages);
        noMessages.setVisibility(View.GONE);
    }

    public class InboxRecycleViewAdapter extends RecyclerView.Adapter<InboxRecycleViewAdapter.InboxViewHolder> {
        private ArrayList<ProfileDetails> profiles = null;

        public InboxRecycleViewAdapter(ArrayList<ProfileDetails> profiles) {
            this.profiles = profiles;
        }

        @Override
        public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inbox, parent, false);
            return new InboxViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InboxViewHolder holder, int position) {
            ProfileDetails profile = profiles.get(position);
            holder.textViewNaam.setText(profile.getName());

            if(!profile.getPhotoUri().isEmpty())
                Picasso.with(getActivity().getApplicationContext()).load(profile.getPhotoUri())
                        .placeholder(R.drawable.ic_menu_camera).fit()
                        .centerCrop().into(holder.imageViewPhoto);

            String[] previewMsg;
            for(Message msg : mMessages){
                if(msg.getReceiver().equals(profile.getProfileId()) || msg.getSender().equals(profile.getProfileId())){
                    holder.textViewReceived.setText(DateUtils.getRelativeDateTimeString(getActivity(), msg.getDate().getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));

                    previewMsg = msg.getMsg().split("\n", 2);
                    previewMsg[0] = previewMsg[0].length() > 30 ? previewMsg[0].substring(0, 27) + "..." : previewMsg[0];
                    holder.textViewPreview.setText(previewMsg[0]);
                }
            }
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

        class InboxViewHolder extends RecyclerView.ViewHolder {

            TextView textViewNaam;
            TextView textViewReceived;
            TextView textViewPreview;
            ImageView imageViewPhoto;

            public InboxViewHolder(View v) {
                super(v);
                textViewNaam = (TextView) v.findViewById(R.id.textViewNaam);
                textViewReceived = (TextView) v.findViewById(R.id.textViewReceived);
                textViewPreview = (TextView) v.findViewById(R.id.textViewPreview);
                imageViewPhoto = (ImageView) v.findViewById(R.id.imagePhoto);

                ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        //TextView Name = (TextView) v.findViewById(R.id.textViewNaam);

                        Intent intent = new Intent(thisActivity, ChatActivity.class);
                        intent.putExtra(Const.EXTRA_DATA, profiles.get(position));

                        thisActivity.startActivity(intent);
                    }
                });
            }
        }
    }
}
