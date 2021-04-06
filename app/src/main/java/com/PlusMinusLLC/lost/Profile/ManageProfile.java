package com.PlusMinusLLC.lost.Profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.R;
import com.PlusMinusLLC.lost.Registration.RegisterActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageProfile extends AppCompatActivity {


    private CircleImageView profilePicture;
    private ImageView closeBtn, nameChangeDialogCloseBtn;
    private Button confirmBtn, saveBtn;
    private EditText emailAddress, password, nameField;
    private TextView fullName, setDate;
    private CircleImageView changeBtn;
    private ConstraintLayout constraintLayout;
    private ImageView removeBtn, editBtn;
    private Button updateBtn;
    private CheckBox checkBox;
    private Dialog loadingDialog, passwordDialog, nameDialog;
    private Bitmap bitmap;
    private Uri imageUri, coverUri;
    private boolean updateProfilePhoto = false;
    private RadioButton male, female, others;
    private RadioGroup radioGroup;
    private DocumentSnapshot documentSnapshot;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FrameLayout parentFrameLayout;
    private FirebaseAuth firebaseAuth;
    Uri targetUri = null;
    Uri targetCoverUri = null;
    private DatePickerDialog datePickerDialog;
    String name, email, photo, dateOfBirth, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Loading dialog -start
        loadingDialog = new Dialog(ManageProfile.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        loadingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog.setCancelable(false);
        //Loading dialog -end

        //Password dialog -start
        passwordDialog = new Dialog(ManageProfile.this);
        passwordDialog.setContentView(R.layout.confirm_password_dialog);
        passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        passwordDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        passwordDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        passwordDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        passwordDialog.setCancelable(false);
        password = passwordDialog.findViewById(R.id.signInPassword);
        confirmBtn = passwordDialog.findViewById(R.id.confirm_button_btn);
        checkBox = passwordDialog.findViewById(R.id.checkBox);
        closeBtn = passwordDialog.findViewById(R.id.close);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordDialog.dismiss();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //Password dialog -end

        //Name dialog -start
        nameDialog = new Dialog(ManageProfile.this);
        nameDialog.setContentView(R.layout.name_change_dialog);
        nameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nameDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        nameDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        nameDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nameDialog.setCancelable(false);
        nameField = nameDialog.findViewById(R.id.name_field);
        saveBtn = nameDialog.findViewById(R.id.save_btn);
        nameChangeDialogCloseBtn = nameDialog.findViewById(R.id.name_change_dialog_close_btn);

        nameChangeDialogCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameDialog.dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName.setText(nameField.getText().toString());
                nameDialog.dismiss();
            }
        });
        //Name dialog -end
        profilePicture = findViewById(R.id.profile_picture);
        changeBtn = findViewById(R.id.change_picture_button);
        removeBtn = findViewById(R.id.remove_picture_button);
        updateBtn = findViewById(R.id.update_button);
        fullName = findViewById(R.id.full_name);
        emailAddress = findViewById(R.id.email);
        editBtn = findViewById(R.id.edit_name_button);
        constraintLayout = findViewById(R.id.circle1);
        setDate = findViewById(R.id.set_date);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        others = findViewById(R.id.others);
        radioGroup = findViewById(R.id.radioGroup);
        firebaseAuth = FirebaseAuth.getInstance();
        parentFrameLayout = ManageProfile.this.findViewById(R.id.main_frameLayout);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameDialog.show();
                nameField.setText(fullName.getText().toString());
            }
        });

        fullName.setText(nameField.getText().toString());
        name = getIntent().getStringExtra("NAME");
        email = getIntent().getStringExtra("EMAIL");
        photo = getIntent().getStringExtra("PROFILE_IMAGE");


        Glide.with(ManageProfile.this).load(photo).placeholder(R.drawable.download).into(profilePicture);
        fullName.setText(name);
        emailAddress.setText(email);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    String birthDay = documentSnapshot.get("dateOfBirth").toString();
                    if (birthDay.equals("")) {
                        setDate.setText("DD/MM/YYY");
                        setDate.setTextColor(ColorStateList.valueOf(ManageProfile.this.getColor(R.color.white2)));
                    } else {
                        setDate.setText(birthDay);
                    }
                }
            }
        });


        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = new DatePicker(ManageProfile.this);
                int currentDay = datePicker.getDayOfMonth();
                int currentMonth = (datePicker.getDayOfMonth());
                int currentYear = (datePicker.getYear()) - 1;

                datePickerDialog = new DatePickerDialog(ManageProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                setDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();

            }
        });


        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfilePhoto = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ManageProfile.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        ManageProfile.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = null;
                updateProfilePhoto = false;
                Glide.with(ManageProfile.this).load(R.drawable.download).into(profilePicture);
            }
        });


        emailAddress.addTextChangedListener(new TextWatcher() {
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
        fullName.addTextChangedListener(new TextWatcher() {
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


        //radio Button Start

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.male:
                        Map<String, Object> Male = new HashMap<>();
                        Male.put("gender", "Male");
                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update(Male).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        male.setTextSize(20);
                        female.setTextSize(15);
                        others.setTextSize(15);
                        break;

                    case R.id.female:
                        Map<String, Object> Female = new HashMap<>();
                        Female.put("gender", "Female");
                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update(Female).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        male.setTextSize(15);
                        female.setTextSize(20);
                        others.setTextSize(15);
                        break;

                    case R.id.others:
                        Map<String, Object> Others = new HashMap<>();
                        Others.put("gender", "Others");
                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update(Others).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        male.setTextSize(15);
                        female.setTextSize(15);
                        others.setTextSize(20);
                        break;
                }

            }
        });


        male.setChecked(update("GENDER_MALE"));
        female.setChecked(update("GENDER_FEMALE"));
        others.setChecked(update("GENDER_OTHERS"));
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton1, boolean male_checked) {
                SaveIntoSharedPreferences("GENDER_MALE", male_checked);
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton2, boolean female_checked) {
                SaveIntoSharedPreferences("GENDER_FEMALE", female_checked);
            }
        });

        others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean others_checked) {
                SaveIntoSharedPreferences("GENDER_OTHERS", others_checked);
            }
        });

        //radio Button End


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
                checkEmailAndPassword();
                updateName();
                SetDate();
            }
        });

    }

    public void onStart() {
        super.onStart();
    }


    private void SetDate() {

        Map<String, Object> updateDate = new HashMap<>();
        updateDate.put("dateOfBirth", setDate.getText().toString());
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .update(updateDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingDialog.dismiss();
                    updateBtn.setEnabled(true);
                    updateBtn.setTextColor(Color.rgb(0, 0, 0));
//                    finish();
                }
            }
        });

    }


    private void SaveIntoSharedPreferences(String key, boolean value) {
        SharedPreferences sp = ManageProfile.this.getSharedPreferences("PLUS_MINUS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean update(String key) {
        SharedPreferences sp = ManageProfile.this.getSharedPreferences("PLUS_MINUS", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == ManageProfile.this.RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();
                    targetUri = imageUri;
                    updateImage(imageUri);
                } else {
                    Toast.makeText(ManageProfile.this, "Image not found", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == 3) {
            if (resultCode == ManageProfile.this.RESULT_OK) {
                if (data != null) {
                    coverUri = data.getData();
                    targetCoverUri = coverUri;
                } else {
                    Toast.makeText(ManageProfile.this, "Image not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateImage(Uri imageUri) {
        if (imageUri != null) {
            try {
                updateProfilePhoto = true;
                bitmap = BitmapFactory.decodeStream(ManageProfile.this.getContentResolver().openInputStream(imageUri));
                Glide.with(this).asBitmap().load(bitmap).into(profilePicture);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(ManageProfile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailAddress.getText())) {
            if (!TextUtils.isEmpty(fullName.getText())) {

                updateBtn.setEnabled(true);
                updateBtn.setTextColor(Color.rgb(255, 255, 255));

            } else {
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkEmailAndPassword() {
        String emailPatter = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        Drawable customErrorIcon = getResources().getDrawable(R.drawable.error);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());
        if (emailAddress.getText().toString().matches(emailPatter)) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            if (emailAddress.getText().toString().toLowerCase().equals(email.toLowerCase().trim())) {
                loadingDialog.show();
                if (updateProfilePhoto) {
                    updatePhoto(user);
                }

            } else {
                passwordDialog.show();
                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordDialog.dismiss();
                        loadingDialog.show();
                        userPassword = password.getText().toString();
                        AuthCredential credential = EmailAuthProvider.getCredential(email, userPassword);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updateEmail(emailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                        if (firebaseUser != null) {
                                                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        HashMap<String, Object> update = new HashMap<>();
                                                                        update.put("email", emailAddress.getText().toString());
                                                                        update.put("fullName", fullName.getText().toString());
                                                                        firebaseFirestore.collection("USERS")
                                                                                .document(FirebaseAuth.getInstance().getUid())
                                                                                .update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
//                                                                                    if (updateProfilePhoto) {
                                                                                    Toast.makeText(ManageProfile.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                                                                                    Intent intent = new Intent(ManageProfile.this, RegisterActivity.class);
                                                                                    startActivity(intent);
                                                                                    loadingDialog.dismiss();
                                                                                    firebaseAuth.signOut();
                                                                                    finish();
                                                                                    updatePhoto(user);

                                                                                }
                                                                            }
                                                                        });
                                                                    } else {
                                                                        Toast.makeText(ManageProfile.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        loadingDialog.dismiss();
                                                        updateBtn.setEnabled(true);
                                                        updateBtn.setTextColor(Color.rgb(0, 0, 0));
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ManageProfile.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            loadingDialog.dismiss();
                                            updateBtn.setEnabled(true);
                                            updateBtn.setTextColor(Color.rgb(0, 0, 0));
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ManageProfile.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }

        } else {
            emailAddress.setError("Invalid Email", customErrorIcon);
        }
    }

    private void updateName() {
        HashMap<String, Object> update = new HashMap<>();
        update.put("email", emailAddress.getText().toString());
        update.put("fullName", fullName.getText().toString());
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .update(update);
    }


    private void updatePhoto(final FirebaseUser user) {
        if (updateProfilePhoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Profile/*" + user.getUid() + ".jpg");
            if (imageUri != null) {
                Glide.with(ManageProfile.this).asBitmap().load(imageUri).centerCrop().into(new ImageViewTarget<Bitmap>(profilePicture) {

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
                                                DataBase.profile = task.getResult().toString();
                                                Glide.with(ManageProfile.this).load(DataBase.profile).into(profilePicture);

                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("email", emailAddress.getText().toString());
                                                updateData.put("fullName", fullName.getText().toString());
                                                updateData.put("profile", DataBase.profile);
                                                updateFields(user, updateData);

                                            } else {
                                                loadingDialog.dismiss();
                                                updateBtn.setEnabled(true);
                                                updateBtn.setTextColor(Color.rgb(0, 0, 0));
                                                DataBase.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ManageProfile.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    loadingDialog.dismiss();
                                    updateBtn.setEnabled(true);
                                    updateBtn.setTextColor(Color.rgb(0, 0, 0));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(ManageProfile.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        profilePicture.setImageResource(R.drawable.download);
                    }
                });
            } else {
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DataBase.profile = "";
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("email", emailAddress.getText().toString());
                            updateData.put("fullName", fullName.getText().toString());
                            updateData.put("profile", "");
                            updateFields(user, updateData);

                        } else {
                            loadingDialog.dismiss();
                            updateBtn.setEnabled(true);
                            updateBtn.setTextColor(Color.rgb(0, 0, 0));
                            String error = task.getException().getMessage();
                            Toast.makeText(ManageProfile.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("fullName", fullName.getText().toString());
            updateFields(user, updateData);
        }
    }

    private void updateFields(FirebaseUser user, final Map<String, Object> updateData) {
        FirebaseFirestore.getInstance().collection("USERS")
                .document(user.getUid()).update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (updateData.size() > 1) {
                                DataBase.email = emailAddress.getText().toString().trim();
                                DataBase.fullName = fullName.getText().toString().trim();
                            } else {
                                DataBase.fullName = fullName.getText().toString().trim();
                            }
                            ManageProfile.this.finish();
                            Toast.makeText(ManageProfile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ManageProfile.this, error, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                        updateBtn.setEnabled(true);
                        updateBtn.setTextColor(Color.rgb(0, 0, 0));
                    }
                });
    }
}
