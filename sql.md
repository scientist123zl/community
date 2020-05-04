```
字符串(strings)、哈希(hashes)、列表(lists)、集合(sets)、有序集合(sorted sets)
127.0.0.1:6379> flushdb                                                                                                 
OK
127.0.0.1:6379> set test:count 1                                                                                        
OK
127.0.0.1:6379> get test:count                                                                                          
"1"
127.0.0.1:6379> incr test:count                                                                                         
(integer) 2
127.0.0.1:6379> decr test:count                                                                                         
(integer) 1
127.0.0.1:6379> hset test:user id 1                                                                                     
(integer) 1
127.0.0.1:6379> hset test:user username zhangsan                                                                        
(integer) 1                                                                                    
127.0.0.1:6379> hget test:user username                                                                                 
"zhangsan"
127.0.0.1:6379> lpush test:ids 101 102 103                                                                              
(integer) 3
127.0.0.1:6379> llen test:ids                                                                                           
(integer) 3
127.0.0.1:6379> lindex test:ids 0
"103"
127.0.0.1:6379> lindex test:ids 2
"101"
127.0.0.1:6379> lrange test:ids 0 2
1) "103"
2) "102"
3) "101"
127.0.0.1:6379> rpop test:ids
"101"
127.0.0.1:6379> rpop test:ids
"102"
127.0.0.1:6379> sadd test:teachers aaa bbb ccc ddd eee
(integer) 5
127.0.0.1:6379> scard test:teachers
(integer) 5
127.0.0.1:6379> spop test:teachers
"eee"
127.0.0.1:6379> spop test:teachers
"bbb"
127.0.0.1:6379> smembers test:teachers
1) "aaa"
2) "ccc"
3) "ddd"
127.0.0.1:6379> zadd test:stus 10 aaa 20 bbb 30 ccc 40 ddd 50 eee
(integer) 5

127.0.0.1:6379> zcard test:stus
(integer) 5
127.0.0.1:6379> zscore test:stus ccc
"30"
127.0.0.1:6379> zrank test:stus ccc
(integer) 2
127.0.0.1:6379> zrange test:stus 0 2
1) "aaa"
2) "bbb"
3) "ccc"
127.0.0.1:6379> keys *                                                                                                  
1) "test:stus"
2) "test:user"
3) "test:ids"
4) "test:teachers"
5) "test:students"
6) "test:count"
127.0.0.1:6379> type test:user                                                                                          
hash
127.0.0.1:6379> del test:students                                                                                       
(integer) 1
127.0.0.1:6379> exists test:students                                                                                    
(integer) 0
127.0.0.1:6379> expire test:user 10                                                                                     
(integer) 1
127.0.0.1:6379> keys *
1) "test:stus"
2) "test:user"
3) "test:ids"
4) "test:teachers"
5) "test:count"
127.0.0.1:6379> keys *
1) "test:stus"
2) "test:ids"
3) "test:teachers"
4) "test:count"
```

#### 1.1开发社区首页
- 开发社区首页，显示前10个帖子
- 开发分页组件，分页显示所有的帖子
`entity/page、DiscussPost\user DiscussPostMapper（ selectDiscussPosts  selectDiscussPostRows） DiscussPostService HomeController index.html中的分页逻辑`

#### 2.1发送邮件
- 启用客户端SMTP服务
- jar包（spring-boot-starter-mail）邮箱参数配置（MailProperties）
- 使用 JavaMailSender 发送邮件
`MailClient `

#### 2.2开发注册功能
- 访问注册页面,点击顶部区域内的链接，打开注册页面
- 提交注册数据 
  - 通过表单提交数据 `UserMapper CommunityUtil(generateUUID，md5) UserService(register) LoginController(/register)`
  - 服务端验证账号是否已存在、邮箱是否已注册
  - 服务端发送激活邮件
- 激活注册账号，点击邮件中的链接，访问服务端的激活服务  `UserService(activation)  LoginController(/activation/{userId}/{code})`
` register.html activation.html  operator-result.html`

