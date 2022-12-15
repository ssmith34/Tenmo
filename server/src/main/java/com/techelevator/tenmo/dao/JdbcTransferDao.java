package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> allTransfers() {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "Select transfer_id, sender, receiver, amount, transfer_date, status from transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int userId) {
        return null;
    }

    @Override
    public Transfer makeTransfer(int senderAccountId, Transfer transfer) {
       BigDecimal zero = new BigDecimal("0");
       // Should not be able to send yourself money
        if (senderAccountId == transfer.getReceiverAccountId()){
            return null;
        }
        // Should not be able to send zero or negative money
        if (transfer.getAmount().compareTo(zero) <= 0) {
            return null;
        }
        Integer newTransferId;
        String sql = "INSERT INTO transfer (sender, receiver, amount, transfer_date, status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountId,
                transfer.getReceiverAccountId(),
                transfer.getAmount(), LocalDateTime.now(), "approved");
        transfer.setId(newTransferId);
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setSenderAccountId(results.getInt("sender"));
        transfer.setReceiverAccountId(results.getInt("receiver"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try{
            transfer.setTransferDate(results.getDate("transfer_date").toLocalDate());
        }catch (NullPointerException e) {}
        transfer.setStatus(results.getString("status"));
        return transfer;
    }
}
