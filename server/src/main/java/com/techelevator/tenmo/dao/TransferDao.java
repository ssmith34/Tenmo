package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RequestDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;

import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers();
    TransferDTO getTransfer(int userId);
    Transfer makeTransfer(Account senderId, Transfer transfer);
    Transfer requestTransfer(Account senderId, Transfer transfer);
    TransferDTO[] getHistory(int accountID);
    RequestDTO[] getPendingRequests(int accountID);
}