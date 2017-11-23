package org.ivmlab.proloop.proloop.Tabs.TabsViews;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.TipsAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Sneaker;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Tip;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import org.ivmlab.proloop.proloop.UploadImageActivity;
import org.ivmlab.proloop.proloop.Utils.SingleTouchImageView;
import org.ivmlab.proloop.proloop.Splashscreen;
import org.ivmlab.proloop.proloop.Utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.ivmlab.proloop.proloop.R.id.pager;


public class TabResult extends Fragment {

    String urlPost;
    JSONObject jsonObjectDesignPosts;
    ArrayList<Sneaker> sneaker = new ArrayList<>();
    ArrayList<Tip> tipsArrayList = new ArrayList<>();
    View view;
    SharedPreferences sharedPreferences;
    private JSONObject jsonSneaker;
    private JSONObject jsonSneakerImg1;
    private JSONObject jsonSneakerImg2;
    private JSONObject jsonSneakerImg3;
    private JSONObject jsonSneakerImg4;
    private JSONObject jsonSneakerImg5;
    private JSONObject jsonSneakerImg6;



    ImageView pic1, pic2, pic3, pic4, pic5, pic6;
    private Dialog dialog;
    private RecyclerView recyclerView;
    private String urlPost1;
    public String testClicked;
    public  boolean resultFromJson=false;
    private TipsAdapter recyclerViewAdapter;
    private RatingBar ratingBar;
    private TextView nbrVotes, styleNum, styleNumText, model;
    private ProgressDialog progress;
    boolean gotRating = false;
    private Dialog dialog1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.tabresult_layout, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerTips);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Get shared preferences
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);

        urlPost = sharedPreferences.getString("URL", "bla");
        testClicked = sharedPreferences.getString("testClicked", "bla2");

        nbrVotes = (TextView) view.findViewById(R.id.nbrVotes);
        styleNum = (TextView) view.findViewById(R.id.styleNum);
        styleNumText = (TextView) view.findViewById(R.id.styleNumText);
        model = (TextView) view.findViewById(R.id.model);
        styleNumText.setVisibility(View.INVISIBLE);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setVisibility(View.INVISIBLE);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        final String styleNum = sharedPreferences.getString("styleNum", "styleNum");
        final String styleNum1 = sharedPreferences.getString("styleNum1", "styleNum1");
        Log.e("tesssssstTabResult",styleNum1);

        if (urlPost != "bla" && styleNum != "styleNum"  ) {

            new AsyncTaskGetImages().execute(urlPost);
            /*Snackbar snackbar = Snackbar
                    .make(view, "Your sneaker is probably Authentic please see Tips and pictures below ", Snackbar.LENGTH_LONG);


            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_green_500));
            textView.setTextColor(Color.WHITE);

            snackbar.show();*/


            urlPost1 = "http://api.ivmlab.org/getTipsBySneakerID.php?styleNum=" + styleNum+"-"+styleNum1;
            new AsyncTaskGetTips().execute(urlPost1);



        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if(gotRating==true) {
                    try {
                        String rate = String.valueOf(ratingBar.getRating());
                        String urlPost = "http://api.ivmlab.org/updateRating.php?rate="+ rate+"&styleNum="+styleNum+"-"+styleNum1;
                        Log.e("urlpost",urlPost);
                        new AsyncTaskUpdateRating().execute(urlPost);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Cannot rate", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        pic1 = (ImageView) view.findViewById(R.id.pic1);
        pic2 = (ImageView) view.findViewById(R.id.pic2);
        pic3 = (ImageView) view.findViewById(R.id.pic3);
        pic4 = (ImageView) view.findViewById(R.id.pic4);
        pic5 = (ImageView) view.findViewById(R.id.pic5);
        pic6 = (ImageView) view.findViewById(R.id.pic6);
        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try{
                    editor.putString("imgPath", sneaker.get(0).getImgPath());
                    editor.commit();
                    Intent i = new Intent(getActivity(), SingleTouchImageView.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Result",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try{
                    editor.putString("imgPath", sneaker.get(0).getImgFace1Path());
                    editor.commit();
                    Intent i = new Intent(getActivity(), SingleTouchImageView.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Result",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try{
                    editor.putString("imgPath", sneaker.get(0).getImgFace2Path());
                    editor.commit();
                    Intent i = new Intent(getActivity(), SingleTouchImageView.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Result",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try{
                    editor.putString("imgPath", sneaker.get(0).getImgFace3Path());
                    editor.commit();
                    Intent i = new Intent(getActivity(), SingleTouchImageView.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Result",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try{
                    editor.putString("imgPath", sneaker.get(0).getImgFace4Path());
                    editor.commit();
                    Intent i = new Intent(getActivity(), SingleTouchImageView.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Result",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try{
                    editor.putString("imgPath", sneaker.get(0).getImgFace5Path());
                    editor.commit();
                    Intent i = new Intent(getActivity(), SingleTouchImageView.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Result",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public class AsyncTaskGetTips extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            tipsArrayList = new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            Log.v("url11", urlPost);
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectPosts.getInt("count");
                for (int i = 1; i <= count; i++) {
                    JSONObject tip = jsonObjectPosts.getJSONObject("" + i);
                    String name = tip.getString("name");
                    String tipTxt = tip.getString("tip");

                    tipsArrayList.add(new Tip(name, tipTxt));
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
            ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBarTips);
            pb.setVisibility(View.GONE);

            // Create the recyclerViewAdapter
            recyclerViewAdapter = new TipsAdapter(getActivity(), tipsArrayList);
            recyclerView.setAdapter(recyclerViewAdapter);

        }
    }

    public class AsyncTaskGetImages extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            try {
                jsonObjectDesignPosts = JsonParser.readJsonFromUrl(urlPost);
                jsonSneaker = jsonObjectDesignPosts.getJSONObject("1");
                String model_name = jsonObjectDesignPosts.getJSONObject("sneaker_information").getJSONObject("1").getString("name");
                String imgPath = "http://www.snkimg.ivmlab.org/"+jsonSneaker.getString("image_file_name");
                jsonSneakerImg1 = jsonObjectDesignPosts.getJSONObject("2");
                String imgFace1Path =  "http://www.snkimg.ivmlab.org/"+jsonSneakerImg1.getString("image_file_name");
                jsonSneakerImg2 = jsonObjectDesignPosts.getJSONObject("3");
                String imgFace2Path =  "http://www.snkimg.ivmlab.org/"+jsonSneakerImg2.getString("image_file_name");
                jsonSneakerImg3 = jsonObjectDesignPosts.getJSONObject("4");
                String imgFace3Path =  "http://www.snkimg.ivmlab.org/"+jsonSneakerImg3.getString("image_file_name");
                jsonSneakerImg4 = jsonObjectDesignPosts.getJSONObject("5");
                String imgFace4Path =  "http://www.snkimg.ivmlab.org/"+jsonSneakerImg4.getString("image_file_name");
                jsonSneakerImg5 = jsonObjectDesignPosts.getJSONObject("6");
                String imgFace5Path =  "http://www.snkimg.ivmlab.org/"+jsonSneakerImg5.getString("image_file_name");
                float rating=Float.parseFloat(jsonObjectDesignPosts.getJSONObject("sneaker_information").getJSONObject("1").getString("rating"));
                int nbrVotes=Integer.parseInt(jsonObjectDesignPosts.getJSONObject("sneaker_information").getJSONObject("1").getString("nbrVotes"));





              /*  float rating = Float.parseFloat(jsonSneaker.getString("rating"));
                int nbrVotes = jsonSneaker.getInt("nbrVotes");*/
                //Do NOT FROGET RATING HAMZA


                sneaker.add(new Sneaker( imgPath, imgFace1Path, imgFace2Path, imgFace3Path, imgFace4Path, imgFace5Path,rating,nbrVotes, model_name));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            if (sneaker.size()>0){
                resultFromJson = true;
            }
            return null;
        }


        // Set facebook items to the textviews and imageviews
        @Override
        protected void onPostExecute(String result) {

            if (resultFromJson==true) {
                new DownloadImageTask((ImageView) view.findViewById(R.id.pic1))
                        .execute(sneaker.get(0).getImgPath());
                new DownloadImageTask((ImageView) view.findViewById(R.id.pic2))
                        .execute(sneaker.get(0).getImgFace1Path());
                new DownloadImageTask((ImageView) view.findViewById(R.id.pic3))
                        .execute(sneaker.get(0).getImgFace2Path());
                new DownloadImageTask((ImageView) view.findViewById(R.id.pic4))
                        .execute(sneaker.get(0).getImgFace3Path());
                new DownloadImageTask((ImageView) view.findViewById(R.id.pic5))
                        .execute(sneaker.get(0).getImgFace4Path());
                new DownloadImageTask((ImageView) view.findViewById(R.id.pic6))
                        .execute(sneaker.get(0).getImgFace5Path());


                ratingBar.setRating(sneaker.get(0).getRating());
                ratingBar.setVisibility(View.VISIBLE);
                gotRating=true;
                nbrVotes.setText("("+sneaker.get(0).getNbrVotes()+")");
              /*  styleNumText.setVisibility(View.VISIBLE);
                styleNum.setText(styleNum);
                styleNum.setVisibility(View.VISIBLE);*/
                model.setText(sneaker.get(0).getModel_name());
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(10000);

                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog1 = new Dialog(getActivity());

                                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog1.setContentView(R.layout.popup_result);

                                    dialog1.show();

                                    Button btnYes= (Button) dialog1.findViewById(R.id.yes);
                                    Button btnNo= (Button) dialog1.findViewById(R.id.no);

                                    btnYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                                            String barcode = sharedPreferences.getString("BarCode","No result");
                                            final String styleNum = sharedPreferences.getString("styleNum", "styleNum");
                                            final String styleNum1 = sharedPreferences.getString("styleNum1", "styleNum1");
                                            String urlPost2 = ("http://api.ivmlab.org/insertBarcode.php?barcode=" +barcode+"&styleNum=" + styleNum+"-"+styleNum1 );
                                            new AsyncTaskAddBarcode().execute(urlPost2);
                                            dialog1.dismiss();
                                            Snackbar snackbar = Snackbar
                                                    .make(view, "Your sneaker is probably Authentic please see Tips and pictures below ", Snackbar.LENGTH_LONG);


                                            View sbView = snackbar.getView();
                                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_green_500));
                                            textView.setTextColor(Color.WHITE);

                                            snackbar.show();


                                        }
                                    });

                                    btnNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog1.dismiss();
                                            Snackbar snackbar = Snackbar
                                                    .make(view, "Your sneaker is not authentic  ",7000);


                                            View sbView = snackbar.getView();
                                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                                            textView.setTextColor(Color.WHITE);

                                            snackbar.show();
                                            Thread thread = new Thread() {
                                                @Override
                                                public void run() {
                                                    try {

                                                        sleep(2000);

                                                        getActivity().runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                dialog = new Dialog(getActivity());

                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                dialog.setContentView(R.layout.add_sneaker_pop_up);

                                                                dialog.show();


                                                                ImageView addToDBbtn = (ImageView) dialog.findViewById(R.id.addToDBbtn);
                                                                addToDBbtn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
                                                                        int user_id = sharedPreferences.getInt("USERID", 0);
                                                                        Intent i = new Intent(getActivity(), UploadImageActivity.class);
                                                                        getActivity().startActivity(i);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            };
                                            thread.start();

                                        }
                                    });
                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
                String tabSelector = sharedPreferences.getString("tabSelector", "bla3");
                String testBtnClicked = sharedPreferences.getString("testBtnClicked", "0");

                if (!tabSelector.equals("bla3")  && testBtnClicked.equals("1")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("testBtnClicked", "0");
                    editor.commit();
                    thread.start();
                }
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tabSelector", "bla3");
                editor.commit();
            }
            else{
                Toast.makeText(getActivity(),"No Result",Toast.LENGTH_LONG).show();
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tabSelector", "bla3");
                editor.putString("testBtnClicked", "0");

                editor.commit();
            }
        }
    }

    public class AsyncTaskUpdateRating extends AsyncTask<String, String, String> {


        private JSONObject jsonObjectRating;

        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(getActivity());
            progress.setMessage("Rating");
            progress.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
            progress.show();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            try {
                jsonObjectRating = JsonParser.readJsonFromUrl(urlPost);
                JSONObject rateJson = jsonObjectRating.getJSONObject("updated");
                final String newRating= rateJson.getString("rating");
                final String newNbrVotes = rateJson.getString("nbrVotes");

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                ratingBar.setRating(Float.parseFloat(newRating));
                                nbrVotes.setText("("+newNbrVotes+")");
                            }
                        });
                    }
                };


                thread.start();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getContext().getApplicationContext(),"Thank you for rating",Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    public class AsyncTaskAddBarcode extends AsyncTask<String, String, String> {


        private JSONObject jsonObjectRating;

        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(getActivity());
            progress.setMessage("Adding to database");
            progress.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
            progress.show();
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

            Toast.makeText(getContext().getApplicationContext(),"Added to database",Toast.LENGTH_SHORT).show();
            progress.dismiss();
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
            Bitmap decoded =null;
            try {
                Log.e("insideDowloadImage",urldisplay);
                InputStream in = new java.net.URL(urldisplay).openStream();
                Log.e("INPUTSTREAM",in.toString());
                mIcon11 = BitmapFactory.decodeStream(in);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.JPEG,100,out);
                 decoded =BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                Log.e("DecodeStream",mIcon11.toString());

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

