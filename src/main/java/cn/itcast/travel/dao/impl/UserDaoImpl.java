package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import cn.itcast.travel.util.UuidUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author brian
 * @date 2020/5/8 - 19:09
 */
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User fingByUsername(String username) {
        String sql = "select * from tab_user where username=?";
        User user = null;
        try {
            //注意：jdbcTemplate.queryForObject方法在查询不到用户时，会抛异常，而不是返回null
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException:没有查找到此用户！");
        }
        return user;
    }

    @Override
    public void save(User user) {
        //写sql语句的时候要注意，因为数据库中tab_user表内一共有十个字段，所以要写清楚是为哪些字段赋的值
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        Object[] objects = {user.getUsername(),user.getPassword(),
                user.getName(),user.getBirthday(),user.getSex(),
                user.getTelephone(),user.getEmail(),
                user.getStatus(),user.getCode()};
        jdbcTemplate.update(sql, objects);
    }

    @Override
    public User findByCode(String code) {
        String sql = "select * from tab_user where code = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException:没有查找到此用户！");
        }
        return user;
    }

    @Override
    public int updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid = ?";
        int i = jdbcTemplate.update(sql, user.getUid());
        return i;
    }

    @Override
    public User findByUser(User user) {
        String sql = "select * from tab_user where username=? && password=?";
        User u = null;
        try {
            u = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername(), user.getPassword());
        } catch (DataAccessException e) {
            System.out.println("DataAccessException:没有查找到此用户！");
        }
        return u;
    }
}
