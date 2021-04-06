package com.PlusMinusLLC.lost.Registration;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.PlusMinusLLC.lost.MainActivity;
import com.PlusMinusLLC.lost.Meet.Constants;
import com.PlusMinusLLC.lost.Meet.PreferenceManager;
import com.PlusMinusLLC.lost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static com.PlusMinusLLC.lost.Registration.RegisterActivity.onResetPasswordFragment;

public class SignInFragment extends Fragment {

    private TextView do_not_have_an_account, forgotPassword, emailVerifyStatus;
    private FrameLayout parentFrameLayout;
    private EditText email, password;
    private Button signInButton;
    private CheckBox checkBox;
    private ProgressBar progressBar;
    private ImageView notification;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn = false;
    private FirebaseAuth firebaseAuth;
    private PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        //Initialize - start
        parentFrameLayout = getActivity().findViewById(R.id.main_frameLayout);
        do_not_have_an_account = view.findViewById(R.id.dontHaveAnAccount);
        email = view.findViewById(R.id.signInEmail);
        password = view.findViewById(R.id.signInPassword);
        progressBar = view.findViewById(R.id.signInProgressBar);
        signInButton = view.findViewById(R.id.signInButton);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        emailVerifyStatus = view.findViewById(R.id.emailVerifyStatus);
        notification = view.findViewById(R.id.notification);
        checkBox = view.findViewById(R.id.checkBox);
        firebaseAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getContext().getApplicationContext());
        //Initialize - end


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Check(view);
            }
        });
        signInButton.setTextColor(Color.argb(60, 255, 255, 255));
        return view;
    }

    public void Check(View view) {
        if (checkBox.isChecked()) {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        do_not_have_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }

        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment2(new ResetPasswordFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment2(ResetPasswordFragment resetPasswordFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), resetPasswordFragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {
                signInButton.setEnabled(true);
                signInButton.setTextColor(Color.rgb(255, 255, 255));

            } else {
                signInButton.setEnabled(false);
                signInButton.setTextColor(Color.argb(60, 255, 255, 255));
            }
        } else {
            signInButton.setEnabled(false);
            signInButton.setTextColor(Color.argb(60, 255, 255, 255));
        }
    }

    private void checkEmailAndPassword() {
        Drawable customErrorIcon = getResources().getDrawable(R.drawable.error);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());

        if (email.getText().toString().matches(emailPattern)) {
            if (password.length() >= 8) {
                progressBar.setVisibility(View.VISIBLE);
                signInButton.setEnabled(false);
                signInButton.setTextColor(Color.argb(60, 255, 255, 255));
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    emailVerifyStatus.setText("Your email is not verified");
                                    emailVerifyStatus.setTextColor(Color.RED);
                                    emailVerifyStatus.setVisibility(View.VISIBLE);
                                    notification.setImageResource(R.drawable.failed);
                                    notification.setVisibility(View.VISIBLE);
                                    signInButton.setEnabled(true);
                                    signInButton.setText("Refresh");
                                    signInButton.setTextColor(Color.WHITE);
                                    checkEmailVerification();

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signInButton.setEnabled(true);
                                    signInButton.setTextColor(Color.WHITE);
                                    notification.setImageResource(R.drawable.failed);
                                    notification.setVisibility(View.VISIBLE);
                                    emailVerifyStatus.setVisibility(View.VISIBLE);
                                    emailVerifyStatus.setText("Incorrect email or password");
                                    emailVerifyStatus.setTextColor(Color.RED);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                password.setError("Password must be 8 characters", customErrorIcon);
            }
        } else {
            email.setError("Invalid Email", customErrorIcon);
        }
    }

    private void checkEmailVerification() {

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if (emailFlag) {
            notification.setImageResource(R.drawable.success);
            notification.setVisibility(View.VISIBLE);
            emailVerifyStatus.setVisibility(View.INVISIBLE);
            signInButton.setEnabled(false);
            signInButton.setText("Sign In  >");
            signInButton.setTextColor(Color.argb(60, 255, 255, 255));
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(firebaseAuth.getCurrentUser().getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}