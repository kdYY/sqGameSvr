package org.sq.gameDemo.svr.common;

import sun.security.jca.GetInstance;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 小小的改造一哈 snowflake 算法
 */
public class ConcurrentSnowFlake {

    //初始时间戳 2018-08-28 00:00:00 my birthday~~~
    private static final long INIT_TIME_STAMP = new Date(2018, 8, 28, 0, 0, 0 ).getTime();

    //机器ID所占位数
    private static final long WORK_ID_BITS = 5L;

    //数据标识id所占的位数
    private static final long DATACENTER_ID_BITS = 5L;

    //支持的最大机器id,结果是31 将-1左移 5 位再取反 1111 1111 1111 1111 -> 1111 1111 1110 0000 -> 0000 0000 0001 1111 -> 31
    private static final long MAX_WORKER_ID = ~(-1L << WORK_ID_BITS);

    //支持的最大数据标识id，结果是31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    //序列在id中占的位数
    private final long SEQUENCE_BITS = 12L;

    //机器ID的偏移量（12）
    private final long WORKERID_OFFSET = SEQUENCE_BITS;

    //数据中心id的偏移量(12+5)
    private final long DATACENTER_OFFSET = SEQUENCE_BITS + DATACENTER_ID_BITS;

    //时间戳的偏移量(5+5+12)
    private final long TIMESTAMP_OFFSET = SEQUENCE_BITS + WORK_ID_BITS + DATACENTER_ID_BITS;

    //生成序列的掩码 这里为4095
    //支持的最大机器id,结果是31 将-1左移 5 位再取反
    // 1111 1111 1111 1111
    // -> 1111 0000 0000 0000
    // -> 0000 1111 1111 1111
    // -> 4095
    //用来防止位数溢出
    private final long SEQUENCE_MASK= ~(-1L << SEQUENCE_BITS);

    //工作节点ID(0 - 31)
    private long workerID;

    //数据中心ID(0 - 31)
    private long datacenterID;

    //毫秒内序列号(0 - 4095)
    private long sequence = 0L;

    //上次生成ID的时间戳
    private volatile long lastTimestamp = -1L;

    Lock getlock = new ReentrantLock();
    AtomicLong productI = new AtomicLong(0L);


    /**
     * 构造函数
     * @param workerID 工作ID(0-31)
     * @param datacenterID 数据中心ID(0-31)
     */
    private ConcurrentSnowFlake(long workerID, long datacenterID){
        if(workerID > MAX_WORKER_ID || workerID < 0){
            throw new IllegalArgumentException(String.format("workerID 不能大于 %d 或小于 0",MAX_WORKER_ID));
        }
        if(datacenterID > MAX_DATACENTER_ID || datacenterID < 0){
            throw new IllegalArgumentException(String.format("datacenterID 不能大于 %d 或小于 0",MAX_DATACENTER_ID));
        }
        this.workerID = workerID;
        this.datacenterID  = datacenterID;
    }

    //单机的构造函数
    // TODO: 2018/8/3
    private ConcurrentSnowFlake(){
        this(1L, 1L);
    }

    public static ConcurrentSnowFlake instance = new ConcurrentSnowFlake();

    public static ConcurrentSnowFlake getInstance() {
        return instance;
    }

    public long nextID(){
        getlock.lock();
        try {
            long  timestamp = System.currentTimeMillis();

            //如果当期时间小于上一次ID生成的时间戳，说明系统时钟回退过，这个时候应当抛出异常
            if(timestamp < lastTimestamp){
                throw new RuntimeException("当期时间小于上一次记录的时间戳，系统时间可能发生回退");
            }

            //如果是同一时间生成的，则进行毫秒内序列
            if(lastTimestamp == timestamp){
                sequence = (sequence+1) & SEQUENCE_MASK;
                //sequence 等于 0 说明毫秒内序列已经增长到最大值
                if(sequence == 0){
                    //阻塞到下一个毫秒，获得新的时间戳
                    timestamp = tilNextMillos(lastTimestamp);
                }
            }
            //不是同一时间生成的,时间戳改变，毫秒内序列重置
            else{
                sequence =  0L;
            }

            //上次生成ID的时间戳
            lastTimestamp = timestamp;

            //移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - INIT_TIME_STAMP) << TIMESTAMP_OFFSET)
                    | (datacenterID << DATACENTER_OFFSET)
                    | (workerID << WORKERID_OFFSET)
                    | sequence;
        }catch (Exception e){
            return -1L;
        }finally {
            productI.incrementAndGet();
            getlock.unlock();
        }

    }

    private long tilNextMillos(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while(timestamp <= lastTimestamp){
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public long getCountId(){
        return productI.get();
    }


    public static void main1(String[] args) throws Exception{
        final ConcurrentSnowFlake main = new ConcurrentSnowFlake();
        ExecutorService exes = Executors.newCachedThreadPool();
        for (int i = 0;i < 10; i++){
            exes.execute(()->{
                try {
                    while(true) {
                        //Thread.sleep(100 *((long) Math.random()*10) );
                        long ids = main.nextID();
                        System.out.println(ids);
                    }
                }catch (Exception e){

                }
            });
        }

        exes.execute(()->{
            while(true) {
                try {
                    Thread.sleep(500);
                    long ids = main.getCountId();
                    //System.out.println(ids);
                }catch (Exception e){

                }finally {

                }

            }
        });
    }


}
