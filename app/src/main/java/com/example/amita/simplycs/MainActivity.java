package com.example.amita.simplycs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amita.simplycs.Adapter.SessionManager;
import com.example.amita.simplycs.Fragment.AccountFragment;
import com.example.amita.simplycs.Fragment.AskusFragment;
import com.example.amita.simplycs.Fragment.DashboardFragment;
import com.example.amita.simplycs.Fragment.FullScreenDialog;
import com.example.amita.simplycs.Fragment.ProfileFragment;
import com.example.amita.simplycs.Fragment.SettingFragment;
import com.example.amita.simplycs.Fragment.TopicListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    NavigationView navigationView;
    public SessionManager session;

    View hView;
    ImageView User_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FullScreenDialog dialog = new FullScreenDialog();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, FullScreenDialog.TAG);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_dashboard);

        hView =  navigationView.getHeaderView(0);
        User_pic=(ImageView)hView.findViewById(R.id.banar1);
        User_pic.setImageDrawable(getResources().getDrawable(R.drawable.p1));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (checkNavigationMenuItem() != 0)
            {
                navigationView.setCheckedItem(R.id.nav_dashboard);
                fragment = new DashboardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
            else
                super.onBackPressed();
        }
    }

    private int checkNavigationMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -1;
    }


    public void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        FragmentTransaction transection = getSupportFragmentManager().beginTransaction();
        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_menu1:
                fragment= new AskusFragment();
                transection.replace(R.id.content_frame, fragment);
                transection.addToBackStack(null).commit();
                break;
            case R.id.action_menu2:

                Toast.makeText(getApplicationContext(),"Articles",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu3:

                Toast.makeText(getApplicationContext(),"Current Affairs",Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_menu4:

                Toast.makeText(getApplicationContext(),"Practice",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu5:

                Toast.makeText(getApplicationContext(),"Revision",Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {


        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                break;

            case R.id.nav_account:
                fragment = new AccountFragment();
                break;

            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;

            case R.id.nav_setting:
                fragment = new SettingFragment();
                break;

//            case R.id.nav_logout:
//                logoutUser();
//                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



}
