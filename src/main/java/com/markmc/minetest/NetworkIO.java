package com.markmc.minetest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * NetworkIO.
 * <p>
 * @author markmc
 */
public class NetworkIO {

  private static final int RECEIVED_BUFFER_SIZE = 1024;

  private static final int TIMEOUT = 10 * 1000;

  private String host;

  private int port;

  private DatagramSocket socket;

  private byte[] receiveData;

  private DatagramPacket receivePacket;

  /**
   * NetworkIO constructor.
   * <p>
   * @param host host
   * @param port port
   */
  public NetworkIO(final String host, final int port) {
    this.host = host;
    this.port = port;
    try {
      this.socket = new DatagramSocket();
    } catch (SocketException ex) {
    }
  }

  /**
   * requestData.
   * <p>
   * @return ByteBuffer
   * <p>
   * @throws IOException if something goes wrong
   */
  public ByteBuffer requestData() throws IOException {
    receiveData = new byte[RECEIVED_BUFFER_SIZE];
    receivePacket = new DatagramPacket(receiveData, receiveData.length);
    socket.receive(receivePacket);
    return ByteBuffer.wrap(receivePacket.getData(), receivePacket.getOffset(),
                           receivePacket.getLength());
  }

  /**
   * send.
   * <p>
   * @param msg byte[]
   */
  public void send(final byte[] msg) {
    try {
      InetAddress address = InetAddress.getByName(host);
      DatagramPacket request = new DatagramPacket(
        msg, msg.length, address, port);
      socket.setSoTimeout(TIMEOUT);
      socket.send(request);
    } catch (UnknownHostException ex) {
    } catch (SocketException ex) {
    } catch (IOException ex) {
    }
  }

}
