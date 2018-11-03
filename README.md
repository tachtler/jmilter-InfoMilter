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

#### ```/etc/postfix/main.cf``` 
(Only relevant part of the configuration file!)
```
# --------------------------------------------------------------------------------
# New - http://www.postfix.org/MILTER_README.html
# MILTER CONFIGURATIONS
# --------------------------------------------------------------------------------

# JMilter (info_milter)
info_milter = inet:127.0.0.1:10099
```

#### ```/etc/postfix/master.cf```

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

## Telnet e-Mail-Test
The following **telnet session** will show you the usage of the InfoMilter.jar in combination with ```telnet```.
```bash
# telnet 127.0.0.1 25
Trying 127.0.0.1...
Connected to 127.0.0.1.
Escape character is '^]'.
220 test.example.com ESMTP Postfix
ehlo test.example.com
250-test.example.com
250-PIPELINING
250-SIZE 10240000
250-VRFY
250-ETRN
250-ENHANCEDSTATUSCODES
250-8BITMIME
250 DSN
mail from: <root@example.com>
250 2.1.0 Ok
rcpt to: <klaus@example.com>
250 2.1.5 Ok
data
354 End data with <CR><LF>.<CR><LF>
Subject: Test e-Mail.
From: sender@example.com
To: receiver@example.com

Hello,

test e-Mail.

Greetings
.
250 2.0.0 Ok: queued as 99AB86E6A8D
quit
221 2.0.0 Bye
Connection closed by foreign host.
```

## LOG-File output
While using the InfoMilter.jar the following output will be written to the **standard command output (```screen / shell```) and a folder named logs will be created, which includes the ```jmilter.log``` file and \*.tar.gz files with older logs.**

