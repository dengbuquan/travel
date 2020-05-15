package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import cn.itcast.travel.util.CustomUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RouteServlet",value = "/route/*")
public class RouteServlet extends BaseServlet {
    //第一步：处理前端发送的分页查询请求
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //前端发送过来三个数据：类别id，当前页面和每页展示的信息条数
        String cidStr = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        //因为rname是使用get请求发送过来的中文数据，在tomcat7的环境下需要进行转码
        //意思就是把前端发送过来的数据以iso-8859-1转回二进制然后再以utf-8转成字符串
        if(rname != null && rname.length() != 0 && !"null".equalsIgnoreCase(rname)){
            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        }
        //因为用户首次发送请求时，currentPageStr和pageSizeStr为空，
        //此时我们需要给它们一个默认值
        if(cidStr == null || cidStr.length() == 0 || "null".equalsIgnoreCase(cidStr)){
            //此处的0表示任意的意思，代表着前台没有发送过来cid参数，查询的时候也不需要加上cid的条件
            cidStr = "0";
        }
        if(currentPageStr == null || currentPageStr.length() == 0 || "null".equalsIgnoreCase(currentPageStr)){
            //默认起始页为首页
            currentPageStr = "1";
        }
        if(pageSizeStr == null || pageSizeStr.length() == 0){
            //默认每页的展示条数为5
            pageSizeStr = "5";
        }
        //处理上述的三个数据
        //将类别id，当前页面和每页展示的信息条数转换成int类型
        Integer cid = Integer.valueOf(cidStr);
        Integer currentPage = Integer.valueOf(currentPageStr);
        Integer pageSize = Integer.valueOf(pageSizeStr);

        //第二步：调用业务层完成数据的获取
        RouteService routeService = new RouteServiceImpl();
        PageBean<Route> pageBean = routeService.pageQuery(cid,currentPage,pageSize,rname);
        //第三步：将业务层获取到的数据转化成json
        String json = CustomUtils.toJson(pageBean);
        //第四步：将json返回给前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
