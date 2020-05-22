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
`RedisKeyUtil  LikeService LikeController  disuss-detail.html（点赞部分） discuss.js`

#### 4.4我收到的赞
- 重构点赞功能
- 以用户为key，记录点赞数量
- increment(key)，decrement(key) • 开发个人主页
- 以用户为key，查询点赞数量
`RedisKeyUtil(getEntityLikeKey、getUserLikeKey) UserController(getProfilePage) profile.html`

#### 4.5关注、取消关注
- A关注了B，则A是B的Follower（粉丝），B是A的Followee（目标） `RedisKeyUtil(getFolloweeKey、getFollowerKey)`
- 统计用户的关注数、粉丝数 `FollowService(follow、unfollow findFolloweeCount findFollowerCount hasFollowed)`
`profile.js UserController FollowController profile.html`

#### 4.6关注列表、粉丝列表
- 查询某个用户关注的人，支持分页 
- 查询某个用户的粉丝，支持分页
`FollowService（findFollowees、findFollowers） FollowController  followee.html follower.html`

#### 4.7redis优化登录模块
- 使用Redis存储验证码
  - 验证码需要频繁的访问与刷新，对性能要求较高。
  - 验证码不需永久保存，通常在很短的时间后就会失效。
  - 分布式部署时，存在Session共享的问题。 `LoginController(/kaptcha、/login）`
- 使用Redis存储登录凭证
  - 处理每次请求时，都要查询用户的登录凭证，访问的频率非常高。`UserService login`
- 使用Redis缓存用户信息
  - 处理每次请求时，都要根据凭证查询用户信息，访问的频率非常高。`UserService initCache getCache`
`RedisKeyUtil(getKaptchaKey、getTicketKey、getUserKey)  LoginTicketMapper@Deprecated`
 
#### 5.1-3阻塞队列、Kafka入门 及spring整合Kafka
 `kafkaProperties` `BlockingQueueTests` `KafkaTests` 
 `pom spring-kafka`
 
#### 5.4发送系统通知
- 触发事件
  - 评论后，发布通知  `CommentController 触发评论事件`
  - 点赞后，发布通知  `LikeController 触发点赞事件`
  - 关注后，发布通知  `FollowController 触发关注事件`
- 处理事件 
  - 封装事件对象      `entity/Event`
  - 开发事件的生产者   `event/EventProducer`
  - 开发事件的消费者   `event/EventConsumer`
  
#### 5.5显示系统通知  
- 通知列表    `MessageMapper(selectLatestNotice、selectNoticeCount、selectNoticeUnreadCount、selectNotices)`
  - 显示评论、点赞、关注三种类型的通知 `MessageController(getNoticeList) notice.html`
- 通知详情
  - 分页显示某一类主题所包含的通知  `MessageController(getNoticeDetail) notice-detail.html`
- 未读消息
  - 在页面头部显示所有的未读消息数  `MessageInterceptor WebMvcConfig拦截请求 inex.html添加消息总数量`
  
#### 6.1-2 elasticsearch
- 引入依赖
  - pom spring-boot-starter-data-elasticsearch
- 配置Elasticsearch      (ElasticsearchProperties  cluster-name、cluster-nodes)
- Spring Data Elasticsearch   `DiscussPost @Document(indexName = "discusspost"...`
  - ElasticsearchTemplate
  - ElasticsearchRepository   
`dao.elasticsearch.DiscussPostRepository  ElasticsearchTests`

#### 6.3开发社区搜索功能
- 搜索服务 业务层
  - 将帖子保存至Elasticsearch服务器。  `ElasticsearchService`
  - 从Elasticsearch服务器删除帖子。
  - 从Elasticsearch服务器搜索帖子。
- 发布事件 表现层
  - 发布帖子时，将帖子异步的提交到Elasticsearch服务器。 `DiscussPostController(add 触发发帖事件)`
  - 增加评论时，将帖子异步的提交到Elasticsearch服务器。 `CommentController(addComment 触发发帖事件)`
  - 在消费组件中增加一个方法，消费帖子发布事件。         `EventConsumer 消费发帖事件`
-  显示结果
  - 在控制器中处理搜索请求，在HTML上显示搜索结果。        `SearchController  index.html表头复用 search.html`

