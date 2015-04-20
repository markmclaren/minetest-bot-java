package com.markmc.minetest;

import static com.markmc.minetest.Utils.U16;
import static com.markmc.minetest.Utils.U8;
import static com.markmc.minetest.Utils.getByteArray;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author markmc
 */
public class Constants {

    public static final int SEQNUM_INITIAL = 65500;
    public static final byte[] PROTOCOL_ID = getByteArray("4f457403");
    public static final byte[] PEER_ID_INEXISTENT = getByteArray("0000");

    public static final byte[] TYPE_CONTROL = getByteArray("00");
    public static final byte[] TYPE_ORIGINAL = getByteArray("01");
    public static final byte[] TYPE_SPLIT = getByteArray("02");
    public static final byte[] TYPE_RELIABLE = getByteArray("03");

    public static final byte[] CONTROLTYPE_ACK = getByteArray("00");
    public static final byte[] CONTROLTYPE_SET_PEER_ID = getByteArray("01");
    public static final byte[] CONTROLTYPE_PING = getByteArray("02");
    public static final byte[] CONTROLTYPE_DISCO = getByteArray("03");
    public static final byte[] CONTROLTYPE_ENABLE_BIG_SEND_WINDOW = getByteArray("04");
    
    public static final byte[] SER_FMT_VER_HIGHEST_READ = U8(26);
    public static final byte[] LATEST_PROTOCOL_VERSION = U16(24);    
    public static final byte[] CLIENT_PROTOCOL_VERSION_MIN = U16(13);
    public static final byte[] CLIENT_PROTOCOL_VERSION_MAX = LATEST_PROTOCOL_VERSION;
    
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 4;
    public static final int VERSION_PATCH = 12;
    public static final String VERSION_STRING = String.format("%s.%s.%s-Minetest-Bot", VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH);
    
    public static final Map<ByteBuffer, String> TYPES = new HashMap<ByteBuffer, String>();
    public static final Map<ByteBuffer, String> CONTROLTYPES = new HashMap<ByteBuffer, String>();

    static {
        TYPES.put(ByteBuffer.wrap(TYPE_RELIABLE), "TYPE_RELIABLE");
        TYPES.put(ByteBuffer.wrap(TYPE_ORIGINAL), "TYPE_ORIGINAL");
        TYPES.put(ByteBuffer.wrap(TYPE_CONTROL), "TYPE_CONTROL");
        TYPES.put(ByteBuffer.wrap(TYPE_SPLIT), "TYPE_SPLIT");
        
        CONTROLTYPES.put(ByteBuffer.wrap(CONTROLTYPE_ACK), "CONTROLTYPE_ACK");
        CONTROLTYPES.put(ByteBuffer.wrap(CONTROLTYPE_SET_PEER_ID), "CONTROLTYPE_SET_PEER_ID");
        CONTROLTYPES.put(ByteBuffer.wrap(CONTROLTYPE_PING), "CONTROLTYPE_PING");
        CONTROLTYPES.put(ByteBuffer.wrap(CONTROLTYPE_DISCO), "CONTROLTYPE_DISCO");
    }

}