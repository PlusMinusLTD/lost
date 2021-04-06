package com.PlusMinusLLC.lost;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.PlusMinusLLC.lost.Announcement.AnnouncementActivity;
import com.PlusMinusLLC.lost.Announcement.AnnouncementModel;
import com.PlusMinusLLC.lost.Crime.CrimeFragment;
import com.PlusMinusLLC.lost.Crime.CrimeModel;
import com.PlusMinusLLC.lost.Missing.MissingFragment;
import com.PlusMinusLLC.lost.Missing.MissingModel;
import com.PlusMinusLLC.lost.Wallet.WalletActivity;
import com.PlusMinusLLC.lost.Wallet.WalletModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private DocumentSnapshot documentSnapshot;

    public static String email, fullName, profile, gender, thisId, token, onlineStatus, postalCode;

    public static List<String> missing = new ArrayList<>();
    public static List<MissingModel> missingModelList = new ArrayList<>();

    public static List<String> announcement = new ArrayList<>();
    public static List<AnnouncementModel> announcementModelList = new ArrayList<>();

    public static List<String> crime = new ArrayList<>();
    public static List<CrimeModel> crimeModelList = new ArrayList<>();


    public static List<String> wallet = new ArrayList<>();
    public static List<WalletModel> walletModelList = new ArrayList<>();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static void loadMissingPeople(final Context context, final Dialog loadingDialog, final Boolean onMissingPeople) {

        missingModelList.clear();

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String postalCode1 = documentSnapshot.get("postal_code").toString();

                    firebaseFirestore.collection("MISSING-POST")
                            .document("missing")
                            .collection(postalCode1)
                            .orderBy("Time", Query.Direction.DESCENDING)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    missingModelList.add(new MissingModel(
                                            documentSnapshot.get("PosterImage").toString()
                                            , documentSnapshot.get("PosterName").toString()
                                            , documentSnapshot.get("PosterEmail").toString()
                                            , documentSnapshot.get("PosterID").toString()
                                            , documentSnapshot.get("PostID").toString()
                                            , documentSnapshot.get("MissingPersonImage").toString()
                                            , documentSnapshot.get("MissingPersonName").toString()
                                            , documentSnapshot.get("MissingPersonAge").toString()
                                            , documentSnapshot.get("MissingPersonDetails").toString()
                                            , documentSnapshot.get("MissingPersonPlace").toString()
                                            , documentSnapshot.get("MissingPersonTime").toString()
                                            , documentSnapshot.get("MissingPersonPostalCode").toString()
                                            , documentSnapshot.getDate("Time")
                                    ));
                                }

                                if (onMissingPeople) {
                                    MissingFragment.missingAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                }


            }
        });

    }

    public static void loadAnnouncement(final Context context, final Dialog loadingDialog, final Boolean onAnnouncement) {

        announcementModelList.clear();

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String postalCode2 = documentSnapshot.get("postal_code").toString();


                    firebaseFirestore.collection("ANNOUNCEMENT-POST")
                            .document("announcement")
                            .collection(postalCode2)
                            .orderBy("Time", Query.Direction.DESCENDING)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    announcementModelList.add(new AnnouncementModel(
                                            documentSnapshot.get("PosterImage").toString()
                                            , documentSnapshot.get("PosterName").toString()
                                            , documentSnapshot.get("PosterEmail").toString()
                                            , documentSnapshot.get("PosterID").toString()
                                            , documentSnapshot.get("PostID").toString()
                                            , documentSnapshot.get("MissingPersonImage").toString()
                                            , documentSnapshot.get("MissingPersonName").toString()
                                            , documentSnapshot.get("MissingPersonAge").toString()
                                            , documentSnapshot.get("MissingPersonFindingPrize").toString()
                                            , documentSnapshot.get("MissingPersonDetails").toString()
                                            , documentSnapshot.get("MissingPersonPlace").toString()
                                            , documentSnapshot.get("MissingPersonTime").toString()
                                            , documentSnapshot.get("MissingPostalCode").toString()
                                            , documentSnapshot.getDate("Time")
                                    ));
                                }

                                if (onAnnouncement) {
                                    AnnouncementActivity.announcementAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });

                }
            }
        });

    }

    public static void loadCrime(final Context context, final Dialog loadingDialog, final Boolean onCrime) {

        crimeModelList.clear();

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String postalCode3 = documentSnapshot.get("postal_code").toString();


                    firebaseFirestore.collection("CRIME-POST")
                            .document("crime")
                            .collection(postalCode3)
                            .orderBy("Time", Query.Direction.DESCENDING)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    crimeModelList.add(new CrimeModel(
                                            documentSnapshot.get("PosterImage").toString()
                                            , documentSnapshot.get("PosterName").toString()
                                            , documentSnapshot.get("PosterEmail").toString()
                                            , documentSnapshot.get("PosterID").toString()
                                            , documentSnapshot.get("PostID").toString()
                                            , documentSnapshot.get("CriminalImage").toString()
                                            , documentSnapshot.get("CriminalName").toString()
                                            , documentSnapshot.get("CrimeType").toString()
                                            , documentSnapshot.get("CrimeDetails").toString()
                                            , documentSnapshot.get("CrimePlace").toString()
                                            , documentSnapshot.get("CrimeTime").toString()
                                            , documentSnapshot.get("CriminalPostalCode").toString()
                                            , documentSnapshot.getDate("Time")
                                    ));
                                }

                                if (onCrime) {
                                    CrimeFragment.crimeAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });

                }
            }
        });


    }

    public static void loadWallet(final Context context, final Dialog loadingDialog, final Boolean onMyWallet) {

        walletModelList.clear();

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_CASH")
                .document("History")
                .collection("Transaction")
                .orderBy("Time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        walletModelList.add(new WalletModel(documentSnapshot.get("Name").toString()
                                , documentSnapshot.getDate("Time")
                                , documentSnapshot.get("Color").toString()
                                , documentSnapshot.get("Transaction_id").toString()
                                , documentSnapshot.get("Cash_transfer").toString()
                        ));
                    }
                    if (onMyWallet) {
                        WalletActivity.walletAdapter.notifyDataSetChanged();
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }
        });
    }


    public static void clearData() {
        missingModelList.clear();
        announcementModelList.clear();
        crimeModelList.clear();
        walletModelList.clear();
    }
}
