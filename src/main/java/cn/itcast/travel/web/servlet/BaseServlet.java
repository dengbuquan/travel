package cn.itcast.travel.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 此类的目的在于改变tomcat调用的方法，进而减少servlet的书写数量
 */
public class BaseServlet extends HttpServlet {

    //覆盖HttpServlet中的service方法，然后让对应的业务servlet实现此类
    //使得tomcat调用的方法不再是doget和dopost，而是我们希望它调用的方法。
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径
        String requestURI = req.getRequestURI();
        //从请求路径中解析处此请求对应的方法
        String methodName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        //利用反射得到此方法名对应的方法
        try {
            //利用反射得到方法时的参数：方法名、形参列表的类对象
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //执行得到的方法,参数：执行此方法的对象，方法的实参
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
