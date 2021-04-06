package com.PlusMinusLLC.lost.Registration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.PlusMinusLLC.lost.MainActivity;
import com.PlusMinusLLC.lost.Meet.Constants;
import com.PlusMinusLLC.lost.Meet.PreferenceManager;
import com.PlusMinusLLC.lost.R;
import com.PlusMinusLLC.lost.Wallet.WalletActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.PlusMinusLLC.lost.Registration.RegisterActivity.onResetPasswordFragment;


import in.dd4you.appsconfig.DD4YouConfig;

public class SignUpFragment extends Fragment {
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email, password, confirmPassword, postal_Code;
    public static EditText fullName;
    private ImageView close;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private CheckBox checkBox2, checkBox3;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    public static boolean disableCloseBtn = false;
    private DD4YouConfig dd4YouConfig;
    private String CardHolderNumber;
    private String emailPatter = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static String transactionId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        //Initialize - start
        alreadyHaveAnAccount = view.findViewById(R.id.i_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.main_frameLayout);
        email = view.findViewById(R.id.email);
        fullName = view.findViewById(R.id.full_name);
        postal_Code = view.findViewById(R.id.postal_code);
        password = view.findViewById(R.id.new_password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        close = view.findViewById(R.id.close);
        signUpBtn = view.findViewById(R.id.update_button);
        progressBar = view.findViewById(R.id.progressBar);
        checkBox2 = view.findViewById(R.id.password_checkBox);
        checkBox3 = view.findViewById(R.id.checkBox3);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        dd4YouConfig = new DD4YouConfig(getContext());
        //Initialize - end

        if (disableCloseBtn) {
            close.setVisibility(View.GONE);
        } else {
            close.setVisibility(View.GONE);
        }

        transactionId = UUID.randomUUID().toString().substring(0, 20);

        CardHolderNumber = dd4YouConfig.generateUniqueID(4) + " - " + dd4YouConfig.generateUniqueID(4) + " - " + dd4YouConfig.generateUniqueID(4) + " - " + dd4YouConfig.generateUniqueID(4);

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox2.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox3.isChecked()) {

                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
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
        postal_Code.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(fullName.getText())) {
                if (!TextUtils.isEmpty(postal_Code.getText())) {
                    if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                        if (!TextUtils.isEmpty(confirmPassword.getText())) {
                            signUpBtn.setEnabled(true);
                            signUpBtn.setTextColor(Color.WHITE);

                        } else {
                            signUpBtn.setEnabled(false);
                            signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    } else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkEmailAndPassword() {
        Drawable customErrorIcon = getResources().getDrawable(R.drawable.error);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());
        if (email.getText().toString().matches(emailPatter)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Map<String, Object> UserMoney = new HashMap<>();
                                    UserMoney.put("CardHolderName", fullName.getText().toString());
                                    UserMoney.put("CardNumber", FirebaseAuth.getInstance().getUid());
                                    UserMoney.put("CardHolderNumber", CardHolderNumber);
                                    UserMoney.put("Cash", Long.valueOf("0"));
                                    UserMoney.put("Plus_Money", Long.valueOf("0"));
                                    UserMoney.put("Minus_Money", Long.valueOf("0"));
                                    firebaseFirestore.collection("USERS")
                                            .document(firebaseAuth.getUid())
                                            .collection("USER_CASH")
                                            .document("Info")
                                            .set(UserMoney);
                                    // for user start
                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put(Constants.KEY_NAME, fullName.getText().toString());
                                    userdata.put(Constants.KEY_EMAIL, email.getText().toString());
                                    userdata.put(Constants.KEY_POSTAL_CODE, postal_Code.getText().toString());
                                    userdata.put("profile", "");
                                    userdata.put("gender", "");
                                    userdata.put("dateOfBirth", "");
                                    userdata.put("userStatus", "Active");

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        CollectionReference userDataReference = firebaseFirestore
                                                                .collection(Constants.KEY_COLLECTION_USERS)
                                                                .document(firebaseAuth.getUid())
                                                                .collection("USER_DATA");

                                                        //UserMaps - start
                                                        Map<String, Object> addressMap = new HashMap<>();
                                                        addressMap.put("list_size", (long) 0);

                                                        Map<String, Object> notificationMap = new HashMap<>();
                                                        notificationMap.put("list_size", (long) 0);
                                                        //UserMaps - end


                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_RECENT_MISSING_POST");
                                                        documentNames.add("MY_NOTIFICATION");

                                                        List<Map<String, Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(addressMap);
                                                        documentFields.add(notificationMap);

                                                        for (int x = 0; x < documentNames.size(); x++) {
                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful()) {
                                                                        if (finalX == documentNames.size() - 1) {
                                                                            sendEmailVerification();
                                                                        }
                                                                    } else {
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        signUpBtn.setEnabled(true);
                                                                        signUpBtn.setTextColor(Color.WHITE);
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    // for user end

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpBtn.setEnabled(true);
                                    signUpBtn.setTextColor(Color.WHITE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                confirmPassword.setError("Password does't matched", customErrorIcon);
            }
        } else {
            email.setError("Invalid Email", customErrorIcon);
        }

    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        onResetPasswordFragment = true;
                        setFragment2(new SignInFragment());
                        firebaseAuth.signOut();
                    } else {
                        Toast.makeText(getActivity(), "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setFragment2(SignInFragment signInFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), signInFragment);
        fragmentTransaction.commit();
    }

    private void mainIntent() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}