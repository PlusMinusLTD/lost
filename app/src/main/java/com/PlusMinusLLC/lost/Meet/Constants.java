package com.PlusMinusLLC.lost.Meet;

import java.util.HashMap;

public class Constants {

    public static final String KEY_NAME = "fullName";
    public static final String KEY_USER_ID = "UID";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "FCM-TOKEN";
    public static final String KEY_COLLECTION_USERS = "USERS";
    public static final String KEY_USERS_STATUS = "userStatus";
    public static final String KEY_USERS_PHOTO = "profile";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_POSTAL_CODE = "postal_code";



    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";



    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAyoILc3E:APA91bGd09piJVOAjNcuJwBn--g_m6XCbaN00LIe8UekfGMXTE8-L4qV5pnUTfHwJHRrY0pOgUuTCJj83g9__eEGXc-ma573umx4LyOwYqvRKHEa1_kqKaxnsu3picmLcEYDCjKYAUnM"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
