-- MySQL dump 10.13  Distrib 8.0.26, for Linux (x86_64)
--
-- Host: 139.198.181.54    Database: xc-nacos
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config_info`
--

DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

LOCK TABLES `config_info` WRITE;
/*!40000 ALTER TABLE `config_info` DISABLE KEYS */;
INSERT INTO `config_info` VALUES (1,'mp-config.properties','xc-group','#驼峰下划线转换\r\nmybatis-plus.global-config.db-column-underline = true\r\n#实体扫描，多个package用逗号或者分号分隔\r\nmybatis-plus.typeAliasesPackage = com.xuecheng.*.entity\r\n#字段策略 0:\"忽略判断\",1:\"非 NULL 判断\"),2:\"非空判断\"\r\nmybatis-plus.global-config.field-strategy=2\r\n#全局地开启或关闭配置文件中的所有映射器已经配置的任何缓存，开发时不需要开启。\r\nmybatis-plus.configuration.cache-enabled = false\r\n#映射文件mapper文件存储位置\r\nmybatis-plus.mapper-locations = classpath:com.xuecheng.*.mapper/*.xml\r\n#主键类型  0:\"数据库ID自增\", 1:\"用户输入ID\",2:\"全局唯一ID (数字类型唯一ID)\", 3:\"全局唯一ID UUID\";\r\nmybatis-plus.global-config.id-type = 0\r\n#刷新mapper 调试神器\r\nmybatis-plus.global-config.refresh-mapper = true','c4830681643505108185f52cbff96453','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(2,'content-service-dev.properties','xc-group','#spring http 配置信息\nserver.servlet.context-path = /content\nserver.port=63040\n\n\n#spring druid 配置信息\nspring.datasource.url = jdbc:mysql://192.168.101.65:3306/xc2.0_content?serverTimezone=UTC&userUnicode=true&useSSL=false&characterEncoding=utf8\n\n\n#文件系统配置\n#文件系统微服务的请求地址\nfile.service.url=http://127.0.0.1:56082/farming/generatetoken?origin=qiniu\n#文件存储空间名称\nfile.service.bucket=xczx-lzy\n\n#前端上传文件需要的配置信息\n#文件存储区域的地址\nfile.service.upload.region = http://upload.qiniu.com\n#文件访问的cdn加速域名\ncdn.domain = r3zc5rung.hd-bkt.clouddn.com\n\nfile.token.type = 1\nfile.token.deadline = 3600\n\n\n#异步回调，定义ConfirmCallback，MQ返回结果时会回调这个ConfirmCallback\nspring.rabbitmq.publisher-confirm-type = correlated\n#开启publish-return功能，同样是基于callback机制，不过是定义ReturnCallback\nspring.rabbitmq.publisher-returns = true\n#定义消息路由失败时的策略。true，则调用ReturnCallback；false：则直接丢弃消息\nspring.rabbitmq.template.mandatory = true\n\n\n# 课程发布 交互级名称\ncourse.publish.exchange = course_pub.direct\n# 课程发布 页面生成队列名称\ncourse.publish.queue = course_page.queue\ncourse.publish.routingKey= publish.course','a2962f9623381bdee92050275a3e790b','2022-08-31 13:31:52','2022-09-01 02:50:07',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(3,'spring-druid-config.properties','xc-group','#spring druid 配置信息\n#spring.datasource.driver-class-name = com.mysql.jdbc.Driver\n#spring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_content?userUnicode=true&useSSL=false&characterEncoding=utf8\nspring.datasource.username = root\nspring.datasource.password = mysql\n#初始化连接池的的连接数据量\nspring.datasource.druid.initial-size = 5\n#连接池最小连接数 \nspring.datasource.druid.min-idle = 5\n#获取连接等待超时时间 \nspring.datasource.druid.max-wait = 60000\n# 要启用PreparedStatementCache,必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。\nspring.datasource.druid.max-pool-prepared-statement-per-connection-size = 20\n#连接池中最大激活连接数\nspring.datasource.druid.max-active = 20','420867ac9dff1d6f7a76bb25769ab7bf','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(4,'spring-http-config.properties','xc-group','#srpingboot http 配置信息\nspring.http.encoding.enabled = true\nspring.http.encoding.charset = UTF-8\nspring.http.encoding.force = true\nserver.use-forward-headers = true\n#server.servlet.context-path = /\n#server.port=8888\n#nacos上添加如下配置重启服务即可让我们的服务优先读取本地配置参数信息！\nspring.cloud.config.allow-override=true\nspring.cloud.config.override-none=true\nspring.cloud.config.override-system-properties=false','8a44e28b630fd3aa6384d66f9c351725','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(5,'system-service-dev.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /system\nserver.port=63110\n\n#spring druid 配置信息\nspring.datasource.url = jdbc:mysql://192.168.101.65:3306/xc2.0_system?serverTimezone=UTC&userUnicode=true&useSSL=false&characterEncoding=utf8','a822e380324c0f08cb33d886a8643e5d','2022-08-31 13:31:52','2022-09-01 02:55:16',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(6,'media-service-dev.properties','xc-group','#springboot启动配置修改\nserver.servlet.context-path = /media\nserver.port=63050\n\n# 修改druid公共配置信息里的连接地址\nspring.datasource.url=jdbc:mysql://192.168.101.65:3306/xc2.0_media?serverTimezone=UTC&userUnicode=true&useSSL=false&characterEncoding=utf8\n\naliyun.region = cn-shanghai\naliyun.accessKeyId = LTAI5tPEYTyFEK1qsyyigFM7\naliyun.accessKeySecret = DGJggsZidqb6cyIhUSvZRaXQxEmHMN','9b1e3ce87f944601a2f1e086830796eb','2022-08-31 13:31:52','2022-09-01 02:56:15',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(7,'feign-config.properties','xc-group','# 开启 feign 的远程调用使用熔断\nfeign.sentinel.enabled = true\n# 配置请求GZIP压缩\nfeign.compression.request.enabled = false\n# 配置压缩数据大小的下限\nfeign.compression.request.min-request-size = 2048\n# 配置响应GZIP压缩\nfeign.compression.response.enabled = false\n# 配置压缩支持的MIME TYPE\nfeign.compression.request.mime-types[0] = text/xml\nfeign.compression.request.mime-types[1] = application/xml\nfeign.compression.request.mime-types[2] = application/json','2f3be5ae5f601d5b37e586607956770e','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(8,'ribbon-config.properties','xc-group','#对当前实例的重试次数 default 0\r\nribbon.MaxAutoRetries = 1\r\n#设置连接超时时间 default 2000\r\nribbon.ConnectTimeout = 3000\r\n#对所有操作请求都进行重试 default false\r\nribbon.OkToRetryOnAllOperations = false\r\n#设置读取超时时间 default 5000\r\nribbon.ReadTimeout = 20000\r\n#切换实例的重试次数 default 1\r\nribbon.MaxAutoRetriesNextServer = 1','95976e0fbb744e2c148d1b9a214025b3','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(9,'freemarker-config.properties','xc-group','#开启 freemarker 功能\r\nspring.freemarker.enabled = true\r\n#关闭模板缓存，方便测试\r\nspring.freemarker.cache = false\r\nspring.freemarker.settings.template_update_delay = 0\r\n#页面模板后缀名\r\nspring.freemarker.suffix = .ftl\r\nspring.freemarker.charset = UTF-8\r\n#页面模板位置(默认为 classpath:/templates/)\r\nspring.freemarker.template-loader-path = classpath:/templates/\r\n#关闭项目中的静态资源映射(static、resources文件夹下的资源)\r\nspring.resources.add-mappings = false','b922b2f76a55b5f1bb66bd0e86fd67da','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(10,'rabbitmq-config.properties','xc-group','#rabbitmq 配置信息\nspring.rabbitmq.host = 192.168.101.65\nspring.rabbitmq.port = 5672\nspring.rabbitmq.username = guest\nspring.rabbitmq.password = guest\nspring.rabbitmq.password.virtual-host = /\n\n#correlated 异步回调，定义ConfirmCallback，MQ返回结果时会回调这个ConfirmCallback\nspring.rabbitmq.publisher-confirm-type = correlated\n#开启publish-return功能，同样是基于callback机制，需要定义ReturnCallback\nspring.rabbitmq.publisher-returns = true\n#定义消息路由失败时的策略。true，则调用ReturnCallback；false：则直接丢弃消息\nspring.rabbitmq.template.mandatory = true\n#出现异常时返回nack，消息回滚到mq；没有异常，返回ack\nspring.rabbitmq.listener.simple.acknowledge-mode = auto\n#开启消费者失败重试\nspring.rabbitmq.listener.simple.retry.enabled = true\n#初识的失败等待时长为1秒\nspring.rabbitmq.listener.simple.retry.initial-interval = 1000ms\n#失败的等待时长倍数，下次等待时长 = multiplier * last-interval\nspring.rabbitmq.listener.simple.retry.multiplier = 1\n#最大重试次数\nspring.rabbitmq.listener.simple.retry.max-attempts = 3\n# true无状态；false有状态。如果业务中包含事务，这里改为false\nspring.rabbitmq.listener.simple.retry.stateless=true\n\n\n#消息队列配置\n#消息同步交换机\nxc.mq.msgsync.exchange=xc.msgsync.direct\n#课程发布消息队列\nxc.mq.msgsync.queue.coursepub=xc.course.publish.queue\n#课程发布消息队列routingkey\nxc.mq.msgsync.queue.coursepub.key=xc.course.publish.queue\n#课程发布结果消息队列\nxc.mq.msgsync.queue.coursepubresult=xc.course.publish.result.queue\n#课程发布结果消息队列routingkey\nxc.mq.msgsync.queue.coursepubresult.key=xc.course.publish.result.queue','f7df4f6cf31e7401353c013820bb555c','2022-08-31 13:31:52','2022-09-01 02:57:24',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(11,'coursepub-consumer-service-dev.properties','xc-group','#springboot server 配置\nserver.servlet.context-path = /pub_consumer\nserver.port=63333\n\n#druid 配置信息\nspring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_content?userUnicode=true&useSSL=false&characterEncoding=utf8\n\n#消费端应答模式\nspring.rabbitmq.listener.simple.acknowledge-mode = auto\n\n# 开启消费者失败重试\nspring.rabbitmq.listener.simple.retry.enabled = true\n# 初识的失败等待时长为1秒\nspring.rabbitmq.listener.simple.retry.initial-interval = 1000ms\n# 失败的等待时长倍数，下次等待时长 = multiplier * last-interval\nspring.rabbitmq.listener.simple.retry.multiplier = 1\n# 最大重试次数\nspring.rabbitmq.listener.simple.retry.max-attempts = 3\n# true无状态；false有状态。如果业务中包含事务，这里改为false\nspring.rabbitmq.listener.simple.retry.stateless = true\n\n# 课程发布 交换机名称\ncourse.publish.exchange = course_pub.direct\n# 课程发布 页面生成队列名称\ncourse.publish.queue = course_page.queue\ncourse.publish.routingkey= publish.course\n\n#课程发布 消费失败配置信息\ncourse.publish.error.exchange = error.course_pub.direct\ncourse.publish.error.queue = error.course_page.queue\ncourse.publish.error.routingkey = error.publish_course\n\n#生成静态化页面发布位置\ncourse.publish.position = pages/\n#七牛云的存储消息\nfile.qiniu.accessKey = C_406Zs8cIazVTGeQXLV_BVfg0hLbDgUs5J5K1ro\nfile.qiniu.secretKey = hfsJ1ypCKHOufHzk8mGuWfXACklkvSbwKyjG96RW\nfile.qiniu.bucket = xczx-lzy-static-pages','d0b03638ad59ca16bc2c9357ff406771','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(12,'search-service-dev.properties','xc-group','server.servlet.context-path = /search\nserver.port=63080\n\n# ES 配置信息\nxuecheng.elasticsearch.hostlist = 106.15.33.29:9200\nxuecheng.elasticsearch.course.index = xc2.0_course','00257720a4c531ad7c9edcf4e14da2cf','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(13,'learning-service-dev.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /learning\nserver.port=63070\n\nspring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_learning?userUnicode=true&useSSL=false&characterEncoding=utf8','1d91a8d5b2a4993061089242a2f5e52c','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(14,'teaching-service-dev.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /teaching\nserver.port=63060\n\nspring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_teaching?userUnicode=true&useSSL=false&characterEncoding=utf8','e033922e9ff1db4292280e3689cfe5d9','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(15,'uaa-gateway-server-dev.properties','xc-group','#srpingboot http 配置信息\r\nserver.servlet.context-path = /\r\nserver.port=63010','001a3d10fe3c491c0444f6dcdd6b35e8','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(16,'uaa-service-dev.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /uaa\nserver.port=63020\n\nspring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_uaa?userUnicode=true&useSSL=false&characterEncoding=utf8','bcaad64b545f0d673d52b52084270ff8','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(17,'user-service-dev.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /user\nserver.port=63130\n\n\nspring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_user?userUnicode=true&useSSL=false&characterEncoding=utf8','b3f8bbaca06c2a84c25da45217caa1c3','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(18,'order-service-dev.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /order\nserver.port=63090\n\nspring.datasource.url = jdbc:mysql://127.0.0.1:3306/xc2.0_order?userUnicode=true&useSSL=false&characterEncoding=utf8\n\n#商户微信公共号或开放平台唯一标识\nweixinpay.app-id = wx8397f8696b538317\n#商户号\nweixinpay.mch-id = 1473426802\n#商户密钥\nweixinpay.mch-key = T6m9iK73b0kn9g5v426MKfHQH7X8rKwb\n#微信回调商户的地址\nweixinpay.notify-url = http://www.xuecheng.com/api\n#商户的支付类型（NATIVE 为扫码支付）\nweixinpay.trade-type = NATIVE','8c06a86939b031fb73b3d25212c06920','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(19,'seataServer.properties','xc-group','# 数据存储方式，db代表数据库\nstore.mode=db\nstore.db.datasource=druid\nstore.db.dbType=mysql\nstore.db.driverClassName=com.mysql.jdbc.Driver\nstore.db.url=jdbc:mysql://127.0.0.1:3306/xc2.0_seata?useUnicode=true&rewriteBatchedStatements=true\nstore.db.user=root\nstore.db.password=itcast136\nstore.db.minConn=5\nstore.db.maxConn=30\nstore.db.globalTable=global_table\nstore.db.branchTable=branch_table\nstore.db.queryLimit=100\nstore.db.lockTable=lock_table\nstore.db.maxWait=5000\n# 事务、日志等配置\nserver.recovery.committingRetryPeriod=1000\nserver.recovery.asynCommittingRetryPeriod=1000\nserver.recovery.rollbackingRetryPeriod=1000\nserver.recovery.timeoutRetryPeriod=1000\nserver.maxCommitRetryTimeout=-1\nserver.maxRollbackRetryTimeout=-1\nserver.rollbackRetryTimeoutUnlockEnable=false\nserver.undo.logSaveDays=7\nserver.undo.logDeletePeriod=86400000\n\n# 客户端与服务端传输方式\ntransport.serialization=seata\ntransport.compressor=none\n# 关闭metrics功能，提高性能\nmetrics.enabled=false\nmetrics.registryType=compact\nmetrics.exporterList=prometheus\nmetrics.exporterPrometheusPort=9898','58da867dd87f2b78b55aad31c968bcb5','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(20,'redis-config.properties','xc-group','# Redis数据库索引（默认为0）\nspring.redis.database=0\n# Redis服务器地址\nspring.redis.host=192.168.101.64\n# Redis服务器连接端口\nspring.redis.port=6379\n# Redis服务器连接密码（默认为空）\nspring.redis.password=itcast20220812\nspring.redis.lettuce.pool.max-active=20\nspring.redis.lettuce.pool.max-idle=10\nspring.redis.lettuce.pool.min-idle=0\nspring.redis.lettuce.pool.max-wait=-1\n# 连接池最大连接数（使用负值表示没有限制）\n#spring.redis.jedis.pool.max-active=20\n# 连接池最大阻塞等待时间（使用负值表示没有限制）\n#spring.redis.jedis.pool.max-wait=-1\n# 连接池中的最大空闲连接\n#spring.redis.jedis.pool.max-idle=10\n# 连接池中的最小空闲连接\n#spring.redis.jedis.pool.min-idle=0\n# 连接超时时间（毫秒）\nspring.redis.timeout=1000','179450e4e39ac7dcce283e3d80cb6d97','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(21,'comment-service.properties','xc-group','#srpingboot http 配置信息\nserver.servlet.context-path = /comment\nserver.port=63120\n\nspring.datasource.url = jdbc:mysql://192.168.101.65:3306/xc2.0_comments?userUnicode=true&useSSL=false&characterEncoding=utf8','1e6275014671ea2db7d2bff629055f56','2022-08-31 13:31:52','2022-09-01 03:14:32',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(22,'media-process-service-dev.properties','xc-group','#springboot启动配置修改\n\n# 修改druid公共配置信息里的连接地址\nspring.datasource.url=jdbc:mysql://192.168.101.65:3306/xc2.0_media?serverTimezone=UTC&userUnicode=true&useSSL=false&characterEncoding=utf8\n\n### xxl-job admin address list, such as \"http://address\" or \"http://address01,http://address02\"\nxxl.job.admin.addresses=http://192.168.101.65:8088/xxl-job-admin\n### xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null\nxxl.job.executor.address=\n### xxl-job executor server-info\nxxl.job.executor.ip=\n### xxl-job, access token\nxxl.job.accessToken=default_token\n\n### xxl-job executor appname\nxxl.job.executor.appname=media-process-service\n\nxxl.job.executor.port=9999\n### xxl-job executor log-path\nxxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler\n### xxl-job executor log-retention-days\nxxl.job.executor.logretentiondays=30','2ed5918632f5d7fa6ae5b2d85091a490','2022-08-31 13:31:52','2022-09-01 03:14:21',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(23,'message-process-service-dev.properties','xc-group','### xxl-job admin address list, such as \"http://address\" or \"http://address01,http://address02\"\nxxl.job.admin.addresses=http://192.168.101.65:8088/xxl-job-admin\n### xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null\nxxl.job.executor.address=\n### xxl-job executor server-info\nxxl.job.executor.ip=\n### xxl-job, access token\nxxl.job.accessToken=default_token\n\n### xxl-job executor appname\nxxl.job.executor.appname=media-process-service\n\nxxl.job.executor.port=9999\n### xxl-job executor log-path\nxxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler\n### xxl-job executor log-retention-days\nxxl.job.executor.logretentiondays=30','290992196277c8d37d72add540519ca0','2022-08-31 13:31:52','2022-09-01 03:13:45',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df','','','','properties',''),(24,'cache-service-dev.properties','xc-group','server.servlet.context-path = /\nserver.port=63035','203bcffeeca788f2e212eed4753ac81e','2022-08-31 13:31:52','2022-08-31 13:31:52',NULL,'192.168.101.1','','3adc3388-087e-4c90-a373-c5a6484752df',NULL,NULL,NULL,'properties',NULL),(34,'system-service-prod.properties','xuecheng-plus-project','config.abc=456','7c5a17db32855013580f7998984e1f52','2022-09-11 04:25:57','2022-09-11 04:26:23',NULL,'192.168.101.1','','prod','','','','properties',''),(35,'system-service-test.properties','xuecheng-plus-project','config.abc=789','48efd90540af953eed3dd41254495efc','2022-09-11 04:26:13','2022-09-11 04:26:35',NULL,'192.168.101.1','','test','','','','properties',''),(81,'system-service-dev.yaml','xuecheng-plus-project','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://139.198.181.54:3306/xc_system?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: Dcp#7ujm\n\n','cfe41caaf5d63219b6d8d408e8d3b7da','2022-09-11 06:32:14','2023-06-09 14:32:56','nacos','127.0.0.1','','dev','','','','yaml',''),(82,'system-api-dev.yaml','xuecheng-plus-project','server:\n  servlet:\n    context-path: /system\n  port: 63110\n\ntestconfig:\n a: 1aa','08bc232c5164645dae080a8e0574cf27','2022-09-11 06:33:06','2022-09-11 06:37:16',NULL,'192.168.101.1','','dev','','','','yaml',''),(85,'content-service-dev.yaml','xuecheng-plus-project','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://139.198.181.54:3306/xc_content?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: Dcp#7ujm\n\nxxl:\n  job:\n    admin: \n      addresses: http://192.168.101.65:8088/xxl-job-admin\n    executor:\n      appname: coursepublish-job\n      address: \n      ip: \n      port: 8999\n      logpath: /data/applogs/xxl-job/jobhandler\n      logretentiondays: 30\n    accessToken: default_token\ntest_config:\n a: 2a\n b: 2b\n c: 2c','8776ea30024511b11b311c9dff8cf0d3','2022-09-11 07:33:48','2023-06-10 00:43:09','nacos','127.0.0.1','','dev','','','','yaml',''),(86,'content-api-dev.yaml','xuecheng-plus-project','server:\n  servlet:\n    context-path: /content\n  port: 63040\n\ntest_config:\n a: 3a\n b: 3b\n\n#配置本地优先\nspring:\n cloud:\n  config:\n    override-none: true\n','b253988f9f30d549192402cbab566a0e','2022-09-11 07:54:17','2022-09-11 09:01:41',NULL,'192.168.101.1','','dev','','','','yaml',''),(88,'swagger-dev.yaml','xuecheng-plus-common','# swagger 文档配置\nswagger:\n  title: \"学成在线管理系统接口文档\"\n  description: \"学成在线管理系统信管理数据\"\n  base-package: com.xuecheng\n  enabled: true\n  version: 1.0.0\n\ntest_config:\n a: 1a\n b: 1b\n c: 1c\n d: 1d','9243a86dd3b30e86b46b186b2ed4c812','2022-09-11 08:14:16','2023-06-10 00:54:30','nacos','127.0.0.1','','dev','','','','yaml',''),(91,'logging-dev.yaml','xuecheng-plus-common','# 日志文件配置路径\nlogging:\n  config: classpath:log4j2-dev.xml\n  level:\n    org.springframework.cloud.gateway: trace','cf8b1af28beb25b242f97519c8f2c560','2022-09-11 08:22:12','2022-09-12 13:21:52',NULL,'192.168.101.1','','dev','','','','yaml',''),(105,'gateway-dev.yaml','xuecheng-plus-project','server:\n  port: 63010 # 网关端口\nspring:\n  cloud:\n    gateway:\n#      filter:\n#        strip-prefix:\n#          enabled: true\n      routes: # 网关路由配置\n        - id: content-service # 路由id，自定义，只要唯一即可\n          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址\n          uri: lb://content-service # 路由的目标地址 lb就是负载均衡，后面跟服务名称\n          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件\n            - Path=/content/** # 这个是按照路径匹配，只要以/content/开头就符合要求\n#          filters:\n#            - StripPrefix=1\n        - id: system-service\n          # uri: http://127.0.0.1:8081\n          uri: lb://system-service\n          predicates:\n            - Path=/system/**\n#          filters:\n#            - StripPrefix=1\n        - id: media-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://media-api\n          predicates:\n            - Path=/media/**\n#          filters:\n#            - StripPrefix=1\n        - id: search\n          # uri: http://127.0.0.1:8081\n          uri: lb://search\n          predicates:\n            - Path=/search/**\n#          filters:\n#            - StripPrefix=1\n        - id: auth-service\n          # uri: http://127.0.0.1:8081\n          uri: lb://auth-service\n          predicates:\n            - Path=/auth/**\n#          filters:\n#            - StripPrefix=1\n        - id: checkcode\n          # uri: http://127.0.0.1:8081\n          uri: lb://checkcode\n          predicates:\n            - Path=/checkcode/**\n#          filters:\n#            - StripPrefix=1\n        - id: learning-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://learning-api\n          predicates:\n            - Path=/learning/**\n#          filters:\n#            - StripPrefix=1\n        - id: orders-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://orders-api\n          predicates:\n            - Path=/orders/**\n#          filters:\n#            - StripPrefix=1','d79056569f4f62c53f7eb42108657f62','2022-09-11 09:46:12','2023-06-24 04:32:09','nacos','127.0.0.1','','dev','','','','yaml',''),(107,'media-api-dev.yaml','xuecheng-plus-project','server:\n  servlet:\n    context-path: /media\n  port: 63050\n\n','7e41197fdbb4b6bd1194ae292af19ff8','2022-09-11 10:05:29','2022-09-17 05:26:20',NULL,'192.168.101.1','','dev','','','','yaml',''),(108,'media-service-dev.yaml','xuecheng-plus-project','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://139.198.181.54:3306/xc_media?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: Dcp#7ujm\n  cloud:\n   config:\n    override-none: true\n\nminio:\n  endpoint: http://139.198.181.54:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucket:\n    files: mediafiles\n    videofiles: video\nxxl:\n  job:\n    admin: \n      addresses: http://localhost:8080/xxl-job-admin\n    executor:\n      appname: testHandler\n      address: \n      ip: \n      port: 9999\n      logpath: /data/applogs/xxl-job/jobhandler\n      logretentiondays: 30\n    accessToken: default_token\n\nvideoprocess:\n ffmpegpath: D:/soft/ffmpeg/ffmpeg.exe\n\n','aa75b48c24ed8780ef4a1836df632a4e','2022-09-11 10:05:29','2023-06-22 10:50:44','nacos','127.0.0.1','','dev','xxl.job.admin.addresses','','','yaml',''),(121,'freemarker-config-dev.yaml','xuecheng-plus-common','spring:\r\n  freemarker:\r\n    enabled: true\r\n    cache: false   #关闭模板缓存，方便测试\r\n    settings:\r\n      template_update_delay: 0\r\n    suffix: .ftl   #页面模板后缀名\r\n    charset: UTF-8\r\n    template-loader-path: classpath:/templates/   #页面模板位置(默认为 classpath:/templates/)\r\n    resources:\r\n      add-mappings: false   #关闭项目中的静态资源映射(static、resources文件夹下的资源)\r\n    ','8e97657f299e4a1a6158e8ebf4894e51','2022-09-15 11:18:37','2022-09-15 11:18:37',NULL,'192.168.101.1','','dev',NULL,NULL,NULL,'yaml',NULL),(132,'message-service-dev.yaml','xuecheng-plus-project','spring:\n   datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      primary: content #设置默认的数据源或者数据源组,默认值即为master\n      strict: true #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候回抛出异常,不启动会使用默认数据源.\n      druid:\n        initial-size: 3\n        max-active: 5\n        min-idle: 5\n        max-wait: 60000\n        pool-prepared-statements: true\n        max-pool-prepared-statement-per-connection-size: 20\n        time-between-eviction-runs-millis: 60000\n        min-evictable-idle-time-millis: 300000\n        validation-query: SELECT 1 FROM DUAL\n        test-while-idle: true\n        test-on-borrow: false\n        test-on-return: false\n        stat-view-servlet:\n          enabled: true\n          url-pattern: /druid/*\n          #login-username: admin\n          #login-password: admin\n        filter:\n          stat:\n            log-slow-sql: true\n            slow-sql-millis: 1000\n            merge-sql: true\n          wall:\n            config:\n              multi-statement-allow: true\n      datasource:\n        content:\n          url: jdbc:mysql://192.168.101.65:3306/xcplus_content?serverTimezone=UTC&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8\n          username: root\n          password: mysql\n          driver-class-name: com.mysql.cj.jdbc.Driver\n        media:\n          url: jdbc:mysql://192.168.101.65:3306/xcplus_media?serverTimezone=UTC&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8\n          username: root\n          password: mysql\n          driver-class-name: com.mysql.cj.jdbc.Driver\n\n','7c64bb323a1271815e8aadcd2feffbcd','2022-09-19 10:22:11','2022-09-19 10:25:17',NULL,'192.168.101.1','','dev','','','','yaml',''),(134,'rabbitmq-dev.yaml','xuecheng-plus-common','spring:\n  rabbitmq:\n    host: 192.168.101.65\n    port: 5672\n    username: guest\n    password: guest\n    virtual-host: /\n    publisher-confirm-type: correlated #correlated 异步回调，定义ConfirmCallback，MQ返回结果时会回调这个ConfirmCallback\n    publisher-returns: false #开启publish-return功能，同样是基于callback机制，需要定义ReturnCallback\n    template:\n      mandatory: false #定义消息路由失败时的策略。true，则调用ReturnCallback；false：则直接丢弃消息\n    listener:\n      simple:\n        prefetch: 1  #每次只能获取一条消息，处理完成才能获取下一个消息\n        acknowledge-mode: none #auto:出现异常时返回unack，消息回滚到mq；没有异常，返回ack ,manual:手动控制,none:丢弃消息，不回滚到mq\n        retry:\n          enabled: true #开启消费者失败重试\n          initial-interval: 1000ms #初识的失败等待时长为1秒\n          multiplier: 1 #失败的等待时长倍数，下次等待时长 = multiplier * last-interval\n          max-attempts: 3 #最大重试次数\n          stateless: true #true无状态；false有状态。如果业务中包含事务，这里改为false','9a2acc646d17166ee29989751faceaea','2022-09-20 02:42:44','2022-09-20 05:26:56',NULL,'192.168.101.1','','dev','','','','yaml',''),(145,'feign-dev.yaml','xuecheng-plus-common','feign:\n  client:\n    config:\n      default: # default全局的配置\n        loggerLevel: BASIC # 日志级别，BASIC就是基本的请求和响应信息\n  hystrix:\n    enabled: true\n  circuitbreaker:\n    enabled: true\n  httpclient:\n    enabled: true # 开启feign对HttpClient的支持\n    max-connections: 200 # 最大的连接数\n    max-connections-per-route: 50 # 每个路径的最大连接数\n','2287b4dcf1db4d243a11c74f7a2b6aff','2022-09-20 11:59:29','2022-10-03 01:07:11',NULL,'192.168.101.1','','dev','','','','yaml',''),(152,'search-dev.yaml','xuecheng-plus-project','server:\n  servlet:\n    context-path: /search\n  port: 63080\n\nelasticsearch:\n  hostlist: 139.198.181.54:9200 #多个结点中间用逗号分隔\n  course:\n    index: course-publish\n    source_fields: id,name,grade,mt,st,charge,pic,price,originalPrice,teachmode,validDays,createDate','9d3e45f0d36471102726ca5d51772963','2022-09-24 12:58:14','2023-06-18 01:31:00','nacos','127.0.0.1','','dev','','','','yaml',''),(158,'auth-service-dev.yaml','xuecheng-plus-project','server:\n  servlet:\n    context-path: /auth\n  port: 63070\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://139.198.181.54:3306/xc_users?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: Dcp#7ujm','e843c70a6ea789f3c2ad4c087e6356a7','2022-09-26 12:41:48','2023-06-10 07:39:18','nacos','127.0.0.1','','dev','','','','yaml',''),(163,'checkcode-dev.yaml','xuecheng-plus-project','server:\r\n  servlet:\r\n    context-path: /checkcode\r\n  port: 63075','6033e8fd9f084a4e0019db05d6dc061e','2022-09-29 05:46:44','2022-09-29 05:46:44',NULL,'192.168.101.1','','dev',NULL,NULL,NULL,'yaml',NULL),(165,'learning-service-dev.yaml','xuecheng-plus-project','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://139.198.181.54:3306/xc_learning?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: Dcp#7ujm','03d813986a84e6348a97a4de34f9f98f','2022-10-02 02:22:18','2023-07-12 07:34:37','nacos','127.0.0.1','','dev','','','','yaml',''),(166,'orders-service-dev.yaml','xuecheng-plus-project','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://192.168.101.65:3306/xcplus_orders?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: mysql\n\nxxl:\n  job:\n    admin: \n      addresses: http://192.168.101.65:8088/xxl-job-admin\n    executor:\n      appname: payresultnotify-job\n      address: \n      ip: \n      port: 8989\n      logpath: /data/applogs/xxl-job/jobhandler\n      logretentiondays: 30\n    accessToken: default_token','8da9d76cf7f2d7042fe6fca8a0f128bd','2022-10-02 02:23:03','2022-10-04 22:17:13',NULL,'192.168.101.1','','dev','','','','yaml',''),(167,'learning-api-dev.yaml','xuecheng-plus-project','server:\r\n  servlet:\r\n    context-path: /learning\r\n  port: 63020\r\n\r\n\r\n#配置本地优先\r\nspring:\r\n cloud:\r\n  config:\r\n    override-none: true\r\n','8bbabf9a722ea65b49e13cdcfac95980','2022-10-02 02:23:57','2022-10-02 02:23:57',NULL,'192.168.101.1','','dev',NULL,NULL,NULL,'yaml',NULL),(168,'orders-api-dev.yaml','xuecheng-plus-project','server:\n  servlet:\n    context-path: /orders\n  port: 63030\n\npay:\n alipay:\n   APP_ID: 2016101900725017\n   APP_PRIVATE_KEY: MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCek8F78Zgwdho816Xk3uS8AbR+q1c0NIsfERJjstf5bh+6x2aZUWp2iQdSR6IJVvTd0YUdjj4QyI2e0pBLEQUKO3onk+KSWXwaNSPSOlI7LqnxSUP7KzqBN8bmmZ2b5ywp6AhY9IpfRDHP1je79N0AHHht4q7nruZRLE8nUtxoiLSNRAmTH6Rn0SADT3k91zGJhumxxNDKObZ32kGOJP5GjmNayiQvxXEguPQq9Bw7myAO3eY64rImPO30Vz4VNC0zIZji2im8z5CMsTFGGwu9cJ+njB5g1kfriF1r5XXa1FwIPg64v21tMZLfT5esL8opuV5LLU6c3ZOLzxMfNEvAgMBAAECggEAcs8yp7OjaFJJnZfAPToN+25vYeblEw3AUlqKL/uRIvHdVPiHlOrV0K5dJtPHJN9SnJGQPcMFQBa6jRwRa6WKxf550T00GieZpmBn4Siz9XIwkB2eDhQg1s6wjvZegIqXYq4s7hSKwe0FjX1FMu3ur10Q2B+L2KnEwwm5tu9lijdutAUiRvIYbFFXq6Q1nwpxgbn3Os5r0PZKDO8pQomZnzl9AbAsgeA9f533o7bD2e8khFFyB02TLNTueFX+zTh0xuDyKhdD7Xje6kdGpWNnamUWRBCb9g3N3CcgGD60oZJJVo1zwte/eu9FJypR1H8qoEeJDfbA/Q/DMa5Jim/14QKBgQDTtQqUXPKCG/SR2z7BP5khpg+Fpqe1SYssxIHO2LEudovGUUkDi9u6baTie7cHtvOcD4nqcN2d/Kf8YyGxZ4Pq5EJFS0KplqDQKKxzGHV0xreCKg9iMV3sxJcnCUy5wJHyVZ0wyFopzH6jYmA9CZG0wynifS4zv/0ijCZEGNKSsQKBgQCdxqZHyxcNsg4sXCvuvkWsxAddJKeovDvU8c5ZqcIi6aUaTnL01uBHWZGT0PPPM0Y0W4SzJiEnYUl+MMZZj/VEzOfP2mPKVPH+bc7aT/42WAGpOtZSjl4mFi3KY4NPuO+9zrXlrXazZQhyaEUcFHn5ayygzG68ReSzK2CFIefZ3wKBgFww22GONEC6Yb9eZS6MPmfrw5ik4SVN2GBvVkO4EzgzgVykKxJzRgUiGApUa3jdj6onDhzcd3WD/7cliBeUB1szeTRpuKbXJEJhY+9e+E8Y9fKl1DsjWk5vsY7bOuEs3aFU7PXAWZsYJRGLFnOeBihcUJIDhyob8eSoeUVwNcqRAoGAP2lrw0cYyORbVfDlp1rJ3hoba5Aj4mllErzs1pSn9ig5t0z4XvdNxN925xYAJ7LP8JMnzmjwwkcTpqgr0CtPxIsmUB/SI6voZv3zOUMVRPoyELYZFa0qodwgI0vDpvMJSBwgd2M0Zf2hW3oU7Kg+LcSpdyczCnB87pXsgRWTfbkCgYEAkWy8MCXk0q91QCNNICSAlbvDIK/Mg+eVf1l1ilhrbELaBZlx9Tm9Jwn2Z2PRNAPDbd1FYl/5z8yAWAwf51HzlzXN4CWWCmqG/1ri/yD4geU6utepN9tVYRXLoxOPjIufPgCOSqEXde3C+lF7TleLrwjER720wUv23n/UQ/fas+Y=\n   ALIPAY_PUBLIC_KEY: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoy8UIDCY2ZrrpDRbKIW/Zic3LPBXHGe5nRPLu9t1ud7PRDv5UDzwlhDRwTRnNAe8q3NZJP+ki3NzAY1Ky0QlIJZ6J9R4PRWHshR56U8kReLwuFfhQrYB5aKy8PMpRp41VT39+ywQNlD+UNbziSuRlmT0sjKPM7UCg3D9NucLKlWPfvH5mq+rWIs6pAOfcUDhSOCPS3lgHpMhpr7lYe2RFReKifFsBzEIOWBM8MGbwl0CYyASHKUtydfVDWE2k5g9N7Ypf3QgWYdNpc07vgYjSo3HPl5wLCE7bd7Haphai9gvaGFuEiscApDbQ4b2qWAIpLcwcBJnR+uQbMfYNFr2cQIDAQAB\n\n\n#配置本地优先\nspring:\n cloud:\n  config:\n    override-none: true\n','f048494515f317d930ce66e549e67c00','2022-10-02 02:24:29','2022-10-04 09:00:10',NULL,'192.168.101.1','','dev','','','','yaml',''),(186,'redis-dev.yaml','xuecheng-plus-common','spring: \n  redis:\n    host: 139.198.181.54\n    port: 6379\n    password: Dcp#7ujm\n    database: 0\n    lettuce:\n      pool:\n        max-active: 20\n        max-idle: 10\n        min-idle: 0\n    timeout: 10000\n    #redisson:\n      #配置文件目录\n      #config: classpath:singleServerConfig.yaml','7e85839b6daceb90bc9991ec164b5d3c','2023-06-10 16:54:58','2023-06-10 16:54:58',NULL,'127.0.0.1','','dev',NULL,NULL,NULL,'yaml',NULL);
/*!40000 ALTER TABLE `config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_aggr`
--

DROP TABLE IF EXISTS `config_info_aggr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='增加租户字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_aggr`
--

