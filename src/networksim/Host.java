package networksim;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

public class Host implements Runnable {

    private byte[] ipAddress;
    private byte[] macAddress;
    private String hostName;
    private byte[] subnetMask;

    private byte[] ipAddress2;
    private byte[] macAddress2;
    private byte[] subnetMask2;

    public boolean isRouter = false;

    public Host(byte[] ipAddress, byte[] subnetMask, byte[] macAddress,
            String hostName) {
        this.hostName = hostName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
    }

    public Host(byte[] ipAddress, byte[] subnetMask, byte[] macAddress,
            String hostName, byte[] ipAddress2, byte[] subnetMask2,
            byte[] macAddress2) {
        this.hostName = hostName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;

        this.macAddress2 = macAddress2;
        this.ipAddress2 = ipAddress2;
        this.subnetMask2 = subnetMask2;
        this.isRouter = true;
    }

    public byte[] getIPAddress() {
        return this.ipAddress;
    }

    public byte[] getSubnetMask() {
        return this.subnetMask;
    }

    public byte[] getMACAddress() {
        return this.macAddress;
    }

    public String getHostName() {
        return this.hostName;
    }

    public byte[] getIPAddress2() {
        return this.ipAddress2;
    }

    public byte[] getSubnetMask2() {
        return this.subnetMask2;
    }

    public byte[] getMACAddress2() {
        return this.macAddress2;
    }

    public String toString() {
        return this.hostName;
    }

    public void receive(Packet packet, PriorityQueue<Packet> queue) {
        Layer1.processReceivedPacket(packet, this, queue);
    }

    public void sendFile(byte[] destIPAddress, File fileToSend) {
        try {
            Layer4.receiveFromHost(fileToSend, this, destIPAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Layer1.receiveFromLayer2 (destIPAddress, this);
    }

    // //TODO: Add Support for router to separate networks
    // public void send (byte[] bytesToSend){
    //
    // Packet packetToSend = new Packet (bytesToSend, Packet.getPriorityCounter
    // ());
    // boolean isClassA = (int)this.getIPAddress ()[0] < 0;
    // if(isClassA){
    // Main.classABroadcast.add (packetToSend);
    // }else{
    // Main.classCBroadcast.add (packetToSend);
    // }
    // }
    //
    // public void discardPacket(Packet packet){
    // boolean isClassA = (int)this.getIPAddress ()[0] < 0;
    // if(isClassA){
    // Main.classABroadcast.add (packet);
    // }else{
    // Main.classCBroadcast.add (packet);
    // }
    // }

    @Override
    public synchronized void run() {

        if (isRouter) {
            if (!Main.classABroadcast.isEmpty()) {
                this.receive(Main.classABroadcast.remove(),
                        Main.classABroadcast);
            }
            if (!Main.classCBroadcast.isEmpty()) {
                this.receive(Main.classCBroadcast.remove(),
                        Main.classCBroadcast);
            }
        } else {
            boolean isClassA = (int) this.getIPAddress()[0] > 0;
            if (isClassA) {
                if (!Main.classABroadcast.isEmpty()) {
                    this.receive(Main.classABroadcast.remove(),
                            Main.classABroadcast);
                }
            } else {
                if (!Main.classCBroadcast.isEmpty()) {
                    this.receive(Main.classCBroadcast.remove(),
                            Main.classCBroadcast);
                }
            }
        }

    }
}
