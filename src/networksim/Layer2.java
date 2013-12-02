package networksim;

import java.util.HashMap;

public class Layer2 implements Layer2Interface {
    
    //static ARP table as hashmap
    public static HashMap<IpAddWrapper, byte[]> ARPTable;
    
    /***
     * Wrapper class for byte array. The class implements equals and hashCode method for the byte array.
     * @author meha
     */
    public static class IpAddWrapper  {
        byte[] ip = new byte[6];
        
        public IpAddWrapper(byte[] ip) {
            this.ip = ip;
        }
        
        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (!(other instanceof IpAddWrapper)) return false;
            
            IpAddWrapper obj = (IpAddWrapper) other;
            if (obj == this) return true;
            if(ip.length != obj.ip.length) return false;
            
            for(int i =0; i < ip.length; i++) {
                if(ip[i] != obj.ip[i])
                    return false;
            }
            
            return true;
        }
        
        @Override
        public int hashCode() {
            int hashcode = 0;
            
            for(byte b : ip) {
                int tmp = (int) b;
                if (tmp < 0) 
                    tmp = 256 + tmp;
                hashcode += 31 * b;
            }
            
            return hashcode;
        }
    }
    
    /***
     * Initialises ARPTable entries. This method should be called before any of Layer2 methods are used.
     */
    public static void init() {
        // initialize ARP table
        ARPTable = new HashMap<IpAddWrapper, byte[]>();
        
        byte[] ip1 = {(byte) 10, (byte) 10, (byte) 10, (byte) 10};
        byte[] ip2 = {(byte) 10, (byte) 10, (byte) 11, (byte) 11};
        
        byte[] mac1 = {(byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10};
        byte[] mac2 = {(byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11, (byte) 11};
        
        ARPTable.put(new IpAddWrapper(ip1), mac1);
        ARPTable.put(new IpAddWrapper(ip2), mac2);
    }
    
    /***
     * Receives IP packets from layer 3, performs required layer 2 processing and 
     * hands a Layer 2 frame to Layer 1. 
     * @param frame The layer 3 IP packet to be sent
     * @param nextHopAddress - IP address of the next hop node in the path
     * @param host - current host machine information processing this packet 
     */
    public static void recieveFromLayer3(Layer3Frame frame, byte[] nextHopAddress, Host host) {
        
        // create a Layer2Frame and add the Layer3Frame as part of the body
        Layer2Frame l2 = new Layer2Frame();
        l2.setBody(frame.toByteArray());
        
        // look up ARP table for destination MAC address 
        if(!ARPTable.containsKey(new IpAddWrapper(nextHopAddress)))
            System.out.println("Error: nextHopAddress ip address not found in ARPTable!");
            
        byte[] destMAC = ARPTable.get(new IpAddWrapper(nextHopAddress));
        // Set destination mac address
        l2.setDestAddr(destMAC);
        
        // Set source MAC address
        l2.setSrcAddr(host.getMACAddress());
        
        // call calculate CRC32 method of Layer2Frame 
        l2.calculateAndSetCRC();
        
        // pass the Layer2Frame object to Layer 1
        Layer1.receiveFromLayer2(l2, host, nextHopAddress);
    }

    /***
     * Receives a Layer2 frame from layer 1, performs the required processing and 
     * passes a Leyer3 frame to Layer3
     * @param frame - layer 2 frame
     * @param host - current host machine information processing this packet
     */
    public static void recieveFromLayer1(Layer2Frame frame, Host host) {
        
        // create a layer 3 frame
        Layer3Frame l3 = new Layer3Frame(frame.getBody());
        
        // pass layer 3 frame to layer 3
        Layer3.receiveFromLayer2(l3, host);
    }
    
    public static void main(String[] args) {
        init();
        
        byte[] ip1 = {(byte) 10, (byte) 10, (byte) 10, (byte) 10};
        byte[] ip2 = {(byte) 10, (byte) 10, (byte) 11, (byte) 11};
        
        System.out.println(ARPTable.containsKey(new IpAddWrapper(ip1)));
        System.out.println(ARPTable.containsKey(new IpAddWrapper(ip2)));
    }
}
