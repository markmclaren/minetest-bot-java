package com.markmc.minetest;

import static com.markmc.minetest.Constants.DEFAULT_SERVER_PORT;
import static com.markmc.minetest.Constants.PEER_ID_INEXISTENT;
import static com.markmc.minetest.Constants.SEQNUM_INITIAL;

/**
 * Bot.
 * @author Mark McLaren (mark.mclaren@bristol.ac.uk)
 */
public class Bot {

  private NetworkIO network;

  private int[] outgoingSeqnums
    = new int[] { SEQNUM_INITIAL, SEQNUM_INITIAL, SEQNUM_INITIAL };

  private byte[] peerId = PEER_ID_INEXISTENT;

  private ClientState state;

  String botName;

  public Bot(final String serverIp, final String botName) {
    network = new NetworkIO(serverIp, DEFAULT_SERVER_PORT);
    this.botName = botName;
  }

  public void setPeerId(final byte[] newPeerId){
    this.peerId = newPeerId;
  }

  public byte[] getPeerId(){
    return peerId;
  }

  public NetworkIO getNetwork(){
    return this.network;
  }

  public int[] getOutgoingSeqnums(){
    return this.outgoingSeqnums;
  }

  public void setOutgoingSeqnums(final int[] outgoingSeqnums){
    this.outgoingSeqnums = outgoingSeqnums;
  }

  public String getBotname(){
    return this.botName;
  }

}
