package com.PlusMinusLLC.lost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.PlusMinusLLC.lost.Wallet.WalletActivity;
import com.PlusMinusLLC.lost.Wallet.WalletAdapter;

import static com.PlusMinusLLC.lost.Wallet.WalletActivity.walletAdapter;

public class TransactionHistoryActivity extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView walletRecyclerView;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        setUpRecyclerView();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                DataBase.clearData();
                finish();
            }
        });
    }

    private void setUpRecyclerView() {
        walletRecyclerView = findViewById(R.id.wallet_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TransactionHistoryActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        walletRecyclerView.setLayoutManager(linearLayoutManager);
        walletAdapter = new WalletAdapter(DataBase.walletModelList);
        walletRecyclerView.setAdapter(walletAdapter);

        if (DataBase.walletModelList.size() == 0) {
            DataBase.loadWallet(TransactionHistoryActivity.this, loadingDialog, true);
        } else {
            loadingDialog.dismiss();
        }
        walletAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBase.clearData();
        finish();
    }
}