package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public AccountController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        int userID = userDao.findIdByUsername(principal.getName());
        return accountDao.findByUserID(userID).getBalance();
    }

    @PostMapping(path="/transfer")
    public void makeTransfer(@RequestBody Transfer transfer, Principal principal){
        int senderUserId = userDao.findIdByUsername(principal.getName());
        int senderAccountId = accountDao.findIdByUserID(senderUserId);
        BigDecimal transferAmount = transfer.getAmount();
        if (transferAmount.compareTo(accountDao.findByUserID(senderUserId).getBalance()) <= 0){
            transferDao.makeTransfer(senderAccountId, transfer);
        }
    }
}