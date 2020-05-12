package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author brian
 * @date 2020/5/10 - 15:17
 */
@WebServlet(name = "FindUserServlet",value = "/findUser")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取当前登录的用户
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        //从user中获取当前用户的用户名
        if(user != null){
            String username = user.getUsername();
            //将此用户名返回给前端
            response.getWriter().print(username);
        }else{
            response.getWriter().print("请登录！");
        }
    }
}
