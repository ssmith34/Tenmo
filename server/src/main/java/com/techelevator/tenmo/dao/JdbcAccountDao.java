package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account findByUserID(int userID) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public int findIdByUserID(int userID) {
        int accountID = 0;
        String sql = "SELECT account_id FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);
        if (results.next()) {
            accountID = results.getInt("account_id");
        }
        return accountID;
    }

    @Override
    public boolean create(int userID, BigDecimal balance) {
        return false;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setId(results.getInt("account_id"));
        account.setUserID(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
