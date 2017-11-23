package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.ProfileActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Pos;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<User> users;
    ArrayList<Pos> poses=new ArrayList<>();
    Context context;
    int position;
    View v;
    LinearLayout linearLayoutUser;


    // Adapter's Constructor
    public UserAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Set strings to the views
        final TextView textViewName = (TextView) holder.view.findViewById(R.id.textViewItemName);
        final TextView textViewBio = (TextView) holder.view.findViewById(R.id.textViewBio);

        textViewName.setText(users.get(position).getName()+" "+users.get(position).getLastName());
        if (users.get(position).getBio().equals("null")){
            textViewBio.setText("");
        } else {
            textViewBio.setText(users.get(position).getBio());
        }
        //  textViewBirthday.setText(users.get(position).getUsr_bday_day()+"-"+users.get(position).getUsr_bday_month()+"-"+users.get(position).getUsr_bday_year()+"");

        if (users.get(position).getProfilePic().length()>1){
            new DownloadImageTask((ImageView) holder.view.findViewById(R.id.imageViewImage))
                    .execute(users.get(position).getProfilePic());

        }

        linearLayoutUser = (LinearLayout) holder.view.findViewById(R.id.linearLayoutUser);
        linearLayoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProfileActivity.class);
                String user_id = users.get(position).getId();
                intent.putExtra("user_id",user_id);
                context.startActivity(intent);
            }
        });


      /*  v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("getUsr_id",users.get(position).getId());;

                intent.putExtras(b);
                context.startActivity(intent);
            }
        });*/
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
            Bitmap decoded=null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.JPEG,100,out);
                decoded =BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return decoded;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
