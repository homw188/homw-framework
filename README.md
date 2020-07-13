# Architecture
![Arch](https://github.com/homw188/homw-framework/blob/master/resources/images/arch_v1.0.png)

# Useage
## homw-tool
```
mvn package -Dmaven.test.skip=true
bash {project}/bin/run.sh
```

## homw-schedule
```
mvn package -Dmaven.test.skip=true
cp ${project}/target/${project}-${version}.war ${tomcat}/webapps/
source ${project}/src/main/resources/homw_schedule.sql
bash ${tomcat}/bin/startup.sh
http://{hostname}:{port}/${project}-${version}
username=admin, password=admin
```

## homw-modbus
```
mvn package -Dmaven.test.skip=true
java -jar ${project}/target/${project}-${version}.jar
```
