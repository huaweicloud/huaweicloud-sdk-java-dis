
package com.huaweicloud.dis.util;

import java.util.concurrent.TimeUnit;

public class ExponentialBackOff
{
    
    /**
     * Return value of {@link #getNextBackOff()} that indicates that the operation should not be retried.
     */
    public static final long STOP = -1;
    
    /**
     * The default initial interval.
     */
    public static final long DEFAULT_INITIAL_INTERVAL = 50L;
    
    /**
     * The default multiplier (increases the interval by 50%).
     */
    public static final double DEFAULT_MULTIPLIER = 1.5;
    
    /**
     * The default maximum back off time.
     */
    public static final long DEFAULT_MAX_INTERVAL = 30000L;
    
    /**
     * The default maximum elapsed time.
     */
    public static final long DEFAULT_MAX_ELAPSED_TIME = Long.MAX_VALUE;
    
    private long initialInterval = DEFAULT_INITIAL_INTERVAL;
    
    private double multiplier = DEFAULT_MULTIPLIER;
    
    private long maxInterval = DEFAULT_MAX_INTERVAL;
    
    private long maxElapsedTime = DEFAULT_MAX_ELAPSED_TIME;
    
    /**
     * Create an instance with the default settings.
     *
     * @see #DEFAULT_INITIAL_INTERVAL
     * @see #DEFAULT_MULTIPLIER
     * @see #DEFAULT_MAX_INTERVAL
     * @see #DEFAULT_MAX_ELAPSED_TIME
     */
    public ExponentialBackOff()
    {
    }
    
    /**
     * Create an instance with the supplied settings.
     *
     * @param initialInterval the initial interval in milliseconds
     * @param multiplier the multiplier (should be greater than or equal to 1)
     */
    public ExponentialBackOff(long initialInterval, double multiplier, long maxInterval, long maxElapsedTime)
    {
        if (multiplier < 1)
        {
            throw new IllegalArgumentException("Invalid multiplier '" + multiplier + "'. Should be greater than "
                + "or equal to 1. A multiplier of 1 is equivalent to a fixed interval.");
        }
        this.initialInterval = initialInterval;
        this.multiplier = multiplier;
        this.maxInterval = maxInterval;
        this.maxElapsedTime = maxElapsedTime;
    }
    
    private long currentInterval = -1;
    
    private long currentElapsedTime = 0;
    
    public long backOff(long backOff)
    {
        if (backOff != STOP)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(backOff);
            }
            catch (InterruptedException ignored)
            {
            }
        }
        return backOff;
    }
    
    public long getNextBackOff()
    {
        if (this.currentElapsedTime >= maxElapsedTime)
        {
            return STOP;
        }
        
        long nextInterval = computeNextInterval();
        this.currentElapsedTime += nextInterval;
        return nextInterval;
    }
    
    private long computeNextInterval()
    {
        long maxInterval = this.maxInterval;
        if (this.currentInterval >= maxInterval)
        {
            return maxInterval;
        }
        else if (this.currentInterval < 0)
        {
            long initialInterval = this.initialInterval;
            this.currentInterval = (initialInterval < maxInterval ? initialInterval : maxInterval);
        }
        else
        {
            this.currentInterval = multiplyInterval(maxInterval);
        }
        return this.currentInterval;
    }
    
    private long multiplyInterval(long maxInterval)
    {
        long i = this.currentInterval;
        i *= this.multiplier;
        return (i > maxInterval ? maxInterval : i);
    }
    
    public long getInitialInterval()
    {
        return initialInterval;
    }
    
    public double getMultiplier()
    {
        return multiplier;
    }
    
    public long getMaxInterval()
    {
        return maxInterval;
    }
    
    public long getMaxElapsedTime()
    {
        return maxElapsedTime;
    }
    
    public void resetCurrentInterval()
    {
        this.currentInterval = -1;
    }
    
}