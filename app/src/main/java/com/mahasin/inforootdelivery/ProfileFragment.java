package com.mahasin.inforootdelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    TextView tv_id,tv_name,tv_email,tv_workPhone,tv_personalPhone,tv_gender,tv_token;
    CircleImageView profileImageView;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_id = view.findViewById(R.id.tv_id);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_workPhone = view.findViewById(R.id.tv_workPhone);
        tv_personalPhone = view.findViewById(R.id.tv_personalPhone);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_token = view.findViewById(R.id.tv_token);
        profileImageView=view.findViewById(R.id.profileImageView);




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


        tv_id.setText(userId);
        tv_name.setText(userName);
        tv_email.setText(userEmail);
        tv_workPhone.setText(workPhone);
        tv_personalPhone.setText(personalPhone);
        tv_gender.setText(gender);
        tv_token.setText(token);

        Picasso.get()
                .load(""+profilePhoto)
                .placeholder(R.drawable.img)
                .into(profileImageView);







        return view;
    }
}
