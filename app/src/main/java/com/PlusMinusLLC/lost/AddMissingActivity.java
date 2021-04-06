package com.PlusMinusLLC.lost;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddMissingActivity extends AppCompatActivity {

    private ImageView image;
    private EditText name, age, time, place, details, postalCode;
    private TextView alert;
    private Button uploadBtn;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String upLoadId;
    private DocumentSnapshot documentSnapshot;
    private Bitmap bitmap;
    private boolean updateMissingPeoplePhoto = false;
    private boolean ImageInput = true;
    private Uri imageUri;
    Uri targetUri = null;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_missing);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        image = findViewById(R.id.missing_people_image_add);
        name = findViewById(R.id.missing_people_name);
        age = findViewById(R.id.missing_people_age_add);
        time = findViewById(R.id.missing_people_last_seen_time_add);
        postalCode = findViewById(R.id.missing_postal_code);
        place = findViewById(R.id.missing_people_last_seen_place_add);
        details = findViewById(R.id.missing_people_details_add);
        uploadBtn = findViewById(R.id.uploadBtn);
        alert = findViewById(R.id.alert);
        upLoadId = "mssng-" + UUID.randomUUID().toString().substring(0, 10);

        //Loading dialog -start
        loadingDialog = new Dialog(AddMissingActivity.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        loadingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog.setCancelable(false);
        //Loading dialog -end


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllData();
                loadingDialog.show();
                uploadBtn.setEnabled(false);
                uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        });

        uploadBtn.setEnabled(false);
        uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
                updateMissingPeoplePhoto = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (AddMissingActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        AddMissingActivity.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        postalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (ImageInput) {
            uploadBtn.setEnabled(false);
            name.setAlpha((float) 0.3);
            age.setAlpha((float) 0.3);
            time.setAlpha((float) 0.3);
            place.setAlpha((float) 0.3);
            details.setAlpha((float) 0.3);
            postalCode.setAlpha((float) 0.3);
            name.setEnabled(false);
            age.setEnabled(false);
            time.setEnabled(false);
            place.setEnabled(false);
            details.setEnabled(false);
            postalCode.setEnabled(false);
            uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == AddMissingActivity.this.RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();
                    targetUri = imageUri;
                    updateImage(imageUri);
                } else {
                    Toast.makeText(AddMissingActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateImage(Uri imageUri) {
        if (imageUri != null) {
            try {
                updateMissingPeoplePhoto = true;
                bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
                Glide.with(this).asBitmap().load(bitmap).into(image);
                alert.setVisibility(View.GONE);
                enableField();

                // extractProminentColors(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void enableField() {
        uploadBtn.setEnabled(false);
        name.setAlpha((float) 1);
        age.setAlpha((float) 1);
        time.setAlpha((float) 1);
        place.setAlpha((float) 1);
        details.setAlpha((float) 1);
        postalCode.setAlpha((float) 1);
        name.setEnabled(true);
        age.setEnabled(true);
        time.setEnabled(true);
        place.setEnabled(true);
        details.setEnabled(true);
        postalCode.setEnabled(true);
        uploadBtn.setTextColor(Color.rgb(255, 255, 255));
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(name.getText())) {
            if (!TextUtils.isEmpty(age.getText())) {
                if (!TextUtils.isEmpty(time.getText())) {
                    if (!TextUtils.isEmpty(postalCode.getText())) {
                        if (!TextUtils.isEmpty(place.getText())) {
                            if (!TextUtils.isEmpty(details.getText())) {
                                uploadBtn.setEnabled(true);
                                uploadBtn.setTextColor(Color.WHITE);
                            } else {
                                uploadBtn.setEnabled(false);
                                uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
                            }
                        } else {
                            uploadBtn.setEnabled(false);
                            uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    } else {
                        uploadBtn.setEnabled(false);
                        uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    uploadBtn.setEnabled(false);
                    uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                uploadBtn.setEnabled(false);
                uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            uploadBtn.setEnabled(false);
            uploadBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkAllData() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (updateMissingPeoplePhoto) {
            updatePhoto(user);
//            Glide.with(this).asBitmap().load(bitmap).into(image);
        }
    }

    private void updatePhoto(final FirebaseUser user) {
        if (updateMissingPeoplePhoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("MissingImage/*" + upLoadId + ".jpg");
            if (imageUri != null) {
                Glide.with(AddMissingActivity.this).asBitmap().load(imageUri).into(new ImageViewTarget<Bitmap>(image) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageUri = task.getResult();
                                                String missingPersonImage = task.getResult().toString();
                                                Glide.with(AddMissingActivity.this).load(missingPersonImage).into(image);

                                                Map<String, Object> uploadMissingPeopleInformation = new HashMap<>();
                                                uploadMissingPeopleInformation.put("MissingPersonName", name.getText().toString());
                                                uploadMissingPeopleInformation.put("MissingPersonAge", age.getText().toString() + " Years Old");
                                                uploadMissingPeopleInformation.put("MissingPersonPlace", place.getText().toString());
                                                uploadMissingPeopleInformation.put("MissingPersonTime", time.getText().toString());
                                                uploadMissingPeopleInformation.put("MissingPersonDetails", details.getText().toString());
                                                uploadMissingPeopleInformation.put("MissingPersonImage", missingPersonImage);
                                                uploadMissingPeopleInformation.put("MissingPersonPostalCode", postalCode.getText().toString());
                                                uploadMissingPeopleInformation.put("PosterName", DataBase.fullName);
                                                uploadMissingPeopleInformation.put("PosterEmail", DataBase.email);
                                                uploadMissingPeopleInformation.put("PosterImage", DataBase.profile);
                                                uploadMissingPeopleInformation.put("PosterID", FirebaseAuth.getInstance().getUid());
                                                uploadMissingPeopleInformation.put("PostID", upLoadId);
                                                uploadMissingPeopleInformation.put("Time", FieldValue.serverTimestamp());
                                                updateFields(user, uploadMissingPeopleInformation);

                                            } else {
                                                loadingDialog.dismiss();
//                                                DBqueries.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(AddMissingActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AddMissingActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        image.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                });
            } else {
                Toast.makeText(AddMissingActivity.this, "Please Select Missing People Image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Map<String, Object> uploadMissingPeopleInformation = new HashMap<>();
            uploadMissingPeopleInformation.put("MissingPersonName", name.getText().toString());
            updateFields(user, uploadMissingPeopleInformation);
        }
    }

    private void updateFields(FirebaseUser user, final Map<String, Object> uploadMissingPeopleInformation) {

        firebaseFirestore.collection("MISSING-POST")
                .document("missing")
                .collection(postalCode.getText().toString())
                .document(upLoadId)
                .set(uploadMissingPeopleInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(AddMissingActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AddMissingActivity.this, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });

    }

}