LOCK TABLES `config_info_aggr` WRITE;
/*!40000 ALTER TABLE `config_info_aggr` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_aggr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_beta`
--

DROP TABLE IF EXISTS `config_info_beta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_info_beta';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_beta`
--

LOCK TABLES `config_info_beta` WRITE;
/*!40000 ALTER TABLE `config_info_beta` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_beta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--

LOCK TABLES `config_info_tag` WRITE;
/*!40000 ALTER TABLE `config_info_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_tags_relation`
--

DROP TABLE IF EXISTS `config_tags_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_tag_relation';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_tags_relation`
--

LOCK TABLES `config_tags_relation` WRITE;
/*!40000 ALTER TABLE `config_tags_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_tags_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_capacity`
--

DROP TABLE IF EXISTS `group_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='集群、各Group容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_capacity`
--

LOCK TABLES `group_capacity` WRITE;
/*!40000 ALTER TABLE `group_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `his_config_info`
--

DROP TABLE IF EXISTS `his_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
  KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE,
  KEY `idx_did` (`data_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='多租户改造';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `his_config_info`
--

LOCK TABLES `his_config_info` WRITE;
/*!40000 ALTER TABLE `his_config_info` DISABLE KEYS */;
INSERT INTO `his_config_info` VALUES (152,200,'search-dev.yaml','xuecheng-plus-project','','server:\n  servlet:\n    context-path: /search\n  port: 63080\n\nelasticsearch:\n  hostlist: 192.168.101.65:9200 #多个结点中间用逗号分隔\n  course:\n    index: course-publish\n    source_fields: id,name,grade,mt,st,charge,pic,price,originalPrice,teachmode,validDays,createDate','9b90f07562994fc7295e4cb5fd43debf','2023-06-18 09:31:01','2023-06-18 01:31:00','nacos','127.0.0.1','U','dev'),(108,201,'media-service-dev.yaml','xuecheng-plus-project','','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://139.198.181.54:3306/xc_media?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: Dcp#7ujm\n  cloud:\n   config:\n    override-none: true\n\nminio:\n  endpoint: http://139.198.181.54:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucket:\n    files: mediafiles\n    videofiles: video\nxxl:\n  job:\n    admin: \n      addresses: http://localhost:8088/xxl-job-admin\n    executor:\n      appname: media-process-service\n      address: \n      ip: \n      port: 9999\n      logpath: /data/applogs/xxl-job/jobhandler\n      logretentiondays: 30\n    accessToken: default_token\n\nvideoprocess:\n ffmpegpath: D:/soft/ffmpeg/ffmpeg.exe\n\n','49b70aeff8837ebce9c312d3e4754e66','2023-06-22 18:50:44','2023-06-22 10:50:44','nacos','127.0.0.1','U','dev'),(105,202,'gateway-dev.yaml','xuecheng-plus-project','','server:\n  port: 63010 # 网关端口\nspring:\n  cloud:\n    gateway:\n#      filter:\n#        strip-prefix:\n#          enabled: true\n      routes: # 网关路由配置\n        - id: content-service # 路由id，自定义，只要唯一即可\n          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址\n          uri: lb://content-service # 路由的目标地址 lb就是负载均衡，后面跟服务名称\n          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件\n            - Path=/content/** # 这个是按照路径匹配，只要以/content/开头就符合要求\n#          filters:\n#            - StripPrefix=1\n        - id: system-service\n          # uri: http://127.0.0.1:8081\n          uri: lb://system-service\n          predicates:\n            - Path=/system/**\n#          filters:\n#            - StripPrefix=1\n        - id: media-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://media-api\n          predicates:\n            - Path=/media/**\n#          filters:\n#            - StripPrefix=1\n        - id: search-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://search-api\n          predicates:\n            - Path=/search/**\n#          filters:\n#            - StripPrefix=1\n        - id: auth-service\n          # uri: http://127.0.0.1:8081\n          uri: lb://auth-service\n          predicates:\n            - Path=/auth/**\n#          filters:\n#            - StripPrefix=1\n        - id: checkcode\n          # uri: http://127.0.0.1:8081\n          uri: lb://checkcode\n          predicates:\n            - Path=/checkcode/**\n#          filters:\n#            - StripPrefix=1\n        - id: learning-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://learning-api\n          predicates:\n            - Path=/learning/**\n#          filters:\n#            - StripPrefix=1\n        - id: orders-api\n          # uri: http://127.0.0.1:8081\n          uri: lb://orders-api\n          predicates:\n            - Path=/orders/**\n#          filters:\n#            - StripPrefix=1','c60dbf27ccc0fc2ee7194d36e9a73858','2023-06-24 12:32:09','2023-06-24 04:32:09','nacos','127.0.0.1','U','dev'),(165,203,'learning-service-dev.yaml','xuecheng-plus-project','','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://192.168.101.65:3306/xcplus_learning?serverTimezone=UTC&userUnicode=true&useSSL=false&\n    username: root\n    password: mysql','8413185b8b7606c892d694b6b9590e17','2023-07-12 15:34:38','2023-07-12 07:34:37','nacos','127.0.0.1','U','dev');
/*!40000 ALTER TABLE `his_config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `role` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `action` varchar(8) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `username` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `role` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('nacos','ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_capacity`
--

DROP TABLE IF EXISTS `tenant_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='租户容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_capacity`
--

LOCK TABLES `tenant_capacity` WRITE;
/*!40000 ALTER TABLE `tenant_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_info`
--

DROP TABLE IF EXISTS `tenant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='tenant_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_info`
--

LOCK TABLES `tenant_info` WRITE;
/*!40000 ALTER TABLE `tenant_info` DISABLE KEYS */;
INSERT INTO `tenant_info` VALUES (1,'1','3adc3388-087e-4c90-a373-c5a6484752df','xc3.0_dev','xc3.0_dev','nacos',1661952703443,1661952703443),(3,'1','dev','开发环境','开发环境','nacos',1662865995686,1662865995686),(4,'1','test','测试环境','测试环境','nacos',1662867234159,1662867234159),(5,'1','prod','生产环境','生产环境','nacos',1662868015781,1662868015781);
/*!40000 ALTER TABLE `tenant_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-23 13:19:44
