package org.ivmlab.proloop.proloop.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.ivmlab.proloop.proloop.LoginActivity;
import org.ivmlab.proloop.proloop.MainActivity;
import org.ivmlab.proloop.proloop.ProfileActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.Tabs.TabsAdapters.TabsDesignViewPagerAdapter;
import org.ivmlab.proloop.proloop.Tabs.TabsUtils.SlidingTabLayout;
import org.ivmlab.proloop.proloop.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentContainer extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    ViewPager pager;
    TabsDesignViewPagerAdapter tabsDesignViewPagerAdapter;
    public SlidingTabLayout tabs;
    public static ArrayList<CharSequence> titles=new ArrayList<>();
    int tabNumber = titles.size();
    int tabsPaddingTop;
    private SharedPreferences sharedPreferences;

    private String backSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_container, container, false);
        //get the spinner from the xml.
       Spinner dropdown = (Spinner) view.findViewById(R.id.spinner);
//create a list of items for the spinner.
        String[] items = new String[]{"","View Your Profile", "Log Out"};


//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);

                    int user_id = sharedPreferences.getInt("USERID", 0);


                    intent.putExtra("user_id", Integer.toString(user_id));
                    getActivity().startActivity(intent);

                } else if (i==2) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.logout_dialog);
                    dialog.show();

                    Button btnYes = (Button) dialog.findViewById(R.id.yesbtn);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putInt("USERID",0);
                            editor.commit();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    Button btnNo = (Button) dialog.findViewById(R.id.nobtn);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (titles.size()==0){
            titles.add("TIMELINE");
            titles.add("MESSAGES");
        }

        setupTabs();

        return view;
    }


    public void setupTabs() {
        tabsDesignViewPagerAdapter = new TabsDesignViewPagerAdapter(getFragmentManager(), titles, 2);
        pager = (ViewPager) view.findViewById(R.id.pagerTL);
        pager.setAdapter(tabsDesignViewPagerAdapter);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabsTL);

        tabs.setPadding(convertToPx(0), tabsPaddingTop, convertToPx(0), 0);

        tabs.setViewPager(pager);
    }

    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        backSelected=sharedPreferences.getString("backSelectedH","bla3");
        Log.e("baclSelectedd",backSelected);


        if (!backSelected.equals("bla3")  ){

            pager.setCurrentItem(1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("backSelectedH","bla3");
            editor.commit();


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getItemAtPosition(1).equals("View Your Profile")) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            int user_id = sharedPreferences.getInt("USERID", 0);
            intent.putExtra("user_id", user_id);
            getActivity().startActivity(intent);

        } else if (adapterView.getItemAtPosition(2).equals("Log Out")) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.logout_dialog);
            dialog.show();

            Button btnYes = (Button) dialog.findViewById(R.id.yesbtn);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            Button btnNo = (Button) dialog.findViewById(R.id.nobtn);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