An **example output file** could be look like this. (It's based on the **Telnet e-Mail-Test** - **telnet-session** shown above):
```
----------------------------------------: 
JMilter - ENTRY: connect                : MilterContext context, String hostname, @Nullable InetAddress address
----------------------------------------: 
*hostname                               : localhost
*address.getCanonicalHostName()         : localhost
*address.getHostAddress()               : 127.0.0.1
*address.getHostName()                  : localhost
*address.getAddress()                   : Octet: [127, 0, 0, 1] / Byte: [127, 0, 0, 1]
*address.isAnyLocalAddress()            : false
*address.isLinkLocalAddress()           : false
*address.isLoopbackAddress()            : true
*address.isMCGlobal()                   : false
*address.isMCLinkLocal()                : false
*address.isMCNodeLocal()                : false
*address.isMCOrgLocal()                 : false
*address.isMCSiteLocal()                : false
*address.isMulticastAddress()           : false
*address.isReachable(timeout)           : true
*netif.getDisplayName()                 : lo
*netif.getIndex()                       : 1
*netif.getMTU()                         : 65536
*netif.getName()                        : lo
*netif.getHardwareAddress()             : null
*netif.getInetAddresses()               : /127.0.0.1
*netif.getInterfaceAddresses()          : [/127.0.0.1/8 [null]]
*netif.getParent()                      : null
*netif.isLoopback()                     : true
*netif.isPointToPoint()                 : false
*netif.isUp()                           : true
*netif.isVirtual()                      : false
*netif.supportsMulticast()              : false
*address.isReachable(netif, ttl, time...: true
*address.isSiteLocalAddress()           : false
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : CONNECT
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
----------------------------------------: 
JMilter - LEAVE: connect                : MilterContext context, String hostname, @Nullable InetAddress address
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: helo                   : MilterContext context, String helohost
----------------------------------------: 
*helohost                               : test.example.com
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : HELO
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
----------------------------------------: 
JMilter - LEAVE: helo                   : MilterContext context, String helohost
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: envfrom                : MilterContext context, List<String> from
----------------------------------------: 
*from.get(i)                            : [0] <root@example.com>
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : MAIL_FROM
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
----------------------------------------: 
JMilter - LEAVE: envfrom                : MilterContext context, List<String> from
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: envrcpt                : MilterContext context, List<String> recipients
----------------------------------------: 
*recipients.get(i)                      : [0] <klaus@example.com>
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : RECIPIENTS
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
----------------------------------------: 
JMilter - LEAVE: envrcpt                : MilterContext context, List<String> recipients
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: data                   : MilterContext context, byte[] payload
----------------------------------------: 
*payload                                : null
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : DATA
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: data                   : MilterContext context, byte[] payload
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
*headerName: headerValue                : Subject: Test e-Mail.
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : HEADERS
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
*headerName: headerValue                : From: sender@example.com
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : HEADERS
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
*headerName: headerValue                : To: receiver@example.com
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : HEADERS
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
*headerName: headerValue                : Message-Id: <20181103101829.99AB86E6A8D@test.example.com>
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : HEADERS
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
*headerName: headerValue                : Date: Sat,  3 Nov 2018 11:18:21 +0100 (CET)
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : HEADERS
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: header                 : MilterContext context, String headerName, String headerValue
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: eoh                    : MilterContext context
----------------------------------------: 
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : EOH
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
*context.getMacros(SMFIC_EOH)           : {i=99AB86E6A8D}
*context.getMacros(SMIFC_EOH)|("i")     : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: eoh                    : MilterContext context
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: body                   : MilterContext context, String bodyChun
----------------------------------------: 
*bodyChunk <-- (Start at next line) --> : 
Hello,

test e-Mail.

Greetings

*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : BODY
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
*context.getMacros(SMFIC_EOH)           : {i=99AB86E6A8D}
*context.getMacros(SMIFC_EOH)|("i")     : 99AB86E6A8D
*context.getMacros(SMIFC_BODY)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODY)|("i")    : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: body                   : MilterContext context, String bodyChun
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: eom                    : MilterContext context, @Nullable String bodyChunk
----------------------------------------: 
*bodyChunk <-- (Start at next line) --> : 
null
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : EOM
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
*context.getMacros(SMFIC_EOH)           : {i=99AB86E6A8D}
*context.getMacros(SMIFC_EOH)|("i")     : 99AB86E6A8D
*context.getMacros(SMIFC_BODY)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODY)|("i")    : 99AB86E6A8D
*context.getMacros(SMIFC_BODYEOB)       : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODYEOB)|("i") : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: eom                    : MilterContext context, @Nullable String bodyChunk
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: abort                  : MilterContext context, MilterPacket packet
----------------------------------------: 
*packet                                 : MilterPacket{command=65, payload=EMTPY}
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : EOM
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
*context.getMacros(SMFIC_EOH)           : {i=99AB86E6A8D}
*context.getMacros(SMIFC_EOH)|("i")     : 99AB86E6A8D
*context.getMacros(SMIFC_BODY)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODY)|("i")    : 99AB86E6A8D
*context.getMacros(SMIFC_BODYEOB)       : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODYEOB)|("i") : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: abort                  : MilterContext context, MilterPacket packet
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: abort                  : MilterContext context, MilterPacket packet
----------------------------------------: 
*packet                                 : MilterPacket{command=65, payload=EMTPY}
*context.getMtaProtocolVersion()        : 6
*context.getSessionProtocolVersion()    : 6
*ontextt.milterProtocolVersion()        : 6
*context.PROTOCOL_VERSION               : 6
*context.getMacros(ttl)                 : null
*context.getMacros(timeout)             : null
*context.getMtaActions()                : Actions: 000001FF
*context.getMtaProtocolSteps()          : ProtocolSteps: 001FFFFF
*context.getSessionProtocolSteps()      : ProtocolSteps: 00000000
*context.getSessionState()              : EOM
*context.id()                           : 3c83c09a-3267-489a-8e7c-d64c741c82e5
*context.milterActions()                : Actions: 00000001
*context.milterProtocolSteps()          : ProtocolSteps: 00000000
*context.getMacros(SMIFC_CONNECT)       : {v=Postfix 2.10.1, {daemon_name}=test.example.com, j=test.example.com}
*context.getMacros(SMIFC_CONNECT)|("v") : Postfix 2.10.1
*context.getMacros(SMIFC_CONNECT)|("{...: test.example.com
*context.getMacros(SMIFC_CONNECT)|("j") : test.example.com
*context.getMacros(SMIFC_HELO)          : {}
*context.getMacros(SMIFC_MAIL)          : {{mail_host}=mx1.example.com, {mail_mailer}=smtp, {mail_addr}=root@example.com}
*context.getMacros(SMIFC_MAIL)|("{mai...: mx1.example.com
*context.getMacros(SMIFC_MAIL)|("{mai...: smtp
*context.getMacros(SMIFC_MAIL)|("{mai...: root@example.com
*context.getMacros(SMIFC_RCPT)          : {{rcpt_mailer}=smtp, {rcpt_addr}=klaus@example.com, {rcpt_host}=mx1.example.com}
*context.getMacros(SMIFC_RCPT)|("{rcp...: smtp
*context.getMacros(SMIFC_RCPT)|("{rcp...: klaus@example.com
*context.getMacros(SMIFC_RCPT)|("{rcp...: mx1.example.com
*context.getMacros(SMIFC_DATA)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_DATA)|("i")    : 99AB86E6A8D
*context.getMacros(SMFIC_HEADER)        : {i=99AB86E6A8D}
*context.getMacros(SMIFC_HEADER)|("i")  : 99AB86E6A8D
*context.getMacros(SMFIC_EOH)           : {i=99AB86E6A8D}
*context.getMacros(SMIFC_EOH)|("i")     : 99AB86E6A8D
*context.getMacros(SMIFC_BODY)          : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODY)|("i")    : 99AB86E6A8D
*context.getMacros(SMIFC_BODYEOB)       : {i=99AB86E6A8D}
*context.getMacros(SMIFC_BODYEOB)|("i") : 99AB86E6A8D
----------------------------------------: 
JMilter - LEAVE: abort                  : MilterContext context, MilterPacket packet
----------------------------------------: 
----------------------------------------: 
JMilter - ENTRY: close                  : MilterContext arg0
----------------------------------------: 
----------------------------------------: 
JMilter - LEAVE: close                  : MilterContext arg0
----------------------------------------:
```
