package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserListDTO;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers();
    Transfer getTransfer(int userId);
    Transfer makeTransfer(int senderId, Transfer transfer);
}