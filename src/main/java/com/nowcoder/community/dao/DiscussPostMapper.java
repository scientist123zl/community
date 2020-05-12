package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //userId为空时，即首页显示全部帖子，userId有值时，显示个人主页
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    //查询帖子条数
    //@Param注解用于给参数取别名，如果只有一个参数，并且在<if>中使用，则必须加别名（即userId为0或其他时，动态操作)
    int selectDiscussPostRows(@Param("userId") int userId);


    int insertDiscussPost(DiscussPost discussPost);

    //查询帖子的详情
    DiscussPost selectDiscussPostById(int id);

    //增加帖子评论数量
    int updateCommentCount(int id, int commentCount);

    //修改帖子类型（置顶、加精）
    int updateType(int id,int type);

    //删除帖子
    int updateStatus(int id,int status);

    //更新帖子分数
    int updateScore(int id,double score);




}
