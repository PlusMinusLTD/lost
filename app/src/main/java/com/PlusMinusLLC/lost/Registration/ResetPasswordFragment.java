package com.PlusMinusLLC.lost.Registration;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.PlusMinusLLC.lost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private TextView goBack, checkYourMail;
    private FrameLayout parentFrameLayout;
    private ImageView complete;
    private EditText registeredEmail;
    private Button resetButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    public ViewGroup emailIconCintainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        //Initialize
        parentFrameLayout = getActivity().findViewById(R.id.main_frameLayout);
        goBack = view.findViewById(R.id.goBack);
        complete = view.findViewById(R.id.complete);
        checkYourMail = view.findViewById(R.id.checkYourMail);
        registeredEmail = view.findViewById(R.id.editText);
        resetButton = view.findViewById(R.id.resetButton);
        progressBar = view.findViewById(R.id.progress_circular);
        firebaseAuth = FirebaseAuth.getInstance();
        emailIconCintainer = view.findViewById(R.id.emailIconCintainer);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
        registeredEmail.addTextChangedListener(new TextWatcher() {
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
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailIconCintainer);
                progressBar.setVisibility(View.VISIBLE);
                resetButton.setEnabled(false);
                resetButton.setTextColor(Color.argb(50, 0, 0, 0));

                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Email send Successfully", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    checkYourMail.setVisibility(View.VISIBLE);
                                    checkYourMail.setText("Check your email to reset your password");
                                    checkYourMail.setTextColor(getResources().getColor(R.color.Green));
                                    complete.setVisibility(View.VISIBLE);
                                } else {
                                    String error = task.getException().getMessage();
                                    progressBar.setVisibility(View.GONE);
                                    TransitionManager.beginDelayedTransition(emailIconCintainer);
                                    checkYourMail.setText(error);
                                    checkYourMail.setTextColor(getResources().getColor(R.color.Red));
                                    checkYourMail.setVisibility(View.VISIBLE);
                                    complete.setVisibility(View.VISIBLE);


                                }
                                resetButton.setEnabled(false);
                                resetButton.setTextColor(Color.argb(50, 0, 0, 0));
                            }
                        });
            }
        });

    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registeredEmail.getText())) {
            resetButton.setEnabled(false);
            resetButton.setTextColor(Color.argb(50, 0, 0, 0));
            checkYourMail.setVisibility(View.INVISIBLE);
        } else {
            resetButton.setEnabled(true);
            resetButton.setTextColor(Color.rgb(0, 0, 0));
        }
    }

    private void setFragment(SignInFragment signInFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), signInFragment);
        fragmentTransaction.commit();
    }

}