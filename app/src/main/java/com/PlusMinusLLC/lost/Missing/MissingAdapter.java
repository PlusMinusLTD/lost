package com.PlusMinusLLC.lost.Missing;

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
import com.PlusMinusLLC.lost.Meet.User;
import com.PlusMinusLLC.lost.Meet.listeners.UsersListener;
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

public class MissingAdapter extends RecyclerView.Adapter<MissingAdapter.ViewHolder> {

    private final List<MissingModel> missingModelList;


    public MissingAdapter(List<MissingModel> missingModelList) {
        this.missingModelList = missingModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.missing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MissingAdapter.ViewHolder holder, int position) {

        String PosterImage = missingModelList.get(position).getPosterImageDB();
        String PosterName = missingModelList.get(position).getPosterNameDB();
        String PosterEmail = missingModelList.get(position).getPosterEmailDB();
        String PosterId = missingModelList.get(position).getPosterID();
        String PostId = missingModelList.get(position).getPostID();
        String MissingPeopleName = missingModelList.get(position).getMissingPersonNameDB();
        String MissingPeopleAge = missingModelList.get(position).getMissingPersonAgeDB();
        String MissingPeopleImage = missingModelList.get(position).getMissingPersonImageDB();
        String MissingPeopleDetails = missingModelList.get(position).getMissingPersonDetailsDB();
        String MissingPeoplePlace = missingModelList.get(position).getMissingPersonPlaceDB();
        String MissingPeopleTime = missingModelList.get(position).getMissingPersonTimeDB();
        String MissingPeoplePostalCode=missingModelList.get(position).getMissingPersonPostalCodeDB();
        Date Time = missingModelList.get(position).getTime();
        holder.setData(PosterImage, PosterName, PosterEmail, PosterId, PostId, MissingPeopleName, MissingPeopleAge, MissingPeopleImage, MissingPeopleDetails, MissingPeoplePlace, MissingPeopleTime,MissingPeoplePostalCode, Time);

    }

    @Override
    public int getItemCount() {
        return missingModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView posterName1, posterEmail1, missingPeopleName1, missingPeopleAge1, deletePost;
        private TextView nameDialog, ageDialog, detailsDialog, placeDialog, timeDialog, placeContainerDialog, timeContainerDialog, posterImageText;
        private ImageView posterImage1, missingPeopleImage1, menu, profileView, dismissBtn;
        public static TextView Time;
        private LinearLayout deleteLayout;
        private Button moreDetails, helpBtn;
        private ConstraintLayout prizeContainer,missingContainer;
        private Dialog moreDetailsDialog;
        private boolean visibility = true;
        private LinearLayout crimeTypeContainer, ageContainer;
        public static boolean setMissingPost = true;
        Animation fadeIn, fadeOut;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage1 = itemView.findViewById(R.id.poster_image);
            posterName1 = itemView.findViewById(R.id.poster_name);
            posterEmail1 = itemView.findViewById(R.id.poster_email);
            helpBtn = itemView.findViewById(R.id.help_btn);
            missingPeopleImage1 = itemView.findViewById(R.id.missing_people_image);
            missingPeopleName1 = itemView.findViewById(R.id.missing_people_name);
            missingPeopleAge1 = itemView.findViewById(R.id.missing_people_age);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            deletePost = itemView.findViewById(R.id.delete_post);
            menu = itemView.findViewById(R.id.menu);
            missingContainer = itemView.findViewById(R.id.missingContainer);
            Time = itemView.findViewById(R.id.time);
            moreDetails = itemView.findViewById(R.id.more_details);
            posterImageText = itemView.findViewById(R.id.poster_image_text);

            //Animation
            fadeIn = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_in);
            fadeOut = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_out);


        }

        void setData(String posterImage, String posterName, String posterEmail, String posterId, String postId, String missingPeopleName, String missingPeopleAge, String missingPeopleImage, String missingPersonDetails, String missingPersonPlace, String missingPersonTime,String missingPersonPostalCode, Date time) {


            if (posterImage.equals("")){
                posterImageText.setVisibility(View.VISIBLE);
                posterImageText.setText(posterName.substring(0, 2));
                posterImage1.setVisibility(View.GONE);

            }else {
                posterImage1.setVisibility(View.VISIBLE);
                posterImageText.setVisibility(View.GONE);
                Glide.with(itemView.getContext()).load(posterImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(posterImage1);
            }
            Glide.with(itemView.getContext()).load(missingPeopleImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(missingPeopleImage1);


            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a - d'th', MMM YYYY");
            Time.setText(simpleDateFormat.format(time));
            posterEmail1.setText(posterEmail);
            posterName1.setText(posterName);
            missingPeopleName1.setText(missingPeopleName);
            missingPeopleAge1.setText(missingPeopleAge);

            posterImage1.setAnimation(fadeIn);
            missingPeopleImage1.setAnimation(fadeIn);
            Time.setAnimation(fadeIn);
            posterEmail1.setAnimation(fadeIn);
            posterName1.setAnimation(fadeIn);
            missingPeopleName1.setAnimation(fadeIn);
            missingPeopleAge1.setAnimation(fadeIn);

            if (posterId.equals(FirebaseAuth.getInstance().getUid())) {
                menu.setVisibility(View.VISIBLE);
                helpBtn.setVisibility(View.GONE);
            } else {
                menu.setVisibility(View.GONE);
                helpBtn.setVisibility(View.VISIBLE);
            }

            helpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MeetActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

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
            //Loading dialog -end

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (visibility) {
                        deleteLayout.setVisibility(View.VISIBLE);
                        deleteLayout.setAnimation(fadeIn);
                        visibility = false;
                    } else {
                        deleteLayout.setVisibility(View.GONE);
                        deleteLayout.setAnimation(fadeOut);
                        visibility = true;

                    }
                }
            });

            moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (setMissingPost) {
                        prizeContainer.setVisibility(View.VISIBLE);
                        ageContainer.setVisibility(View.VISIBLE);
                        crimeTypeContainer.setVisibility(View.GONE);
                        placeContainerDialog.setText("Last seen (Place):   ");
                        timeContainerDialog.setText("Last seen (Time):   ");
                        prizeContainer.setVisibility(View.GONE);
                    } else {
                        prizeContainer.setVisibility(View.GONE);
                        ageContainer.setVisibility(View.GONE);
                        crimeTypeContainer.setVisibility(View.VISIBLE);
                        placeContainerDialog.setText("Crime Place:   ");
                        timeContainerDialog.setText("Crime Time:   ");
                        prizeContainer.setVisibility(View.VISIBLE);
                    }
                    moreDetailsDialog.show();

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

//            FirebaseFirestore.getInstance().collection("MISSING-POST").document(posterId)
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                    name.setText(documentSnapshot.get("MissingPersonName").toString());
//                    age.setText(documentSnapshot.get("MissingPersonAge").toString());
//                    details.setText(documentSnapshot.get("MissingPersonDetails").toString());
//                    place.setText(documentSnapshot.get("MissingPersonPlace").toString());
//                    timee.setText(documentSnapshot.get("MissingPersonTime").toString());
//
//                }
//            });


            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreDetailsDialog.dismiss();

                }
            });


        }
    }
}
