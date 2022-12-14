package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers();

    Transfer getTransfer(int userId);

    boolean makeTransfer (int senderId, int recieverId, BigDecimal transferAmount);
}
