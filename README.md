# homw-framework
Focus on buiding reuseable and extensible application framework.

# The framework consists of the following modules
## homw-common
Abstract and extract the application of common content.

## homw-dao-support
Data access layer abstraction. 
Integration [MyBatis framework](https://blog.mybatis.org/) and provide basic configurations.

## homw-web-support
Web layer abstraction. 
Integration [Springmvc framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) and provide custom web components & spring context util.

## homw-message
Message service module. 
Support locale message, note message, push message services. 
Note message use [yuntongxun platform](https://www.yuntongxun.com/). 
Push message use [JPush plugin](https://www.yuntongxun.com/).

## homw-schedule
The module provides a scheduled task scheduing function, and base on [Quartz framework](http://www.quartz-scheduler.org/).
```
Useage:
1. cd ${homw-schedule} && mvn clean package -Dmaven.test.skip=true
2. cp target/homw-schedule.war ${tomcat-home}/webapps/
3. connect mysql server & create database homw_schedule & source ${homw-schedule}/src/main/resources/homw_schedule.sql
4. cd ${tomcat-home} && ./bin/startup.sh
5. open web browser & input http://{hostname}:{port}/homw-schedule
6. sign in (default account is: username=admin, password=admin123)
```

## homw-modbus
The module provides a java implement for modbus protocol and compatible modbus rtu & tcp protocol. 
It base on [Netty framework](https://netty.io/) implementation.
```
Useage:
1. cd ${homw-modbus} && mvn clean package -Dmaven.test.skip=true
2. java -jar target/homw-modbus.jar Main server
3. java -jar target/homw-modbus.jar Main client
```

## homw-robot
The module provides a robot host client implementation, base on [Netty framework](https://netty.io/).

## homw-tool
The module provides a programmable application toolkit, use to resolve problems at work.
```
Useage:
1. cd ${homw-tool} && mvn clean package -Dmaven.test.skip=true
2. java -jar target/homw-tool.jar xxxApp xxxAppParams
```

## homw-test
The module provides a series of experimental tests for a variety of techs.
