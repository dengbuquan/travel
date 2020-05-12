package cn.itcast.travel.util;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author brian
 * @date 2020/5/10 - 16:21
 */
public class CustomUtils {
    public static ResultInfo checkCheckCode(HttpServletRequest request){
        //获取前台发送过来的验证码
        String check = request.getParameter("check");
        //获取session中存储的验证码（验证码servlet中进行了验证码的操作）
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //清除session中的验证码，保证每一个验证码仅能使用一次
        session.removeAttribute("CHECKCODE_SERVER");
        //判断两个验证码是否相同,并把结果封装到resultInfo中
        ResultInfo resultInfo = new ResultInfo();
        if(check==null || !check.equalsIgnoreCase(checkcode_server)){
            //用户输入的验证码错误，返回验证码错误提示信息
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误！");
        }else{
            resultInfo.setFlag(true);
        }
        return resultInfo;
    }

    public static User paraToUser(HttpServletRequest request){
        //获取前端发送过来的数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(parameterMap != null){
            User user = new User();
            try {
                //org.apache.commons.beanutils.BeanUtils,
                //其中的populate方法可以把对象的属性名和Map集合进行匹配，完成对象的封装
                BeanUtils.populate(user, parameterMap);
                return user;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (
                    InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String toJson(Object obj){
        //使用jackson将obj序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
