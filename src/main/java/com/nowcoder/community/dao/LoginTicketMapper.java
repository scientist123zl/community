package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
@Deprecated//不推荐使用
public interface LoginTicketMapper {

    //插入登录信息
    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")//声明主键自动生成
    int insertLoginTicket(LoginTicket loginTicket);

    //通过ticket查找用户登录信息
    @Select({
            "select id, user_id, ticket, status, expired",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    //通过ticket更新登录状态  动态sql <script>标签表示为一个脚本  <if>字标签
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    void updateStatus(String ticket,int status);
}
