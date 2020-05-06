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