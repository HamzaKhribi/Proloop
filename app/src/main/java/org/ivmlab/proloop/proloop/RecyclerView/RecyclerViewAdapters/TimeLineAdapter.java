package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.ProfileActivity;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Footer;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Item;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Comments;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePost;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePosts;
import org.ivmlab.proloop.proloop.Utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_POST = 0;
    private static final int TYPE_FOOTER = 1;

    private ArrayList<Item> posts;
    private ArrayList<Comments> comments=new ArrayList<>();
    private Context context;
    private Dialog dialog;
    private PostCommentsAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private boolean liked;
    private EditText cmntToPush;
    private TimeLinePost p;
    private String formattedDate;
    private int user_id;
    private String a;
    private String getComURL;
    private int positionPub;
    private TimeLinePosts nbrCmnt;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
        }
    };
    private String urlPost2;
    private String urlPost;
    private String urlPost3;

    public TimeLineAdapter(Context context, ArrayList<Item> posts) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (posts.get(position) instanceof TimeLinePosts) {
            return TYPE_POST;
        } else if (posts.get(position) instanceof Footer) {
            return TYPE_FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POST) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.tl_post, parent, false);
            PostsViewHolder pvh = new PostsViewHolder(row);
            return pvh;
        } else {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(row);
            return vh;
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        if (holder instanceof PostsViewHolder) {
            final TimeLinePosts timeLinePosts = (TimeLinePosts) posts.get(position);

            final PostsViewHolder postsViewHolder = (PostsViewHolder) holder;

            postsViewHolder.getName().setText(timeLinePosts.getName() + " " + timeLinePosts.getLastName());

            postsViewHolder.getNbrCmnt().setText(timeLinePosts.getNbrOfComs() + " comments");


            if (timeLinePosts.getProfileImgPath().length() > 1) {
                new DownloadImageTask(postsViewHolder.getProfile_img())
                        .execute(timeLinePosts.getProfileImgPath());

            }

            postsViewHolder.getTxt().setText(timeLinePosts.getText());
            if (timeLinePosts.getTagPub().equalsIgnoreCase("media")) {
                new DownloadImageTask(postsViewHolder.getImg_post())
                        .execute(timeLinePosts.getImgPath());

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
                postsViewHolder.getImg().setLayoutParams(layoutParams);
            }
            postsViewHolder.getNumbLikes().setText(timeLinePosts.getCountFaves() + " Likes");

            if (timeLinePosts.getFaved() == "1") {
                liked = true;
                postsViewHolder.getLike().setImageDrawable(context.getResources().getDrawable(R.drawable.like_done));
            } else {
                liked = false;
                postsViewHolder.getLike().setImageDrawable(context.getResources().getDrawable(R.drawable.like));
            }

            postsViewHolder.getLikeArea().setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    if (timeLinePosts.getFaved() == "1") {
                        liked = true;
                    } else {
                        liked = false;
                    }

                    if (liked == true) {
                        int nbrLikes = timeLinePosts.getCountFaves() - 1;
                        timeLinePosts.setFaved("0");
                        timeLinePosts.setCountFaves(nbrLikes);
                        postsViewHolder.getNumbLikes().setText(nbrLikes + " Likes");
                        postsViewHolder.getLike().setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                        sharedPreferences = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
                        int user_id = sharedPreferences.getInt("USERID", 0);

                        urlPost2 = "http://api.ivmlab.org/unfavPub.php?user_id=" + user_id + "&pub_id=" + timeLinePosts.getPub_id();
                        new favPub().execute(urlPost2);
                        liked = false;
                    } else {
                        int nbrLikes = timeLinePosts.getCountFaves() + 1;
                        timeLinePosts.setCountFaves(nbrLikes);
                        timeLinePosts.setFaved("1");
                        postsViewHolder.getNumbLikes().setText(nbrLikes + " Likes");
                        liked = true;
                        postsViewHolder.getLike().setImageDrawable(context.getResources().getDrawable(R.drawable.like_done));
                        sharedPreferences = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
                        int user_id = sharedPreferences.getInt("USERID", 0);
                        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date= datetime.format(new Date());
                        String[] splited = date.split("\\s+");
                        urlPost2 = "http://api.ivmlab.org/favPub.php?userID=" + user_id + "&pub_id=" + timeLinePosts.getPub_id()+"&fav_time="+splited[0]+"%20"+splited[1];
                        new favPub().execute(urlPost2);
                    }


                }
            });

            postsViewHolder.getCmntArea().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String id = String.valueOf(timeLinePosts.getPub_id());

                    dialog = new Dialog(context);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_menu);

                    String urlPost1 = "http://api.ivmlab.org/getPublicationByPubID.php?id=" + id;
                    Log.d("url", urlPost1);

                    positionPub = position;
                    new getPubInfoTask().execute(urlPost1);


                    recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerViewTLPosts21);


                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);


                    getComURL = "http://api.ivmlab.org/getCom.php?pub_id=" + id;
                    new AsyncTaskNewsParseJson().execute(getComURL);
                    dialog.show();
                }
            });

            postsViewHolder.getName().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ProfileActivity.class);
                    String user_id = timeLinePosts.getUserID();
                    intent.putExtra("user_id",user_id);
                    context.startActivity(intent);
                }
            });

            postsViewHolder.getProfile_img().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ProfileActivity.class);
                    String user_id = timeLinePosts.getUserID();
                    intent.putExtra("user_id",user_id);
                    context.startActivity(intent);
                }
            });
        }
    }

    public int getItemCount() {
        return posts.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView nbrCmnt;
        private TextView txt;
        private ImageView img;
        private ImageView profile_img;
        private ImageView img_post;
        private ImageView like;
        private RelativeLayout likeArea;
        private LinearLayout postView;
        private RelativeLayout cmntArea;
        private TextView numbLikes;

        public PostsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.profile_name);
            nbrCmnt = (TextView) itemView.findViewById(R.id.nbrCmnt);
            txt = (TextView) itemView.findViewById(R.id.post_txt);
            numbLikes = (TextView) itemView.findViewById(R.id.numbLikes);
            img = (ImageView) itemView.findViewById(R.id.img_post);
            like = (ImageView) itemView.findViewById(R.id.like);
            profile_img = (ImageView) itemView.findViewById(R.id.profile_img);
            img_post = (ImageView) itemView.findViewById(R.id.img_post);
            likeArea = (RelativeLayout) itemView.findViewById(R.id.LikeArea);
            postView = (LinearLayout) itemView.findViewById(R.id.postView);
            cmntArea = (RelativeLayout) itemView.findViewById(R.id.cmntArea);
        }

        public TextView getName() {
            return name;
        }

        public TextView getNbrCmnt() {
            return nbrCmnt;
        }

        public TextView getTxt() {
            return txt;
        }

        public ImageView getProfile_img() {
            return profile_img;
        }

        public ImageView getImg_post() {
            return img_post;
        }

        public TextView getNumbLikes() {
            return numbLikes;
        }

        public ImageView getImg() {
            return img;
        }

        public ImageView getLike() {
            return like;
        }

        public RelativeLayout getLikeArea() {
            return likeArea;
        }

        public LinearLayout getPostView() {
            return postView;
        }

        public RelativeLayout getCmntArea() {
            return cmntArea;
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar getProgressBar() {
            return progressBar;
        }

        private ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.footer);
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

    private class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            comments=new ArrayList<>();
            comments.clear();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            try {
                Log.d("jsonObject",urlPost.toString());
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectPosts.getInt("count");
                for (int i=1;i<=count;i++){
                    JSONObject user=jsonObjectPosts.getJSONObject(""+i);
                    String com_text=user.getString("com_text");
                    String DateTime=user.getString("DateTime");
                    String name=user.getString("name");
                    String id=user.getString("id");
                    String user_id=user.getString("user_id");
                    String profileImg = user.getString("profilePictureImgPath");

                    comments.add(new Comments(com_text,DateTime,name,profileImg,id,user_id));
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {


            // Create the recyclerViewAdapter

            recyclerViewAdapter = new PostCommentsAdapter(context, comments);
            recyclerView.setAdapter(recyclerViewAdapter);

            //swipeRefreshLayout = (SwipeRefreshLayout) dialog.findViewById(R.id.swipe_container1);
            //swipeRefreshLayout.setRefreshing(false);

            ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.GONE);

        }
    }

    private class getPubInfoTask extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            comments=new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectPosts.getInt("count");
                for (int i=1;i<=count;i++){
                    JSONObject user=jsonObjectPosts.getJSONObject(""+i);
                    String pub_id=user.getString("id");
                    String userID=user.getString("userID");
                    String text=user.getString("text");
                    String imgPath=user.getString("imgPath");
                    String tagPub=user.getString("tag_pub");
                    String dateTime=user.getString("DateTime");
                    String name=user.getString("Name");
                    String lastName=user.getString("LastName");
                    String profileImgPath=user.getString("profilePictureImgPath");

                    p = new TimeLinePost(pub_id,userID,text,imgPath,tagPub,dateTime,name,lastName,profileImgPath);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {
            final TextView name = (TextView) dialog.findViewById(R.id.profile_name1);

            name.setText(p.getName()+" "+p.getLastName());
            final TextView txt = (TextView) dialog.findViewById(R.id.post_txt1);
            txt.setText(p.getText());
            if (p.getProfileImgPath().length()>1){
                new DownloadImageTask((ImageView) dialog.findViewById(R.id.profile_img1))
                        .execute(p.getProfileImgPath());
            }

            /*if (p.getTagPub().equalsIgnoreCase("media")){
                final ImageView img = (ImageView) dialog.findViewById(R.id.img_post1);
                Log.d("img_path",p.getImgPath());
                new DownloadImageTask((ImageView) dialog.findViewById(R.id.img_post1))
                        .execute(p.getImgPath());
                img.getLayoutParams().height = 400;
                img.requestLayout();
            }*/

            final ImageView like = (ImageView) dialog.findViewById(R.id.like);
            /*like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setImageDrawable(context.getResources().getDrawable(R.drawable.like_done));

                }
            });*/

            ImageView pushCmnt = (ImageView) dialog.findViewById(R.id.pushCmnt);
            cmntToPush = (EditText) dialog.findViewById(R.id.cmntToPush);
            pushCmnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    datetime.setTimeZone(TimeZone.getTimeZone("gmt"));
                    String date= datetime.format(new Date());
                    String[] splited = date.split("\\s+");


                    sharedPreferences = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
                    user_id = sharedPreferences.getInt("USERID", 0);

                    String cmntToInsert = cmntToPush.getText().toString();
                    a = cmntToInsert.replaceAll(" ","%20");
                    if (cmntToInsert.equals("")){
                        Toast.makeText(context, "Enter some text!",Toast.LENGTH_LONG).show();
                    }else{
                         urlPost3 = "http://api.ivmlab.org/insertCom.php?user_id="+user_id+"&pub_id="+p.getPub_id()+"&cmnt="+a+"&DateTime="+splited[0]+"%20"+splited[1];
                        Log.e("commentaiiiiire",urlPost3);
                        new favPub1().execute(urlPost3);

                        cmntToPush.setText("");
                         urlPost = "http://api.ivmlab.org/getCom.php?pub_id="+ p.getPub_id();
                        new AsyncTaskNewsParseJson().execute(urlPost);

                        /*String nbrCom = String.valueOf(posts.get(positionPub).getNbrOfComs()+1);
                        Log.d("nbrCom",nbrCom);
                        nbrCmnt.setText(nbrCom+" comments");*/
                    }
                }
            });

            //ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            //progressBar.setVisibility(View.GONE);

        }
    }

    private class favPub extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

             urlPost2 = url[0];

            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost2);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews

        @Override
        protected void onPostExecute(String result) {

            //ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            //progressBar.setVisibility(View.GONE);

        }
    }

    private class favPub1 extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

             urlPost3 = url[0];
           // HttpURLConnectionExample e = new HttpURLConnectionExample();
            try {
               // e.sendPost(urlPost,String.valueOf(user_id),p.getPub_id(),a,formattedDate);
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost3);
            } catch (Exception e1) {
                e1.printStackTrace();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
