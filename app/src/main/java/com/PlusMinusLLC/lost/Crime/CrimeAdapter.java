package com.PlusMinusLLC.lost.Crime;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder> {

    private List<CrimeModel> crimeModelList;

    public CrimeAdapter(List<CrimeModel> crimeModelList) {
        this.crimeModelList = crimeModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crime_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String PosterImage = crimeModelList.get(position).getPosterImageDB();
        String PosterName = crimeModelList.get(position).getPosterNameDB();
        String PosterEmail = crimeModelList.get(position).getPosterEmailDB();
        String PosterId = crimeModelList.get(position).getPosterID();
        String PostId = crimeModelList.get(position).getPostID();
        String CriminalName = crimeModelList.get(position).getCriminalNameDB();
        String CrimeType = crimeModelList.get(position).getCrimeTypeDB();
        String CriminalImage = crimeModelList.get(position).getCriminalImageDB();
        String CrimeDetails = crimeModelList.get(position).getCrimeDetailsDB();
        String CrimePlace = crimeModelList.get(position).getCrimePlaceDB();
        String CrimeTime = crimeModelList.get(position).getCrimeTimeDB();
        String CrimePostal = crimeModelList.get(position).getCrimePostalCodeDB();
        Date Time = crimeModelList.get(position).getTime();
        holder.setData(PosterImage, PosterName, PosterEmail, PosterId, PostId, CriminalName, CrimeType, CriminalImage, CrimeDetails, CrimePlace, CrimeTime,CrimePostal, Time);
    }

    @Override
    public int getItemCount() {
        return crimeModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView posterName1, posterEmail1, criminalName1, crimeType1, deletePost,Time, posterImageText;
        private TextView nameDialog, crimeTypeDialog, detailsDialog, placeDialog, timeDialog, placeContainerDialog, timeContainerDialog;
        private ImageView posterImage1, criminalImage1, menu, profileView, dismissBtn;
        private final LinearLayout deleteLayout;
        private final Button moreDetails;
        private Button helpBtn;
        private ConstraintLayout prizeContainer, crimeContainer;
        private LinearLayout crimeTypeContainer, ageContainer;
        private Dialog moreDetailsDialog;
        private boolean visibility = true;
        public static boolean setCrimePost = true;
        Animation fadeIn, fadeOut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage1 = itemView.findViewById(R.id.poster_image);
            posterName1 = itemView.findViewById(R.id.poster_name);
            posterEmail1 = itemView.findViewById(R.id.poster_email);
            posterImageText = itemView.findViewById(R.id.poster_image_text);
            criminalImage1 = itemView.findViewById(R.id.criminal_image);
            criminalName1 = itemView.findViewById(R.id.criminal_name);
            crimeType1 = itemView.findViewById(R.id.crime_type);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            crimeContainer = itemView.findViewById(R.id.CrimeContainer);
            deletePost = itemView.findViewById(R.id.delete_post);
            menu = itemView.findViewById(R.id.menu);
            Time = itemView.findViewById(R.id.time);
            helpBtn=itemView.findViewById(R.id.button2);
            moreDetails = itemView.findViewById(R.id.more_details);

            //Animation
            fadeIn = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_in);
            fadeOut = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_out);
        }

        public void setData(String posterImage, String posterName, String posterEmail, String posterId, String postId, String criminalName, String crimeType, String criminalImage, String crimeDetails, String crimePlace, String crimeTime,String crimePostal, Date time) {
            Glide.with(itemView.getContext()).load(posterImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(posterImage1);
            Glide.with(itemView.getContext()).load(criminalImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(criminalImage1);
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a - d'th', MMM YYYY");
            Time.setText(simpleDateFormat.format(time));
            posterEmail1.setText(posterEmail);
            posterName1.setText(posterName);
            criminalName1.setText(criminalName);
            crimeType1.setText(crimeType);

            posterImage1.setAnimation(fadeIn);
            criminalImage1.setAnimation(fadeIn);
            Time.setAnimation(fadeIn);
            posterEmail1.setAnimation(fadeIn);
            posterName1.setAnimation(fadeIn);
            criminalName1.setAnimation(fadeIn);
            crimeType1.setAnimation(fadeIn);

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

            //Loading dialog -start
            moreDetailsDialog = new Dialog(itemView.getContext());
            moreDetailsDialog.setContentView(R.layout.details_popup);
            moreDetailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            moreDetailsDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
            moreDetailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            moreDetailsDialog.setCancelable(false);

            nameDialog = moreDetailsDialog.findViewById(R.id.name);
            crimeTypeDialog = moreDetailsDialog.findViewById(R.id.crime_type);
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

            nameDialog.setText(criminalName);
            crimeTypeDialog.setText(crimeType);
            detailsDialog.setText(crimeDetails);
            placeDialog.setText(crimePlace);
            timeDialog.setText(crimeTime);
            Glide.with(itemView.getContext()).load(criminalImage).apply(new RequestOptions().placeholder(R.color.DarkBlue)).into(profileView);
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

            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseFirestore.getInstance().collection("CRIME-POST")
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
                    if (setCrimePost) {
                        prizeContainer.setVisibility(View.GONE);
                        ageContainer.setVisibility(View.GONE);
                        crimeTypeContainer.setVisibility(View.VISIBLE);
                        placeContainerDialog.setText("Crime Place:   ");
                        timeContainerDialog.setText("Crime Time:   ");
                    } else {
                        prizeContainer.setVisibility(View.VISIBLE);
                        ageContainer.setVisibility(View.VISIBLE);
                        crimeTypeContainer.setVisibility(View.GONE);
                        placeContainerDialog.setText("Last seen (Place):   ");
                        timeContainerDialog.setText("Last seen (Time):   ");

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

