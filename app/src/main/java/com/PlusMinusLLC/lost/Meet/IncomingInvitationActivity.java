package com.PlusMinusLLC.lost.Meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.PlusMinusLLC.lost.MainActivity;
import com.PlusMinusLLC.lost.Meet.Network.ApiClient;
import com.PlusMinusLLC.lost.Meet.Network.ApiService;
import com.PlusMinusLLC.lost.R;
import com.bumptech.glide.Glide;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingInvitationActivity extends AppCompatActivity {

    private String meetingType = null;
    private ImageView imageAcceptInvitation, imageRejectInvitation;
    private CircleImageView profilePicture;
    private TextView textFirstChar, textUserName, textEmail;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);

        ImageView imageMeetingType = findViewById(R.id.imageMeetingType);
        meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);

        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_round_videocam_24);
            } else {
                imageMeetingType.setImageResource(R.drawable.ic_round_local_phone_24);
            }
        }

        textFirstChar = findViewById(R.id.textFirstChar);
        textUserName = findViewById(R.id.textUsername);
        textEmail = findViewById(R.id.textEmail);
        profilePicture = findViewById(R.id.user_profile);
        imageAcceptInvitation = findViewById(R.id.ImageAcceptInvitation);
        imageRejectInvitation = findViewById(R.id.ImageRejectInvitation);


        String firstName = getIntent().getStringExtra(Constants.KEY_NAME);
        String photo = Constants.KEY_USERS_PHOTO;



        if (photo.equals("")) {
            textFirstChar.setText(firstName.substring(0, 2));
            profilePicture.setVisibility(View.GONE);
            textFirstChar.setVisibility(View.VISIBLE);
        }else {
            Glide.with(IncomingInvitationActivity.this).load(Constants.KEY_USERS_PHOTO).placeholder(R.drawable.download).into(profilePicture);
            profilePicture.setVisibility(View.VISIBLE);
            textFirstChar.setVisibility(View.GONE);
        }

        textUserName.setText(getIntent().getStringExtra(Constants.KEY_NAME));
        textEmail.setText(getIntent().getStringExtra(Constants.KEY_EMAIL));

        imageAcceptInvitation.setOnClickListener(v -> {
            sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_ACCEPTED, getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN));
            stopRingTone();
        });

        imageRejectInvitation.setOnClickListener(v -> {
            sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_REJECTED, getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN));
            stopRingTone();
        });

        if (mediaPlayer ==null){
            mediaPlayer = MediaPlayer.create(this,R.raw.ring_tone);
        }
        mediaPlayer.start();
        mediaPlayer.isLooping();
    }

    private void sendInvitationResponse(String type, String receiverToken) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), type);
            finish();

        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void sendRemoteMessage(String remoteMessageBody, String type) {
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeaders(), remoteMessageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {

                        try {
                            URL serverURL = new URL("https://meet.jit.si");

                            JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder();
                            builder.setServerURL(serverURL);
                            builder.setWelcomePageEnabled(false);
                            builder.setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM));
                            if (meetingType.equals("audio")) {
                                builder.setVideoMuted(true);
                            }
                            JitsiMeetActivity.launch(IncomingInvitationActivity.this, builder.build());
                            finish();

                        } catch (Exception exception) {
                            Toast.makeText(IncomingInvitationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } else if (type.equals(Constants.REMOTE_MSG_INVITATION_REJECTED)){
                        Toast.makeText(IncomingInvitationActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(IncomingInvitationActivity.this, MeetActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(IncomingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(IncomingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED)) {
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    private void stopRingTone(){
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver, new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
        stopRingTone();
    }
}