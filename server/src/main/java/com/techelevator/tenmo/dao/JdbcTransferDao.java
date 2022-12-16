package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> allTransfers() {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfer_id, sender, receiver, amount, transfer_date, status FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, sender, receiver, amount, transfer_date, " +
                "status FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Transfer makeTransfer(Account senderAccount, Transfer transfer) {
        Integer newTransferId;
        String sql = "INSERT INTO transfer (sender, receiver, amount, transfer_date, status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, senderAccount.getId(), transfer.getReceiverAccountId(),
                    transfer.getAmount(), LocalDate.now(), "approved");
            transfer.setId(newTransferId);
            updateTransfer(senderAccount,transfer);
        }catch(NullPointerException e){
            return null;
        }
        return transfer;
    }

    @Override
    public Transfer requestTransfer(Account requestingAccount, Transfer transfer) {
        Integer newTransferId;
        String sql = "INSERT INTO transfer (sender, receiver, amount, transfer_date, status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, requestingAccount.getId(), transfer.getReceiverAccountId(), transfer.getAmount(), LocalDate.now(), "pending");
            transfer.setId(newTransferId);
        } catch (NullPointerException e) {
            return null;
        }
        return transfer;
    }

    private void updateTransfer(Account senderAccount, Transfer transfer) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getSenderAccountId());

        sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getReceiverAccountId() );
    }

    @Override
    public List<Transfer> getHistory(int accountID) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, sender, receiver, amount, transfer_date, status " +
                "FROM transfer WHERE sender = ? OR receiver = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID, accountID);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setSenderAccountId(results.getInt("sender"));
        transfer.setReceiverAccountId(results.getInt("receiver"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try{
            transfer.setTransferDate(results.getDate("transfer_date").toLocalDate());
            transfer.setStatus(results.getString("status"));
        }catch (NullPointerException e) {
            transfer = null;
        }
        return transfer;
    }
}
