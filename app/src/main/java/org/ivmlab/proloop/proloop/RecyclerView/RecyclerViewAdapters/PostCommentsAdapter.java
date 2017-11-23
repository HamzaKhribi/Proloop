package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.ProfileActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Comments;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.ProfilePosts;
import org.ivmlab.proloop.proloop.Splashscreen;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.util.HttpURLConnectionExample;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.ViewHolder>{

    private ArrayList<Comments> posts;
    Context context;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
        }
    };

    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private int userID;

    public class deleteCom extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            //ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            //progressBar.setVisibility(View.GONE);

        }
    }

    // Adapter's Constructor
    public PostCommentsAdapter(Context context, ArrayList<Comments> posts) {
        this.posts = posts;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public PostCommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tl_post_comment, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TextView name = (TextView) holder.view.findViewById(R.id.profile_name);
        name.setText(posts.get(position).getName()+"");
        final TextView time = (TextView) holder.view.findViewById(R.id.post_time);
        time.setText(posts.get(position).getDateTime()+"");
        final TextView comment = (TextView) holder.view.findViewById(R.id.post_txt);
        comment.setText(posts.get(position).getCom_text()+"");

        new DownloadImageTask((ImageView) holder.view.findViewById(R.id.profile_img))
                .execute(posts.get(position).getProfileImg());

        sharedPreferences = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("USERID", 0);

        ImageView deleteCmnt = (ImageView) holder.view.findViewById(R.id.deleteCmnt);

        if (Integer.parseInt(posts.get(position).getUser_id())==userID) {
            deleteCmnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog = new Dialog(context);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_delete);

                    Button yes = (Button) dialog.findViewById(R.id.yes);
                    Button no = (Button) dialog.findViewById(R.id.no);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String comID = posts.get(position).getComID();
                            String urlPost = "http://api.ivmlab.org/deleteCom.php?comID=" + comID;
                            new deleteCom().execute(urlPost);
                            dialog.dismiss();

                            posts.remove(position);

                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,posts.size());
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
        else {
            deleteCmnt.setVisibility(View.INVISIBLE);
        }

        TextView profileName = (TextView) holder.view.findViewById(R.id.profile_name);
        ImageView profileImg = (ImageView) holder.view.findViewById(R.id.profile_img);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProfileActivity.class);
                String user_id = posts.get(position).getUser_id();
                intent.putExtra("user_id",user_id);
                context.startActivity(intent);
            }
        });

        profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProfileActivity.class);
                String user_id = posts.get(position).getUser_id();
                intent.putExtra("user_id",user_id);
                context.startActivity(intent);
            }
        });

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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
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
}
