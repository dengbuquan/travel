package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    //因为分类的信息每一次刷新页面都会进行查询，但是它又不会经常发生改变，
    //所以可以加入redis缓存进行性能的优化。
    //事实证明，加了缓存后，页面的刷新速度快了很多，并且因为使用了sortedset进行了排序，页面的展示顺序也和数据库一致了
    //这里因为只是简单的一点数据，所以没有对redis中的数据进行持久化的存储，如果要使用，建议使用RDB。
    @Override
    public List<Category> findAll() {
        //第一步：获取jedis对象，用来操作redis数据库
        Jedis jedis = JedisUtil.getJedis();
        //第二步：从redis中获取category的数据
        //此处要注意的是，我们希望页面展示的分类信息的顺序能够和数据库中cid的顺序一致
        //所以使用sortedset类型category数据的保存,所以使用zrange进行数据的获取
        Set<String> categories = jedis.zrange("category", 0, -1);
        //判断得到的categories中是否为空，或者有没有数据
        //创建一个categoryList对象，用来保存最后返回的数据
        List<Category> categoryList = null;
        if(categories == null || categories.size() == 0){
            //加入一个日志，表示数据的来源
            System.out.println("redis中没有数据，正在MySql中查询。。。");
            //如果其中没有任何的数据或者redis中没有此键值对，则代表是首次进行的查询
            //那么我们就需要从MySql中进行数据的读取
            categoryList = categoryDao.findAll();
            //将得到的数据使用sortedset的形式存入到redis中
            for (Category category : categoryList) {
                //因为我们希望页面展示的顺序和数据库中的顺序一致，所以使用Cid来作为排序的分数
                jedis.zadd("category",category.getCid(),category.getCname());
            }
        }else{
            //加入一个日志，表示数据的来源
            System.out.println("redis中已有数据，正在返回。。。");
            //如果不为空，则代表redis中已经有了数据，那么我们就可以直接将categories返回
            //但是因为categories的类型是Set<String>，所以我们需要进行类型的转换
            categoryList = new ArrayList<Category>();
            for (String category : categories) {
                //因为我们从redis中读取到的数据是字符串类型的set集合，而我们需要返回的是Category的list集合
                //所以我们需要进行数据的包装
                Category category1 = new Category();
                category1.setCname(category);
                categoryList.add(category1);
            }
        }
        return categoryList;
    }
}
