#!/bin/bash
cur_dir=`pwd`
docker stop imooc-mysql
docker rm imooc-mysql
docker run --name imooc-mysql -v /${cur_dir}/conf:/etc/mysql/config.d -v /${cur_dir}/data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql
