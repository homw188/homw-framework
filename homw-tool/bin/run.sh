#!/bin/bash

VERSION=1.0.0
JAR_FILE=homw-tool

cygwin=false
darwin=false
os400=false
case "`uname`" in
  CYGWIN*) cygwin=true;;
  Darwin*) darwin=true;;
  OS400*) os400=true;;
esac

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

if [ -z "$JAVA_HOME" ]; then
  if $darwin; then
    if [ -x "/usr/libexec/java_home" ]; then
      export JAVA_HOME="/usr/libexec/java_home"
    elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
      export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
    fi
  else
    JAVA_PATH=`dirname $(readlink -f $(which java))`
    if [ "x$JAVA_PATH" != "x" ]; then
      export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
    fi
  fi
  
  if [ -z "$JAVA_HOME" ]; then
    echo "Please set the JAVA_HOME variable in your environment, we need java(x64)! jdk7 or later is better!"
    exit 1
  fi
fi

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`

JAVA_OPT="-Xms128m -Xmx128m -Xmn64m"
JAVA_OPT="$JAVA_OPT -jar $BASE_DIR/target/${JAR_FILE}-${VERSION}.jar"

$JAVA $JAVA_OPT $*