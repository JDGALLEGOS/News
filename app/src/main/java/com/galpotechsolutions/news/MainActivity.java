package com.galpotechsolutions.news;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        /*ViewPager viewPager = findViewById(R.id.viewpager);

        simpleFragmentPagerAdapter adapter = new simpleFragmentPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_news);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the optionMenu specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewFragment()).commit();
                break;
            case R.id.nav_science:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScienceFragment()).commit();
                break;
            case R.id.nav_music:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MusicFragment()).commit();
                break;
            case R.id.nav_books:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookFragment()).commit();
                break;
            case R.id.nav_tech:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TechFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, getResources().getString(R.string.share_menu), Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
