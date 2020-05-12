package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

/**
 * @author brian
 * @date 2020/5/8 - 19:05
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    //ResultInfo为一个实体类，用来封装业务处理的结果信息,方便转化成json返回给前端
    //根据返回的结果不同，在ResultInfo中封装不同的信息
    private ResultInfo resultInfo = new ResultInfo();

    @Override
    public ResultInfo registUser(User user) {
        //第一步：判断此用户是否已经存在
        User u = userDao.fingByUsername(user.getUsername());

        if(u != null){
            //用户已经存在了，注册失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名已存在！");
            return resultInfo;
        }else{
            //用户不存在，注册成功
            resultInfo.setFlag(true);
            //第二步：用户不存在的情况下，向数据库增加此用户
            //status属性用来标识用户账户的激活状态，首次注册为N，激活后为Y
            //code属性用来给客户随机生成一个与它账户一一对应的验证码，用做激活时标识身份
            user.setStatus("N");
            String code = UuidUtil.getUuid();
            user.setCode(code);
            userDao.save(user);
            //设置邮件的正文内容，将code拼接到链接的后面，用来标识用户的身份
            String content = "恭喜您注册成功！" +
                    "请点击<a href='http://localhost:8080/travel/user/active?code="+ code +"'>激活</a>" +
                    "您的账户！";
            MailUtils.sendMail(user.getEmail(),content,"激活邮件");
            return resultInfo;
        }
    }

    @Override
    public ResultInfo active(String code) {
        //调用dao层
        //1、根据code查询是否存在此用户
        User user = userDao.findByCode(code);
        //2、如果存在，修改此用户的status值为Y
        if(user != null){
            //用户存在,修改status
            int result = userDao.updateStatus(user);
            //修改成功
            if(result > 0){
                resultInfo.setFlag(true);
                return resultInfo;
            }
        }
        //用户不存在或者修改失败
        resultInfo.setFlag(false);
        return resultInfo;
    }

    @Override
    public ResultInfo login(User user) {
        //根据user中的信息判断
        //1、是否存在该用户，校验用户名和密码
        User u = userDao.findByUser(user);
        //2、如果存在此用户，则判断该用户是否已经进行了激活
        ResultInfo resultInfo = new ResultInfo();
        if(u != null){
            //存在此用户，进一步判断该用户是由进行了激活
            if(u.getStatus().equalsIgnoreCase("Y")){
                //已经进行了激活，登录成功
                resultInfo.setFlag(true);
                return resultInfo;
            }else{
                //没有进行激活，登录失败，提示进行激活
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("您的账户没有被激活！");
                return resultInfo;
            }
        }
        //没有此用户，提示用户名或者密码错误
        resultInfo.setFlag(false);
        resultInfo.setErrorMsg("用户名或者密码错误!");
        return resultInfo;
    }
}
