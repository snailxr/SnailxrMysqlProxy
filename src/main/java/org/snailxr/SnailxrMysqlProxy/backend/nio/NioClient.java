package org.snailxr.SnailxrMysqlProxy.backend.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by snailxr on 16/5/15.
 */
public class NioClient {
    private SocketChannel socketChannel;
    private Selector selector;
    private String host;
    private int port;
    public NioClient(){

    }
    public NioClient(String host,int port) throws IOException {
        this.host =host;
        this.port=port;
        socketChannel =SocketChannel.open();
        selector=Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress(host, port));

    }
    public void connect() throws IOException {
        while (true){
            int count=selector.select();
            if (count<1){
                continue;
            }
            Iterator<SelectionKey>iterator=selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();
                process(key);
                iterator.remove();
            }
        }

    }
    public void process(SelectionKey key) throws IOException {
        if (key.isConnectable()) {
            SocketChannel client = (SocketChannel) key.channel();
            if (client.isConnectionPending()) {
                client.finishConnect();
                client.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (key.isReadable()) {

        } else if (key.isWritable()) {

        }
    }
}
