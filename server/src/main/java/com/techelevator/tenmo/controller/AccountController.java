package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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

    @GetMapping(path = "/all-users")
    public List<User> userList() {
        return userDao.findAll();
    }

    @PostMapping(path="/transfer/{receiverId}")
    public void makeTransfer(@Valid @PathVariable int receiverId, @RequestBody Transfer transfer, Principal principal){
        int senderId = userDao.findIdByUsername(principal.getName());
        BigDecimal transferAmount = transfer.getAmount();
        if (transferAmount.compareTo(accountDao.findByUserID(senderId).getBalance()) >= 0){
            transferDao.makeTransfer(senderId, receiverId, transferAmount);
        }
    }
}