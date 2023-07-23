# 项目环境简介
以Mac电脑为例

开发环境：jdk1.8

数据库版本：8.x

Mac上安装nginx与Linux安装方法相同

Ngnix安装好后，nginx配置文件相关内容拷贝到已安装好的nginx配置文件中

xc-ui-pc-static-portal是网站前台，路径配置到nginx，nginx启动后，访问：http://www.51xuecheng.cn/

配置hosts

Windows类似

# 服务说明
project-xczx2-portal-vue-ts：前端管理后台

xc-ui-pc-static-portal：网站前台的前端

xuecheng-plus-nacos：Nacos服务中心

xuecheng-plus-auth：认证服务

xuecheng-plus-base：基础工程

xuecheng-plus-checkcode：验证码服务

xuecheng-plus-content：内容管理服务

xuecheng-plus-gateway：网关

xuecheng-plus-generator：代码生成器

xuecheng-plus-learning：选课、学习中心服务

xuecheng-plus-media：媒资管理服务

xuecheng-plus-message-sdk：消息服务

xuecheng-plus-search：搜索服务

xuecheng-plus-system：系统管理服务

xxl-job：任务调度服务

# docx文件说明

mysqlBackup.sh Linux中的数据库导出SQL文件的shell脚本

hosts.txt  hosts文件内容

# 技术描述

文件服务器：minio
搜索服务：elasticsearch

# 项目启动

1.数据库脚本依次导入db文件夹下面的SQL脚本

2.无需自行再下载nacos启动文件，直接启动项目中的nacos即可

3.Nginx需安装启动，Nginx配置文件替换为docx文件夹里面的配置文件

4.配置本地hosts

5.推荐Redis：6.x，数据库MySQL：8.x

6.nacos配置自行替换为你用的数据库

项目启动完成后访问：http://www.51xuecheng.cn/#/ 即可
