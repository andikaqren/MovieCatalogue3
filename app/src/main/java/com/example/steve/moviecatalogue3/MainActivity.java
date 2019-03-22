package com.example.steve.moviecatalogue3;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private final String titleMovies ="Movies";
    private final String titleTvSeries="Tv Series";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_Movies:
                    item.setChecked(true);
                    fragment = new Fragment_movies();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Content_main,fragment)
                            .commit();
                    getSupportActionBar().setTitle(titleMovies);
                    break;
                case R.id.navigation_TV_Series:
                    item.setChecked(true);
                    fragment = new Fragment_tv_series();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Content_main,fragment)
                            .commit();
                    getSupportActionBar().setTitle(titleTvSeries);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("List Movies");
        if (savedInstanceState == null){
            Fragment currentFragment = new Fragment_movies();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.Content_main,currentFragment)
                    .commit();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu ){
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem menuItem ){
        switch (menuItem.getItemId()){
            case R.id.optionmenu:
                Intent setting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(setting);
        }
        return super.onOptionsItemSelected(menuItem);

    }

}
