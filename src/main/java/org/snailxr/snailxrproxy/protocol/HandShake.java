package org.snailxr.snailxrproxy.protocol;

/**
 * Created by snailxr on 2018/4/14.
 */
public class HandShake extends Packet{
    private int potocolVersion;
    private String serverVersion;
    private int connectionId;


    /**
     1              [0a] protocol version
     string[NUL]    server version
     4              connection id
     string[8]      auth-plugin-data-part-1
     1              [00] filler
     2              capability flags (lower 2 bytes)
     if more data in the packet:
     1              character set
     2              status flags
     2              capability flags (upper 2 bytes)
     if capabilities & CLIENT_PLUGIN_AUTH {
     1              length of auth-plugin-data
     } else {
     1              [00]
     }
     string[10]     reserved (all [00])
     if capabilities & CLIENT_SECURE_CONNECTION {
     string[$len]   auth-plugin-data-part-2 ($len=MAX(13, length of auth-plugin-data - 8))
     if capabilities & CLIENT_PLUGIN_AUTH {
     string[NUL]    auth-plugin name
     }
     */




}
