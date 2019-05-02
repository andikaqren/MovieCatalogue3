package com.example.steve.moviecatalogue3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.steve.moviecatalogue3.R;
import com.example.steve.moviecatalogue3.db.FavHelper;
import com.example.steve.moviecatalogue3.fragment.Favourite_Film;
import com.example.steve.moviecatalogue3.fragment.Fragment_movies;
import com.example.steve.moviecatalogue3.fragment.Fragment_tv_series;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private final String titleMovies = "Movies";
    private final String titleTvSeries = "Tv Series";
    private final String titleFav = " My Favourite";
    Fragment fragment = null;
    private FavHelper favHelper;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_Movies:
                    item.setChecked(true);
                    fragment = new Fragment_movies();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Content_main, fragment)
                            .commit();
                    getSupportActionBar().setTitle(titleMovies);
                    break;
                case R.id.navigation_TV_Series:
                    item.setChecked(true);
                    fragment = new Fragment_tv_series();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Content_main, fragment)
                            .commit();
                    getSupportActionBar().setTitle(titleTvSeries);
                    break;
                case R.id.navigation_fav:
                    item.setChecked(true);
                    fragment = new Favourite_Film();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Content_main, fragment)
                            .commit();
                    getSupportActionBar().setTitle(titleFav);
                    break;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("List Movies");
        if (savedInstanceState == null) {
            Fragment currentFragment = new Fragment_movies();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.Content_main, currentFragment)
                    .commit();
        }
        favHelper = FavHelper.getInstance(getApplicationContext());
        favHelper.open();
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.optionmenu:
                Intent setting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(setting);
                break;
            case R.id.navigation_fav_subcription:
                Intent subscription = new Intent(this, Subscription_Menu.class);
                startActivity(subscription);
                break;
        }
        return super.onOptionsItemSelected(menuItem);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        favHelper.close();
    }

}
