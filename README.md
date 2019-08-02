# springboot-fek

## 目标

单点安装filebeat -> elasticsearch -> kibana 解析spring boot 日志

FEK方式，日志收集使用filebeat收集springboot日志文件信息，发送至es。

## 环境

### 服务器列表

1. 192.168.1.10 filebeat
2. 192.168.1.11 kibana elaticsearch

### 软件环境
- jdk 1.8
- centos 7.6
- elk 7.3 (current yum latest)

## 双机准备

两台机器加入ELK yum仓库

```
rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch

vi /etc/yum.repos.d/elastic.repo


[elastic-7.x]
name=Elastic repository for 7.x packages
baseurl=https://artifacts.elastic.co/packages/7.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
```

## es and kibana安装

192.168.1.11 执行

```

yum -y install elaticsearch
yum -y install kibana

yum enable elaticsearch
yum enable kibana
```

### 9200 非localhost可访问
```
vi /etc/elasticsearch/elasticsearch.yml 

network.host: 0.0.0.0
discovery.seed_hosts: ["0.0.0.0"] 
```

### 非localhost IP可访问kibana
```
vi /etc/kibana/kibana.yml
server.host: "192.168.1.11"
```

### 启动
```
yum restart elaticsearch
yum restart kibana
```


## filebeat 安装

192.168.1.10 执行

```
yum -y install filebeat
yum -y enable filebeat
vi /etc/filebeat/filebeat.yml 
```

### 修改filebeat生成默认索引名称
```
vi /etc/filebeat/filebeat.yml 

output.elasticsearch:
  # Array of hosts to connect to.
  hosts: ["192.168.1.11:9200"]
setup.ilm.enabled: auto
setup.ilm.rollover_alias: "springapp1"
setup.ilm.pattern: "{now/d}-000001"
```

### 上传springboot应用解析模块

1. 上传 springboot 目录至 `/usr/share/filebeat/module` 目录
2. 上传 springboot.yum 文件至 `/etc/filebeat/modules.d` 目录

### 修改新增模块应用名称及收集日志来源

根据自己springboot 应用日志位置不同修改文件内容，默认是/root/log.txt
```
vi /usr/share/filebeat/module/aipc/debug/manifest.yml

    os.linux: 
      - /root/log.txt
```

### 启用新日志收集模块
filebeat modules enable springboot

### 应用部署
略

测试源码可通过 giteee 下载

1. git clone 
2. cd spring-boot-elk
3. mvn clean package
4. java -jar springboot-elk.jar

## 验证
1. springboot 日志 `/root/log.txt` 有内容
2. 浏览器<http://192.168.10:8080/test> 可看到当前系统时间
3. 浏览器<http://192.168.10:8080/fail> 可`/root/log.txt` 看到异常堆栈信息
4. chrome head 插件 访问192.168.1.11:9200 可看到es索引情况，可见索引springapp1-yyyy.MM.dd-000001，索引。docs 数字不为0
5. kibana discover 新建springapp1-*，可按到springboot日志内容，并可正确解析 level、TID、THREADNAME、message输出
6. kibana 查看多行处理正常


## 调试提示
重新开始收集日志
1. chrome es head 插件删除索引springapp1-yyyy.MM.dd-000001
2. 停止filebeat `systemctl stop filebeat`
3. 删除现有filebeat 进度`rm -rf /var/lib/filebeat`
4. 删除filebeat module 在es缓存 `curl XDELETE http://192.168.1.11:9200/_ingest/pipeline/filebeat-7.3.0-springboot-debug-default`
5. 修改调试位置
6. 启动filebeat收集 `systemctl start filebeat`

### 关键日志

filebeat可以开启日志，或者直接从系统日志查看启动运行情况
`tail -f /var/log/message`


## 核心点
filebeat springboot module(`/usr/share/filebeat/module/aipc/debug/default.json`) 解析配置及logback日志格式匹配

## 附1：开放端口号
- filebeat 
- kibana 5601
- elaticsearch 9200
- elaticsearch 9300 集群内TCP


## 附2：官方配置
- <https://www.elastic.co/guide/en/beats/filebeat/7.2/setup-repositories.html>

