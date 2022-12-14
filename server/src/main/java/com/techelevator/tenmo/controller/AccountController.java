package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

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

    @PostMapping(path="/transfer/{senderId}")
    public void makeTransfer(@Valid @PathVariable int senderId, @RequestBody Transfer transfer, Principal principal){
        int receiverId = userDao.findIdByUsername(transfer.getUserName());
        BigDecimal balance = transfer.getAmount();
        if (balance.compareTo(accountDao.findByUserID(senderId).getBalance()) >= 0){
            transferDao.makeTransfer(senderId,receiverId,balance);

        }

    }
}