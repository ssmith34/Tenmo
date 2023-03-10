package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    private final UserDao userDao;
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final BigDecimal zero = new BigDecimal("0");

    public TenmoController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @GetMapping(path = "/all-users")
    public List<UserListDTO> userList() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        int userID = userDao.findIdByUsername(principal.getName());
        return accountDao.findByUserID(userID).getBalance();
    }

    @GetMapping(path = "/transfer-history")
    public TransferDisplayDTO[] getHistory(Principal principal) {
        TransferDisplayDTO[] displayTransfers;
        int userID = userDao.findIdByUsername(principal.getName());
        int accountID = accountDao.findIdByUserID(userID);
        displayTransfers = transferDao.getHistory(accountID);
        if (displayTransfers == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return displayTransfers;
    }

    @GetMapping(path = "/transfer-history/{id}")
    public TransferDisplayDTO getTransferDisplayDTOByID(@PathVariable int id) {
        TransferDisplayDTO transferDetails = transferDao.getTransferDisplayDTOByID(id);
        if (transferDetails == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Transfer ID.");
        }
        return transferDetails;
    }

    @GetMapping(path = "/view-pending-requests")
    public RequestDTO[] getPendingRequests(Principal principal) {
        int userID = userDao.findIdByUsername(principal.getName());
        int accountID = accountDao.findIdByUserID(userID);
        RequestDTO[] pendingRequests = transferDao.getPendingRequests(accountID);
        if (pendingRequests == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pending requests.");
        }
        return pendingRequests;
    }

    @PutMapping(path = "/approve-request/{transferID}")
    public void approveRequest(@PathVariable int transferID) {
        boolean success = false;
        TransferDisplayDTO approvedTransfer = transferDao.getTransferDisplayDTOByID(transferID);
        // Cannot send more money than you have
        Account senderAccount = accountDao.findByUserID(approvedTransfer.getSenderUserID());
        BigDecimal balance = senderAccount.getBalance();

        if (approvedTransfer.getTransferAmount().compareTo(accountDao.findByUserID(approvedTransfer.getSenderUserID()).getBalance()) <= 0) {
            Transfer transfer = transferDao.getTransferByID(transferID);
            success = transferDao.approveRequest(transfer);
        }
        if (!success)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/deny-request/{transferID}")
    public void denyRequest(@PathVariable int transferID) {
        boolean success = false;
        success = transferDao.denyRequest(transferID);
        if (!success)
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
    }

    @PutMapping(path= "/send-money")
    public void sendMoney(@RequestBody Transfer transfer, Principal principal){
        Transfer returnTransfer = null;
        int senderUserId = userDao.findIdByUsername(principal.getName());
        transfer.setSenderAccountId(accountDao.findIdByUserID(senderUserId));
        int actualReceiverID = accountDao.findIdByUserID(transfer.getReceiverAccountId());
        transfer.setReceiverAccountId(actualReceiverID);
        // Should not be able to send yourself money or zero or negative money
        if (transfer.getSenderAccountId() == transfer.getReceiverAccountId() || transfer.getAmount().compareTo(zero) <= 0){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // Cannot send more money than you have in your account
        if (transfer.getAmount().compareTo(accountDao.findByUserID(senderUserId).getBalance()) <= 0){
           returnTransfer = transferDao.sendMoney(transfer);
        }
        if (returnTransfer == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(path = "/request-money")
    public void requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        Transfer returnTransfer = null;
        int requestingUserID = userDao.findIdByUsername(principal.getName());
        transfer.setReceiverAccountId(accountDao.findIdByUserID(requestingUserID));
        int actualSenderAccountID = accountDao.findIdByUserID(transfer.getSenderAccountId());
        transfer.setSenderAccountId(actualSenderAccountID);
        // Should not be able to request money from yourself or request zero or negative amount
        if (transfer.getSenderAccountId() == transfer.getReceiverAccountId() || transfer.getAmount().compareTo(zero) <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid request.");
        }
        returnTransfer = transferDao.requestMoney(transfer);
        if (returnTransfer == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}