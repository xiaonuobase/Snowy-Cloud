<div align="center">
    <p align="center">
        <img src="./_web/public/logo.png" height="150" alt="logo"/>
    </p>
</div>

### 框架介绍
<div align="center"><h3 align="center">Snowy-Cloud为Snowy生态技术框架环境中的cloud微服务版本</h3></div>
<div align="center"><h3 align="center">前后端分离架构，开箱即用，紧随前沿技术</h3></div>

<p align="center">     
    <p align="center">
        <a href="https://www.antdv.com/docs/vue/introduce-cn/">
            <img src="https://img.shields.io/badge/vue--ant--design-2.1.0-blue.svg" alt="bootstrap">
        </a> 
        <a href="https://spring.io/projects/spring-cloud">
            <img src="https://img.shields.io/badge/spring--cloud-Hoxton-orange.svg" alt="spring-cloud">
        </a>
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-2.3.1-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3.3.2-blue.svg" alt="mybatis-plus">
        </a>  
        <a href="./LICENSE">
            <img src="https://img.shields.io/badge/license-Apache%202-red" alt="license Apache 2.0">
        </a>
        <a href="https://gitee.com/xiaonuobase/snowy-cloud">
            <img src="https://gitee.com/xiaonuobase/snowy-cloud/badge/star.svg?theme=dark" alt="Gitee star">
        </a>
        <a href="https://gitee.com/xiaonuobase/snowy-cloud">
            <img src="https://gitee.com/xiaonuobase/snowy-cloud/badge/fork.svg?theme=dark" alt="Gitee fork">
        </a>
    </p>
</p>

### 其他版本

