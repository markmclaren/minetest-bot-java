package com.markmc.minetest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 *
 * @author markmc
 */
public class NetworkIO {

    private final String host;
    private final int port;
    private DatagramSocket socket;
    private byte[] receiveData;
    private DatagramPacket receivePacket;

    public NetworkIO(final String host, final int port) {
        this.host = host;
        this.port = port;
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException ex) {
        }
    }

    public ByteBuffer requestData() throws IOException {
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        return ByteBuffer.wrap(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
    }

    public void send(byte[] msg) {
        try {
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket request = new DatagramPacket(
                    msg, msg.length, address, port);
            socket.setSoTimeout(10 * 1000);
            socket.send(request);
        } catch (UnknownHostException ex) {
        } catch (SocketException ex) {
        } catch (IOException ex) {
        }
    }

}
