package org.snailxr.snailxrproxy.protocol;

/**
 * Created by snailxr on 2018/4/13.
 */
public class Packet {
    private int payloadLength;
    private int sequenceId;
    private String payload;

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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
