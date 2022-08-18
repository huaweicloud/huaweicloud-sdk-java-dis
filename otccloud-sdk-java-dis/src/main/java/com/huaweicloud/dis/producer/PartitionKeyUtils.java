package com.otccloud.dis.producer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otccloud.dis.core.util.StringUtils;


public class PartitionKeyUtils
{
    private static final Logger log = LoggerFactory.getLogger(PartitionKeyUtils.class);
    
    private static String hashAlgorithm = "MD5";
    private static final long MinHashValue = 0;
    private static final long MaxHashValue = Long.MAX_VALUE;
    private static final int BYTE_MASK = 0xFF;
    
    private static final String SHARD_ID = "shardId";
    
    public static int calPartitionIndex(int partitionCount, long hashKey){
        long avg = Long.MAX_VALUE/partitionCount;
        
        int activePartitionIndex = (int)(hashKey/avg);
        long mod = hashKey%avg;
        
        if(activePartitionIndex > 0  && mod == 0){
            activePartitionIndex = activePartitionIndex - 1;
        }
        
        return activePartitionIndex;
    }
    
    public static int getPartitionNumberFromShardId(String shardId)
    {
        final String splitToken = "-";
        final int prefixIndex = 0;
        final int partitionValueIndex = 1;
        final int expectedParameters = 2;

        String[] values = shardId.split(splitToken);
        String partitionIdString;
        if (values.length != expectedParameters || !values[prefixIndex].equalsIgnoreCase(SHARD_ID))
        {
            // Use original method. For backward compact support
            log.debug("ShardId format is invalid [eg. shardId-0000000001]. Set PartitionId == ShardId");
            partitionIdString = shardId;
        }
        else
        {
            partitionIdString = values[partitionValueIndex];
        }

        int partitionNumber;
        try
        {
            partitionNumber = Integer.parseInt(partitionIdString);
        }
        catch (NumberFormatException ne)
        {
            String errorMessage =
                    String.format("Unable to parse partitionId from input shardId [%s] - %s", shardId, ne.getMessage());
            log.error(errorMessage);
            throw new RuntimeException("invalid shardId");
        }

        if (partitionNumber < 0)
        {
            String errorMessage =
                    String.format("Unable to parse partitionId from input shardId [%s] - negative value", shardId);
            log.error(errorMessage);
            throw new RuntimeException("invalid shardId");
        }

        return partitionNumber;
    }
    
    public static String getPartitionIdFromShardIdAsString(String shardId)
    {
        Integer partitionId = getPartitionNumberFromShardId(shardId);

        return partitionId.toString();
    }
    
    public static final boolean isHashKeyWithinRange(long hashKey)
    {
        return hashKey >= MinHashValue && hashKey <= MaxHashValue;
    }

    public static final boolean isHashKeyWithinRange(String hashKey)
    {
        try
        {
            return isHashKeyWithinRange(Long.valueOf(hashKey));
        }
        catch (NumberFormatException e)
        {
            log.warn(e.getMessage());
        }
        return false;
    }
    
    private static ThreadLocal<MessageDigest> messageDigestThreadLocal = new ThreadLocal<MessageDigest>();
    private static MessageDigest getMessageDigest()
    {
        MessageDigest messageDigest = messageDigestThreadLocal.get();
        if(messageDigest == null){
            try
            {
                messageDigest = MessageDigest.getInstance(hashAlgorithm);
            }
            catch (NoSuchAlgorithmException e)
            {
                log.error(e.getMessage(), e);
                return null;
            }
            messageDigestThreadLocal.set(messageDigest);
        }
        return messageDigest;
    }
    
    public static long getHash(String value)
    {
        MessageDigest md = getMessageDigest();
        if (md == null)
        {
            log.error("Unabled to get Md5 MessageDigest");
            return -1;
        }

        md.update(value.getBytes());
        byte byteData[] = md.digest();

        return getLong(byteData) & MaxHashValue; // remove negative.
    }
    
    protected static final long getLong(final byte[] array)
    {
        final int SHIFTBITS = 8;
        final int NUMBER_HASH_BYTES = 8;

        int totalBytesToConvert = (array.length > NUMBER_HASH_BYTES) ? NUMBER_HASH_BYTES : array.length;

        long value = 0;
        for (int i = 0; i < totalBytesToConvert; i++)
        {
            value = ((value << SHIFTBITS) | (array[i] & BYTE_MASK));
        }
        return value;
    }
    
    public static Long getHashKey(String partitionKey, String explicitHashKey)
    {
        Long hashKey = 0L;
        if (explicitHashKey != null && !explicitHashKey.isEmpty())
        {
            long explicitHashValue = Long.valueOf(explicitHashKey);
            if (isHashKeyWithinRange(explicitHashValue))
            {
                hashKey = explicitHashValue;
            }
            else
            {
                log.info("ExplicitHashKey {} is out of range, using partitionKey {} instead",
                    explicitHashKey,
                    partitionKey);
                hashKey = getHash(partitionKey);
            }
        }
        else if (!StringUtils.isNullOrEmpty(partitionKey))
        {
            hashKey = getHash(partitionKey);
        }
        else
        {
            // 当 explicitHashKey, partitionKey 均为空时，使用时间戳作为 partitionKey
            hashKey = getHash(String.valueOf(System.nanoTime()));
        }
        return hashKey;
    }
}
