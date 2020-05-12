package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.CustomUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//使用/user/*作为此servlet的定位符，使得所有应该访问user类型的servlet的请求，都访问此servlet
//因为此类继承了BaseServlet，所以它得到的service方法已经被重写了，可以实现根据请求URI的不同，调用不同的方法
@WebServlet(name = "UserServlet",value = "/user/*")
public class UserServlet extends BaseServlet {
    //代替RegistUserServlet
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo resultInfo = CustomUtils.checkCheckCode(req);
        if(resultInfo.getFlag() == true){
            //用户输入的验证码正确，调用service验证用户名是否已经存在
            User user = CustomUtils.paraToUser(req);
            //3、调用service完成注册业务，并接收响应
            UserService userService = new UserServiceImpl();
            resultInfo = userService.registUser(user);
        }
        //将resultInfo序列化为json
        String json = CustomUtils.infoToJson(resultInfo);
        //将得到的json返回给前端
        //设置响应内容为application/json
        resp.setContentType("application/json;charset=utf-8");
        //因为响应的是一个字符串，所以建议使用字符流输出，
        //但是不能使用println方法，因为换行符也会被返回回去
        resp.getWriter().print(json);
    }
    //代替ActiveServlet
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取验证码
        String code = req.getParameter("code");
        //判断验证码是否存在
        UserService userService = new UserServiceImpl();
        //业务层专门处理激活的方法
        ResultInfo result = userService.active(code);
        //如果验证码存在，则修改此用户的status字段的值
        if(result.getFlag() == true){
            //激活成功,跳转到激活成功的页面
            req.getRequestDispatcher("/active_ok.html").forward(req,resp);
        }else{
            //激活失败，跳转到激活失败的页面
            req.getRequestDispatcher("/active_no.html").forward(req,resp);
        }
    }
    //代替LoginServlet
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo resultInfo = CustomUtils.checkCheckCode(req);
        if(resultInfo.getFlag() == true){
            User user = CustomUtils.paraToUser(req);
            if(user != null){
                //调用service中登录的方法
                UserService userService = new UserServiceImpl();
                resultInfo = userService.login(user);
                //如果登录的结果为true，那么就把用户名和密码存入到session中，
                //并且给用户发送cookie，然后通过过滤器实现自动登录
                if(resultInfo.getFlag() == true){
                    HttpSession session = req.getSession();
                    //在session中保存user，当session中保存有user时，表示用户已经登录
                    session.setAttribute("user",user);
                    //创建cookie
                    String username = user.getUsername();
                    String password = user.getPassword();
                    Cookie cookie = new Cookie("userinfo",username+"#"+password);
                    cookie.setMaxAge(60*10);
                    cookie.setPath("/travel");
                    resp.addCookie(cookie);
                }
            }
        }
        //将resultInfo序列化为json
        String json = CustomUtils.infoToJson(resultInfo);
        //将得到的json返回给前端
        //设置响应内容为application/json
        resp.setContentType("application/json;charset=utf-8");
        //因为响应的是一个字符串，所以建议使用字符流输出，
        //但是不能使用println方法，因为换行符也会被返回回去
        resp.getWriter().print(json);
    }
    //代替FindUserServlet
    public void find(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取当前登录的用户
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");
        //从user中获取当前用户的用户名
        if(user != null){
            String username = user.getUsername();
            //将此用户名返回给前端
            resp.getWriter().print(username);
        }else{
            resp.getWriter().print("请登录！");
        }
    }
    //代替ExitServlet
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //第一步：清除session中的user
        HttpSession session = req.getSession();
        session.removeAttribute("user");
        //第二步：清除cookie
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if("userinfo".equalsIgnoreCase(name)){
                    cookie.setMaxAge(0);
                }
            }
        }
        //第三步：将请求转发到登录页面
        resp.sendRedirect("/travel/login.html");
    }

}
