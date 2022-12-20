package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers();
    Transfer getTransferByID(int transferId);
    TransferDisplayDTO getTransferDisplayDTOByID(int transferID);
    Transfer sendMoney(Transfer transfer);
    Transfer requestMoney(Transfer transfer);
    TransferDisplayDTO[] getHistory(int accountID);
    RequestDTO[] getPendingRequests(int accountID);
    boolean approveRequest(Transfer transfer);
    boolean denyRequest(int transferID);
}