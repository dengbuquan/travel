package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.CustomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author brian
 * @date 2020/5/10 - 13:43
 */
@WebServlet(name = "LoginServlet",value = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = CustomUtils.checkCheckCode(request);
        if(resultInfo.getFlag() == true){
            User user = CustomUtils.paraToUser(request);
            if(user != null){
                //调用service中登录的方法
                UserService userService = new UserServiceImpl();
                resultInfo = userService.login(user);
                //如果登录的结果为true，那么就把用户名和密码存入到session中，
                //并且给用户发送cookie，然后通过过滤器实现自动登录
                if(resultInfo.getFlag() == true){
                    HttpSession session = request.getSession();
                    //在session中保存user，当session中保存有user时，表示用户已经登录
                    session.setAttribute("user",user);
                    //创建cookie
                    String username = user.getUsername();
                    String password = user.getPassword();
                    Cookie cookie = new Cookie("userinfo",username+"#"+password);
                    cookie.setMaxAge(60*10);
                    cookie.setPath("/travel");
                    response.addCookie(cookie);
                }
            }
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
