##### Downer
The goal of this project is to achieve all of the downloadable url of Files which in the website. 
 [linuxidc](http://linux.linuxidc.com "linuxidc")
* prepare
   >1.you need install [mysql](http://mysql.org","mysql") database on your computer;  
   >2.replace the **user** and **password** and **port** with yours.  
   >3.create database named ***linuxidc*** on your database.
   
   
* compile
   >1.install [maven](http://maven.org,"mavne").  
   >2.setting environment variables about maven.   
   >3.$mvn clean package  
   >4.you can find the runnable jar in folder of 'target'.
* usage
    >$>java -jar Down-1.0-snapshot-withdependency.jar action [option]
    >> java -jar Down-1.0-snapshot-withdependency.jar --help will print help mesage.
    
    
    
* issue
   > when you package the project,the maven may be told you,it can't find the mysql-connector-java.jar,
   now you should navigate to the folder of maven local repository on your computer.create relateive folder
   and .pom file within pom.xml in section <dependency>. then run 'mvn clean package' again.
   
