# Mars
## 介绍
mars系统是----

支持redis、http、sql、mongo、rabbitmq、延迟执行、参数替换、断言、运行报告

## 系统功能

自定义mock数据

服务监控

一键运行所有测试用例

一键生成指定数据

## 一、mysql
dev-docker目录下一键启动mysql，端口号为6606，请确保已安装docker（只在开发环境）

```
docker-compose up
```
## 二、server
### 下载gradle
eg: https://www.cnblogs.com/ycyzharry/p/11089016.html

### 打包
```
gradle build -x test // 打包
```
### 运行
jar包在build/libs/里面

在运行jar之前确保配置文件和jar包在同级目录
把application-local.yml, application.yml, logback-spring.xml等文件移到
jar包所在的目录。
```
cd build/libs
java -jar suda-0.0.1-SNAPSHOT.jar // 运行
```

## 三、client
```
cd client
npm install
npm run serve
```

## *如果基础依赖都下载过，则只看以下操作即可

```
1、修改server下的，- application-local.yml里的spring > datasource > url 的ip

2、chmod u+x ./start.sh

启动前端: ./start.sh init-f (首次)，以后执行 ./start.sh f 即可
启动后端: ./start.sh b

```
默认账号：

root@admin.com
123456