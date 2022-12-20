package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestDTO {
    private int transferID;
    private int senderAccountID;
    private int receiverAccountID;
    private String receiverUsername;
    private BigDecimal amount;
    private LocalDate transferDate;

    public RequestDTO() {
    }

    public RequestDTO(int transferID, int senderAccountID, int receiverAccountID, String receiverUsername, BigDecimal amount, LocalDate transferDate) {
        this.transferID = transferID;
        this.senderAccountID = senderAccountID;
        this.receiverAccountID = receiverAccountID;
        this.receiverUsername = receiverUsername;
        this.amount = amount;
        this.transferDate = transferDate;
    }

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public int getSenderAccountID() {
        return senderAccountID;
    }

    public void setSenderAccountID(int senderAccountID) {
        this.senderAccountID = senderAccountID;
    }

    public int getReceiverAccountID() {
        return receiverAccountID;
    }

    public void setReceiverAccountID(int receiverAccountID) {
        this.receiverAccountID = receiverAccountID;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    @Override
    public String toString() {
        return transferID + "         ";
    }
}