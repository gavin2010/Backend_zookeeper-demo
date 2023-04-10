package org.example.zookeeper;

import org.apache.zookeeper.*;

import java.nio.charset.StandardCharsets;

public class TestDemo {
    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("172.19.0.202:2181,172.19.0.203:2181,172.19.0.204:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                }
            });
            String str = zooKeeper.create("/root12", "aaa".getBytes(StandardCharsets.UTF_8),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(zooKeeper.exists(str,null).getAversion());
            zooKeeper.delete(str,-1);
        } catch (Exception e) {
            System.out.println("出现异常");
            e.printStackTrace();
        }
    }

}
