package be.howest.nmct.hellocal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<ChatUser> uList;
    public static ChatUser user;

    /*
    public InboxFragment() {
        // Required empty public constructor
    }
    */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance().getReference();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
    */

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

        database.child("chatTest").child("users").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                dia.dismiss();

                long size = dataSnapshot.getChildrenCount();
                if(size == 0){
                    Toast.makeText(getActivity(),
                            R.string.msg_no_user_found,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                uList = new ArrayList<ChatUser>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ChatUser user = ds.getValue(ChatUser.class);
                    Logger.getLogger(InboxFragment.class.getName()).log(Level.ALL, user.getUsername());
                    if(!user.getId().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        uList.add(user);
                }

                Log.i("test", uList.toString());

                ListView list = (ListView) getActivity().findViewById(R.id.list);
                list.setAdapter(new UserAdapter());
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3)
                    {
                        startActivity(new Intent(getActivity(),
                                ChatActivity.class).putExtra(
                                Const.EXTRA_DATA,  uList.get(pos)));
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class UserAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return uList.size();
        }

        @Override
        public ChatUser getItem(int arg0)
        {
            return uList.get(arg0);
        }

        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getActivity().getLayoutInflater().inflate(R.layout.chat_item, null);

            ChatUser c = getItem(pos);
            TextView lbl = (TextView) v;
            Log.i("test",c.getUsername());
            lbl.setText(c.getUsername());
            /*
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.isOnline() ? R.drawable.ic_online
                            : R.drawable.ic_offline, 0, R.drawable.arrow, 0);
                            */


            Log.i("test",v.toString());

            return v;
        }

    }
}
