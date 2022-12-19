package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers();
    TransferDisplayDTO getTransferByID(int userId);
    Transfer sendMoney(Transfer transfer);
    Transfer requestMoney(Account senderId, Transfer transfer);
    TransferDisplayDTO[] getHistory(int accountID);
    RequestDTO[] getPendingRequests(int accountID);
}