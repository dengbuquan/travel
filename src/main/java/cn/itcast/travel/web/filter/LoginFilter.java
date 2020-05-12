package cn.itcast.travel.web.filter;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author brian
 * @date 2020/5/10 - 16:43
 */
@WebFilter(filterName = "LoginFilter",value = "/login.html")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        HttpSession session = request.getSession();
        //从session中获取user，如果不为空，代表已经登录
        User user = (User)session.getAttribute("user");
        if(user != null){
            response.sendRedirect("/travel/index.html");
        }else{
            //如果session中没有user，代表是一个新的会话，此时查看是否有cookie
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    //查找到名称为userinfo的cookie，不存在就放行
                    if("userinfo".equalsIgnoreCase(name)){
                        String value = cookie.getValue();
                        String[] split = value.split("#");
                        String username = split[0];
                        String password = split[1];
                        UserService userService = new UserServiceImpl();
                        User user1 = new User();
                        user1.setUsername(username);
                        user1.setPassword(password);
                        ResultInfo resultInfo = userService.login(user1);
                        //防止用户修改cookie，如果cookie正确，则把此用户保存到session中
                        //然后重定向到首页
                        if(resultInfo.getFlag() == true){
                            session.setAttribute("user",user1);
                            response.sendRedirect("/travel/index.html");
                        }
                    }
                }
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
