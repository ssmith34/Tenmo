package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransferDisplayDTO {

    private int transferID;
    private int senderAccountID;
    private int senderUserID;
    private String senderUsername;
    private int receiverAccountID;
    private int receiverUserID;
    private String receiverUsername;
    private BigDecimal transferAmount;
    private LocalDate transferDate;
    private String transferType;
    private String status;

    public TransferDisplayDTO() {}

    public TransferDisplayDTO(int transferID, int senderAccountID, int senderUserID, String senderUsername,
                              int receiverAccountID, int receiverUserID, String receiverUsername,
                              BigDecimal transferAmount, String transferDate, String transferType, String status) {
        this.transferID = transferID;
        this.senderAccountID = senderAccountID;
        this.senderUserID = senderUserID;
        this.senderUsername = senderUsername;
        this.receiverAccountID = receiverAccountID;
        this.receiverUserID = receiverUserID;
        this.receiverUsername = receiverUsername;
        this.transferAmount = transferAmount;
        this.transferDate = LocalDate.parse(transferDate);
        this.transferType = transferType;
        this.status = status;
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

    public int getSenderUserID() {
        return senderUserID;
    }

    public void setSenderUserID(int senderUserID) {
        this.senderUserID = senderUserID;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public int getReceiverAccountID() {
        return receiverAccountID;
    }

    public void setReceiverAccountID(int receiverAccountID) {
        this.receiverAccountID = receiverAccountID;
    }

    public int getReceiverUserID() {
        return receiverUserID;
    }

    public void setReceiverUserID(int receiverUserID) {
        this.receiverUserID = receiverUserID;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = LocalDate.parse(transferDate);
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString(String loggedInUser) {
        String middle = null;
        if (loggedInUser.equals(receiverUsername))
            middle = "From: " + getSenderUsername();
        else
            middle = "To: " + getReceiverUsername();
        String message = transferID + "          " + middle + "          $" + transferAmount;
        return message;
    }
}