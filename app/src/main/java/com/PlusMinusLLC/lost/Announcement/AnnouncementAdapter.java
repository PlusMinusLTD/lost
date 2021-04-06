package com.PlusMinusLLC.lost.Announcement;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.Meet.MeetActivity;
import com.PlusMinusLLC.lost.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private List<AnnouncementModel> announcementModelList;

    public AnnouncementAdapter(List<AnnouncementModel> announcementModelList) {
        this.announcementModelList = announcementModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.ViewHolder holder, int position) {
        String PosterImage = announcementModelList.get(position).getPosterImageDB();
        String PosterName = announcementModelList.get(position).getPosterNameDB();
        String PosterEmail = announcementModelList.get(position).getPosterEmailDB();
        String PosterId = announcementModelList.get(position).getPosterID();
        String PostId = announcementModelList.get(position).getPostID();
        String MissingPeopleName = announcementModelList.get(position).getMissingPersonNameDB();
        String MissingPeopleAge = announcementModelList.get(position).getMissingPersonAgeDB();
        String MissingPeopleImage = announcementModelList.get(position).getMissingPersonImageDB();
        String MissingAnnouncementPrize = announcementModelList.get(position).getMissingPersonPrizeDB();
        String MissingPeopleDetails = announcementModelList.get(position).getMissingPersonDetailsDB();
        String MissingPeoplePlace = announcementModelList.get(position).getMissingPersonPlaceDB();
        String MissingPeopleTime = announcementModelList.get(position).getMissingPersonTimeDB();
        String MissingPostal = announcementModelList.get(position).getMissingPersonPostalCodeDB();
        Date Time = announcementModelList.get(position).getTime();
        holder.setData(PosterImage, PosterName, PosterEmail, PosterId, PostId, MissingPeopleName, MissingPeopleAge, MissingPeopleImage, MissingAnnouncementPrize, MissingPeopleDetails, MissingPeoplePlace, MissingPeopleTime,MissingPostal, Time);
    }

    @Override
    public int getItemCount() {
        return announcementModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView posterName1, posterEmail1, missingPeopleName1, missingPeopleAge1, deletePost, prize, placeContainerDialog, timeContainerDialog, posterImageText;
        private TextView nameDialog, ageDialog, detailsDialog, placeDialog, timeDialog, announcementPrizePopDialog;
        private ImageView posterImage1, missingPeopleImage1, menu, profileView, dismissBtn;
        public static TextView Time;
        private LinearLayout deleteLayout;
        private ConstraintLayout prizeContainer,missingContainer;
        private Button moreDetails, helpBtn;
        private Dialog moreDetailsDialog;
        private LinearLayout crimeTypeContainer, ageContainer;
        private boolean visibility = true;
        Animation topAnim, bottomAnim;
        public static boolean setAnnouncementPost = true;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage1 = itemView.findViewById(R.id.poster_image);
            posterName1 = itemView.findViewById(R.id.poster_name);
            posterEmail1 = itemView.findViewById(R.id.poster_email);
            posterImageText = itemView.findViewById(R.id.poster_image_text);
            missingPeopleImage1 = itemView.findViewById(R.id.missing_people_image);
            missingPeopleName1 = itemView.findViewById(R.id.missing_people_name);
            missingPeopleAge1 = itemView.findViewById(R.id.missing_people_age);
            missingContainer = itemView.findViewById(R.id.missingContainerAdd);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            deletePost = itemView.findViewById(R.id.delete_post);
            prize = itemView.findViewById(R.id.announcement_prize);
            menu = itemView.findViewById(R.id.menu);
            Time = itemView.findViewById(R.id.time);
            helpBtn = itemView.findViewById(R.id.button);
            moreDetails = itemView.findViewById(R.id.more_details);

            //Animation
            topAnim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_in);
            bottomAnim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_out);

        }

        public void setData(String posterImage, String posterName, String posterEmail, String posterId, String postId, String missingPeopleName, String missingPeopleAge, String missingPeopleImage, String missingAnnouncementPrize, String missingPersonDetails, String missingPersonPlace, String missingPersonTime,String missingPostal, Date time) {

            Glide.with(itemView.getContext()).load(posterImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(posterImage1);
            Glide.with(itemView.getContext()).load(missingPeopleImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(missingPeopleImage1);
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a - d'th', MMM YYYY");
            Time.setText(simpleDateFormat.format(time));
            posterEmail1.setText(posterEmail);
            posterName1.setText(posterName);
            missingPeopleName1.setText(missingPeopleName);
            missingPeopleAge1.setText(missingPeopleAge);
            prize.setText(missingAnnouncementPrize);
            if (posterId.equals(FirebaseAuth.getInstance().getUid())) {
                menu.setVisibility(View.VISIBLE);
                helpBtn.setVisibility(View.GONE);
            } else {
                menu.setVisibility(View.GONE);
                helpBtn.setVisibility(View.VISIBLE);
            }


            if (posterImage.equals("")){
                posterImageText.setVisibility(View.VISIBLE);
                posterImageText.setText(posterName.substring(0, 2));
                posterImage1.setVisibility(View.GONE);

            }else {
                posterImage1.setVisibility(View.VISIBLE);
                posterImageText.setVisibility(View.GONE);
                Glide.with(itemView.getContext()).load(posterImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(posterImage1);
            }

            helpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MeetActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
            posterImage1.setAnimation(topAnim);
            missingPeopleImage1.setAnimation(topAnim);
            Time.setAnimation(topAnim);
            posterEmail1.setAnimation(topAnim);
            posterName1.setAnimation(topAnim);
            missingPeopleName1.setAnimation(topAnim);
            missingPeopleAge1.setAnimation(topAnim);
            //Loading dialog -start
            moreDetailsDialog = new Dialog(itemView.getContext());
            moreDetailsDialog.setContentView(R.layout.details_popup);
            moreDetailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            moreDetailsDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
            moreDetailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            moreDetailsDialog.setCancelable(false);

            nameDialog = moreDetailsDialog.findViewById(R.id.name);
            ageDialog = moreDetailsDialog.findViewById(R.id.age);
            detailsDialog = moreDetailsDialog.findViewById(R.id.details);
            placeDialog = moreDetailsDialog.findViewById(R.id.place);
            timeDialog = moreDetailsDialog.findViewById(R.id.time);
            profileView = moreDetailsDialog.findViewById(R.id.profile_view);
            dismissBtn = moreDetailsDialog.findViewById(R.id.dismissBtn);
            prizeContainer = moreDetailsDialog.findViewById(R.id.prize_container);
            announcementPrizePopDialog = moreDetailsDialog.findViewById(R.id.announcement_prize_pop);
            crimeTypeContainer = moreDetailsDialog.findViewById(R.id.crime_type_container);
            ageContainer = moreDetailsDialog.findViewById(R.id.age_container);
            placeContainerDialog = moreDetailsDialog.findViewById(R.id.place_container);
            timeContainerDialog = moreDetailsDialog.findViewById(R.id.time_container);

            nameDialog.setText(missingPeopleName);
            ageDialog.setText(missingPeopleAge);
            detailsDialog.setText(missingPersonDetails);
            placeDialog.setText(missingPersonPlace);
            timeDialog.setText(missingPersonTime);
            Glide.with(itemView.getContext()).load(missingPeopleImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(profileView);


            nameDialog.setText(missingPeopleName);
            ageDialog.setText(missingPeopleAge);
            detailsDialog.setText(missingPersonDetails);
            placeDialog.setText(missingPersonPlace);
            timeDialog.setText(missingPersonTime);
            announcementPrizePopDialog.setText(missingAnnouncementPrize);
            Glide.with(itemView.getContext()).load(missingPeopleImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(profileView);


            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (visibility) {
                        deleteLayout.setVisibility(View.VISIBLE);
                        deleteLayout.setAnimation(topAnim);
                        visibility = false;
                    } else {
                        deleteLayout.setVisibility(View.GONE);
                        deleteLayout.setAnimation(bottomAnim);
                        visibility = true;

                    }
                }
            });

            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseFirestore.getInstance().collection("MISSING-POST")
//                            .document(modelList.get(Integer.parseInt(index)).getPostID())
                            .document(postId)
                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            deleteLayout.setVisibility(View.GONE);
                        }
                    });
                }
            });

            moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (setAnnouncementPost) {
                        prizeContainer.setVisibility(View.VISIBLE);
                        ageContainer.setVisibility(View.VISIBLE);
                        crimeTypeContainer.setVisibility(View.GONE);
                        placeContainerDialog.setText("Last seen (Place):   ");
                        timeContainerDialog.setText("Last seen (Time):   ");
                    } else {
                        prizeContainer.setVisibility(View.GONE);
                        ageContainer.setVisibility(View.GONE);
                        crimeTypeContainer.setVisibility(View.VISIBLE);
                        placeContainerDialog.setText("Crime Place:   ");
                        timeContainerDialog.setText("Crime Time:   ");
                    }
                    moreDetailsDialog.show();

                }
            });


            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreDetailsDialog.dismiss();

                }
            });


        }
    }
}