#### 7.1-7.2 Spring Security权限管理
- 登录检查
  - 之前使用拦截器实现了登录检查，这是简单的权限管理方案，现在将其废弃 `WebMvcConfig去掉loginRequiredInterceptor`
- 权限配置
  - 对当前系统内包含的所有请求，分配访问权限（普通用户、版主、管理员） `SecurityConfig`
- 认证方案
  - 绕过Security,采用系统原来的认证方案 `UserService(getAuthorities)--> LoginTicketInteceptor(SecurityContextHolder)-->loginController(logout)`
- csrf配置
  - 防止crsf,攻击的基本原理，以及表单、AJAX相关配置    
  
#### 7.3置顶、加精、删除
- 功能实现
  - 点击置顶，修改帖子的类型   `DiscussPostMapper(updateType、updateStatus)`
  - 点击加精、删除，修改帖子状态
- 权限管理
  - 版主可执行置顶、加精操作   `DiscussPostController(setTop、setWonderful、setDelete)`
  - 管理员可执行删除操作       `EventConsumer消费删贴事件`
- 按钮显示
  - 版主可以看到置顶、加精按钮  `Thymeleaf+SpringSecurity discuss-detail.html对置顶加精删除按钮的处理`
  - 管理员可以看到删除按钮      `discuss.js`

#### 7.4Redis高级数据类型  
- HyperLogLog   高级不精确去重的数据结构   `RedisTests`
- Bitmap        byte数组，适合存储大量连续的数据的布尔值  010100010100111  

#### 7.5网站数据统计
- UV(Unique Visitor)
  - 独立访客，需要通过用户IP排重统计数据
  - 每次独立访问都要进行统计
  - HyperLogLog,性能好，且存储空间小
- DAU(Daily Active User)
  - 日活跃用户，需通过用户ID排重统计数据
  - 访问过一次，则认为其活跃
  - Bitmap,性能好，且可统计精确结果
`RedisKeyUtil DataService DataController DataInteceptor WebMvcConfig->securityConfig`  

#### 7.6任务执行和调度（线程池） 
- JDK线程池
  - ExecutorService        `ThreadPoolTests`
  - ScheduledExecutorService
- Spring线程池
  - ThreadPoolTaskExecutor 
  - ThreadPoolTaskScheduler
- 分布式定时任务
  - Spring Quartz `pom spring-boot-starter-quartz` `QuartzProperties`
  `quartz/AlphaJob` `QuartzConfig` 
  
#### 7.7热帖排序
- log(精华分+评论数*10+点赞数*2+收藏数*2)+（分布时间-牛客纪元）
- 重构DiscussPostMapper(selectDiscussPosts)、DiscussPostService(findDiscussPosts)、HomeController  
其实就是加入orderMode默认为0表示选择最新的帖子，为1表示选择最热门的帖子
**`quartz/PostScoreRefreshJob`** `QuartzConfig` `RedisKeyUtil`
`DiscussPostController发帖加精时计算帖子分数 CommentController添加评论 LikeController  index.html`

#### 7.8生成长图
- wkhtmltoimage (下载wkhtmltox,并配置环境变量)
- Runtime.getRuntime().exec()
    `wkProperties WKTests` `WkConfig` `ShareController` `EventConsumer  @KafkaListener(topics = TOPIC_SHARE)`

#### 7.9文件上传至服务器
- 客户端上传 
  - 客户端将数据提交给云服务器，并等待其响应  `UserController/upload`
  - 用户上传头像时，将表单提交给云服务器  `TODO没有实现表单提交`
- 服务器直传
  - 应用服务器将数据直接提交给云服务器，并等待其响应  
  - 分享时，服务端将自动生成的图片，直接提交给云服务器  `ShareController EventConsumer @KafkaListener(topics = TOPIC_SHARE)`
 `pom aliyun-sdk-oss #AliyunossProperties`  **`AliyunOssUtil`**

#### 7.10优化网站的性能
- 本地缓存
  - 将数据缓存在应用的服务器上，性能最好 `DiscussPostService 对不经常更新的数据做本地缓存 如：查询首页最热帖子列表、查询帖子条数`
  - 常用缓存工具：Caffeine
