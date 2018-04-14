package org.snailxr.snailxrproxy.protocol;

import java.util.Arrays;
import java.util.List;

/**
 * Created by snailxr on 2018/4/14.
 */
public class Protocol {
    public int offset=0;
    public byte[] packet;

    public Protocol(int offset, byte[] packet) {
        this.offset = offset;
        this.packet = packet;
    }

    public static long getFixedLengthInt(int length,int offset, byte[] bytes) {
        long value = 0;
        for (int i = length - 1; i > 0; i--) {
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
    public static long getLengthEncodedInteger(byte[] intBytes) {
        int firstByte = intBytes[0];
        if (firstByte <  0xfb) {
            return getFixedLengthInt(1, intBytes);
        } else if (firstByte == 0xfc) {
            return getFixedLengthInt(2, intBytes);
        } else if (firstByte == 0xfd) {
            return getFixedLengthInt(3, intBytes);
        } else if (firstByte == 0xfe) {
            return getFixedLengthInt(8, intBytes);
        }
        throw new RuntimeException("parsePayload lengthEncodeInteger failed.");
    }


    public static String getFixedLengthString(int length, byte[] stringBytes){
        byte[] newBytes=new byte[length];
        System.arraycopy(stringBytes,0,newBytes,0,length);
        return new String(stringBytes);


    }

    public static String getNulTerminatedString(byte[] stringBytes){
        List list=Arrays.asList(stringBytes);
        int length=list.indexOf(0x00)+1;

        byte[] newBytes=new byte[length];
        System.arraycopy(stringBytes,0,newBytes,0,length);

        return new String(newBytes);
    }
    public static void main(String[] args) {
        System.out.println(Integer.parseInt("ff", 16));
        System.out.println(254==0xff);
    }
}
