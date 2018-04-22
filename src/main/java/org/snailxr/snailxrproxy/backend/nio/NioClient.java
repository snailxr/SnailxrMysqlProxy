package org.snailxr.snailxrproxy.backend.nio;

import org.snailxr.snailxrproxy.protocol.HandShake;
import org.snailxr.snailxrproxy.protocol.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
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
                client.register(selector, SelectionKey.OP_READ);
            }
        } else if (key.isReadable()) {
           // https://blog.insanecoder.top/tcp-packet-splice-and-split-issue/
            SocketChannel client= (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            HandShake handShake=new HandShake();
            handShake.loadPacket(key,byteBuffer);
            System.out.println(handShake);

//           // String s=new String(byteBuffer.array(),byteBuffer.position(),byteBuffer.limit());
//            byte[] fixIntByte=new byte[3];
//            byteBuffer.get(fixIntByte,0,3);
//            int paylodLength=0;
//            for (int i=fixIntByte.length-1;i>0;i--){
//                paylodLength|=fixIntByte[i]&0xFF;
//                paylodLength=paylodLength<<8;
//
//            }
//            paylodLength|=fixIntByte[0]&0xFF;
//
//            System.out.println(paylodLength);
//            int sequenceId=0;
//            byte[] sequenceIdByte=new byte[1];
//            byteBuffer.get(sequenceIdByte,0,1);
//            sequenceId|=sequenceIdByte[0]&0xFF;
//            System.out.println(sequenceId);
//            System.out.println(Arrays.toString(fixIntByte));
//            byte[] payloadByte=new byte[paylodLength];
//            byteBuffer.get(payloadByte, 0, paylodLength);
//            //System.out.println(Arrays.toString(byteBuffer.array()));
//            System.out.println(Arrays.toString(payloadByte));
//            HandShake handShake=new HandShake();
//            handShake.setPayloadLength(paylodLength);
//            handShake.setPayload(payloadByte);
        } else if (key.isWritable()) {

        }
    }

    public static void main(String[] args) throws IOException {
        NioClient client=new NioClient("localhost",3306);
        client.connect();
    }
}
