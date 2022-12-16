package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcUserDao userdao;
    private JdbcAccountDao accountdao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userdao = new JdbcUserDao(jdbcTemplate);
        accountdao = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void createAccountTest() {
        boolean userCreated = userdao.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        Account testAcccount = accountdao.findByUserID(userdao.findByUsername("TEST_USER").getId());
        // Will pass if ran as group with other test, one of if ran locally in JdbcAccountTest
        Assert.assertEquals(2004, testAcccount.getId());
    }

    @Test
    public void findByUserID_should_return_proper_userID() {
        Account testAccount = accountdao.findByUserID(1001);
        int expected = 2001;
        Assert.assertEquals(expected, testAccount.getId());
    }

    @Test
    public void findIDByUserID_should_return_proper_accountID() {
        int expected = 2002;
        Assert.assertEquals(expected, accountdao.findIdByUserID(1002));
    }
}