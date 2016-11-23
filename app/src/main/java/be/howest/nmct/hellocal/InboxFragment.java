package be.howest.nmct.hellocal;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import be.howest.nmct.hellocal.models.ChatUser;
import be.howest.nmct.hellocal.models.Const;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    DatabaseReference database;
    private static RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView noMessages;

    static Activity thisActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v =inflater.inflate(R.layout.fragment_inbox, container, false);
        database = FirebaseDatabase.getInstance().getReference();

        //noMessages = (TextView) getActivity().findViewById(R.id.noMessages);
        //noMessages.setVisibility(View.INVISIBLE);

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
        loadUserList();
    }

    private void loadUserList(){
        final ProgressDialog dia = ProgressDialog.show(getActivity(), null, getString(R.string.alert_loading));

        DatabaseReference queryRef = database.child("chatTest").child("users").getRef();

        Log.i("------------------->", queryRef.toString());
        FirebaseRecyclerAdapter<ChatUser,MessageViewHolder> adapter = new FirebaseRecyclerAdapter<ChatUser, MessageViewHolder>(
                ChatUser.class,
                R.layout.row_inbox,
                MessageViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                queryRef
        ){
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, ChatUser model, int position) {
                dia.dismiss();

                viewHolder.textViewNaam.setText(model.getName());
                Log.i("------------------->", viewHolder.textViewNaam.getText().toString());

                //Picasso.with(getApplicationContext()).load(model.getPhotoUri()).into(viewHolder.imageViewPhoto);
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNaam;
        ImageView imageViewPhoto;
        public ChatUser chatUser;

        public MessageViewHolder(View v) {
            super(v);
            textViewNaam = (TextView) v.findViewById(R.id.textViewNaam);
            imageViewPhoto = (ImageView) v.findViewById(R.id.imagePhoto);

            ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    TextView Name = (TextView) v.findViewById(R.id.textViewNaam);

                    Intent intent = new Intent(thisActivity, ChatActivity.class);
                    intent.putExtra("Name",Name.getText().toString());
                    //intent.putExtra("PhotoUri",Name.getTag(R.id.photoUri).toString());

                    thisActivity.startActivity(intent);
                }
            });
        }
    }
}
