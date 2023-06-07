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
  * 服务端流控时增加重启（response status 429）

- Bugfixs
  * jackson版本升级到2.14.2
  * slf4j升级到1.7.36
  * log4j升级到2.20.0
  * httpclient升级到4.5.14
  * joda-time升级到2.12.2
  * commons-io升级到2.12.0
  * protobuf升级到3.21.9