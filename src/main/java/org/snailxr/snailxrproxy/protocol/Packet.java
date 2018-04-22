package org.snailxr.snailxrproxy.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by snailxr on 2018/4/13.
 */
public abstract class Packet {
    private int payloadLength;
    private int sequenceId;
    private byte[] payload;
    private int offset = 0;

    public int getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void loadPacket(SelectionKey key,ByteBuffer byteBuffer) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();

        while (client.read(byteBuffer) > 0) {
            int limit = byteBuffer.limit();
            int position = byteBuffer.position();
            byteBuffer.flip();
            Protocol p = new Protocol();
            if(payload==null){
                if (byteBuffer.remaining() < 4) {
                    byteBuffer.limit(limit);
                    byteBuffer.position(position);
                    continue;
                }

                byte[] fixIntByte = new byte[3];
                byteBuffer.get(fixIntByte, 0, 3);
                payloadLength = (int)p.getIntByBytes(fixIntByte);
                byte[] sequenceIdByte = new byte[1];
                byteBuffer.get(sequenceIdByte, 0, 1);
                sequenceId =(int) p.getIntByBytes(sequenceIdByte);
                payload=new byte[payloadLength];
                offset=4;
            }
            int needReadLength=payloadLength+4-offset;
            if (byteBuffer.remaining() >= needReadLength) {
                byteBuffer.get(payload, offset-4, needReadLength);
                byteBuffer.compact();
                offset=0;
                break;
            } else{
                int remaining=byteBuffer.remaining();
                byteBuffer.get(payload,offset-4,remaining);
                offset=offset+remaining;
                byteBuffer.compact();
            }

        }


    }
}
