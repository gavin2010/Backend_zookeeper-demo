package org.example.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * curator连接zookeeper demo
 */
public class CuratorDemo {
    public static void main(String[] args) {
        try {
            CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("172.19.0.202:2181,172.19.0.203:2181,172.19.0.204:2181").
                    sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("").build();

            curatorFramework.start();
            Stat stat = new Stat();
            //查询节点数据
            byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/myZnode");
            System.out.println("数据为："+new String(bytes));
            curatorFramework.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
