package com.PlusMinusLLC.lost.Wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.R;
import com.PlusMinusLLC.lost.TransactionHistoryActivity;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WalletActivity extends AppCompatActivity {

    private TextView cardNumber, cardHolderName, cardHolderNumber, cash, title, Current_Money,Send_Money,Add_Money;
    private ImageView backBtn, qrCode, QRCode;
    private Button addCash, sendCash, dismissBtn, transactionHistoryBtn;
    public static WalletAdapter walletAdapter;
    private Dialog loadingDialog, qrDialog, coinDialog;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentSnapshot documentSnapshot1, documentSnapshot2, documentSnapshot3;
    public static List<WalletModel> walletModelList;
    public static String transactionId;
    public static IntentResult result;
    private EditText receiverID;
    private LineChart lineChart;
    private PieChart pieChart;
    private Animation topAnim;
    public static long C,M,P;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        backBtn = findViewById(R.id.backBtn);
        title = findViewById(R.id.title);
        Send_Money = findViewById(R.id.Send_money);
        Current_Money = findViewById(R.id.current_money);
        Add_Money = findViewById(R.id.Add_money);
        addCash = findViewById(R.id.add_cash);
        sendCash = findViewById(R.id.send_cash);
        cardHolderName = findViewById(R.id.name);
        cash = findViewById(R.id.cash);
        cardNumber = findViewById(R.id.cardNumber);
        cardHolderNumber = findViewById(R.id.cardHolderNumber);
        QRCode = findViewById(R.id.qr_code);
        transactionHistoryBtn = findViewById(R.id.transaction_btn);
        pieChart = findViewById(R.id.line_chart);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Animation
        topAnim = AnimationUtils.loadAnimation(WalletActivity.this, R.anim.fade_in);
        cash.setAnimation(topAnim);
        cardHolderName.setAnimation(topAnim);
        cardHolderNumber.setAnimation(topAnim);


        //Loading dialog -start
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        loadingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        //Loading dialog -end

        cardHolderName.setText(DataBase.fullName);
        transactionId = UUID.randomUUID().toString().substring(0, 20);

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_CASH")
                .document("Info")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot1 = task.getResult();
                    cardNumber.setText(FirebaseAuth.getInstance().getUid());
                    cash.setText("TK. " + documentSnapshot1.get("Cash").toString() + "/=");

                    C = documentSnapshot1.getLong("Cash");
                    M = documentSnapshot1.getLong("Minus_Money");
                    P = documentSnapshot1.getLong("Plus_Money");

                    Current_Money.setText("Tk. "+documentSnapshot1.getLong("Cash")+"/=");
                    Send_Money.setText("Tk. "+documentSnapshot1.getLong("Minus_Money")+"/=");
                    Add_Money.setText("Tk. "+documentSnapshot1.getLong("Plus_Money")+"/=");

                }
            }
        });

        sendCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCash();
            }
        });

        qrDialog = new Dialog(WalletActivity.this);
        qrDialog.setContentView(R.layout.qrcode_image_dialog);
        qrDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        qrDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        qrDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        qrDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        dismissBtn = qrDialog.findViewById(R.id.cancel_btn);
        ImageView qrCodeImage = qrDialog.findViewById(R.id.qr_code_image);
        String text = FirebaseAuth.getInstance().getUid();
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrDialog.dismiss();
                DataBase.clearData();
            }
        });


        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrDialog.show();
            }
        });

        addCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrDialog.show();

            }
        });

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_CASH")
                .document("Info")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot1 = task.getResult();
                    cardHolderNumber.setText(documentSnapshot1.get("CardHolderNumber").toString());
                    loadingDialog.dismiss();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                DataBase.clearData();
                finish();
            }
        });

        transactionHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this, TransactionHistoryActivity.class);
                startActivity(intent);
            }
        });

        chart();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBase.clearData();
        finish();
    }

    private void sendCash() {
        coinDialog = new Dialog(WalletActivity.this);
        coinDialog.setContentView(R.layout.send_dialog);
        coinDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        coinDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        coinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        coinDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        coinDialog.show();


        final EditText amountEditText = coinDialog.findViewById(R.id.amount_edit_text);
        receiverID = coinDialog.findViewById(R.id.receiver_id);
        Button cancelBtn = coinDialog.findViewById(R.id.cancel_btn);
        qrCode = coinDialog.findViewById(R.id.qr_code_scanner_dialog_opener_btn);
        Button sendMoneyBtn = coinDialog.findViewById(R.id.send_Money_btn);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinDialog.dismiss();
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCode();
            }
        });

        sendMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(amountEditText.getText())) {
                    if (!TextUtils.isEmpty(receiverID.getText())) {
                        loadingDialog.show();

                        //for get cash value
                        firebaseFirestore.collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("USER_CASH")
                                .document("Info")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    final long availableMoney = Long.valueOf(documentSnapshot1.get("Cash").toString());
                                    final long sendingMoney = Long.valueOf(amountEditText.getText().toString());

                                    if (availableMoney >= sendingMoney) {
                                        documentSnapshot1 = task.getResult();

                                        //for minus send amount from cash
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("Cash", availableMoney - sendingMoney);
                                        firebaseFirestore.collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_CASH")
                                                .document("Info")
                                                .update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    //for get receiver cash value
                                                    firebaseFirestore.collection("USERS")
                                                            .document(receiverID.getText().toString())
                                                            .collection("USER_CASH")
                                                            .document("Info")
                                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                documentSnapshot2 = task.getResult();
                                                                long receiverAvailableMoney = Long.valueOf(documentSnapshot2.get("Cash").toString());

                                                                //for plus amount in receiver amount
                                                                Map<String, Object> update = new HashMap<>();
                                                                update.put("Cash", receiverAvailableMoney + sendingMoney);
                                                                firebaseFirestore.collection("USERS")
                                                                        .document(receiverID.getText().toString())
                                                                        .collection("USER_CASH")
                                                                        .document("Info")
                                                                        .update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            // for update cash value in account
                                                                            cash.setText("Tk. " + availableMoney + "/=");

                                                                            //for adding transaction receiver name
                                                                            firebaseFirestore.collection("USERS")
                                                                                    .document(receiverID.getText().toString())
                                                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        //for transaction history
                                                                                        documentSnapshot3 = task.getResult();
                                                                                        Map<String, Object> transaction = new HashMap<>();
                                                                                        transaction.put("Name", DataBase.fullName);
                                                                                        transaction.put("Color", "Red");
                                                                                        transaction.put("Transaction_id", transactionId);
                                                                                        transaction.put("Cash_transfer", "- Tk. " + Long.valueOf(amountEditText.getText().toString()) + "/=");
                                                                                        transaction.put("Time", FieldValue.serverTimestamp());
                                                                                        firebaseFirestore.collection("USERS")
                                                                                                .document(FirebaseAuth.getInstance().getUid())
                                                                                                .collection("USER_CASH")
                                                                                                .document("History")
                                                                                                .collection("Transaction")
                                                                                                .document(transactionId)
                                                                                                .set(transaction)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            firebaseFirestore.collection("USERS")
                                                                                                                    .document(FirebaseAuth.getInstance().getUid())
                                                                                                                    .collection("USER_CASH")
                                                                                                                    .document("Info")
                                                                                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        long MinusMoney = documentSnapshot1.getLong("Minus_Money");
                                                                                                                        Map<String, Object> minusUpdate = new HashMap<>();
                                                                                                                        minusUpdate.put("Minus_Money", MinusMoney + Long.valueOf(amountEditText.getText().toString()));
                                                                                                                        firebaseFirestore.collection("USERS")
                                                                                                                                .document(FirebaseAuth.getInstance().getUid())
                                                                                                                                .collection("USER_CASH")
                                                                                                                                .document("Info")
                                                                                                                                .update(minusUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                    Map<String, Object> transaction = new HashMap<>();
                                                                                                                                    transaction.put("Name", DataBase.fullName);
                                                                                                                                    transaction.put("Color", "Green");
                                                                                                                                    transaction.put("Transaction_id", transactionId);
                                                                                                                                    transaction.put("Cash_transfer", "+ Tk. " + Long.valueOf(amountEditText.getText().toString()) + "/=");
                                                                                                                                    transaction.put("Time", FieldValue.serverTimestamp());
                                                                                                                                    firebaseFirestore.collection("USERS")
                                                                                                                                            .document(receiverID.getText().toString())
                                                                                                                                            .collection("USER_CASH")
                                                                                                                                            .document("History")
                                                                                                                                            .collection("Transaction")
                                                                                                                                            .document(transactionId)
                                                                                                                                            .set(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                firebaseFirestore.collection("USERS")
                                                                                                                                                        .document(receiverID.getText().toString())
                                                                                                                                                        .collection("USER_CASH")
                                                                                                                                                        .document("Info")
                                                                                                                                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                            long plusMoney = documentSnapshot1.getLong("Plus_Money");
                                                                                                                                                            Map<String, Object> plusUpdate = new HashMap<>();
                                                                                                                                                            plusUpdate.put("Plus_Money", plusMoney + Long.valueOf(amountEditText.getText().toString()));
                                                                                                                                                            firebaseFirestore.collection("USERS")
                                                                                                                                                                    .document(receiverID.getText().toString())
                                                                                                                                                                    .collection("USER_CASH")
                                                                                                                                                                    .document("Info")
                                                                                                                                                                    .update(plusUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                    if (task.isSuccessful()) {

                                                                                                                                                                        coinDialog.dismiss();
                                                                                                                                                                        loadingDialog.dismiss();
                                                                                                                                                                        Toast.makeText(WalletActivity.this, "Thank you for sending Money. Stay with us", Toast.LENGTH_LONG).show();

                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            });
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                            } else {
                                                                                                                                                Toast.makeText(WalletActivity.this, "Ops! Failed", Toast.LENGTH_LONG).show();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });

                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                        } else {
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(WalletActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(WalletActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(WalletActivity.this, "You have not enough Money", Toast.LENGTH_SHORT).show();
                                        coinDialog.show();
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(WalletActivity.this, "Please Scan or Type Receiver ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WalletActivity.this, "Please Type your sending amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            receiverID.setText(result.getContents());
        }
    }

    private void qrCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(WalletActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    private void chart() {

        ArrayList<PieEntry> dataSet = new ArrayList<>();

        dataSet.add(new PieEntry(C, "Current Money"));
        dataSet.add(new PieEntry(M, "Send Money"));
        dataSet.add(new PieEntry(P, "Add Money"));

        PieDataSet pieDataSet = new PieDataSet(dataSet, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.black));
        pieDataSet.setValueTextSize(15f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animate();
    }
}