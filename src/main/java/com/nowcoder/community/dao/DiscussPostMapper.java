package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DisscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //userId为空时，即首页显示全部帖子，userId有值时，显示个人主页
    List<DisscussPost> selectDiscussPosts(int userId,int offset,int limit);

    //查询帖子条数
    //@Param注解用于给参数取别名，住过只有一个参数，并且在<if>中使用，则必须加别名（即userId为0或其他时，动态操作)
    int selectDiscussPostRows(@Param("userId") int userId);


}
