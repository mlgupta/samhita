#!/bin/sh
export ORACLE_HOME=/u01/apps/oracle/ias
export CLASSPATH=./classes/:./lib/wfjava.jar:$ORACLE_HOME/lib/xmlparserv2.jar:$ORACLE_HOME/jlib/fndctx.jar:./lib/wfapi.jar:./lib/classes12.jar:$ORACLE_HOME/jlib/share.jar:$ORACLE_HOME/jlib/ewt3.jar:$ORACLE_HOME/jlib/ewt3-nls.jar:$ORACLE_HOME/jlib/swingall-1_1_1.jar:./classes:./lib/log4j-1.2.8.jar:./lib/cmsdk.jar
$ORACLE_HOME/jdk/jre/bin/java  oracle.apps.fnd.wf.WFFALsnr $1 | tee -a $LOG

