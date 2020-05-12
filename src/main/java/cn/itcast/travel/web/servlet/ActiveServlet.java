package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.Location;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author brian
 * @date 2020/5/10 - 11:55
 *
 * 处理激活请求
 */
@WebServlet(name = "ActiveServlet",value = "/active")
public class ActiveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取验证码
        String code = request.getParameter("code");
        //判断验证码是否存在
        UserService userService = new UserServiceImpl();
        //业务层专门处理激活的方法
        ResultInfo result = userService.active(code);
        //如果验证码存在，则修改此用户的status字段的值
        if(result.getFlag() == true){
            //激活成功,跳转到激活成功的页面
            request.getRequestDispatcher("/active_ok.html").forward(request,response);
        }else{
            //激活失败，跳转到激活失败的页面
            request.getRequestDispatcher("/active_no.html").forward(request,response);
        }
    }
}
