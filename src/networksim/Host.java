package networksim;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class Host implements Runnable {

    private byte[] ipAddress;
    private byte[] macAddress;
    private String hostName;
    private byte[] subnetMask;

    //the following fields only apply to Router
    private byte[] ipAddress2;
    private byte[] macAddress2;
    private byte[] subnetMask2;
    
    public boolean isRouter;
    
    public Host(byte[] ipAddress, byte[] subnetMask, byte [] macAddress, String hostName){
    
        this.hostName = hostName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.isRouter = false;
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

    public void receive(Packet packet, BlockingQueue<Packet> queue) {
        Layer1.processReceivedPacket(packet, this, queue);
    }
    
    /**
     * Sends file to the given destination Host
     * Also initiates the file transfer
     * @param destIPAddress
     * @param fileToSend
     * @throws IOException
     */
    public void sendFile(byte [] destIPAddress, File fileToSend) throws IOException{
        Layer4.receiveFromHost (fileToSend, this, destIPAddress);
    }


    @Override
    public synchronized void run() {

//        while(Main.transferring.get() || !Main.classABroadcast.isEmpty() || !Main.classCBroadcast.isEmpty()) causes packet out of order delivery
        {
            if (isRouter) {
                if (!Main.classABroadcast.isEmpty()) {
                    synchronized(this) {
                        try {
                            this.receive(Main.classABroadcast.take(),
                                Main.classABroadcast);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!Main.classCBroadcast.isEmpty()) {
                    synchronized(this) {
                        try {
                            this.receive(Main.classCBroadcast.take(),
                                Main.classCBroadcast);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                boolean isClassA = (int) this.getIPAddress()[0] > 0;
                if (isClassA) {
                    if (!Main.classABroadcast.isEmpty()) {
                        synchronized(this) {
                            try {
                                this.receive(Main.classABroadcast.take(),
                                    Main.classABroadcast);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    if (!Main.classCBroadcast.isEmpty()) {
                        synchronized(this) {
                            try {
                                this.receive(Main.classCBroadcast.take(),
                                    Main.classCBroadcast);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } 
                }
            }
        }
    }
}
