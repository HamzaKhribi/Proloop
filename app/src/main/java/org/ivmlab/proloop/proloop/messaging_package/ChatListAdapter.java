package org.ivmlab.proloop.proloop.messaging_package;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.R;
import com.firebase.client.Query;

import java.io.InputStream;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    Drawable image;
    private String image_path;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername, Drawable image,String image_path) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
        this.image=image;
        this.image_path=image_path;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        //TextView authorText = (TextView) view.findViewById(R.id.author);
        //CircleImageView profilePicture = (CircleImageView) view.findViewById(R.id.profile_pic);
        //CircleImageView profilePicture2 = (CircleImageView) view.findViewById(R.id.profile_pic2);
        TextView message = (TextView) view.findViewById(R.id.message);
        //TextView message2 = (TextView) view.findViewById(R.id.message2);
        //authorText.setText(author + ": ");
        RelativeLayout messageContainer = (RelativeLayout) view.findViewById(R.id.messageContainer);
        LinearLayout containerContainer = (LinearLayout) view.findViewById(R.id.containerContainer);       // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
           // message.setTextColor(Color.RED);
            //new DownloadImageTask(profilePicture).execute(image_path);
            message.setText(chat.getMessage());
            message.setTextColor(Color.BLACK);
            containerContainer.setGravity(Gravity.LEFT);
            messageContainer.setBackgroundColor(Color.parseColor("#EEEEEE"));

        } else {
            //message.setTextColor(Color.BLUE);
            //profilePicture.setImageDrawable(image);
            message.setText(chat.getMessage());
            message.setTextColor(Color.WHITE);
            containerContainer.setGravity(Gravity.RIGHT);
            messageContainer.setBackgroundColor(Color.parseColor("#669de0"));

        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
