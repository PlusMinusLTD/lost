package com.PlusMinusLLC.lost.Dashoard;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.PlusMinusLLC.lost.AddAnnouncementActivity;
import com.PlusMinusLLC.lost.AddCrimeActivity;
import com.PlusMinusLLC.lost.AddMissingActivity;
import com.PlusMinusLLC.lost.Announcement.AnnouncementActivity;
import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.Meet.Constants;
import com.PlusMinusLLC.lost.Meet.MeetActivity;
import com.PlusMinusLLC.lost.Meet.PreferenceManager;
import com.PlusMinusLLC.lost.Profile.ManageProfile;
import com.PlusMinusLLC.lost.R;
import com.PlusMinusLLC.lost.Registration.RegisterActivity;
import com.PlusMinusLLC.lost.Wallet.WalletActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class DashboardFragment extends Fragment {

    private ImageView addButton, closeBtn, profilePicture, timeImage;
    private Dialog addPostDialog, weatherDialog;
    private ConstraintLayout Profile, wallet, announcement, meet, signOut;
    private Button missing, crime, announcementBtn;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentSnapshot documentSnapshot;
    private TextView name, email, welcome_text;
    private PreferenceManager preferenceManager;
    private TextView date, temp, description, city;
    Animation topAnim, bottomAnim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        addButton = view.findViewById(R.id.add_button);
        Profile = view.findViewById(R.id.profile);
        wallet = view.findViewById(R.id.wallet);
        announcement = view.findViewById(R.id.announcement);
        meet = view.findViewById(R.id.meet);
        welcome_text = view.findViewById(R.id.welcomeText);
        profilePicture = view.findViewById(R.id.profilePicture);
        timeImage = view.findViewById(R.id.time_image);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.profileEmail);
        signOut = view.findViewById(R.id.sign_Out_btn);
        TextView profileText = view.findViewById(R.id.profilePicture2);
        preferenceManager = new PreferenceManager(getContext().getApplicationContext());
        //Animation
        topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        bottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_animation);

        //Loading dialog -start
        addPostDialog = new Dialog(getContext());
        addPostDialog.setContentView(R.layout.pop);
        addPostDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addPostDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        addPostDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        addPostDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addPostDialog.setCancelable(false);
        closeBtn = addPostDialog.findViewById(R.id.closeBtn);
        missing = addPostDialog.findViewById(R.id.missing);
        crime = addPostDialog.findViewById(R.id.crime);
        announcementBtn = addPostDialog.findViewById(R.id.announcementBtn);
        //Loading dialog -end


        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    DataBase.email = documentSnapshot.getString("email");
                    DataBase.token = documentSnapshot.getString("FCM-TOKEN");
                    DataBase.fullName = documentSnapshot.getString("fullName");
                    DataBase.profile = documentSnapshot.getString("profile");
                    DataBase.gender = documentSnapshot.getString("gender");
                    DataBase.onlineStatus = documentSnapshot.getString("userStatus");
                    DataBase.postalCode = documentSnapshot.getString("postal_code");

                    preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                    preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                    preferenceManager.putString(Constants.KEY_USERS_PHOTO, documentSnapshot.getString(Constants.KEY_USERS_PHOTO));
                    preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                    preferenceManager.putString(Constants.KEY_TOKEN, documentSnapshot.getString(Constants.KEY_TOKEN));
                    preferenceManager.putString(Constants.KEY_USERS_STATUS, documentSnapshot.getString(Constants.KEY_USERS_STATUS));

                    if (DataBase.profile.equals("")) {
                        profileText.setVisibility(View.VISIBLE);
                        profileText.setText(DataBase.fullName.substring(0, 2));
                        profilePicture.setVisibility(View.GONE);
                    } else {
                        Glide.with(getContext()).load(DataBase.profile).apply(new RequestOptions()).into(profilePicture);
                        profilePicture.setVisibility(View.VISIBLE);
                        profileText.setVisibility(View.GONE);
                    }
                    String Name = documentSnapshot.getString("fullName");
                    name.setText(Name);
                    email.setText(DataBase.email);
                    name.setAnimation(topAnim);
                    email.setAnimation(topAnim);


                    Calendar c = Calendar.getInstance();
                    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                    if (timeOfDay >= 0 && timeOfDay < 4) {  //////////// 12am- 3.59am
                        welcome_text.setText("Good Night, " + Name);
                        timeImage.setImageResource(R.drawable.night);
                    } else if (timeOfDay >= 4 && timeOfDay < 12) {  ///////// 4- 11.59am
                        welcome_text.setText("Good Morning, " + Name);
                        timeImage.setImageResource(R.drawable.morning);
                    } else if (timeOfDay >= 12 && timeOfDay < 16) {  ////////////12pm- 3.59pm
                        welcome_text.setText("Good Afternoon, " + Name);
                        timeImage.setImageResource(R.drawable.evening);

                    } else if (timeOfDay >= 16 && timeOfDay < 20) {  ////////////4pm- 7.59pm
                        welcome_text.setText("Good Evening, " + Name);
                        timeImage.setImageResource(R.drawable.evening);

                    } else if (timeOfDay >= 20 && timeOfDay < 24) { //////////////8pm- 11.59pm
                        welcome_text.setText("Good Night, " + Name);
                        timeImage.setImageResource(R.drawable.night);
                    }
                }
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });

        meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MeetActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostDialog.show();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostDialog.dismiss();

            }
        });
        missing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMissingActivity.class);
                startActivity(intent);
            }
        });
        announcementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAnnouncementActivity.class);
                startActivity(intent);
            }
        });
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AnnouncementActivity.class);
                startActivity(intent);
            }
        });
        crime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCrimeActivity.class);
                startActivity(intent);
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageProfile.class);
                intent.putExtra("NAME", DataBase.fullName);
                intent.putExtra("EMAIL", DataBase.email);
                intent.putExtra("PROFILE_IMAGE", DataBase.profile);
                startActivity(intent);
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WalletActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


    private void SignOut() {
        Toast.makeText(getContext(), "Signing Out...", Toast.LENGTH_SHORT).show();
        Map<String, Object> FcmUpdate = new HashMap<>();
        FcmUpdate.put(Constants.KEY_TOKEN, FieldValue.delete());
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getUid())
                .update(FcmUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            DataBase.clearData();
                            Intent intent = new Intent(getContext(), RegisterActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
    }
}