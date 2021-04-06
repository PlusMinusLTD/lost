package com.PlusMinusLLC.lost.Meet;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.Meet.listeners.UsersListener;
import com.PlusMinusLLC.lost.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> userModelList;
    private UsersListener usersListener;
    private final List<User> selectedUsers;

    public UsersAdapter(List<User> userModelList, UsersListener usersListener) {
        this.userModelList = userModelList;
        this.usersListener = usersListener;
        selectedUsers = new ArrayList<>();
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String FullName = userModelList.get(position).getFullName();
        String Email = userModelList.get(position).getEmail();
        String Token = userModelList.get(position).getToken();
        String Status = userModelList.get(position).getStatus();
        String Photo = userModelList.get(position).getPhoto();
        String UID = userModelList.get(position).getUID();
        holder.setUserData(FullName, Email, Token, Status, Photo, UID, userModelList.get(position));

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userEmail, activeIndicator, userImageText;
        ImageView videoCalling, audioCalling, userPhoto, imageSelected;
        ConstraintLayout userContainer;


        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userImageText = itemView.findViewById(R.id.user_profile_image_text);
            userEmail = itemView.findViewById(R.id.email);
            videoCalling = itemView.findViewById(R.id.video_call);
            audioCalling = itemView.findViewById(R.id.voice_call);
            userPhoto = itemView.findViewById(R.id.user_profile_image);
            activeIndicator = itemView.findViewById(R.id.user_active_status_text);
            userContainer = itemView.findViewById(R.id.userContainer);
            imageSelected = itemView.findViewById(R.id.imageSelected);
        }

        void setUserData(String fullName, String email, String token, String status, String photo, String uid, User user) {
            userName.setText(fullName);
            userEmail.setText(email);


            if (photo.equals("")) {
                userImageText.setText(fullName.substring(0, 2));
                userPhoto.setVisibility(View.GONE);
                userImageText.setVisibility(View.VISIBLE);
            } else {
                Glide.with(itemView.getContext()).load(photo).into(userPhoto);
                userPhoto.setVisibility(View.VISIBLE);
                userImageText.setVisibility(View.GONE);
            }


            audioCalling.setOnClickListener(v -> usersListener.initiateAudioMeeting(user));
            videoCalling.setOnClickListener(v -> usersListener.initiateVideoMeeting(user));

            activeIndicator.setText(status);
            if (token == null || token.trim().isEmpty() || status.equals("Inactive")) {
                activeIndicator.setTextColor(Color.rgb(255, 18, 98));
                audioCalling.setEnabled(false);
                videoCalling.setEnabled(false);
                userContainer.setEnabled(false);
                userContainer.setAlpha((float) 0.5);
            } else if (status.equals("Active")) {
                activeIndicator.setTextColor(Color.rgb(34, 215, 120));
                audioCalling.setEnabled(true);
                audioCalling.setAlpha((float) 1);
                videoCalling.setEnabled(true);
                videoCalling.setAlpha((float) 1);
                userContainer.setEnabled(true);
                userContainer.setAlpha((float) 1);
            }

            userContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (imageSelected.getVisibility() != View.VISIBLE) {
                        selectedUsers.add(user);
                        imageSelected.setVisibility(View.VISIBLE);
                        videoCalling.setVisibility(View.GONE);
                        audioCalling.setVisibility(View.GONE);
                        usersListener.OnMultipleUserAction(true);
                    }
                    return true;
                }
            });

            userContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageSelected.getVisibility() == View.VISIBLE) {
                        selectedUsers.remove(user);
                        imageSelected.setVisibility(View.GONE);
                        videoCalling.setVisibility(View.VISIBLE);
                        audioCalling.setVisibility(View.VISIBLE);
                        if (selectedUsers.size() == 0) {
                            usersListener.OnMultipleUserAction(false);
                        }
                    } else {
                        if (selectedUsers.size() > 0) {
                            selectedUsers.add(user);
                            imageSelected.setVisibility(View.VISIBLE);
                            audioCalling.setVisibility(View.GONE);
                            videoCalling.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }
}
