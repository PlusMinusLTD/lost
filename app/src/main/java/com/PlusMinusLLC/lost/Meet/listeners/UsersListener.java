package com.PlusMinusLLC.lost.Meet.listeners;

import com.PlusMinusLLC.lost.Meet.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);
    void initiateAudioMeeting(User user);
    void OnMultipleUserAction(Boolean isMultipleUserSelected);
}
