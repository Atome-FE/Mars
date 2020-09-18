### 简介
后端系统
用来保存测试人员录入的数据

### 表结构
在根级目录的sql文件中

### 打包
```
gradle build -x test
```

jar包在build/libs/里面

在运行jar之前确保配置文件和jar包在同级目录

把application-local.yml, application.yml, logback-spring.xml等文件移到
jar包所在的目录。
修改application.yml文件中active为local

### 运行
```
cd build/libs
java -jar suda-0.0.1-SNAPSHOT.jar
```

暴露的端口为9098