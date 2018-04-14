package org.snailxr.snailxrproxy.protocol;

/**
 * Created by snailxr on 2018/4/13.
 */
public abstract class Packet {
    private int payloadLength;
    private int sequenceId;
    private byte[] payload;
    private int offset=0;

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

    protected abstract void parsePayload();
}
