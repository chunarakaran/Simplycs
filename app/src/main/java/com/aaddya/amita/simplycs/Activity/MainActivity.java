package com.aaddya.amita.simplycs.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Fragment.CourseFragment;
import com.aaddya.amita.simplycs.Fragment.SettingFragment;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Adapter.SessionManager;
import com.aaddya.amita.simplycs.Fragment.AskusFragment;
import com.aaddya.amita.simplycs.Fragment.DashboardFragment;
import com.aaddya.amita.simplycs.Fragment.ExamListFragment;
import com.aaddya.amita.simplycs.Fragment.FullScreenDialog;
import com.aaddya.amita.simplycs.Fragment.ProfileFragment;
import com.aaddya.amita.simplycs.Fragment.WebinarListFragment;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    NavigationView navigationView;
    public SessionManager session;

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;
    PromptDialog promptDialog;

    View hView;
    ImageView User_pic;
    TextView User_name;

    String User_id;
    public static final String PREFS_NAME = "login";

    private Activity mContext;

    // for App update
    private AppUpdateManager mAppUpdateManager;
    private int RC_APP_UPDATE = 999;
    private int inAppUpdateType;
    private com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Creates instance of the manager.
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();
        //lambda operation used for below listener
        //For flexible update
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        //For Immediate
        inAppUpdateType = AppUpdateType.IMMEDIATE; //1
        inAppUpdate();


        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        mContext = this;


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        promptDialog = new PromptDialog(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        if (sharedPref.getString("LAST_LAUNCH_DATE","nodate").contains(currentDate)){
            // Date matches. User has already Launched the app once today. So do nothing.
        }
        else
        {
            // Display dialog text here......
            // Do all other actions for first time launch in the day...

            FullScreenDialog dialog = new FullScreenDialog();
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenDialog.TAG);

            // Set the last Launched date to today.
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LAST_LAUNCH_DATE", currentDate);
            editor.commit();
        }



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_dashboard);

        if(isNetworkAvailable()){
            GetProfile();
        }
        else {
            promptDialog.setCancelable(false);
            promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WARNING);
            promptDialog.setAnimationEnable(true);
            promptDialog.setTitleText("Connection Failed");
            promptDialog.setContentText("Please Check Your Internet Connection");
            promptDialog.setPositiveListener("Setting", new PromptDialog.OnPositiveListener() {
                @Override
                public void onClick(PromptDialog dialog) {
                    Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS );
                    startActivity(intent);
                    finish();
                }
            }).show();

        }



        hView =  navigationView.getHeaderView(0);
        User_pic=(ImageView)hView.findViewById(R.id.banar1);
        User_name = (TextView)hView.findViewById(R.id.user_name);

//        User_pic.setImageDrawable(getResources().getDrawable(R.drawable.p1));

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

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

//                startActivity(new Intent(this,AskusActivity.class));

                break;
//            case R.id.action_menu2:
//
//                break;
//            case R.id.action_menu3:
//
//                Toast.makeText(getApplicationContext(),"Current Affairs",Toast.LENGTH_SHORT).show();
//                break;

            case R.id.action_menu4:

                fragment= new WebinarListFragment();
                transection.replace(R.id.content_frame, fragment);
                transection.addToBackStack(null).commit();

//                Toast.makeText(getApplicationContext(),"Webinars",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu5:

                fragment= new ExamListFragment();
                transection.replace(R.id.content_frame, fragment);
                transection.addToBackStack(null).commit();

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

            case R.id.nav_courses:
                fragment = new CourseFragment();
                break;


            case R.id.nav_profile:
                fragment = new ProfileFragment();
//                startActivity(new Intent(this,ProfileActivity.class));
                break;

//            case R.id.nav_setting:
//                fragment = new SettingFragment();
//                break;

            case R.id.nav_share:

                String shareBody = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

                break;

            case R.id.nav_logout:
//                Toast.makeText(getApplicationContext(),HTTP_JSON_URL,Toast.LENGTH_SHORT).show();
                Logout();
                break;

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

    public void Logout()
    {

        pDialog.setMessage("Please Wait...");
        showDialog();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/Logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                                logoutUser();

                            }
                            else if (success.equalsIgnoreCase("false")){
                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);

    }

    public void GetProfile()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");


                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                String name,Pic;
                                JSONObject user = jObj.getJSONObject("data");

                                Pic=user.getString("profile_pic");
                                name=user.getString("name");

                                Picasso.with(getApplicationContext()).load(Pic).into(User_pic);
                                User_name.setText(name);
                                hideDialog();


                            }
                            else if (success.equalsIgnoreCase("false")){
//                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                String errorcode = jObj.getString("error_code");

                                if (errorcode.equalsIgnoreCase("-1")){
//                                    Toast.makeText(getApplicationContext(), "Re-Login", Toast.LENGTH_LONG).show();

                                    promptDialog.setCancelable(false);
                                    promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_INFO);
                                    promptDialog.setAnimationEnable(true);
                                    promptDialog.setTitleText("Please Re-Login");
                                    promptDialog.setContentText("You Are Logged in another device Please Re-login");
                                    promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            logoutUser();
                                        }
                                    }).show();

                                }

                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

        return isConnected;
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        try {
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() ==
                        UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                inAppUpdateType,
                                this,
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });


            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                //For flexible update
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            //when user clicks update button
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "App download starts...", Toast.LENGTH_LONG).show();
            } else if (resultCode != RESULT_CANCELED) {
                //if you want to request the update again just call checkUpdate()
                Toast.makeText(MainActivity.this, "App download canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(MainActivity.this, "App download failed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void inAppUpdate() {

        try {
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            // For a flexible update, use AppUpdateType.FLEXIBLE
                            && appUpdateInfo.isUpdateTypeAllowed(inAppUpdateType)) {
                        // Request the update.

                        try {
                            mAppUpdateManager.startUpdateFlowForResult(
                                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                    appUpdateInfo,
                                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                    inAppUpdateType,
                                    // The current activity making the update request.
                                    MainActivity.this,
                                    // Include a request code to later monitor this update request.
                                    RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void popupSnackbarForCompleteUpdate() {
        try {
            Snackbar snackbar =
                    Snackbar.make(findViewById(R.id.content_frame), "An update has just been downloaded.\nRestart to update", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("INSTALL", view -> {
                if (mAppUpdateManager != null){
                    mAppUpdateManager.completeUpdate();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.card_pressed));
            snackbar.show();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

}
