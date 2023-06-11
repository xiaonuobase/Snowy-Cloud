# Nacos注册中心应用启动组件

#### Nacos 相关资料
* Gihub 发布版本下载地址：https://github.com/alibaba/nacos/releases
* Gitee 发布版本下载地址：https://gitee.com/mirrors/Nacos/releases
* 目前版本 Snowy-Cloud 使用的 Nacos 版本为：2.1.2.RELEASE

#### Nacos 开发环境启动
* mvn install 安装 snowy-nacos-app/pom.xml
* 创建 Snowy-Cloud 使用的nacos数据库 snowy-nacos-app
* 导入 snowy-nacos-app db 初始化数据
```
src/main/resources/META-INF/nacos-db.sql
```
* 导入 snowy-nacos-app db 更新数据
```
src/main/resources/META-INF/update/nacos-db-update-20230611.sql
```
* 运行 com.alibaba.nacos.SnowyNacosApp 类，即可在开发环境启动 nacos 2.1.2

#### Snowy-Cloud Nacos配置数据
* src/main/resources/META-INF/config-data

#### Nacos 服务器方式启动
* 建议使用官方的编译完成的部署包进行 nacos 集群搭建

#### 小诺网盘资料备份
* 【nacos-2.1.2.zip】 工程源码： https://pan.xiaonuo.vip/#s/9Ws_e8OA
* 【nacos-server-2.1.2.zip】 部署包： https://pan.xiaonuo.vip/#s/9Ws_tIFg
* 【nacos-server-2.1.2.zip】 部署包： https://pan.xiaonuo.vip/#s/9Ws_8ENQ