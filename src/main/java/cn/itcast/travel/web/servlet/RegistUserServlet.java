package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author brian
 * @date 2020/5/8 - 18:54
 *
 * 处理注册请求
 *
 */
@WebServlet(name = "RegistUserServlet", value = "/regist")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = CustomUtils.checkCheckCode(request);
        if(resultInfo.getFlag() == true){
            //用户输入的验证码正确，调用service验证用户名是否已经存在
            User user = CustomUtils.paraToUser(request);
            //3、调用service完成注册业务，并接收响应
            UserService userService = new UserServiceImpl();
            resultInfo = userService.registUser(user);
        }
        //将resultInfo序列化为json
        String json = CustomUtils.infoToJson(resultInfo);
        //将得到的json返回给前端
        //设置响应内容为application/json
        response.setContentType("application/json;charset=utf-8");
        //因为响应的是一个字符串，所以建议使用字符流输出，
        //但是不能使用println方法，因为换行符也会被返回回去
        response.getWriter().print(json);
    }
}
