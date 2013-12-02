package networksim;

import java.io.File;
import java.util.PriorityQueue;

public class Host implements Runnable{
    
    private byte[] ipAddress;
    private byte[] macAddress;
    private String hostName;
    private byte[] subnetMask;
    
    public Host(byte[] ipAddress, byte[] subnetMask, byte [] macAddress, String hostName){
        this.hostName = hostName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
    }
    
    public byte [] getIPAddress(){
        return this.ipAddress;
    }
    
    public byte [] getSubnetMask(){
        return this.subnetMask;
    }
    
    public byte [] getMACAddress(){
        return this.macAddress;
    }
    public String getHostName(){
        return this.hostName;
    }
    
    public String toString(){
        return this.hostName;
    }
        
    public void receive (Packet packet, PriorityQueue<Packet> queue){
        Layer1.processReceivedPacket (packet, this, queue);
    }
    
    public void sendFile(byte [] destIPAddress, File fileToSend){
        Layer4.receiveFromHost (fileToSend, this, destIPAddress);
        //Layer1.receiveFromLayer2 (destIPAddress, this);
    }
    
//    //TODO: Add Support for router to separate networks
//    public void send (byte[] bytesToSend){
//        
//        Packet packetToSend = new Packet (bytesToSend, Packet.getPriorityCounter ());
//        boolean isClassA = (int)this.getIPAddress ()[0] < 0;
//        if(isClassA){
//            Main.classABroadcast.add (packetToSend);
//        }else{
//            Main.classCBroadcast.add (packetToSend);
//        }
//    }
//    
//    public void discardPacket(Packet packet){
//        boolean isClassA = (int)this.getIPAddress ()[0] < 0;
//        if(isClassA){
//            Main.classABroadcast.add (packet);
//        }else{
//            Main.classCBroadcast.add (packet);
//        }
//    }

    @Override
    public synchronized void run () {
        /////////////Add router support
        boolean isClassA = (int)this.getIPAddress ()[0] < 0;
        if(isClassA){
            if(!Main.classABroadcast.isEmpty ()){
                this.receive (Main.classABroadcast.remove (), Main.classABroadcast);
            }
        }else{
            if(!Main.classCBroadcast.isEmpty ()){
                this.receive (Main.classCBroadcast.remove (), Main.classCBroadcast);
            }
        }
    }
}
