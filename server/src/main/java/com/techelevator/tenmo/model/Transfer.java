package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Transfer {

    private int id;
    private int senderAccountId;
    private String senderName;
    private int receiverAccountId;
    private String receiverName;
    private BigDecimal amount;
    private LocalDate transferDate;
    private String transferType;
    private String status;

    public Transfer(){}

    public Transfer(int id, int senderId, String senderName, int receiverAccountId, String receiverName, BigDecimal amount,
                    LocalDate transferDate, String transferType, String status) {
        this.id = id;
        this.senderAccountId = senderId;
        this.senderName = senderName;
        this.receiverAccountId = receiverAccountId;
        this.receiverName = receiverName;
        this.amount = amount;
        this.transferDate = transferDate;
        this.transferType = transferType;
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

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
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

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", senderId=" + senderAccountId +
                ", receiverId=" + receiverAccountId +
                ", amount=" + amount +
                ", transferDate=" + transferDate +
                ", status='" + status + '\'' + '}';
    }
}
