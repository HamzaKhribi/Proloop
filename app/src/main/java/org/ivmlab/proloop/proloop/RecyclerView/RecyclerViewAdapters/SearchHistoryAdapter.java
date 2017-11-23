package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Search;

public class SearchHistoryAdapter extends ArrayAdapter<Search> {

    // declaring our ArrayList of items
    private ArrayList<Search> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public SearchHistoryAdapter(Context context, int textViewResourceId, ArrayList<Search> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_search_history, null);
        }

        Search i = objects.get(position);

        if (i != null) {

            TextView barcodeSearch = (TextView) v.findViewById(R.id.barcodeSearch);
            TextView styleNumSearch = (TextView) v.findViewById(R.id.styleNumSearch);

            barcodeSearch.setText(i.getBarCode());
            styleNumSearch.setText(i.getStyleNum());
        }

        return v;

    }

}