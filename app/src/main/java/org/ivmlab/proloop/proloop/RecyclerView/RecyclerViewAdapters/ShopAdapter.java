
package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.R;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Shop;
import org.ivmlab.proloop.proloop.ShopsMaps;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>{

    private ArrayList<Shop> shops;

    Context context;
    int position;
    View v;



    // Adapter's Constructor
    public ShopAdapter(Context context, ArrayList<Shop> shops) {
        this.shops = shops;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Set strings to the views
        final TextView shopName = (TextView) holder.view.findViewById(R.id.textViewItemName);
        final TextView textViewBirthday = (TextView) holder.view.findViewById(R.id.textViewBio);

        shopName.setText(shops.get(holder.getAdapterPosition()).getName());
        textViewBirthday.setText(shops.get(holder.getAdapterPosition()).getAdress());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShopsMaps.class);
                intent.putExtra("LAT",shops.get(holder.getAdapterPosition()).getLat());
                intent.putExtra("LONGI",shops.get(holder.getAdapterPosition()).getLng());
                intent.putExtra("shopName",shops.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shops.size();
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