- 分布式缓存
  - 将数据缓存在NoSQL数据库上，跨服务器
  - 常用缓存工具：MemCache、Redis
- 多级缓存
  - 一级缓存（本地缓存） --》 二级缓存（分布式缓存） --》 DB
  - 避免缓存雪崩（缓存失效，大量请求直达DB），提高系统的可用性
 `pom caffeine #caffeineProperties  CaffeineTests jmeter测压`

#### 8.1单元测试
- spring-boot-starter-test        `SpringBootTests`
- 要求：保证测试方法的独立性(每个测试方法执行前初始化，执行后销毁数据)
- 步骤：初始化数据、执行测试代码、验证测试结果、清理测试数据
- 常用注解： @BeforeClass、@AfterClass、@Before、@After

#### 8.2项目监控
- `pom spring-boot-starter-actuator`
- Endpoints:监控应用的入口，springboot内置了很多端点，页支持自定义端点。 `actuator/DatabaseEndPoint`
- 访问路径 例如"localhost:8080/community/actuator/health http://39.105.191.47/actuator/database
- 注意事项，按需配置暴露的端点，并对所有端点进行权限控制 `SecurityConfig /actuator/**(AUTHORITY_ADMIN)`
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

```text
mysql -u root -p
show databases;
user community;
source C:/Users/26033/Desktop/community-init-sql-1.5/tables_mysql_innodb.sql
```
```text
reids-server
redis-cli
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


```text
C:\Users\26033>e:
E:\>cd E:\kafka_2.12-2.5.0
E:\kafka_2.12-2.5.0>bin\windows\zookeeper-server-start.bat config\zookeeper.properties

C:\Users\26033>e:
E:\>cd E:\kafka_2.12-2.5.0
E:\kafka_2.12-2.5.0>bin\windows\kafka-server-start.bat config\server.properties

E:\>cd E:\kafka_2.12-2.5.0\bin\windows
E:\kafka_2.12-2.5.0\bin\windows>kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
E:\kafka_2.12-2.5.0\bin\windows>kafka-topics.bat --list --bootstrap-server localhost:9092
test
E:\kafka_2.12-2.5.0\bin\windows>kafka-console-producer.bat --broker-list localhost:9092 --topic test
>hello
>world
>how are you

E:\>cd e:kafka_2.12-2.5.0\bin\windows
E:\kafka_2.12-2.5.0\bin\windows>kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
hello
world
how are you
```
```text
双击E:\elasticsearch-6.4.3\bin 下的elasticsearch.bat

curl -X GET "localhost:9200/_cat/health?v"  查看集群健康状态 GREEN为健康
curl -X GET "localhost:9200/_cat/nodes?v"   查看集群中的节点
curl -X GET "localhost:9200/_cat/indices?v" 查看索引
curl -X PUT "localhost:9200/test"           创建名为test的索引
curl -X DELETE "localhost:9200/test"        删除名为test的索引

用postman
get localhost:9200/_cat/indices?v  查看索引
put loaclhost:9200/test            创建名为test的索引
delete localhost:9200/test         删除名为test的索引
put localhost:9200/test/_doc/1     test为索引，_doc占位符不管 1为id  增加数据
body raw json
{
	"title":"hello",
	"content":"How are you"
}

get localhost:9200/test/_doc/1    查询数据
{
	"_index": "test",
	"_type": "_doc",
	"_id": "1",
	"_version": 1,
	"found": true,
	"_source": {
		"title": "hello",
		"content": "How are you"
	}
}
delete localhost:9200/test/_doc/1  删除数据

get localhost:9200/test/_search?q=title:互联网      搜索标题带有互联网的数据
get localhost:9200/test/_search?q=content:运营实习  搜索内容为运营实习（分词为运营+实习）的数据

get localhost:9200/test/_search                    搜索
body raw json
{
	"query":{
		"multi_match":{
			"query":"互联网",
			"fields":["title","content"]
		}
	}
}
```

```text
>wkhtmltopdf https://www.nowcoder.com f:/data/wk-pdfs/1.pdf
>wkhtmltoimage https:www.nowcoder.com f:/data/wk-images/1.png
>wkhtmltoimage --quality 75 https:www.nowcoder.com f:/data/wk-images/2.png
```


