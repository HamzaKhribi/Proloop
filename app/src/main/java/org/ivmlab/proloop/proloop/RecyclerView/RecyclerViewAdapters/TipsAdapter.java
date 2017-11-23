package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Comments;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePosts;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Tip;
import org.ivmlab.proloop.proloop.Splashscreen;
import org.ivmlab.proloop.proloop.Utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder>{

    private ArrayList<Tip> tipsArray;
    Context context;
    Dialog dialog;
    ArrayList<Comments> comments=new ArrayList<>();

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
        }
    };
    private PostCommentsAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Adapter's Constructor
    public TipsAdapter(Context context, ArrayList<Tip> tips) {
        this.tipsArray = tips;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public TipsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_layout, parent, false);

        final int position = parent.indexOfChild(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }});
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Set strings to the views
        final TextView name = (TextView) holder.view.findViewById(R.id.profile_name);
        name.setText(tipsArray.get(position).getName());
        final TextView txt = (TextView) holder.view.findViewById(R.id.post_txt);
        txt.setText(tipsArray.get(position).getTip());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tipsArray.size();
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
