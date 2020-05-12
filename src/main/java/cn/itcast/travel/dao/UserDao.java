package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

/**
 * @author brian
 * @date 2020/5/8 - 19:07
 */
public interface UserDao {
    //根据用户名查询一个用户的全部信息
    User fingByUsername(String username);

    //保存用户的信息
    void save(User user);

    //根据验证码查询一个用户的信息
    User findByCode(String code);

    //修改用户的status
    int updateStatus(User user);

    //根据用户名和密码查询是否存在某用户
    User findByUser(User user);
}