#### 2.3会话管理
- cookie 是服务器发送到浏览器，由浏览器保存，浏览器下次访问该服务器时，会自动携带块该数据，将其发送给服务器。
- session 服务器保存在内存

#### 2.4生成验证码
- kaptcha `pom.xml  KaptcahConfig LoginController(/kaptcha)`

##### 2.5开发登录、退出功能
- 访问登录页面,点击顶部区域内的链接，打开登录页面
- 登录
  - 验证账号、密码、验证码 `LoginTicketMapper UserService( login)`
  - 成功时，生成登录凭证，发放给客户端。失败时，跳转回登录页 `LoginController(/login)`
- 退出，将登录凭证修改为失效状态，跳转至网站首页`LoginController(/logout)`

#### 2.6拦截器显示登录信息(即登录状态与未登录时头部显示不一样)
- 定义拦截器，实现HandlerInteceptor  **`LoginTicketInteceptor`**
- 配置拦截器，为它指定拦截、排除的路径  `WebMvcConfig`
- 在请求开始时查询登录用户preHandle  `util/CookieUtil`
- 在本次请求中持有用户数据preHandle `util/HostHolder`
- 在模板视图上显示用户数据postHandle
- 在请求结束时清理用户数据afterCompletion
- `LoginTicketMapper UserSevice(login)`

#### 2.7账号设置（更换头像、密码）
- 访问账号设置页面
- 上传头像MultipartFile
- 获取头像
`UserController(getSettingPage、uploadHeader、getHeader）`

#### 2.8检查登录状态（即不是登录状态时不能通过地址栏访问一些页面）
- 自定义注解（@Target、@Retention）  `（annotation/@LoginRequired) LoginRequiredInterceptor`
- 使用拦截器拦截带有该注解的请求 `UserController(getSettingPage、uploadHeader)`

#### 3.1过滤敏感词
- 定义前缀树 `Sensitive-word.txt  util/SensitiveFilter(TrieNode)`
- 根据敏感词，初始化前缀树
- 编写过滤敏感词的方法 `SensitiveFilter(filter) SensitiveTests`

#### 3.2发布帖子
- 示例，使用jQuery发送AJAX请求 `pom-fastjson  CommunityUtil(getJSONString main)   ajax-demo.html   alphaController(testAjax)` 
- 采用AJAX请求，实现发布帖子的功能 `DiscussPostController(addDiscussPost)` **`js/index.js`**
`DiscussPostMapper(insertDiscussPost) DiscussPostService` 
`在不重新加载页面的情况下发送请求给服务器。接受并使用从服务器发来的数据,更新页面。`

#### 3.3帖子详情
`DiscussPostMapper(selectDiscussPostById) DiscussPostService DiscussPostController(/detail/{discussPostId})`
- index.html,在帖子标题上增加访问详情页面的链接
- discuss-detail.html

#### 3.4事务管理
- 通过注解，声明某方法的事务特征 
- 通过TransactionTemplate管理事务
` AlphaService(save1、save2) TransactionTests`

#### 3.5 显示评论
-  数据层
  - 根据实体查询一页评论数据。`CommentMapper(selectCommentsByEntity、selectCountByEntity)`
  - 根据实体查询评论的数量。
- 业务层
  - 处理查询评论的业务。 `CommentService`
  - 处理查询评论数量的业务。
- 表现层
  - 显示帖子详情数据时，同时显示该帖子所有的评论数据。 **`DiscussPostController(/detail/{discussPostId})`**

#### 3.6增加评论
- 数据层
  - 增加评论数据。`CommentMapper(insertComment)`
  - 修改帖子的评论数量。 `DiscussPostMapper(updateCommentCount）`
- 业务层
  - 处理添加评论的业务。 `CommentService(addComment)`
  - 先增加评论、再更新帖子的评论数量。 `DiscussPostMapper(updateCommentCount)`
- 表现层
  - 处理添加评论数据的请求。 `CommentController(/add/{discussPostId}`
  - 设置添加评论的表单。

#### 3.7私信列表
- 私信列表
  - 查询当前用户的会话列表，支持分页显示。 `MessageController(getLetterList)`
  - 每个会话只显示一条最新的私信。 `letter.html(私信列表)`
