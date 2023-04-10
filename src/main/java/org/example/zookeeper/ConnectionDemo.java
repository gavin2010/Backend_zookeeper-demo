package org.example.zookeeper;


import org.apache.zookeeper.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * 原生Zookeeper jar 连接
 */
public class ConnectionDemo {
    public static void main(String[] args) {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("172.19.0.202:2181,172.19.0.203:2181,172.19.0.204:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
                        //收到了服务端响应事件，连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            //Connected
            System.out.println("Zookeeper的状态："+zooKeeper.getState());

            /**
             * PERSISTENT			// 持久节点，一旦创建成功不会被删除，除非客户端主动发起删除请求
             * PERSISTENT_SEQUENTIAL		// 持久顺序节点，会在用户路径后面拼接一个不会重复的自增数字后缀，其他同上
             * EPHEMERAL			// 临时节点，当创建该节点的客户端链接断开后自动被删除
             * EPHEMERAL_SEQUENTIAL		// 临时顺序节点，基本同上，也是增加一个数字后缀
             * CONTAINER			// 容器节点，一旦子节点被删除完就会被服务端删除
             * PERSISTENT_WITH_TTL		// 带过期时间的持久节点，带有超时时间的节点，如果超时时间内没有子节点被创建，就会被删除
             * PERSISTENT_SEQUENTIAL_WITH_TTL	// 带过期时间的持久顺序节点，基本同上，多了一个数字后缀
             * 最后两种带 TTL 的类型，这两种类型在 ZK 默认配置下还是不支持的，需要在 zoo.cfg 配置中添加 extendedTypesEnabled=true 启用扩展功能，否则的话就会收到 Unimplemented for 的错误
             */
            zooKeeper.create("/myzk","0".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            System.out.println("创建节点成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
