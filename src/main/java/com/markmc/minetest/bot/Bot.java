package com.markmc.minetest.bot;

import com.markmc.minetest.connector.ClientState;
import com.markmc.minetest.utils.NetworkIO;
import static com.markmc.minetest.utils.Constants.DEFAULT_SERVER_PORT;
import static com.markmc.minetest.utils.Constants.PEER_ID_INEXISTENT;
import static com.markmc.minetest.utils.Constants.SEQNUM_INITIAL;

/**
 * Bot.
 * @author markmc
 */
public class Bot {

  private final NetworkIO network;

  private int[] outgoingSeqnums
    = new int[] { SEQNUM_INITIAL, SEQNUM_INITIAL, SEQNUM_INITIAL };

  private byte[] peerId = PEER_ID_INEXISTENT;

  private ClientState state;

  String botName;

  public Bot(final String serverIp, final String botName) {
    this.network = new NetworkIO(serverIp, DEFAULT_SERVER_PORT);
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
  
  public ClientState getState(){
      return this.state;
  }
  
  public void setState(final ClientState state){
      this.state = state;
  }
  

}
