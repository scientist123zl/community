package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE= "followee";//被关注的人
    private static final String PREFIX_FOLLOWER = "follower";//关注者
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_UV = "uv";//访客
    private static final String PREFIX_DAU = "dau";//日活跃用户
    private static final String PREFIX_POST = "post";//热帖排序

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId) //存到集合，装的是userId,可以查到谁给实体点赞
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)//实体Id,时间
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)//粉丝Id,时间
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA +SPLIT +owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日DAU 活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间DAU 活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    public static String getPostScoreKey(){
        return PREFIX_POST + SPLIT + "score";
    }



}
