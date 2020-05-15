package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid,String rname) {
        //因为要实现前台的搜索功能，所以前台发送过来的数据可能只包含rname，也可能只包含cid，所以要进行判断
        //根据情况进行sql和参数的匹配
        //使用baseSql和StringBuilder进行sql的拼接
        String baseSql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(baseSql);
        List list = new ArrayList();
        //此处的0表示任意的意思，代表着前台没有发送过来cid参数，查询的时候也不需要加上cid的条件
        if(cid != 0){
            list.add(cid);
            sb.append(" and cid = ? ");
        }
        if(rname != null && rname.length() != 0 && !"null".equalsIgnoreCase(rname)){
            //代表rname中有前台传过来的值，需要进行模糊查询
            sb.append(" and rname like ? ");
            list.add("%"+ rname +"%");
        }
        //将StringBuilder转换成我们需要的String类型的sql语句
        String sql = sb.toString();
        Object[] params = list.toArray();
        return jdbcTemplate.queryForObject(sql,Integer.class,params);
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //整个的改写思路同上。在进行参数拼接的时候要保证参数在集合中的插入顺序，所以不能使用set集合
        String baseSql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(baseSql);
        //使用List集合实现参数的拼接
        List list = new ArrayList();
        //此处的0表示任意的意思，代表着前台没有发送过来cid参数，查询的时候也不需要加上cid的条件
        if(cid != 0){
            list.add(cid);
            sb.append(" and cid = ? ");
        }
        if(rname != null && rname.length() != 0 && !"null".equalsIgnoreCase(rname)){
            sb.append(" and rname like ? ");
            list.add("%"+ rname +"%");
        }
        sb.append(" limit ? , ?");
        list.add(start);
        list.add(pageSize);
        String sql = sb.toString();
        //将List集合转换成参数需要的数组类型
        Object[] params = list.toArray();
        List<Route> routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params);
        return routes;
    }
}
