package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private JdbcUserDao userDao;
    private JdbcAccountDao accountDao;
    private JdbcTransferDao transferDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        accountDao = new JdbcAccountDao(jdbcTemplate);
        transferDao = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void allTransfers_should_return_a_list_of_transfers() {
        int actualLength = transferDao.allTransfers().size();
        int expectedLength = 2;
        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void getTransfer_should_return_correct_transfer() {
        Transfer expected = new Transfer(3001, 2001, 2002, new BigDecimal("250.00"),
                LocalDate.of(2022,12,16),
                "Approved");
        Transfer actual = transferDao.getTransfer(3001);
        assertTransfersMatch(expected, actual);
    }

    @Test
    public void makeTransfer_should_return_updated_transfer_object() {
        Account senderTest = accountDao.findByUserID(1001);
        Transfer transferTest = new Transfer();
        transferTest.setSenderAccountId(2001);
        transferTest.setReceiverAccountId(2002);
        transferTest.setAmount(new BigDecimal("200.00"));

        transferDao.makeTransfer(senderTest, transferTest);
        Assert.assertEquals(3003, transferTest.getId());
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getSenderAccountId(), actual.getSenderAccountId());
        Assert.assertEquals(expected.getReceiverAccountId(), actual.getReceiverAccountId());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getTransferDate(), actual.getTransferDate());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }
}
