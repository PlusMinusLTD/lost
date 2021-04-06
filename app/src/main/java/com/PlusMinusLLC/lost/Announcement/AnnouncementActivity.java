package com.PlusMinusLLC.lost.Announcement;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.MainActivity;
import com.PlusMinusLLC.lost.R;

public class AnnouncementActivity extends AppCompatActivity {

    public static AnnouncementAdapter announcementAdapter;
    private RecyclerView channelRecyclerView;
    public static Dialog loadingDialog;
    private ConstraintLayout no_internet_connection_container;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        no_internet_connection_container = findViewById(R.id.no_internet_connection_container);
        channelRecyclerView = findViewById(R.id.channel_recyclerView);

        swipeRefreshLayout.setColorSchemeColors(
                this.getResources().getColor(R.color.violet_500),
                this.getResources().getColor(R.color.violet_500),
                this.getResources().getColor(R.color.violet_500));
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

        connectivityManager = (ConnectivityManager) AnnouncementActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        loadUi();
//        DataBase.clearData();

        announcementAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networkInfo = connectivityManager.getActiveNetworkInfo();
                DataBase.clearData();
                loadUi();
            }
        });
    }

    private void loadUi() {
        if (networkInfo != null && networkInfo.isConnected() == true) {

            no_internet_connection_container.setVisibility(View.GONE);
            MainActivity.chipNavigationBar.setEnabled(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AnnouncementActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            channelRecyclerView.setLayoutManager(linearLayoutManager);
            announcementAdapter = new AnnouncementAdapter(DataBase.announcementModelList);
            channelRecyclerView.setAdapter(announcementAdapter);

            if (DataBase.announcementModelList.size() == 0) {
                DataBase.loadAnnouncement(AnnouncementActivity.this, loadingDialog, true);
                AnnouncementActivity.swipeRefreshLayout.setRefreshing(false);
            } else {
                loadingDialog.dismiss();
            }
            announcementAdapter.notifyDataSetChanged();
        } else {
            AnnouncementActivity.swipeRefreshLayout.setRefreshing(false);
            no_internet_connection_container.setVisibility(View.VISIBLE);
            channelRecyclerView.setVisibility(View.GONE);
        }

    }
}