package com.mahasin.inforootdelivery;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText numberEditText, emailEditText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numberEditText = findViewById(R.id.numberEditText);
        emailEditText = findViewById(R.id.emailEditText);
        Button loginButton = findViewById(R.id.loginButton);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true);

        if (!isFirstLogin) {
            // User has already logged in, start MainActivity directly
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = numberEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Store data in SharedPreferences
                editor.putString("number", number);
                editor.putString("email", email);
                editor.putBoolean("isFirstLogin", false);
                editor.apply();

                // Call a function to send login request and get Bearer Token
                if (!email.isEmpty() && !number.isEmpty()) {
                    // Both email and number are not empty, call the login function
                    performLogin(email, number);
                   // startActivity(new Intent(Login.this,MainActivity.class));
                } else {
                    Toast.makeText(Login.this, "Please enter email and number", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    //========================================================================

    private void performLogin(String email, String number) {
        String url = "https://ahmadi.fintaxzone.com/api/driver-login"; // Update with your login endpoint
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", number);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest to send the login request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Toast.makeText(Login.this, ""+message, Toast.LENGTH_SHORT).show();

                    if (code == 1) {
                        JSONObject data = response.getJSONObject("data");

                        // Extract user data
                        String userId = data.getString("id");
                        String userEmail = data.getString("email");
                        String userName = data.getString("name");
                        String profilePhoto = data.getString("profile_photo");
                        String workPhone = data.getString("work_phone");
                        String personalPhone = data.getString("personal_phone");
                        String gender = data.getString("gender");
                        String authToken = data.getString("token");

                        /*
                        // Now, you can pass this data to the profile activity using an Intent
                        Intent profileIntent = new Intent(Login.this, ProfileFragment.class);
                        profileIntent.putExtra("userId", userId);
                        profileIntent.putExtra("userEmail", userEmail);
                        profileIntent.putExtra("userName", userName);
                        profileIntent.putExtra("profilePhoto", profilePhoto);
                        profileIntent.putExtra("workPhone", workPhone);
                        profileIntent.putExtra("personalPhone", personalPhone);
                        profileIntent.putExtra("gender", gender);
                        profileIntent.putExtra("authToken", authToken);
                        startActivity(profileIntent);
                         */



                        // Store the received Bearer Token in SharedPreferences
                        editor.putString("userId", userId);
                        editor.putString("userEmail", userEmail);
                        editor.putString("userName", userName);
                        editor.putString("profilePhoto", profilePhoto);
                        editor.putString("workPhone", workPhone);
                        editor.putString("personalPhone", personalPhone);
                        editor.putString("gender", gender);
                        editor.putString("token", authToken);
                        editor.apply();

                        // Start the MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Handle the case where the login response has a code other than 1 (e.g., an error)
                        // You might want to display an error message to the user.
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle the error or invalid response
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // You may want to display an error message to the user.
            }
        });

        // Add the login request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(loginRequest);
    }


}
