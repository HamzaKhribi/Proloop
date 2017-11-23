package org.ivmlab.proloop.proloop.Tabs.TabsViews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.BarcodeActivity;
import org.ivmlab.proloop.proloop.Fragments.FragmentAllUsers;
import org.ivmlab.proloop.proloop.Fragments.FragmentTestMySneaker;
import org.ivmlab.proloop.proloop.MainActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.SearchHistoryAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Search;
import org.ivmlab.proloop.proloop.UploadImageActivity;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.Utils.MySQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TabBarcodeScan extends Fragment {

    View view;
    ImageView testBtn;
    private SharedPreferences sharedPreferences;
    private String urlPost;
    private TextView addSneakerTextView;
    private EditText modelT;
    private EditText styleNumT;
    private EditText styleNumT1;
    private Dialog dialog;
    private Dialog dialog1;
    private Dialog dialog2;
    private ListView searchHistoryLV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_barcode, container, false);
        ImageView testBtn=(ImageView)view.findViewById(R.id.testBtn);
        testBtn.setVisibility(view.INVISIBLE);
        LinearLayout linearLayoutTest=(LinearLayout)view.findViewById(R.id.linearLayoutTest);
        linearLayoutTest.setVisibility(view.INVISIBLE);
        searchHistoryLV = (ListView) view.findViewById(R.id.searchHistoryLV);
        searchHistoryLV.setVisibility(View.VISIBLE);

        /////////////////////////////////////////////////////////////

        SearchHistoryAdapter adapter;
        ArrayList<Search> listItems=new ArrayList<Search>();
        //listItems.add(0,"test0");
        //listItems.add(1,"test1");
        //listItems.add(2,"test2");

        final MySQLiteHelper db = new MySQLiteHelper(getContext());
        List<Search> list = db.getLastFiveSearches();
        db.getLastFiveSearches();

        for (int i=0; i<list.size(); i++){
            //listItems.add("Barcode: "+list.get(i).getBarCode()+", Style Number: "+list.get(i).getStyleNum());
            listItems.add(list.get(i));
        }

        adapter=new SearchHistoryAdapter(getContext(),
                R.layout.item_search_history,
                listItems);
        searchHistoryLV.setAdapter(adapter);

        //////////////////////////////////////////////////////////////

        ImageView scanBtn = (ImageView) view.findViewById(R.id.test);
        ImageView qrBtn = (ImageView) view.findViewById(R.id.testQR);
        ImageView nfcBtn = (ImageView) view.findViewById(R.id.testNFC);
        ImageView wmBtn = (ImageView) view.findViewById(R.id.testWatermark);

        nfcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogUnderDev = new Dialog(getActivity());

                dialogUnderDev.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogUnderDev.setContentView(R.layout.popup_nfc_under_dev);

                dialogUnderDev.show();

            }
        });

        wmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogUnderDev = new Dialog(getActivity());

                dialogUnderDev.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogUnderDev.setContentView(R.layout.popup_nfc_under_dev);

                dialogUnderDev.show();

            }
        });

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogUnderDev = new Dialog(getActivity());

                dialogUnderDev.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogUnderDev.setContentView(R.layout.popup_nfc_under_dev);

                dialogUnderDev.show();

            }
        });


        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BarcodeActivity.class);
                getActivity().startActivity(i);


            }
        });


        if(getActivity().getIntent().hasExtra("addStyle")) {
            String addStyle = getActivity().getIntent().getExtras().getString("addStyle");

            if (addStyle.equals("1")) {
                Log.e("heloooo", "it's me");
                testBtn.setVisibility(view.VISIBLE);
                linearLayoutTest.setVisibility(view.VISIBLE);
                scanBtn.setVisibility(view.GONE);
                qrBtn.setVisibility(view.GONE);
                nfcBtn.setVisibility(view.GONE);
                wmBtn.setVisibility(view.GONE);
                searchHistoryLV.setVisibility(view.GONE);
                Snackbar snackbar = Snackbar
                        .make(view, "Please add the style Number", 5000);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                textView.setTextColor(Color.WHITE);

                snackbar.show();
                testBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        EditText styleNumT = (EditText) view.findViewById(R.id.style_num);
                        EditText styleNumT1 = (EditText) view.findViewById(R.id.style_num1);

                        String styleNum = styleNumT.getText().toString();
                        String styleNum1 = styleNumT1.getText().toString();
                        if(styleNum.length()<6 && styleNum1.length()<3){

                            Snackbar snackbar = Snackbar
                                    .make(view, "Please insert a style number", Snackbar.LENGTH_LONG);


                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                            textView.setTextColor(Color.WHITE);

                            snackbar.show();
                        }else{
                            sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);

                            //final String barcode = sharedPreferences.getString("BarCode", "0");
                            urlPost = ("http://api.ivmlab.org/getSneakerResult.php?styleNum=" + styleNum + "-" + styleNum1);
                            //urlPost1 = ("http://api.ivmlab.org/insertBarcode.php?barcode=" +barcode+"&styleNum=" + styleNum+"-"+styleNum1 );
                            //new AsyncTaskAddBarcode().execute(urlPost1);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("URL", urlPost);
                            editor.putString("styleNum", styleNum);
                            editor.putString("styleNum1", styleNum1);
                            editor.putString("testClicked", "Clicked");
                            editor.putString("tabSelector", "tabSelector");
                            editor.putString("testBtnClicked", "1");

                            String barcode = sharedPreferences.getString("BarCode","No result");
                            db.addSearch(new Search(barcode,styleNum+"-"+styleNum1));

                            editor.commit();

                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                        }


                    }
                });
                getActivity().getIntent().removeExtra("addStyle");


            } else if (addStyle.equals("0")) {
                String styleNumber = getActivity().getIntent().getExtras().getString("styleNum");
                urlPost = ("http://api.ivmlab.org/getSneakerResult.php?styleNum=" + styleNumber);
                String[] separated = styleNumber.split("-");
                String styleNum = separated[0];
                String styleNum1 = separated[1];
                sharedPreferences = getActivity().getSharedPreferences("VALUES", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("URL", urlPost);
                editor.putString("styleNum", styleNum);
                editor.putString("styleNum1", styleNum1);
                editor.putString("tabSelector", "tabSelector");

                String barcode = sharedPreferences.getString("BarCode","No result");
                db.addSearch(new Search(barcode,styleNum+"-"+styleNum1));

                editor.commit();
                Log.e("tesssssstTabBarcodScan",sharedPreferences.getString("styleNum1","notset"));
                FragmentTestMySneaker fragmentTestMySneaker = new FragmentTestMySneaker();
                //fragmentTestMySneaker.setTabSelector(1);

                /*

                MainActivity.fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                MainActivity.fragmentTransaction.replace(R.id.fragment, fragmentTestMySneaker);
                MainActivity.fragmentTransaction.commit();
                getActivity().getIntent().removeExtra("addStyle");


                */
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);


            }
        }

        return view;
    }




    public class addSuggestionAsync extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {

        }

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

        @Override
        protected void onPostExecute(String result) {
        }
    }


}
