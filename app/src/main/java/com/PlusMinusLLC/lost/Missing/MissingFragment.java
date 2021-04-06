package com.PlusMinusLLC.lost.Missing;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.PlusMinusLLC.lost.DataBase;
import com.PlusMinusLLC.lost.MainActivity;
import com.PlusMinusLLC.lost.Meet.User;
import com.PlusMinusLLC.lost.R;

import java.util.ArrayList;
import java.util.List;

public class MissingFragment extends Fragment {

    public static MissingAdapter missingAdapter;
    private RecyclerView channelRecyclerView;
    public static Dialog loadingDialog;
    private ConstraintLayout no_internet_connection_container;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private List<User> users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_missing, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        no_internet_connection_container = view.findViewById(R.id.no_internet_connection_container);
        channelRecyclerView = view.findViewById(R.id.channel_recyclerView);


        swipeRefreshLayout.setColorSchemeColors(
                getContext().getResources().getColor(R.color.violet_500),
                getContext().getResources().getColor(R.color.violet_500),
                getContext().getResources().getColor(R.color.violet_500));

        //Loading dialog -start
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
        loadingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        //Loading dialog -end

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        loadUi();
//        DataBase.clearData();

        missingAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networkInfo = connectivityManager.getActiveNetworkInfo();
                DataBase.clearData();
                loadUi();
            }
        });

        return view;
    }

    private void loadUi() {
        if (networkInfo != null && networkInfo.isConnected() == true) {

            users = new ArrayList<>();
            no_internet_connection_container.setVisibility(View.GONE);
            MainActivity.chipNavigationBar.setEnabled(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            channelRecyclerView.setLayoutManager(linearLayoutManager);
            missingAdapter = new MissingAdapter(DataBase.missingModelList);
            channelRecyclerView.setAdapter(missingAdapter);

            if (DataBase.missingModelList.size() == 0) {
                DataBase.loadMissingPeople(getContext(), loadingDialog, true);
                MissingFragment.swipeRefreshLayout.setRefreshing(false);
            } else {
                loadingDialog.dismiss();
            }
            missingAdapter.notifyDataSetChanged();
        } else {
            MissingFragment.swipeRefreshLayout.setRefreshing(false);
            no_internet_connection_container.setVisibility(View.VISIBLE);
            channelRecyclerView.setVisibility(View.GONE);
        }

    }

}