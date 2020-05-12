package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.util.CustomUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet",value = "/category/*")
public class CategoryServlet extends BaseServlet {
    //创建一个CategoryService成员变量，供所有的servlet方法使用
    private CategoryService categoryService = new CategoryServiceImpl();

    //代表查询所有的servlet
    public void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用findAll方法，获得所有的分类信息
        List<Category> categories =  categoryService.findAll();
        //调用toJson方法，将获得的所有的分类信息转化成json字符串
        String json = CustomUtils.toJson(categories);
        //将得到的json返回给前端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(json);
    }
}
