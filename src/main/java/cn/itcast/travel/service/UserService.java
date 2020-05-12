package cn.itcast.travel.service;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;

/**
 * @author brian
 * @date 2020/5/8 - 19:04
 */
public interface UserService {
    //处理注册用户业务
    ResultInfo registUser(User user);
    //处理激活账户业务
    ResultInfo active(String code);
    //处理登录账户业务
    ResultInfo login(User user);
}
