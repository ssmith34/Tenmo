package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RequestDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
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
    public TransferDTO getTransfer(int transferId) {
        TransferDTO transferDTO = null;
        String sql = "SELECT transfer_id, sender, receiver, amount, transfer_date, " +
                "status FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transferDTO = mapRowToTransferDTO(results);
        }
        return transferDTO;
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
            withdrawFromAccount(transfer);
            depositToAccount(transfer);
        }catch(NullPointerException e){
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

    @Override
    public TransferDTO[] getHistory(int accountID) {
        ArrayList<TransferDTO> transferDTOList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.sender, t.receiver, t.amount, t.transfer_date, " +
                "t.status, u.user_id, u.username FROM tenmo_user as u LEFT OUTER JOIN account as a " +
                "ON u.user_id = a.user_id LEFT OUTER JOIN transfer as t ON a.account_id = t.sender " +
                "WHERE sender = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
        while (results.next()) {
            TransferDTO transferDTO = mapRowToTransferDTO(results);
            transferDTOList.add(transferDTO);
        }
        TransferDTO[] transferDTOs = new TransferDTO[transferDTOList.size()];
        transferDTOs = transferDTOList.toArray(transferDTOs);
        return transferDTOs;
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

    private TransferDTO mapRowToTransferDTO (SqlRowSet results) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(results.getInt("transfer_id"));
        transferDTO.setSenderAccountId(results.getInt("sender"));
        transferDTO.setSenderName(results.getString("username"));
        transferDTO.setReceiverAccountId(results.getInt("receiver"));
        transferDTO.setAmount(results.getBigDecimal("amount"));
        return transferDTO;
    }
}
