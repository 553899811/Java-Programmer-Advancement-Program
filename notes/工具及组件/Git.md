[TOC]
参考：

- [x] [阮一峰-读懂diff](http://www.ruanyifeng.com/blog/2012/08/how_to_read_diff.html)
- [x] [Git远程操作详解](http://www.ruanyifeng.com/blog/2014/06/git_remote.html)
- [x] [ 官网-Git 基础 - 记录每次更新到仓库](https://git-scm.com/book/zh/v2/Git-%E5%9F%BA%E7%A1%80-%E8%AE%B0%E5%BD%95%E6%AF%8F%E6%AC%A1%E6%9B%B4%E6%96%B0%E5%88%B0%E4%BB%93%E5%BA%93)
- [x] [廖雪峰的官方网站](https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000/001375234012342f90be1fc4d81446c967bbdc19e7c03d3000#0)

# Git常用命令

## git log
查看提交记录

## git init
初始化仓库,即把这个目录变成git可以管理的git仓库;


## git branch

查看当前所在分支

```
git branch
```

查看远程分支

```
git branch -r
```
删除本地分支

```
git branch -D <branch-name>
```
删除远程分支
```
git branch -r -d origin/branch-name  
```

## git checkout

切换分支

```
git checkout 新分支
```

拉取远程某个分支，并切换到该分支

```
git checkout -b 本地分支名x origin/远程分支名x
```

使用该方式会在本地新建分支x，但是不会自动切换到该本地分支x，需要手动checkout。
```
git fetch origin 远程分支名x:本地分支名x
```

## git push

提交到远程库

```
git push origin local_branch:remote_branch
```

对于在仓库中新建的一个分支，推至远程：

```
git push --set-upstream origin branch-name
```



### 提交代码至Github（同其他服务器）
- 本地生成私钥、公钥，并将公钥放至github。
- 首先在github创建仓库。
- 在本地代码执行git init创建本地仓库。
    - git add . 将本地代码加至本地仓库。
    - git commit -m "描述"，提交操作。
- 执行以下代码，推送代码。

```
git remote add origin git@github.com:YukunSun/mydemo.git
git push -u origin master
```


## git diff


本地分支与远程比较


## git fetch

![image](http://image.beekka.com/blog/2014/bg2014061202.jpg)

git pull= git fetch + git merge


将远程代码强制覆盖本地：

```
git fetch --all  
git reset --hard origin/master 
git pull
```
## git stash

git stash 可用来暂存当前正在进行的工作， 比如想pull 最新代码， 又不想加新commit， 或者另外一种情况，为了fix 一个紧急的bug,  先stash, 使返回到自己上一个commit, 改完bug之后再stash pop, 继续原来的工作。
基础命令：


```
$git stash
$do some work
$git stash pop
```

进阶：
git stash save "work in progress for foo feature"
当你多次使用’git stash’命令后，你的栈里将充满了未提交的代码，这时候你会对将哪个版本应用回来有些困惑，

’git stash list’ 命令可以将当前的Git栈信息打印出来，你只需要将找到对应的版本号，例如使用’git stash apply stash@{1}’就可以将你指定版本号为stash@{1}的工作取出来，当你将所有的栈都应用回来的时候，可以使用’git stash clear’来将栈清空。


```
git stash          # save uncommitted changes            # pull, edit, etc.
git stash list     # list stashed changes in this git
git show stash@{0} # see the last stash 
git stash pop      # apply last stash and remove it from the list

git stash --help   # for more info
```

## git reset

强制覆盖本地master分支代码

```
git reset --hard <commit_id>
git push origin HEAD --force

git reset --hard origin/master
```

## git remote

查看远程仓库地址

```
git remote -v
```

## git merge

比如正在dev分支写好了东西，想合并至master.

```
1. 切换到目标分支master.
2. 执行git merge dev.
3. 完成合并。
```


合并经过多次commit的目标分支，为一个提交接点。

```
git merge <target-branch-name> --squash
```

## git tag

添加tag

```
git tag version-1.0
```
查看tag

```
git tag
```

Git Tag的使用:http://www.jianshu.com/p/32054f3e415d

# git show

查看某个分支提交的内容

```
git show 9efcb7c:index.html
```




# 忽略文件

可以将忽略的文件配置在.gitignore里，但是对于已经提交过的文件是无济于事的。
可以这么解决：

```
git rm --cached logs/xx.log
git commit -m "We really don't want Git to track this anymore!
```
使用`git update-index --assume-unchanged <PATH>`同样可以达到暂时的目的，但是这么操作是不对的。
https://segmentfault.com/q/1010000000430426


# git config：为git命令设置别名 

使用该命令可以为git命令设置别名，通常可以简化命令。

例：
```
git config --global alias.ci commit
```
- 意思是为commit设置一个别名ci，这样就可以使用git ci 代替git commit了。
- --global参数是全局参数，也就是这些命令在这台电脑的所有Git仓库下都有用。如果不加，那只针对当前的仓库起作用。
- 设置全局时，意味着将该配置放到了当前用户主目录下的一个隐藏文件.gitconfig中。想要去掉别名时，只需删除相应配置即可。


例如：

git lg
```
git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit"
```





# .gitignore配置样例文件:

```
target/
!.mvn/wrapper/maven-wrapper.jar
!.mvn/wrapper/maven-wrapper.properties

### STS ###
.apt_generated
### Eclipse template

.metadata
bin/
tmp/
*.tmp
*.bak
*.swp
*~.nib
local.properties
.settings/
.loadpath
.recommenders

# Eclipse Core
.project

# External tool builders
.externalToolBuilders/

# Locally stored "Eclipse launch configurations"
*.launch

# PyDev specific (Python IDE for Eclipse)
*.pydevproject

# CDT-specific (C/C++ Development Tooling)
.cproject

# JDT-specific (Eclipse Java Development Tools)
.classpath

# Java annotation processor (APT)
.factorypath

# PDT-specific (PHP Development Tools)
.buildpath

# sbteclipse plugin
.target

# Tern plugin
.tern-project

# TeXlipse plugin
.texlipse

# STS (Spring Tool Suite)
.springBeans

# Code Recommenders
.recommenders/
### Maven template
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties

/.mvn
.classpath
.factorypath
.project
.settings
.springBeans

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
nbproject/private/
build/
nbbuild/
dist/
nbdist/
.nb-gradle/
/mvnw
/mvnw.cmd

### Eclipse template

.metadata
bin/
tmp/
*.tmp
*.bak
*.swp
*~.nib
local.properties
.settings/
.loadpath
.recommenders

# Eclipse Core
.project

# External tool builders
.externalToolBuilders/

# Locally stored "Eclipse launch configurations"
*.launch

# PyDev specific (Python IDE for Eclipse)
*.pydevproject

# CDT-specific (C/C++ Development Tooling)
.cproject

# JDT-specific (Eclipse Java Development Tools)
.classpath

# Java annotation processor (APT)
.factorypath

# PDT-specific (PHP Development Tools)
.buildpath

# sbteclipse plugin
.target

# Tern plugin
.tern-project

# TeXlipse plugin
.texlipse

# STS (Spring Tool Suite)
.springBeans

# Code Recommenders
.recommenders/
### Maven template
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties

# Exclude maven wrapper
!/.mvn/wrapper/maven-wrapper.jar
### JetBrains template
# Covers JetBrains IDEs: IntelliJ, RubyMine, PhpStorm, AppCode, PyCharm, CLion, Android Studio and Webstorm
# Reference: https://intellij-support.jetbrains.com/hc/en-us/articles/206544839

# User-specific stuff:
.idea/workspace.xml
.idea/tasks.xml

# Sensitive or high-churn files:
.idea/dataSources/
.idea/dataSources.ids
.idea/dataSources.xml
.idea/dataSources.local.xml
.idea/sqlDataSources.xml
.idea/dynamic.xml
.idea/uiDesigner.xml

# Gradle:
.idea/gradle.xml
.idea/libraries

# Mongo Explorer plugin:
.idea/mongoSettings.xml

## File-based project format:
*.iws

## Plugin-specific files:

# IntelliJ
/out/

# mpeltonen/sbt-idea plugin
.idea_modules/

# JIRA plugin
atlassian-ide-plugin.xml

# Crashlytics plugin (for Android Studio and IntelliJ)
com_crashlytics_export_strings.xml
crashlytics.properties
crashlytics-build.properties
fabric.properties


```
