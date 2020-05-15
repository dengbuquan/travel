package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    //分页查询业务
    PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String rname);
}
