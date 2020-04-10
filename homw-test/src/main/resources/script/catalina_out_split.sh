#!/bin/bash
# author: Hom
# date: 2019-12-12
# description: 按天切分catalina.out日志脚本
#####################################################################

# 常量定义
CATALINA_HOME="/data/application/apache-tomcat-appName"
CATALINA_LOG_DIR=$CATALINA_HOME"/logs"
CATALINA_LOG_FILE=$CATALINA_LOG_DIR"/catalina.out"
CATALINA_LOG_BAK_DIR=$CATALINA_LOG_DIR"/catalina_out_bak"

# 当前格式化日期
TODAY=$(date +%Y-%m-%d)
EXPIRE_DAY_NUM=30
BAK_LOG_EXTENSION=".out.tar.gz"

BAK_FILE_NAME="catalina."$TODAY".out"
BAK_GZ_FILE_NAME="catalina."$TODAY$BAK_LOG_EXTENSION
CATALINA_LOG_BAK_FILE=$CATALINA_LOG_BAK_DIR"/"$BAK_FILE_NAME
CATALINA_LOG_BAK_GZ_FILE=$CATALINA_LOG_BAK_DIR"/"$BAK_GZ_FILE_NAME

# 调试开关
DEBUG=false
#####################################################################

# 创建备份目录
if [ ! -e $CATALINA_LOG_BAK_DIR ]; then
    if $DEBUG; then echo "mkdir -p $CATALINA_LOG_BAK_DIR"; fi
    mkdir -p $CATALINA_LOG_BAK_DIR
fi

# 备份日志文件
if [ ! -e $CATALINA_LOG_BAK_GZ_FILE ]; then
    # 检查日志是否存在
    if [ ! -e $CATALINA_LOG_FILE ]; then
        echo "$CATALINA_LOG_FILE not exist, exit!"
        exit 0
    fi

    # 备份日志
    if [ ! -e $CATALINA_LOG_BAK_FILE ]; then
        if $DEBUG; then echo "cat $CATALINA_LOG_FILE > $CATALINA_LOG_BAK_FILE"; fi
        cat $CATALINA_LOG_FILE > $CATALINA_LOG_BAK_FILE
    else
        if $DEBUG; then echo "cp -f $CATALINA_LOG_FILE $CATALINA_LOG_BAK_FILE"; fi
        cp -f $CATALINA_LOG_FILE $CATALINA_LOG_BAK_FILE
    fi

    # 检查备份是否成功
    if [ $? != 0 ]; then
        echo "copy phase failed, exit!"
        exit 0
    fi

    # 清空源日志
    if $DEBUG; then echo "echo \"\" > $CATALINA_LOG_FILE"; fi
    echo "" > $CATALINA_LOG_FILE

    # 压缩日志
    if $DEBUG; then echo "cd $CATALINA_LOG_BAK_DIR"; echo "tar -czvf $BAK_GZ_FILE_NAME $BAK_FILE_NAME"; fi
    cd $CATALINA_LOG_BAK_DIR
    tar -czvf $BAK_GZ_FILE_NAME $BAK_FILE_NAME

    if [ $? == 0 ]; then
        # 删除非压缩日志
        if $DEBUG; then echo "rm -f $CATALINA_LOG_BAK_FILE"; fi
        rm -f $CATALINA_LOG_BAK_FILE
    fi
fi
#####################################################################

# 删除过期日志
if $DEBUG; then echo "find $CATALINA_LOG_BAK_DIR/ -mtime +$EXPIRE_DAY_NUM -name \"*$BAK_LOG_EXTENSION\" -exec rm -rf {} "; fi
find $CATALINA_LOG_BAK_DIR"/" -mtime +$EXPIRE_DAY_NUM -name "*$BAK_LOG_EXTENSION" -exec rm -rf {} \;