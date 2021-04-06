package com.PlusMinusLLC.lost.Crime;

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
import com.PlusMinusLLC.lost.R;

public class CrimeFragment extends Fragment {
    public static CrimeAdapter crimeAdapter;
    private RecyclerView channelRecyclerView;
    public static Dialog loadingDialog;
    private ConstraintLayout no_internet_connection_container;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);
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

        crimeAdapter.notifyDataSetChanged();

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

            no_internet_connection_container.setVisibility(View.GONE);
            MainActivity.chipNavigationBar.setEnabled(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            channelRecyclerView.setLayoutManager(linearLayoutManager);
            crimeAdapter = new CrimeAdapter(DataBase.crimeModelList);
            channelRecyclerView.setAdapter(crimeAdapter);

            if (DataBase.crimeModelList.size() == 0) {
                DataBase.loadCrime(getContext(), loadingDialog, true);
                CrimeFragment.swipeRefreshLayout.setRefreshing(false);
            } else {
                loadingDialog.dismiss();
            }
            crimeAdapter.notifyDataSetChanged();
        } else {
            CrimeFragment.swipeRefreshLayout.setRefreshing(false);
            no_internet_connection_container.setVisibility(View.VISIBLE);
            channelRecyclerView.setVisibility(View.GONE);
        }

    }

}