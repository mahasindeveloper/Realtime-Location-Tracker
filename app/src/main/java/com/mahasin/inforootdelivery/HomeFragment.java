package com.mahasin.inforootdelivery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    TextView tv_name, tv_number;
    SharedPreferences sharedPreferences;
    private Intent service;
    Switch aSwitch;
    CircleImageView man;


    //========================
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private String locationName;
    public String address;
    ///===========
    private Double latestLatitude;
    private Double latestLongitude;
//================================


    private final ActivityResultLauncher<String> backgroundLocation =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        // Permission granted, you can proceed here
                        requireContext().startService(service);
                    } else {
                        // Permission denied, handle it as needed
                    }
                }
            });

    private final ActivityResultLauncher<String[]> locationPermissions =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                if (permissions.get(android.Manifest.permission.ACCESS_FINE_LOCATION) && permissions.get(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        backgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                    } else {
                        requireContext().startService(service);
                    }
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        service = new Intent(getActivity(), LocationService.class);


        aSwitch = view.findViewById(R.id.aSwitch);
        man = view.findViewById(R.id.man);

        //======
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);


            locationName =""+address;
           // Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();




        //============================================

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkPermissions();
                } else {
                    requireContext().stopService(service);
                }
            }
        });


        tv_name = view.findViewById(R.id.tv_name);
        tv_number = view.findViewById(R.id.tv_number);

        Context context = getContext();
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        } else {
            // Handle the case where context is null
        }


        String userId = sharedPreferences.getString("userId", "Default userId");
        String userEmail = sharedPreferences.getString("userEmail", "Default userEmail");
        String userName = sharedPreferences.getString("userName", "Default userName");
        String workPhone = sharedPreferences.getString("workPhone", "Default workPhone");
        String personalPhone = sharedPreferences.getString("personalPhone", "Default personalPhone");
        String gender = sharedPreferences.getString("gender", "Default gender");
        String token = sharedPreferences.getString("token", "Default token");
        String profilePhoto = sharedPreferences.getString("profilePhoto", "Default profilePhoto");


        if (tv_name != null && tv_number != null) {
            // Update the TextViews with data
            tv_name.setText(userName);
            tv_number.setText(workPhone);


            Picasso.get()
                    .load("" + profilePhoto)
                    .placeholder(R.drawable.img)
                    .into(man);


        } else {
            // Handle the case where a TextView is null
        }


        return view;
    }

    //==================================================================
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationPermissions.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            } else {
                requireContext().startService(service); // Use requireContext() to start the service
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireContext().stopService(service);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void receiveLocationEvent(LocationEvent locationEvent) {
        String userId = sharedPreferences.getString("userId", "Default userId");
        String token = sharedPreferences.getString("token", "Default token");
        latestLatitude = Double.valueOf(locationEvent.getLatitude());
        latestLongitude = Double.valueOf(locationEvent.getLongitude());


        // Convert coordinates to an address
        address = getAddressFromLocation(latestLatitude, latestLongitude);

        if (address != null) {
            locationName = "" + address;
            // Share the address (you can customize this part)
            // shareAddress(address);
          //  Toast.makeText(getContext(), locationName, Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where address couldn't be obtained
        }

        // Convert the address to coordinates (latitude and longitude)
        LatLng latLng = getLatLngFromAddress(locationName);

        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom level may vary
        } else {
            // Handle the case where coordinates couldn't be obtained
        }


        String url = "https://ahmadi.fintaxzone.com/api/send-location";
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("driver_id", userId); // Replace with your driver ID
            parameters.put("lattitude", locationEvent.getLatitude()); // Replace with the latitude value
            parameters.put("longitude", locationEvent.getLongitude()); // Replace with the longitude value

        } catch (Exception e) {

        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                Log.i("onResponse", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
                if (error==null){
                    Toast.makeText(getContext(), "App is working", Toast.LENGTH_SHORT).show();
                }            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
        // Add the location update request to the Volley queue




        /*
       // tvLatitude.setText("Latitude -> " + locationEvent.getLatitude());
       // tvLongitude.setText("Longitude -> " + locationEvent.getLongitude());
        String userId = sharedPreferences.getString("userId", "Default userId");


        String url = "https://maha786.000webhostapp.com/apps/inforootinsert.php?la=" + locationEvent.getLatitude() + "&lo=" + locationEvent.getLongitude()+"&dr_id="+userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "DataBase Show", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


         */

        /*
      getAddressFromLocation(latestLatitude, latestLongitude);
        // Convert coordinates to an address
         address = getAddressFromLocation(latestLatitude, latestLongitude);

        if ( address != null ) {
            // Share the address (you can customize this part)
            //shareAddress(address);
            Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();



        } else {
            // Handle the case where address couldn't be obtained
        }

         */

    }

    //=================================================================================================

    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressText.append(address.getAddressLine(i)).append(" ");
                }
                return addressText.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //============================================================ add map

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Convert the address to coordinates (latitude and longitude)
        LatLng latLng = getLatLngFromAddress(locationName);

        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom level may vary
        } else {
            // Handle the case where coordinates couldn't be obtained
        }
    }


    //====
    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




}