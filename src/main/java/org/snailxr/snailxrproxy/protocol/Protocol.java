package org.snailxr.snailxrproxy.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by snailxr on 2018/4/14.
 */
public class Protocol {
    private int offset=0;
    private byte[] packet;

    public Protocol() {

    }

    public byte[] getPacket() {
        return packet;
    }

    public void setPacket(byte[] packet) {
        this.packet = packet;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public  long getFixedLengthInt(int length) {
        long value = 0;
        for (int i = (length+offset - 1); i > offset; i--) {
            value |= packet[i] & 0xFF;
            value = value << 8;
        }
        value |= packet[offset] & 0xFF;
        offset=offset+length;
        return value;
    }
    public  long getIntByBytes( byte[] bytes) {
        long value = 0;
        for (int i = (bytes.length - 1); i > 0; i--) {
            value |= bytes[i] & 0xFF;
            value = value << 8;
        }
        value |= bytes[0] & 0xFF;
        return value;
    }
    /**
     * To convert a length-encoded integer into its numeric value, check the first byte:
     * If it is < 0xfb, treat it as a 1-byte integer.
     * If it is 0xfc, it is followed by a 2-byte integer.

     * If it is 0xfd, it is followed by a 3-byte integer.
     * If it is 0xfe, it is followed by a 8-byte integer.
     *
     * @param intBytes
     * @return
     */
    public  long getLengthEncodedInteger(byte[] intBytes) {
        int firstByte = packet[offset];
        offset=offset+1;
        if (firstByte <  0xfb) {
            return getFixedLengthInt(1);
        } else if (firstByte == 0xfc) {
            return getFixedLengthInt(2);
        } else if (firstByte == 0xfd) {
            return getFixedLengthInt(3);
        } else if (firstByte == 0xfe) {
            return getFixedLengthInt(8);
        }
        throw new RuntimeException("parsePayload lengthEncodeInteger failed.");
    }


    public  String getFixedLengthString(int length){
        byte[] newBytes=new byte[length];
        System.arraycopy(packet,offset,newBytes,0,length);
        offset=offset+length;
        return new String(newBytes);


    }

    public  String getNulTerminatedString(byte[] stringBytes){
        int length=0;
        while (packet[offset]!=0x00){
            length+=1;
        }
        String str=getFixedLengthString(length);
        offset+=1;

        return str;
    }
    public static void main(String[] args) {
        System.out.println(Integer.parseInt("ff", 16));
        System.out.println(254==0xff);
    }
}
