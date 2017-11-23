package org.ivmlab.proloop.proloop.Utils;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.Utils.TouchImageView;
import org.ivmlab.proloop.proloop.Utils.TouchImageView.OnTouchImageViewListener;


public class SingleTouchImageView extends Activity {

    private TouchImageView image;
    private TextView scrollPositionTextView;
    private TextView zoomedRectTextView;
    private TextView currentZoomTextView;
    private DecimalFormat df;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_touch_image_view);
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        String imgPath = sharedPreferences.getString("imgPath", "bla");
        //
        // DecimalFormat rounds to 2 decimal places.
        //
        df = new DecimalFormat("#.##");

        //  image = (TouchImageView) findViewById(R.id.img);
        new DownloadImageTask((TouchImageView) findViewById(R.id.img))
                .execute(imgPath);

        //
        // Set the OnTouchImageViewListener which updates edit texts
        // with zoom and scroll diagnostics.
        //
     /*   image.setOnTouchImageViewListener(new OnTouchImageViewListener() {

            @Override
            public void onMove() {
               // PointF point = image.getScrollPosition();
                //RectF rect = image.getZoomedRect();
                //float currentZoom = image.getCurrentZoom();
                //boolean isZoomed = image.isZoomed();

            }
        });*/
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

