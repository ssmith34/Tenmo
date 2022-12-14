package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Transfer> allTransfers() {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "Select transfer_id, sender, receiver, amount, transfer_date, status " +
                "from transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);

        }

        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setSenderId(results.getInt("sender"));
        transfer.setRecieverId(results.getInt("receiver"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try{
            transfer.setTransferDate(results.getDate("transfer_date").toLocalDate());


        }catch (NullPointerException e) {
        }

        transfer.setStatus(results.getString("status"));

        return transfer;
    }


    @Override
    public Transfer getTransfer(int userId) {
        return null;
    }

    @Override
    public boolean makeTransfer(int senderId, int receiverId, BigDecimal transferAmount) {
       BigDecimal zero = new BigDecimal("0");
        if (senderId == receiverId){
            return false;
        }
        if (transferAmount.compareTo(zero) <= 0) {
            return false;
        }

        if (se)
        return false;
    }
}
