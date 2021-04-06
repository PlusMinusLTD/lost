package com.PlusMinusLLC.lost.Wallet;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PlusMinusLLC.lost.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private List<WalletModel> walletModelList;

    public WalletAdapter(List<WalletModel> walletModelList) {
        this.walletModelList = walletModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ReceiverName = walletModelList.get(position).getReceiverNAme();
        Date Time = walletModelList.get(position).getTime();
        String TransactionID = walletModelList.get(position).getTransactionID();
        String Color = walletModelList.get(position).getColor();
        String CashTransfer = walletModelList.get(position).getCashTransfer();
        holder.setData(ReceiverName, Time, TransactionID, CashTransfer, Color);

    }

    @Override
    public int getItemCount() {
        return walletModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public static TextView receiverName, transactionID, amount;
        public static TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverName = itemView.findViewById(R.id.receiver_name);
            transactionID = itemView.findViewById(R.id.transaction_id);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount);
        }

        public void setData(String Name, Date Time, String TransactionID, String cashTransfer, String color) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a - d'th', MMM YYYY");

            receiverName.setText(Name);
            time.setText(simpleDateFormat.format(Time));
            transactionID.setText(TransactionID);
            amount.setText(cashTransfer);

            if (color.equals("Red")) {
                amount.setTextColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.Red)));
            } else if (color.equals("Green")) {
                amount.setTextColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.Green)));

            }

        }
    }
}
