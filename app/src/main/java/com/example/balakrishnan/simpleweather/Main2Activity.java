package com.example.balakrishnan.simpleweather;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.balakrishnan.simpleweather.Main2Activity.PlaceholderFragment.wAdapter;
import static com.example.balakrishnan.simpleweather.Main2Activity.PlaceholderFragment.wList;

public class Main2Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content), "SimpleWeather app made by S Balakrishnan", Snackbar.LENGTH_LONG);
            View sView=snackbar.getView();
            sView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            snackbar.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {


        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        public void checkPermissions(View v)
        {
            if (ActivityCompat.checkSelfPermission(v.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }

        }
        Double currentLongitude=0.0,currentLatitude=0.0;
        public void UpdateLatLong(View v)
        {

        }
        public void Tab1Function(View v,View v2)
        {
            checkPermissions(v);
            if (ActivityCompat.checkSelfPermission(v.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                GPSTracker gps = new GPSTracker(v.getContext());
                System.out.println(String.valueOf(gps.getLastLocation().getLatitude()));
                System.out.println(String.valueOf(gps.getLastLocation().getLongitude()));
                currentLongitude = gps.getLastLocation().getLongitude();
                currentLatitude = gps.getLastLocation().getLatitude();
            }

             if(currentLatitude!=0 && currentLongitude!=0) {
                /* final RelativeLayout rl = (RelativeLayout)v.findViewById(R.id.relLayout);
                // final RecyclerView rl1 = (RecyclerView) v2.findViewById(R.id.recycler_view);

                 final ImageView img =(ImageView)v.findViewById(R.id.backgroundImage);
                 Picasso.with(v.getContext())
                         .load("https://source.unsplash.com/collection/319663").fit().centerCrop().into(img, new Callback() {
                     @Override
                     public void onSuccess() {
                         rl.setBackgroundDrawable(img.getDrawable());
                  //      rl1.setBackgroundDrawable(img.getDrawable());
                     }

                     @Override
                     public void onError() {
                         Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                     }
                 });
*/
                 BackgroundJSONCall b = new BackgroundJSONCall(v, getActivity());
                 b.execute(currentLatitude, currentLongitude);
             }
            else
            {
                Toast.makeText(getContext(),"Couldnt get location",Toast.LENGTH_LONG).show();
            }
            Tab2Function(v2);
        }
        private RecyclerView recyclerView;
        public static WeatherAdapter wAdapter;
        public static List<WeatherInfo> wList = new ArrayList<>();

        public void Tab2Function(View v)
        {
            try {

                if (ActivityCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    UpdateLatLong(v);
                    System.out.println("tab2");
                    recyclerView = v.findViewById(R.id.recycler_view);
                    wAdapter = new WeatherAdapter(wList, v.getContext());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,false);
                    if(recyclerView==null)
                        System.out.println("recyclerview is null");
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(wAdapter);
                    BackgroundForecast bf = new BackgroundForecast(getActivity());
                    bf.execute(currentLatitude, currentLongitude);
                    System.out.println("wlist="+wList.toString());

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView=null;
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
            {rootView = inflater.inflate(R.layout.activity_main, container, false);
                View rootView1 = inflater.inflate(R.layout.activity_home, container, false);

                Tab1Function(rootView,rootView1);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                rootView = inflater.inflate(R.layout.activity_home, container, false);
                Tab2Function(rootView);
            }
            return rootView;
        }




        }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }





}