- 私信详情
  - 查询某个会话所包含的私信。 支持分页显示。`MessageController(getLetterDetail)`
`MessageMapper 前5方法   letter-detail.html(私信列表)`

#### 3.8发送私信
- 发送私信
  - 采用异步的方式发送私信。 `MessageMapper(insertMessage) MessageController(sendLetter)`
  - 发送成功后刷新私信列表。 
- 设置已读
  - 访问私信详情时，将显示的私信设置为已读状态。` MessageMapper(updateStatus)` `MessageController(getLetterDetail)` 
**`letter.js`**  `letter.html/letter-detail.html(弹出框sendModal)`  

#### 3.9统一处理异常
- @ControllerAdvice 
- @ExceptionHandler
`ExceptionAdvice HomeController(getErrorPage) error/404、500.html`

#### 3.10统一记录日志
`aspect/AlphaAspect、ServiceLogAspect`  

#### 4.1-2redis入门及spring整合redis
- redis数据结构：字符串(strings)、哈希(hashes)、列表(lists)、集合(sets)、有序集合(sorted sets)等
- redis应用场景包括：缓存、排行榜、计数器、社交网络、消息队列等。
- pom spring-boot-starter-data-redis
- 配置Redis
  - 配置数据库参数   RedisProperties
  - 编写配置类，构造RedisTemplate `RedisConfig RedisTests`
  
#### 4.3点赞
- 支持对帖子、评论点赞； 第1次点赞，第2次取消点赞。
- 首页点赞数量
- 统计帖子的点赞数量
- 详情页点赞数量
- 统计点赞数量
- 显示点赞状态
`RedisKeyUtil getEntityLikeKey  LikeService LikeController  disuss-detail.html（点赞部分） discuss.js`

#### 4.4我收到的赞
- 重构点赞功能
- 以用户为key，记录点赞数量
- increment(key)，decrement(key) • 开发个人主页
- 以用户为key，查询点赞数量
`RedisKeyUtil getUserLikeKey UserController(getProfilePage) profile.html`


```sql
用户表
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `salt` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0-普通用户; 1-超级管理员; 2-版主;',
  `status` int(11) DEFAULT NULL COMMENT '0-未激活; 1-已激活;',
  `activation_code` varchar(100) DEFAULT NULL COMMENT '用户激活码',
  `header_url` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_username` (`username`(20)),
  KEY `index_email` (`email`(20))
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8;
```
```sql
帖子表
CREATE TABLE `discuss_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `type` int(11) DEFAULT NULL COMMENT '0-普通; 1-置顶;',
  `status` int(11) DEFAULT NULL COMMENT '0-正常; 1-精华; 2-拉黑;',
  `create_time` timestamp NULL DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `score` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8;
```

```sql
登录表
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL COMMENT '登录凭证',
  `status` int(11) DEFAULT '0' COMMENT '0-登录状态有效; 1-无效;',
  `expired` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_ticket` (`ticket`(20))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
```

```sql
帖子评论表
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '评论的发布人',
  `entity_type` int(11) DEFAULT NULL COMMENT '评论类型 1-评论帖子 2-二级评论',
  `entity_id` int(11) DEFAULT NULL COMMENT '评论针对的帖子id',
  `target_id` int(11) DEFAULT NULL COMMENT '二级评论 @haha 指向的用户id',
  `content` text,
  `status` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=232 DEFAULT CHARSET=utf8;
```
```sql
私信表 conversation_id为一次会话的id，一个会话有多条私信
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL  COMMENT '为1时表示系统发的通知，而不是私信;',
  `to_id` int(11) DEFAULT NULL,
  `conversation_id` varchar(45) NOT NULL COMMENT '111_122 为from_id和to_id拼接，且小id在前，因为是一个会话',
  `content` text,
  `status` int(11) DEFAULT NULL COMMENT '0-未读;1-已读;2-删除;',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_from_id` (`from_id`),
  KEY `index_to_id` (`to_id`),
  KEY `index_conversation_id` (`conversation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=355 DEFAULT CHARSET=utf8;

```



