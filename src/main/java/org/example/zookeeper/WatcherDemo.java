package org.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WatcherDemo implements Watcher {
    static ZooKeeper zooKeeper;
    static {
        try {
            zooKeeper = new ZooKeeper("172.19.0.202:2181,172.19.0.203:2181,172.19.0.204:2181",4000,new WatcherDemo());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("eventType:"+watchedEvent.getType());
        if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
            try {
                zooKeeper.exists(watchedEvent.getPath(),true);
                byte[] bytes = zooKeeper.getData(watchedEvent.getPath(),false,null);
                System.out.println("节点值："+new String(bytes));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{
        String path = "/watcher";
        if(zooKeeper.exists(path,false) == null){
            zooKeeper.create(path,"100".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
        Thread.sleep(1000);
        System.out.println("--------------------------");
        Stat stat = zooKeeper.exists(path,true);
        System.in.read();
    }
}
