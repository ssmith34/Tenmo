package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private int id;
    private int senderAccountId;
    private String senderName;
    private int receiverAccountId;
    private String receiverName;
    private BigDecimal amount;

    public TransferDTO() {}

    public TransferDTO(int id, int senderAccountId, String senderName, int receiverAccountId, String receiverName, BigDecimal amount) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.senderName = senderName;
        this.receiverAccountId = receiverAccountId;
        this.receiverName = receiverName;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return id + "          From: " + senderName + "          $" + amount;
    }
}
