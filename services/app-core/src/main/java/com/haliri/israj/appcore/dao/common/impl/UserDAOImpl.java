package com.haliri.israj.appcore.dao.common.impl;

import com.haliri.israj.appcore.utils.AppUtils;
import com.haliri.israj.appcore.dao.common.UserDAO;
import com.haliri.israj.appcore.domain.common.Role;
import com.haliri.israj.appcore.domain.common.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by israjhaliri on 8/28/17.
 */
@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getDataById(Object id) {
        String sql = "SELECT * FROM CONFIG.USERS WHERE ID= ?";
        String sqlRole = "SELECT ur.user_id,r.role,r.id FROM CONFIG.user_roles ur JOIN CONFIG.role r on ur.role_id = r.id WHERE USER_ID = ?";
        try {
            User user = (User) jdbcTemplate.queryForObject(sql, new Object[]{id.toString()}, new BeanPropertyRowMapper(User.class));
            List<Role> roles = jdbcTemplate.query(sqlRole, new Object[]{id.toString()}, new BeanPropertyRowMapper(Role.class));

            user.setRoles(roles);
            AppUtils.getLogger(this).debug("USERS LOG : {}", user.toString());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return new User();
        }
    }

    @Override
    public User getData() {
        return null;
    }

    @Override
    public List<User> getListData() {
        return null;
    }

    @Override
    public User getDataByParameters(Object parameters) {
        return null;
    }

    @Override
    public List<User> getListDataByParameters(Object parameters) {
        return null;
    }

    @Override
    @Transactional
    public void saveData(User user) {
        String sql = "insert into config.users (id,password,enable,username) values (?,?,?)";
        jdbcTemplate.update(sql, new Object[]{user.getId(), user.getPassword(), user.getEnable(),user.getUsername()});
        String sqlRole = "insert into config.role (role,user_id) values (?,?,?)";
        for (Role role : user.getRoles()) {
            jdbcTemplate.update(sqlRole, new Object[]{role.getRole(), role.getUserId()});
        }
    }

    @Override
    public void updateData(User parameters) {

    }

    @Override
    public void saveOrUpdate(User parameters) {

    }

    @Override
    public void deleteData(Object id) {

    }

    @Override
    public void saveToken(String token, String username) {
        String sql = "update config.users set token =? where id = ?";
        jdbcTemplate.update(sql, new Object[]{token, username});
    }

    @Override
    public void deleteToken(String username) {
        String sql = "update config.users set token = '' where id = ?";
        jdbcTemplate.update(sql, new Object[]{username});
    }
}