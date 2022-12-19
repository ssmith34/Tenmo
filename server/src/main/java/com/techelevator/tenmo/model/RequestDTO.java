package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestDTO {
    private int id;
    private int senderAccountId;
    private BigDecimal amount;
    private LocalDate transferDate;
    private String status;

    public RequestDTO() {
    }

    public RequestDTO(int id, int senderAccountId, BigDecimal amount, LocalDate transferDate, String status) {
        this.id = id;
        this.senderAccountId = senderAccountId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}