package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.Fragments.FragmentContainer;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.CollectionCard;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Comments;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePosts;
import org.ivmlab.proloop.proloop.Splashscreen;

import java.util.ArrayList;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>{

    private ArrayList<CollectionCard> posts;
    Context context;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"Popup to open",Toast.LENGTH_SHORT).show();
        }
    };
    private Dialog dialog;

    // Adapter's Constructor
    public CollectionAdapter(Context context, ArrayList<CollectionCard> posts) {
        this.posts = posts;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_card, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionCard p = posts.get(parent.indexOfChild(v));


                dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_collection_card);

                TextView sneakerName = (TextView) dialog.findViewById(R.id.sneakerName);
                TextView buyDate = (TextView) dialog.findViewById(R.id.buyDate);

                sneakerName.setText(p.getSneaker_model());
                buyDate.setText(p.getDateOfBuy());

                dialog.show();
            }});

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Set strings to the views
        final ImageView sneaker_img = (ImageView) holder.view.findViewById(R.id.sneaker_img);
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
