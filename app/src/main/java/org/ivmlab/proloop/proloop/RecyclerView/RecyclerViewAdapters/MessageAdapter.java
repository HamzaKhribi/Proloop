package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.MainActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Pos;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.User;
import org.ivmlab.proloop.proloop.messaging_package2.MessagingActivity2;

import java.io.InputStream;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private ArrayList<User> users;
    ArrayList<Pos> poses=new ArrayList<>();
    Context context;
    int position;
    View v;
    private ImageView imageViewImage;


    // Adapter's Constructor
    public MessageAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Set strings to the views
        final TextView textViewName = (TextView) holder.view.findViewById(R.id.textViewItemName);
        final TextView textViewBirthday = (TextView) holder.view.findViewById(R.id.textViewItemBirthday);

        textViewName.setText(users.get(position).getName()+" "+users.get(position).getLastName());
      //  textViewBirthday.setText(users.get(position).getUsr_bday_day()+"-"+users.get(position).getUsr_bday_month()+"-"+users.get(position).getUsr_bday_year()+"");

        if (users.get(position).getProfilePic().length()>1){
            new DownloadImageTask((ImageView) holder.view.findViewById(R.id.imageViewImage))
                .execute(users.get(position).getProfilePic());
        }


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MessagingActivity2.class);
                Bundle b = new Bundle();
                b.putString("chatting_with",users.get(position).getName()+" "+users.get(position).getLastName());
                b.putString("chatting_with_id", users.get(position).getId());


                imageViewImage = (ImageView) holder.view.findViewById(R.id.imageViewImage);
                imageViewImage.buildDrawingCache();
                Bitmap image= imageViewImage.getDrawingCache();
                b.putParcelable("imagebitmap", image);


                intent.putExtras(b);
                context.startActivity(intent);
                ((MainActivity)context).finish();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
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
