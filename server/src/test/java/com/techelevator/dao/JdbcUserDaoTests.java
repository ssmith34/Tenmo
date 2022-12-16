package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertNotEquals("Username was different than created.", "TEST_USER1", user.getUsername());
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void findAll_should_return_all_current_users_in_database() {
        int expected = 2;
        Assert.assertEquals(expected, sut.findAll().size());
    }

    @Test
    public void findIDByUsername_should_return_userID_for_given_username() {
        int expected = 1001;
        Assert.assertEquals(expected, sut.findIdByUsername("bob"));
    }

    @Test
    public void findByUsername_should_return_user_object_based_on_username() {
        User testUser = sut.findByUsername("bob");
        Assert.assertEquals(1001, testUser.getId());
        Assert.assertEquals("bob", testUser.getUsername());
        Assert.assertEquals("$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", testUser.getPassword());
        Assert.assertTrue("true", testUser.isActivated());
        Assert.assertEquals("[Authority{name=ROLE_USER}]", testUser.getAuthorities().toString());
    }

    @Test
    public void mapRowToUser_should_return_object_from_SqlRowSet() {
        User createdObject = new User();
        createdObject.setId(1001);
        createdObject.setUsername("bob");
        createdObject.setPassword("$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2");
        createdObject.setActivated(true);
        createdObject.setAuthorities("USER");
        Assert.assertEquals(createdObject, sut.findByUsername("bob"));
    }
}