# MySQL修改字符集编码
```
  character-set-server = utf8mb4
  skip-character-set-client-handshake
  collation-server = utf8mb4_unicode_ci
  init_connect='SET NAMES utf8mb4'
  mysqld_safe --defaults-file=配置文件名 &

  /etc/my.cnf
```

