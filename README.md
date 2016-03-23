AquaLauder Management
===============
### ENVIRONMENT

* JDK 7+
* Tomcat 7+
* Mysql 5+

### HOW TO DEPLOY
###### 数据源配置
```
edit /conf/application.conf
db.default.url=jdbc:mysql://host/db?useUnicode=true&characterEncoding=utf8  
db.default.driver=com.mysql.jdbc.Driver
db.default.user=****
db.default.pass=****
```
###### 打包部署
````
#git clone ${URL}
# play run aqualauder
or
# play war aqualauder -o ./aqualauder.war --zip
# mv aqualauder.war ${webServerWorkingDirectory}
````
