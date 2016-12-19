package be.howest.nmct.hellocal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.Conversation;
import be.howest.nmct.hellocal.models.ProfileDetails;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Conversation> convList;
    private ChatAdapter adp;
    private EditText txt;
    private ProfileDetails buddy;
    private Date lastMsgDate;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        buddy = (ProfileDetails) getIntent().getSerializableExtra(Const.EXTRA_DATA);

        if(ab != null)
            ab.setTitle(buddy.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConversationList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            sendMessage();
        }
    }

    private void sendMessage() {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {

            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            Calendar calendar = Calendar.getInstance(timeZone);
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            simpleDateFormat.setTimeZone(timeZone);

            final Conversation conversation = new Conversation(s,
                    calendar.getInstance().getTime(),
                    user.getUid(),
                    buddy.getProfileId(),
                    "");
            conversation.setStatus(Conversation.STATUS_SENDING);
            convList.add(conversation);
            final String key = FirebaseDatabase.getInstance()
                    .getReference("messages")
                    .push().getKey();
            FirebaseDatabase.getInstance().getReference("messages").child(key)
                    .setValue(conversation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()) {
                                                       convList.get(convList.indexOf(conversation)).setStatus(Conversation.STATUS_SENT);
                                                   } else {
                                                       convList.get(convList.indexOf(conversation)).setStatus(Conversation.STATUS_FAILED);
                                                   }
                                                   FirebaseDatabase.getInstance()
                                                           .getReference("messages")
                                                           .child(key).setValue(convList.get(convList.indexOf(conversation)))
                                                           .addOnCompleteListener(new
                                                                                          OnCompleteListener<Void>() {
                                                                                              @Override
                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                  adp.notifyDataSetChanged();
                                                                                              }
                                                                                          });

                                               }
                                           }
                    );
        }
        adp.notifyDataSetChanged();
        txt.setText(null);
    }

    private void loadConversationList() {
        FirebaseDatabase.getInstance().getReference("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot ds, String prevChildKey) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    Conversation conversation = ds.getValue(Conversation.class);
                    if ((conversation.getReceiver().contentEquals(user.getUid()) && conversation.getSender().contentEquals(buddy.getProfileId())) || (conversation.getSender().contentEquals(user.getUid()) && conversation.getReceiver().contentEquals(buddy.getProfileId()))) {
                        convList.add(conversation);
                        if (lastMsgDate == null
                                || lastMsgDate.before(conversation.getDate()))
                            lastMsgDate = conversation.getDate();
                        adp.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey){

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey){

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        FirebaseDatabase.getInstance().getReference("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Conversation conversation = ds.getValue(Conversation.class);
                        if ((conversation.getReceiver().contentEquals(user.getUid()) && conversation.getSender().contentEquals(buddy.getProfileId())) || (conversation.getSender().contentEquals(user.getUid()) && conversation.getReceiver().contentEquals(buddy.getProfileId()))) {
                            convList.add(conversation);
                            if (lastMsgDate == null
                                    || lastMsgDate.before(conversation.getDate()))
                                lastMsgDate = conversation.getDate();

                            adp.notifyDataSetChanged();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

    }

    private class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return convList.size();
        }

        @Override
        public Conversation getItem(int arg0) {
            return convList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.row_chat_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.row_chat_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent()) {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    lbl.setText(R.string.delivered_text);
                else {
                    if (c.getStatus() == Conversation.STATUS_SENDING)
                        lbl.setText(R.string.sending_text);
                    else {
                        lbl.setText(R.string.failed_text);
                    }
                }
            } else
                lbl.setText("");

            return v;
        }

    }
}
