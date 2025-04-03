# 1.3.1

- Features
  * add describeApp and listApps features
  * use NIO for DISClientAsync 
  * add delete checkpoint
  * add describe consumer state
  * support tags in stream apis

# 1.3.2

- Features
  * Support configuring proxy
  * Support caching failed records locally
  * Support resend caching failed records

- Bugfixs
  * update jackson-core version to 2.8.11

# 1.3.3

- Bugfixs
  * proxy configuration bug fix, support configuring 'NonProxyHosts'
  * Some configurations cannot be read from config.properties
  * Cannot load default Java TrustStore
  
# 1.3.4

- Features
  * Support Lz4 compression
  
# 1.3.7

- Bugfixs
  * Update lz4-java version to 1.7.1
  
# 1.3.8

- Features
  * 支持下载被授权的通道数据
  
# 1.3.10

- Features
  * 支持通过通道ID查询被授权通道的详情
  
# 1.3.13

- Features
  * 修复通过非配置文件方式配置参数`records.retriable.error.code`和`exception.retriable.error.code`不生效的问题

# 1.3.16

- Features
  * 服务端流控时增加重试（response status 429）

- Bugfixs
  * jackson版本升级到2.14.2
  * slf4j升级到1.7.36
  * log4j升级到2.20.0
  * httpclient升级到4.5.14
  * joda-time升级到2.12.2
  * commons-io升级到2.12.0
  * protobuf升级到3.21.9
  * lz4-java升级到1.8.0
  * 修复httpClient开源问题：设置超时时间不生效问题（https://stackoverflow.com/question/6764035/apache-httpclient-timeout）
  * 添加生产消费主线程异常卡主堆栈信息打印

# 1.3.17

- Bugfixs
  * 单条记录上传时，如果被限流，有异常时，抛出异常

# 1.3.18
- Bugfixs
  * 添加下载异步接口异常堆栈信息打印
  * 修复客户端加密不起作用问题

# 1.3.19
- Bugfixs
  * jackson版本升级到2.15.4
  * log4j升级到2.21.1
  * httpclient升级到4.5.14
  * commons-io升级到2.16.0
  * protobuf升级到3.25.5
  * snappy-java升级到1.1.10.4
- Features
  * 支持adapter sdk跨账号授权
# 1.3.20
- Bugfixs
  * jackson-databind 类加载死锁问题处理：https://github.com/FasterXML/jackson-databind/issues/2715