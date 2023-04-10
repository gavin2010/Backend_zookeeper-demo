package org.example.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class InterprocessLock {
    public static void main(String[] args) {
        CuratorFramework zkClient = getZkClient();
        String lockPath = "/lock";
        InterProcessMutex lock = new InterProcessMutex(zkClient, lockPath);
        for(int i=0; i<50l; i++){
            new Thread(new TestRunnable(i,lock)).start();
        }
    }

    static class TestRunnable implements Runnable{
        private Integer threadFlag;
        private InterProcessMutex lock;

        public TestRunnable(Integer threadFlag,InterProcessMutex lock){
            this.threadFlag = threadFlag;
            this.lock = lock;
        }
        @Override
        public void run() {
            try {
                lock.acquire();
                System.out.println("第"+threadFlag+"个线程获得锁");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static CuratorFramework getZkClient() {
        String zkServerAddress = "172.19.0.202:2181,172.19.0.203:2181,172.19.0.204:2181";
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000,3, 5000);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder().connectString(zkServerAddress).sessionTimeoutMs(5000).
                connectionTimeoutMs(5000).retryPolicy(retry).build();
        zkClient.start();
        return zkClient;
    }
}
