package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers();
    Transfer getTransfer(int userId);
    Transfer makeTransfer(Account senderId, Transfer transfer);
}