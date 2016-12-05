package be.howest.nmct.hellocal;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;

import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.Message;
import be.howest.nmct.hellocal.models.ProfileDetails;
import be.howest.nmct.hellocal.models.User;


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
        super.onResume();
        loadInbox();
    }

    private void loadInbox(){
        mContactIds = new ArrayList<String>();
        mMessages = new ArrayList<>();
        database.child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
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
        database.child("profileDetails").child(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetails profile = dataSnapshot.getValue(ProfileDetails.class);
                mProfiles.add(profile);

                InboxRecycleViewAdapter adapter = new InboxRecycleViewAdapter(mProfiles);
                mRecyclerView.setAdapter(adapter);

                progressDia.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                Picasso.with(getActivity().getApplicationContext()).load(profile.getPhotoUri()).into(holder.imageViewPhoto);

            for(Message msg : mMessages){
                if(msg.getReceiver().equals(profile.getProfileId()) || msg.getSender().equals(profile.getProfileId())){
                    holder.textViewReceived.setText(DateUtils.getRelativeDateTimeString(getActivity(), msg.getDate().getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));
                    holder.textViewPreview.setText(msg.getMsg());
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
