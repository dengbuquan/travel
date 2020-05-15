package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    //查询线路的总条数
    int findTotalCount(int cid,String rname);
    //根据分页的信息查询符合条件的线路集合
    List<Route> findByPage(int cid,int start,int pageSize,String rname);
}
