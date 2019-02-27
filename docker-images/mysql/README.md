# MYSQL bash

```git
#!/bin/bash
cur_dir=`pwd`
docker stop imooc-mysql
docker rm imooc-mysql
docker run --name imooc-mysql -v /${cur_dir}/conf:/etc/mysql/config.d -v /${cur_dir}/data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql
```

## 直接运行该文件，即可生成一个Mysql的容器

### 注：windows和linux在目录上有一定的区别，即 win:'/${cur_dir}'; linux:'${cur_dir}'

### 如果你非第一次启动该容器，则设置密码以第一次为主: MYSQL_ROOT_PASSWORD=password

###  关于开启远程访问：

1. 进入容器环境  docker exec -it imooc-mysql bash(windows环境请加上winpty) 
2. 进入mysql  mysql -u root -p
3. 修改root密码  alter user 'root'@'localhost' identified by 'password';
4. 新建用户   create user 'koral'@'%' identified with mysql_native_password by 'password';
5. 授权 grant all privileges on *.* to 'koral'@'%';
6. 远程连接测试

### 如果你仍旧想要修改密码，请尝试执行 `docker-compose rm -v` 删除所有内容并重置它 [github issue](https://github.com/docker-library/mysql/issues/51)
