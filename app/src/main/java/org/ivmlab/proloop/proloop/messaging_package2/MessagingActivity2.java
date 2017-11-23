package org.ivmlab.proloop.proloop.messaging_package2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.ivmlab.proloop.proloop.MainActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MessagingActivity2 extends ActionBarActivity implements View.OnClickListener,
        MessageDataSource.MessagesCallbacks{

    public static final String USER_EXTRA = "USER";

    public static final String TAG = "ChatActivity";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private String mConvoId;
    private MessageDataSource.MessagesListener mListener;
    private Firebase mFirebaseRef;
    private String Usr_Name;
    private int usr_id_2,usr_id;
    public SharedPreferences sharedPreferences;
    private String username,urlPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeMessage);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging2);
        if(getIntent().getExtras()!=null){
            Bundle b = getIntent().getExtras();
            Usr_Name = b.getString("chatting_with");
            usr_id_2 = Integer.parseInt(b.getString("chatting_with_id"));
            Log.e("Usr_Name",Usr_Name);
            Log.e("usr_id_2",Integer.toString(usr_id_2));

        }

        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        usr_id = sharedPreferences.getInt("USERID", 0);
        username=sharedPreferences.getString("NAME","user");


        mRecipient = Usr_Name;
        Firebase.setAndroidContext(this);

        //Newer version of Firebase
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


        mListView = (ListView)findViewById(R.id.messages_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);
        mListView.setAdapter(mAdapter);

        setTitle(mRecipient);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton sendMessage = (FloatingActionButton) findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

        /*String[] ids = {"Ajay","-", "Ashok"};
        Arrays.sort(ids);
        mConvoId = ids[0]+ids[1]+ids[2];*/

        scrollMyListViewToBottom(mListView,mAdapter);
        if (usr_id < usr_id_2){
            mConvoId = usr_id+"-"+ usr_id_2;
            //FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications"+usr_id+"-"+usr_id_2);
        } else {
            mConvoId = usr_id_2 +"-"+usr_id;
            //FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications"+usr_id_2+"-"+usr_id);
        }

        mListener = MessageDataSource.addMessagesListener(mConvoId, this);

    }

    public void onClick(View v) {
        EditText newMessageView = (EditText)findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        if (!newMessage.equals("")){
            newMessageView.setText("");
            Message msg = new Message();
            msg.setDate(new Date());
            msg.setText(newMessage);
            msg.setSender(username);
            MessageDataSource.saveMessage(msg, mConvoId);



            SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            datetime.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date= datetime.format(new Date());

            String[] splited = date.split("\\s+");
            urlPost="http://api.ivmlab.org/InsertAlterMessaging.php?user_id="+usr_id+"&user_id2="+usr_id_2+"&msg="+msg+"&chatting_with="+username+"&DateTime="+splited[0]+"%20"+splited[1];
            Log.e("urlllllPost",urlPost);
            new AsyncTaskUpdateLastMessage().execute(urlPost);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        scrollMyListViewToBottom(mListView,mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(MessagingActivity2.this, MainActivity.class);

        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("backSelectedH", "1");
        //editor.putString("URL1", urlPost1);


        editor.commit();
        finish();
        return;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageDataSource.stop(mListener);
    }


    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages){
            super(MessagingActivity2.this, R.layout.message, R.id.message, messages);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView)convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)nameView.getLayoutParams();

            int sdk = Build.VERSION.SDK_INT;
            if (message.getSender().equals(username)){
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                    nameView.setTextColor(Color.WHITE);
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                    nameView.setTextColor(Color.WHITE);
                }
                layoutParams.gravity = Gravity.RIGHT;
            }else{
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                    nameView.setTextColor(Color.BLACK);
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                    nameView.setTextColor(Color.BLACK);
                }
                layoutParams.gravity = Gravity.LEFT;
            }

            nameView.setLayoutParams(layoutParams);


            return convertView;
        }
    }
    private void scrollMyListViewToBottom(final ListView myListView, final MessagesAdapter myListAdapter ) {
        myListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                myListView.setSelection(myListAdapter.getCount() - 1);
            }
        });
    }
    public class AsyncTaskUpdateLastMessage extends AsyncTask<String, String, String> {


        private JSONObject jsonObjectRating;

        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            try {
                jsonObjectRating = JsonParser.readJsonFromUrl(urlPost);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
