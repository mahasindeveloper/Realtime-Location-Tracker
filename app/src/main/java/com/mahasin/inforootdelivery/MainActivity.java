package com.mahasin.inforootdelivery;

import static com.mahasin.inforootdelivery.R.id.profile_image;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    CircleImageView profile_image;
    TextView tv_name;
    TextView onlineChack;
    View heder_view;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout=findViewById(R.id.drawerLayout);
        toolbar=findViewById(R.id.toolBar);
        frameLayout=findViewById(R.id.frameLayout);
        navigationView=findViewById(R.id.navigationView);
        heder_view=navigationView.getHeaderView(0);

        profile_image=heder_view.findViewById(R.id.profile_image);
        tv_name=heder_view.findViewById(R.id.tv_name);
        onlineChack=heder_view.findViewById(R.id.onlineChack);

        if (isDeviceOnline(this)) {
            // Device is online, you can perform network-related operations
            // For example, change the text of your "onlineChack" TextView
            onlineChack=heder_view.findViewById(R.id.onlineChack);
            onlineChack.setText("Online..");
        } else {
            // Device is offline
            // Update the UI or take appropriate actions for offline state
            onlineChack=heder_view.findViewById(R.id.onlineChack);
            onlineChack.setText("Offline..");
        }





        sharedPreferences =getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "Default userId");
        String userEmail = sharedPreferences.getString("userEmail", "Default userEmail");
        String userName = sharedPreferences.getString("userName", "Default userName");
        String workPhone = sharedPreferences.getString("workPhone", "Default workPhone");
        String personalPhone = sharedPreferences.getString("personalPhone", "Default personalPhone");
        String gender = sharedPreferences.getString("gender", "Default gender");
        String token = sharedPreferences.getString("token", "Default token");
        String profilePhoto = sharedPreferences.getString("profilePhoto", "Default profilePhoto");


        tv_name.setText(userName);


        Picasso.get()
                .load(""+profilePhoto)
                .placeholder(R.drawable.img)
                .into(profile_image);








        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(

              MainActivity.this,drawerLayout,toolbar ,R.string.open,R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        askNotificationPermission();




        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout,new HomeFragment());
        fragmentTransaction.commit();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.home){
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout,new HomeFragment());
                    fragmentTransaction.commit();

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (item.getItemId()==R.id.natification) {
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout,new NotivicationFragment());
                    fragmentTransaction.commit();

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (item.getItemId()==R.id.histri) {
                    Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout,new HistoryFragment());
                    fragmentTransaction.commit();
                }else if (item.getItemId()==R.id.profile) {
                    Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout,new ProfileFragment());
                    fragmentTransaction.commit();

                }else if (item.getItemId()==R.id.about) {
                    Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);

                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout,new AboutFragment());
                    fragmentTransaction.commit();

                }else if (item.getItemId() == R.id.logout) {
                    // Clear the user login status in SharedPreferences (for example)
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isFirstLogin", true);
                    editor.apply();

                    String userId = sharedPreferences.getString("userId", "Default userId");
                    String token = sharedPreferences.getString("token", "Default token");




                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);

                    String url = "https://ahmadi.fintaxzone.com/api/driver-logout";
                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("driver_id", userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest logoutRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Handle the logout response here
                                    Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                    // Redirect the user back to the login screen
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle logout errors
                                    Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            // Basic Authentication
                            //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                            headers.put("Authorization", "Bearer " + token);
                            return headers;
                        }
                    };
// Add the logout request to the Volley queue
                    RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(logoutRequest);



                }
                return true;
            }
        });




    }
    //===========================================================================================

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });



    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Notification")
                        .setMessage("App ta sundor bhaba chalanor jono Parmminson na dita hoba...")
                        .setPositiveButton("OKY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);

                            }
                        })
                        .setNegativeButton("No Thank", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();


            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }


    //=====================================================================

    // Check if the device is online
    public boolean isDeviceOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Device is online
            return true;
        } else {
            // Device is offline
            return false;
        }


    }

}