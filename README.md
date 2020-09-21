# 简介

## 什么是Mars系统

Mars系统是Advance.ai公司Atome业务线团队开发的测试平台，该平台包括用例平台、数据平台、mock工厂、环境服务器监控、代码覆盖率统计等功能，用于提升测试人员和开发人员的工作效率。

## 系统介绍图

![系统介绍图](https://res.cloudinary.com/dqhbr3uh3/image/upload/v1600502704/mars%E7%B3%BB%E7%BB%9F%E4%BB%8B%E7%BB%8D%E5%9B%BE_b9ev02.png)

## 开始上手

``` bash
git clone git@github.com:Atome-FE/Mars.git
```

### 配置

1、修改server下的，- application-local.yml里的spring > datasource > url 的IP为本机IP

2、[下载gradle](https://www.cnblogs.com/ycyzharry/p/11089016.html)

3、安装docker并登录

### 启动

```bash
# 启动前端:
./start.sh init-f # 首次启动(包含npm install)

./start.sh f # 以后启动命令

# 启动后端:
./start.sh b

```

其目录结构大致如下：

```
├─ client                 # 前端代码
├─ dev-docker
│  ├─ base                # mysql初始化数据
│  └─ docker-compose.yml  # 启动 mysql和后端接口
├─ server                 # 后端代码
├─ docs
│  ├─ README.md
│  ├─ zh
│  └─ .vuepress
│     └─ config.js
└─ ...
```
