package org.ivmlab.proloop.proloop.bluesourceteam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.R;

/**
 * Created by Masterkey on 21.06.2015.
 */
public class DetailActivity2 extends AppCompatActivity {
    public static final String PARAM_MEMBER = "MEMBER";

    private View container;
    private View containerLogo;
    private ImageView imageView;
    private TextView txtName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Details");
        }

        TeamMember member = (TeamMember) getIntent().getSerializableExtra(PARAM_MEMBER);

        imageView = (ImageView) findViewById(R.id.activity_detail_image);
        imageView.setImageResource(member.getImageId());
        txtName = (TextView) findViewById(R.id.activity_detail_name);
        txtName.setText(member.getName());
        ((TextView) findViewById(R.id.activity_detail_description)).setText(member.getDescription());

        container = findViewById(R.id.activity_detail_container);
        containerLogo = findViewById(R.id.activity_detail_container_logo);

        if(getResources().getBoolean(R.bool.animations_enabled)) {
            final ScrollView scrollView = (ScrollView) findViewById(R.id.activity_detail_scrollview2);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    float scrollY = scrollView.getScrollY();

                    float max = DisplayUtil.getPixel(DetailActivity2.this, 130);
                    float percentage;
                    if (scrollY > max) {
                        percentage = 1;
                    } else if (scrollY < 0) {
                        percentage = 0;
                    } else {
                        percentage = 1.f / max * scrollY;
                    }

                    updateContainer(percentage);
                    updateImage(percentage);
                    updateText(percentage);
                    updateBg(percentage);
                }
            });
        }

        // init scrollAnimation
        imageView.post(new Runnable() {
            @Override
            public void run() {
                updateImage(0f);
                updateText(0f);
            }
        });
    }

    private boolean bgShown = false;
    private void updateBg(float percentage) {
        if(percentage == 1f && !bgShown) {
            showContiBg();
            bgShown = true;
        } else if(percentage < 1f && bgShown) {
            hideContiBg();
            bgShown = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showContiBg() {
        View contiBg = findViewById(R.id.activity_detail_container_bg);

        // get the center for the clipping circle
        int cx = (containerLogo.getLeft() + containerLogo.getRight()) / 2;
        int cy = (containerLogo.getTop() + containerLogo.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(contiBg.getWidth(), contiBg.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(contiBg, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        contiBg.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideContiBg() {
        // previously visible view
        final View contiBg = findViewById(R.id.activity_detail_container_bg);

        // get the center for the clipping circle
        int cx = (containerLogo.getLeft() + containerLogo.getRight()) / 2;
        int cy = (containerLogo.getTop() + containerLogo.getBottom()) / 2;

        // get the initial radius for the clipping circle
        int initialRadius = contiBg.getWidth();

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(contiBg, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contiBg.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

    private void updateContainer(float percentage) {
        float max = DisplayUtil.getPixel(this, 200);
        float min = DisplayUtil.getPixel(this, 80);

        container.getLayoutParams().height = (int) calcStatus(max, min, Math.abs(percentage - 1));
        container.requestLayout();
    }

    private void updateImage(float percentage) {
        // adjust X
        float maxX = DisplayUtil.getDisplayWidth(this) / 2 - imageView.getWidth() / 2;
        float minX = DisplayUtil.getPixel(this, 10);

        imageView.setX((int) calcStatus(maxX, minX, Math.abs(percentage - 1)));

        // adjust size
        float maxSize = DisplayUtil.getPixel(this, 160);
        float minSize = DisplayUtil.getPixel(this, 70);

        imageView.getLayoutParams().height = imageView.getLayoutParams().width = (int) calcStatus(maxSize, minSize, Math.abs(percentage - 1));

        imageView.requestLayout();
    }

    private void updateText(float percentage) {
        // adjust x
        float maxX = DisplayUtil.getDisplayWidth(this) / 2 - txtName.getWidth() / 2;
        float minX = DisplayUtil.getPixel(this, 90);

        txtName.setX((int) calcStatus(maxX, minX, Math.abs(percentage - 1)));

        // adjust y
        float maxY = DisplayUtil.getPixel(this, 170);
        float minY = DisplayUtil.getPixel(this, 30);

        txtName.setY((int) calcStatus(maxY, minY, Math.abs(percentage - 1)));

        txtName.requestLayout();
    }

    private float calcStatus(float max, float min, float percentage) {
        return min + (max - min) * percentage;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getResources().getBoolean(R.bool.animations_enabled)) {
            finishAfterTransition();
        } else {
            finish();
        }
        return true;
    }
}
