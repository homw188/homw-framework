# Architecture
![Arch](https://github.com/homw188/homw-framework/blob/master/resources/images/arch_v1.0.png)

# Useage
## homw-tool
```
1. package: mvn clean package -Dmaven.test.skip=true
2. exec: java -jar ${project}/target/homw-tool.jar
```

## homw-schedule
```
1. package: mvn clean package -Dmaven.test.skip=true
2. deploy: cp ${project}/target/homw-schedule.war ${tomcat}/webapps/
3. prepare data: create database homw_schedule
4.               import ${project}/src/main/resources/homw_schedule.sql
5. start server: bash ${tomcat}/bin/startup.sh
6. broswer access: http://{hostname}:{port}/homw-schedule
7. login: username=admin, password=admin
```

## homw-modbus
```
1. package: mvn clean package -Dmaven.test.skip=true
2. start server: java -jar ${project}/target/homw-modbus.jar server
3. start client: java -jar ${project}/target/homw-modbus.jar client
```
