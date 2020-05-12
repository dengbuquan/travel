package cn.itcast.travel.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author brian
 * @date 2020/5/10 - 15:36
 */
@WebServlet(name = "ExitServlet",value = "/exit")
public class ExitServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //第一步：清除session中的user
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        //第二步：清除cookie
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if("userinfo".equalsIgnoreCase(name)){
                    cookie.setMaxAge(0);
                }
            }
        }
        //第三步：将请求转发到登录页面
        response.sendRedirect("/travel/login.html");
    }
}
