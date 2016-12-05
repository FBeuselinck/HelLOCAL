package be.howest.nmct.hellocal;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.Conversation;
import be.howest.nmct.hellocal.models.ProfileDetails;

public class ChatActivity extends FragmentActivity implements View.OnClickListener{

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

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.setTitle(buddy.getProfileId());

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
            final Conversation conversation = new Conversation(s,
                    Calendar.getInstance().getTime(),
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

        FirebaseDatabase.getInstance().getReference("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Conversation conversation = ds.getValue(Conversation.class);
                        if (conversation.getReceiver().contentEquals(user.getUid()) || conversation.getSender().contentEquals(user.getUid())) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
