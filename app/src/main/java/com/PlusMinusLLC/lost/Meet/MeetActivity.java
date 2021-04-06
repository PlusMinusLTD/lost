package com.PlusMinusLLC.lost.Meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.PlusMinusLLC.lost.Dashoard.DashboardFragment;
import com.PlusMinusLLC.lost.MainActivity;
import com.PlusMinusLLC.lost.Meet.listeners.UsersListener;
import com.PlusMinusLLC.lost.Missing.MissingFragment;
import com.PlusMinusLLC.lost.R;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MeetActivity extends AppCompatActivity implements UsersListener {
    private RecyclerView usersRecyclerView;
    private List<User> userModelList;
    private UsersAdapter usersAdapter;
    private TextView textErrorMsg;
    private ConstraintLayout viewContainerMeet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PreferenceManager preferenceManager;
    private ImageView imageConference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);

        textErrorMsg = findViewById(R.id.text_error_message);
        usersRecyclerView = findViewById(R.id.recyclerView);
        imageConference = findViewById(R.id.imageConference);
        viewContainerMeet = findViewById(R.id.containerMeet);
        preferenceManager = new PreferenceManager(getApplicationContext());

        userModelList = new ArrayList<>();
        usersAdapter = new UsersAdapter(userModelList, this);
        usersRecyclerView.setAdapter(usersAdapter);

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getUsers();
    }

    private void getUsers() {
        userModelList.clear();
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .get().addOnCompleteListener(task -> {
                    String myUserID = preferenceManager.getString(Constants.KEY_USER_ID);
                    swipeRefreshLayout.setRefreshing(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (myUserID.equals(documentSnapshot.getId())) {
                                continue;
                            }
                            userModelList.add(new User(
                                    documentSnapshot.getString("fullName")
                                    ,documentSnapshot.getString("email")
                                    ,documentSnapshot.getString("FCM-TOKEN")
                                    ,documentSnapshot.getString("userStatus")
                                    ,documentSnapshot.getString("profile")
                                    ,documentSnapshot.getId()
                            ));
                        }
                        if (userModelList.size() > 0) {
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            textErrorMsg.setText(String.format("No Users Available"));
                            textErrorMsg.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textErrorMsg.setText(String.format("No Users Available"));
                        textErrorMsg.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void initiateVideoMeeting(User user) {

        if (user.getToken() == null || user.getToken().trim().isEmpty()) {
            Toast.makeText(this, user.getFullName() + " is not available in online.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), OutGoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "video");
            startActivity(intent);
        }

    }

    @Override
    public void initiateAudioMeeting(User user) {
        if (user.getToken() == null || user.getToken().trim().isEmpty()) {
            Toast.makeText(this, user.getFullName() + " is not available for meeting.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), OutGoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }
    }

    @Override
    public void OnMultipleUserAction(Boolean isMultipleUserSelected) {
        if (isMultipleUserSelected) {
            imageConference.setVisibility(View.VISIBLE);
            imageConference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), OutGoingInvitationActivity.class);
                    intent.putExtra("selectedUsers", new Gson().toJson(usersAdapter.getSelectedUsers()));
                    intent.putExtra("type", "video");
                    intent.putExtra("isMultiple", true);
                    startActivity(intent);
                }
            });
        }
        else {
            imageConference.setVisibility(View.GONE);
        }
    }
    public void onBackPressed() {
        super.onStop();
        finish();
    }


}