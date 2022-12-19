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

    @PostMapping(path = "/request")
    public String requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        BigDecimal zero = new BigDecimal("0");
        Transfer requestedTransfer = null;
        int requestingUserID = userDao.findIdByUsername(principal.getName());
        Account requestingAccount = accountDao.findByUserID(requestingUserID);
        if (transfer.getSenderAccountId() == transfer.getReceiverAccountId() || transfer.getAmount().compareTo(zero) <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid request.");
        }
        transferDao.requestTransfer(requestingAccount, transfer);
        return "pending";
    }

    @PutMapping(path= "/transfer")
    public void makeTransfer(@RequestBody Transfer transfer, Principal principal){
        Transfer returnTransfer = null;
        BigDecimal zero = new BigDecimal("0");
        int senderUserId = userDao.findIdByUsername(principal.getName());
        int tempReceiverID = transfer.getReceiverAccountId();
        transfer.setReceiverAccountId(accountDao.findIdByUserID(tempReceiverID));
        Account senderAccount = accountDao.findByUserID(senderUserId);
        BigDecimal transferAmount = transfer.getAmount();
        // Should not be able to send yourself money or zero or negative money
        if (senderAccount.getId() == transfer.getReceiverAccountId() || transfer.getAmount().compareTo(zero) <= 0){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (transferAmount.compareTo(accountDao.findByUserID(senderUserId).getBalance()) <= 0){
           returnTransfer = transferDao.makeTransfer(senderAccount, transfer);
        }
        if (returnTransfer == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/history")
    public TransferDTO[] getHistory(Principal principal) {
        TransferDTO[] transfers;
        int userID = userDao.findIdByUsername(principal.getName());
        int accountID = accountDao.findIdByUserID(userID);
        transfers = transferDao.getHistory(accountID);
        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return transfers;
    }

    @GetMapping(path = "/history/{id}")
    public TransferDTO getTransfer(@PathVariable int id) {
        TransferDTO transfer = transferDao.getTransfer(id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Transfer ID.");
        }
        return transfer;
    }
}