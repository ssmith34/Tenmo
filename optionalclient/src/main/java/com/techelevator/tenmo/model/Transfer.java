package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transfer {

    private int id;
    private int senderAccountId;
    private int receiverAccountId;
    private BigDecimal amount;
    private String transferDate;
    private String status;

    public Transfer(){}

    public Transfer(int id, int senderId, int receiverId, BigDecimal amount, String transferDate, String status) {
        this.id = id;
        this.senderAccountId = senderId;
        this.receiverAccountId = receiverId;
        this.amount = amount;
        this.transferDate = transferDate;
        this.status = status;
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

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id = " + id +
                ", senderId = " + senderAccountId +
                ", receiverId = " + receiverAccountId +
                ", amount = " + amount +
                ", transferDate = " + transferDate +
                ", status = '" + status + '\'' + '}';
    }


}
