# jmilter-InfoMilter
Milter for Sendmail or Postfix to log all possible parts of the e-Mail communication.

The project was based on the great work of Dmitry from nightcode.org which you can download from here: ![nightcode/jmilter](https://github.com/nightcode/jmilter)

## JAR-File
The creation of the **InfoMilter.jar** was done with following directory structure:
```
javax.inject.jar
jmilter-0.1.2.jar
jsr305-1.3.9.jar
log4j-api-2.11.1.jar
log4j-core-2.11.1.jar
netty-buffer-4.1.24.Final.jar
netty-codec-4.1.24.Final.jar
netty-common-4.1.24.Final.jar
netty-handler-4.1.24.Final.jar
netty-transport-4.1.24.Final.jar
yaranga-0.5.5.jar
log4j2.xml
net/tachtler/jmilter/InfoMilterHandler.java
net/tachtler/jmilter/package-info.java
net/tachtler/jmilter/InfoMilter.java
```

The uploaded **InfoMilter.jar** was created with follwing commands:

Command for the \*.class compilation:)

(**Linux**):
```
javac -g:none -target 1.8 -cp .:*  net/tachtler/jmilter/*.java
```
(**Windows**):
```
"<PATH-TO-JDK>\bin\javac.exe" -g:none -target 1.8 -cp *  net\tachtler\jmilter\*.java
```

Command for the JAR file creation:

(**Linux**):
```
jar -cvfe InfoMilter.jar *.jar log4j2.xml net/tachtler/jmilter/*.class
```
(**Windows**):
```
<PATH-TO-JDK>\bin\jar.exe" -cvfe InfoMilter.jar *.jar log4j2.xml net\tachtler\jmilter\*.class
```

## JAR-File execution
The **execution of the JAR file** could be done, for example on a Linux ```shell``` with following command:
```bash
# java -jar InfoMilter.jar 
Nov 03, 2018 11:31:58 AM org.nightcode.common.service.AbstractService started
INFO: [MilterGatewayManager]: service has been started
^C
```
and could be **stopped** with **[CTRL-c]** key combination.

Under **Linux**, you can see with following command, on which **ipv4-address and port** the **InfoMilter.jar** was bind to:
```bash
# netstat -tulpen | grep java
tcp       0      0 127.0.0.1:10099        0.0.0.0:*            LISTEN      1000       38821      2520/java
```

## Postfix Milter integration
In order to include InforMilter.jar with ![Postfix](http://www.postfix.org/), minimal adjustments are required in the two following configuration files of Postfix.
  - ```/etc/postfix/main.cf```
  - ```/etc/postfix/master.cf```

##### ```/etc/postfix/main.cf``` 
(Only relevant part of the configuration file!)
```
# --------------------------------------------------------------------------------
# New - http://www.postfix.org/MILTER_README.html
# MILTER CONFIGURATIONS
# --------------------------------------------------------------------------------

# JMilter (info_milter)
info_milter = inet:127.0.0.1:10099
```

##### ```/etc/postfix/master.cf```

(Only relevant part of the configuration file!)
```
#
# Postfix master process configuration file.  For details on the format
# of the file, see the master(5) manual page (command: "man 5 master").
#
# Do not forget to execute "postfix reload" after editing this file.
#
# ==========================================================================
# service type  private unpriv  chroot  wakeup  maxproc command + args
#               (yes)   (yes)   (yes)   (never) (100)
# ==========================================================================
smtp      inet  n       -       n       -       -       smtpd
# InfoMilter
   -o smtpd_milters=${info_milter}
```

:exclamation: **After the changes in the configuration files, you have to restart the Postfix Daemon!**
