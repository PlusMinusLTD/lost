package com.PlusMinusLLC.lost.Wallet;

import java.util.Date;

public class WalletModel {
    private String receiverNAme;
    private Date time;
    private String color;
    private String transactionID;
    private String cashTransfer;

    public WalletModel(String receiverNAme, Date time, String color, String transactionID, String cashTransfer) {
        this.receiverNAme = receiverNAme;
        this.time = time;
        this.color = color;
        this.transactionID = transactionID;
        this.cashTransfer = cashTransfer;
    }

    public String getReceiverNAme() {
        return receiverNAme;
    }

    public void setReceiverNAme(String receiverNAme) {
        this.receiverNAme = receiverNAme;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCashTransfer() {
        return cashTransfer;
    }

    public void setCashTransfer(String cashTransfer) {
        this.cashTransfer = cashTransfer;
    }
}