package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestDTO {
    private int id;
    private int senderAccountId;
    private BigDecimal amount;
    private LocalDate transferDate;

    public RequestDTO() {
    }

    public RequestDTO(int id, int senderAccountId, BigDecimal amount, String transferDate) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.amount = amount;
        this.transferDate = LocalDate.parse(transferDate);
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

    public void setTransferDate(String transferDate) {
        this.transferDate = LocalDate.parse(transferDate);
    }

    @Override
    public String toString() {
        return "Request: " +
                "Request Account" + id +
                ", senderAccountId=" + senderAccountId +
                ", amount=" + amount +
                ", transferDate=" + transferDate;
    }
}
