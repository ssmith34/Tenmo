package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RequestDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDisplayDTO;
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
    public TransferDisplayDTO getTransferByID(int transferId) {
        TransferDisplayDTO transferDetails = null;
        String sql = "SELECT t.transfer_id, t.sender, t.receiver, t.amount, t.transfer_date, t.transfer_type, t" +
                ".status, afrom.user_id " +
                "as from_userid, ato.user_id as to_userid, ufrom.username as from_username, uto.username as " +
                "to_username FROM transfer as t JOIN account as afrom ON t.sender = afrom.account_id JOIN account as " +
                "ato ON t.receiver = ato.account_id JOIN tenmo_user as ufrom ON afrom.user_id = ufrom.user_id JOIN " +
                "tenmo_user as uto ON ato.user_id = uto.user_id WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transferDetails = mapRowToTransferDisplayDTO(results);
        }
        return transferDetails;
    }

    @Override
    public Transfer sendMoney(Transfer transfer) {
        Integer newTransferId;
        String sql = "INSERT INTO transfer (sender, receiver, amount, transfer_date, transfer_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getSenderAccountId(),
                    transfer.getReceiverAccountId(),
                    transfer.getAmount(), LocalDate.now(), "Send", "approved");
            transfer.setId(newTransferId);
            withdrawFromAccount(transfer);
            depositToAccount(transfer);
        }catch(NullPointerException e){
            return null;
        }
        return transfer;
    }

    @Override
    public Transfer requestMoney(Transfer transfer) {
        Integer newTransferId;
        String sql = "INSERT INTO transfer (sender, receiver, amount, transfer_date, transfer_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getSenderAccountId(),
                    transfer.getReceiverAccountId(), transfer.getAmount(), LocalDate.now(), "" "pending");
            transfer.setId(newTransferId);
        } catch (NullPointerException e) {
            return null;
        }
        return transfer;
    }

    public void depositToAccount(Transfer transfer) {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getReceiverAccountId());
    }

    private void withdrawFromAccount(Transfer transfer) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getSenderAccountId());
    }

    @Override
    public TransferDisplayDTO[] getHistory(int accountID) {
        ArrayList<TransferDisplayDTO> displayTransferList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.sender, t.receiver, t.amount, t.transfer_date, t.status, t" +
                ".transfer_type, afrom.user_id as from_userid, ato.user_id as to_userid, ufrom.username as from_username, uto.username as " +
                "to_username FROM transfer as t JOIN account as afrom ON t.sender = afrom.account_id JOIN account as " +
                "ato ON t.receiver = ato.account_id JOIN tenmo_user as ufrom ON afrom.user_id = ufrom.user_id JOIN " +
                "tenmo_user as uto ON ato.user_id = uto.user_id WHERE sender = ? OR receiver = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID, accountID);
        while (results.next()) {
            TransferDisplayDTO transferDisplayDTO = mapRowToTransferDisplayDTO(results);
            displayTransferList.add(transferDisplayDTO);
        }
        TransferDisplayDTO[] transferDisplayArray = new TransferDisplayDTO[displayTransferList.size()];
        transferDisplayArray = displayTransferList.toArray(transferDisplayArray);
        return transferDisplayArray;
    }

    public RequestDTO[] getPendingRequests(int accountID) {
        ArrayList<RequestDTO> pendingList = new ArrayList<>();
        String sql = "SELECT transfer_id, sender, amount, transfer_date, status FROM transfer " +
                "WHERE receiver = ? AND status = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID, "pending");
        while (results.next()) {
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setId(results.getInt("transfer_id"));
            requestDTO.setSenderAccountId(results.getInt("sender"));
            requestDTO.setAmount(results.getBigDecimal("amount"));
            try {
                requestDTO.setTransferDate(results.getDate("transfer_date").toLocalDate());
            } catch (NullPointerException e) {
                requestDTO = null;
            }
            requestDTO.setStatus(results.getString("status"));
            pendingList.add(requestDTO);
        }
        RequestDTO[] pendingRequest = new RequestDTO[pendingList.size()];
        pendingRequest = pendingList.toArray(pendingRequest);
        return pendingRequest;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setSenderAccountId(results.getInt("sender"));
        transfer.setSenderName(results.getString("from_username"));
        transfer.setReceiverAccountId(results.getInt("receiver"));
        transfer.setReceiverName(results.getString("receiver_name"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try{
            transfer.setTransferDate(results.getDate("transfer_date").toLocalDate());
            transfer.setStatus(results.getString("status"));
        }catch (NullPointerException e) {
            transfer = null;
        }
        return transfer;
    }

    private TransferDisplayDTO mapRowToTransferDisplayDTO(SqlRowSet results) {
        TransferDisplayDTO transferDisplayDTO = new TransferDisplayDTO();
        transferDisplayDTO.setTransferID(results.getInt("transfer_id"));
        transferDisplayDTO.setSenderAccountID(results.getInt("sender"));
        transferDisplayDTO.setSenderUserID(results.getInt("from_userid"));
        transferDisplayDTO.setSenderUsername(results.getString("from_username"));
        transferDisplayDTO.setReceiverAccountID(results.getInt("receiver"));
        transferDisplayDTO.setReceiverUserID(results.getInt("to_userid"));
        transferDisplayDTO.setReceiverUsername(results.getString("to_username"));
        transferDisplayDTO.setTransferAmount(results.getBigDecimal("amount"));
        try{
            transferDisplayDTO.setTransferDate(results.getDate("transfer_date").toLocalDate());
            transferDisplayDTO.setTransferType(results.getString("transfer_type"));
            transferDisplayDTO.setStatus(results.getString("status"));
        }catch (NullPointerException e) {
            transferDisplayDTO = null;
        }
        return transferDisplayDTO;
    }
}