* layui单体版本：[https://gitee.com/xiaonuobase/snowy-layui](https://gitee.com/xiaonuobase/snowy-layui)
* vue前后分离版本：[https://gitee.com/xiaonuobase/snowy](https://gitee.com/xiaonuobase/snowy)
* cloud微服务前后分离版本：[https://gitee.com/xiaonuobase/snowy-cloud](https://gitee.com/xiaonuobase/snowy-cloud)
* 我们的其他产品线同样开源，如需关注最新动态可加入QQ群聊探讨：[732230670](https://wpa.qq.com/msgrd?v=3&uin=732230670&_blank)
* 如果我们的产品能满足您的需求，很期待您给我们右上角点个 star

### 快速启动

您的开发电脑需要安装：NodeJs（最新版）、npm或yarn（最新版）建议使用yarn、Mysql5.7、Jdk1.8

* 启动前端：打开_web文件夹，进行依赖下载，运行npm install或yarn命令，再运行npm run serve或 yarn run serve
* 启动后端：

        1.按顺序启动：
          vip.xiaonuo.eureka.SnowyEurekaApp
          vip.xiaonuo.actuator.SnowyActuatorApp
          vip.xiaonuo.config.SnowyConfigApp
          vip.xiaonuo.gateway.SnowyGatewayApp
          
        2.打开application-local中配置数据库信息，运行 SnowyMainApp 类即可启动

        3.打开application-local中配置数据库信息，运行 SnowySampleApp 类即可启动
        
* 浏览器访问：<http://localhost:9000> （默认前端端口为：9000，后端端口为：9004）

### 框架说明及后续补充

* 纯手研发搭建框架脚手架，在自己用的时候，也为各位小伙伴打下坚固的接私活利器。
* 后续我们会行发多个版本，将适配多个数据库环境，国产化环境，并且根据多年经验会出相关系统中用到的案例，提供给大家使用！
* 如需了解我们更多，请移步官网：[https://xiaonuo.vip](https://xiaonuo.vip)
* 当然，有问题讨论的小伙伴还可以加入我们的QQ技术群：[732230670](https://wpa.qq.com/msgrd?v=3&uin=732230670&_blank)，一起学习讨论。

### 在线演示

* 账号密码：`superAdmin/123456`，地址：[https://cloud.xiaonuo.vip](https://cloud.xiaonuo.vip)

### 在线文档

* https://doc.xiaonuo.vip

### 架构原理图
* 业务架构
<p align="center">
    <img src="https://pan.xiaonuo.vip/?explorer/share/fileOut&shareID=68yzjuiA&path=%7BshareItemLink%3A68yzjuiA%7D%2F"/>
</p>

* 应用架构
<p align="center">
    <img src="https://pan.xiaonuo.vip/?explorer/share/fileOut&shareID=68yzy4Aw&path=%7BshareItemLink%3A68yzy4Aw%7D%2F"/>
</p>

* 数据架构
<p align="center">
    <img src="https://pan.xiaonuo.vip/?explorer/share/fileOut&shareID=68y0EfOg&path=%7BshareItemLink%3A68y0EfOg%7D%2F"/>
</p>

* 技术架构
<p align="center">
    <img src="https://pan.xiaonuo.vip/?explorer/share/fileOut&shareID=68y0Q2EA&path=%7BshareItemLink%3A68y0Q2EA%7D%2F"/>
</p>

* 部署架构
<p align="center">
    <img src="https://pan.xiaonuo.vip/?explorer/share/fileOut&shareID=68y0jARw&path=%7BshareItemLink%3A68y0jARw%7D%2F"/>
</p>

### 效果图

<table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-62d4b535dadbfa8ff343cb290d58be43ef0.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-98b3e79f8008b6319ce6394d80172ff02a3.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2020/1208/133142_37420daa_1980003.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2020/1208/133250_3749a395_1980003.jpeg"/></td>
    </tr>
</table>

### 框架优势

1. 模块化架构设计，层次清晰，业务层推荐写到单独模块，方便升级。
2. 前后端分离架构，分离开发，分离部署，前后端互不影响。
3. 前端技术采用vue + antdvPro + axios。
3. 后端采用spring cloud + spring boot + mybatis-plus + hutool等，开源可靠。
4. 基于spring security(jwt) + 用户UUID双重认证。
5. 基于AOP实现的接口粒度的鉴权，最细粒度过滤权限资源。
6. 基于hibernate validator实现的校验框架，支持自定义校验注解。
7. 提供Request-No的响应header快速定位线上异常问题。
8. 在线用户可查，可在线踢人，同账号登录可同时在线，可单独在线（通过系统参数配置）。
9. 支持前端 + 后端在线代码生成（开发中）。
10. 文件，短信，缓存，邮件等，利用接口封装，方便拓展。
11. 文件默认使用本地文件，短信默认使用阿里云sms，缓存默认使用内存缓存。

### 详细功能

1. 主控面板、控制台页面，可进行工作台，分析页，统计等功能的展示。
2. 用户管理、对企业用户和系统管理员用户的维护，可绑定用户职务，机构，角色，数据权限等。
3. 应用管理、通过应用来控制不同维度的菜单展示。
4. 机构管理、公司组织架构维护，支持多层级结构的树形结构。
5. 职位管理、用户职务管理，职务可作为用户的一个标签，职务目前没有和权限等其他功能挂钩。
6. 菜单管理、菜单目录，菜单，和按钮的维护是权限控制的基本单位。
7. 角色管理、角色绑定菜单后，可限制相关角色的人员登录系统的功能范围。角色也可以绑定数据授权范围。
8. 字典管理、系统内各种枚举类型的维护。
9. 访问日志、用户的登录和退出日志的查看和管理。
10. 操作日志、用户的操作业务的日志的查看和管理。
11. 服务监控、服务器的运行状态，Java虚拟机信息，jvm等数据的查看。
12. 在线用户、当前系统在线用户的查看。
13. 数据监控、druid控制台功能，可查看sql的运行信息。
14. 公告管理、系统的公告的管理。
15. 文件管理、文件的上传下载查看等操作，文件可使用本地存储，阿里云oss，腾讯cos接入，支持拓展。
16. 定时任务、定时任务的维护，通过cron表达式控制任务的执行频率。
17. 系统配置、系统运行的参数的维护，参数的配置与系统运行机制息息相关。
18. 邮件发送、发送邮件功能。
19. 短信发送、短信发送功能，可使用阿里云sms，腾讯云sms，支持拓展。

### 官方微信群

##### 因群达到200人以上，需加微信拉群

<table>
    <tr>
        <td>微信群</td>
        <td><img src="https://pan.xiaonuo.vip/?explorer/share/file&hash=ecc5LVAwwRuysFB28xQQb1p3IPC13hzAtji_Sxyr42naNTnj7xurmI8&name=weixin.png" width="120"/></td>
    </tr>
</table>

### 参与贡献

- 欢迎各路英雄好汉参与Snowy全系版本代码贡献，期待您的加入！
- 1.  Fork 本仓库
- 2.  新建 Feat_xxx 分支
- 3.  提交代码
- 4.  新建 Pull Request

### 更新日志：

更新日志:
[https://doc.xiaonuo.vip/snowy_cloud/#%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97
](https://doc.xiaonuo.vip/snowy_cloud/#%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97
)

### 版权说明

- Snowy生态技术框架全系版本采用 Apache License2.0协议
- 代码可用于个人项目等接私活或企业项目脚手架使用，Snowy全系开源版完全免费
- 二次开发请不要删除和修改根目录下的LICENSE文件及代码头部作者声明。
- 请不要删除和修改Snowy源码头部的版权声明及出处。


### 小诺技术团队荣誉作品

| 成员组成 | 负责内容 |
| :---: | :---: |
| 俞宝山 | 全栈 |
| 徐玉祥 | 全栈 |
| 董夏雨 | 全栈 |

