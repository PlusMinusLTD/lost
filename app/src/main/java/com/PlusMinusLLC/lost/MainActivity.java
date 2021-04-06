package com.PlusMinusLLC.lost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.PlusMinusLLC.lost.Crime.CrimeFragment;
import com.PlusMinusLLC.lost.Dashoard.DashboardFragment;
import com.PlusMinusLLC.lost.Meet.Constants;
import com.PlusMinusLLC.lost.Missing.MissingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private int REQUEST_CODE_BATTERY_OPTIMIZATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.navigation_missing, true);

        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, new MissingFragment()).commit();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.navigation_missing:
                        fragment = new MissingFragment();
                        DataBase.clearData();
                        break;
                    case R.id.navigation_crime:
                        fragment = new CrimeFragment();
                        break;
                    case R.id.navigation_profile:
                        fragment = new DashboardFragment();
                        break;
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, fragment).commit();
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    sendFCMTokenToDataBase(task.getResult().getToken());
                }
            }
        });
        checkForBatteryOptimization();
    }

    private void sendFCMTokenToDataBase(String token) {
        Map<String, Object> FcmUpdate = new HashMap<>();
        FcmUpdate.put(Constants.KEY_TOKEN, token);
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getUid())
                .update(FcmUpdate)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Token not Updated", Toast.LENGTH_SHORT).show());
    }

    private void checkForBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Battery optimization is enabled. It can interrupt running background services");
                builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE_BATTERY_OPTIMIZATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATION) {
            checkForBatteryOptimization();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .update("userStatus", "Active");
    }

    public void onBackPressed() {
        super.onStop();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .update("userStatus", "Inactive");
        finish();
    }
}