package networksim;

import java.util.HashMap;

public class Layer2 {
    
    //static ARP table as hashmap
    public static HashMap<IpAddWrapper, byte[]> ARPTable;
    
    /***
     * Wrapper class for byte array. The class implements equals and hashCode method for the byte array.
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
     * @param ip - IP address
     * @param mac - corresponding MAC address
     */
    public static void init(byte[] ip, byte[] mac) {
        // initialize ARP table
        if(ARPTable == null)
            ARPTable = new HashMap<IpAddWrapper, byte[]>();
            ARPTable.put(new IpAddWrapper(ip), mac);
    }
    
    /***
     * Receives IP packets from layer 3, performs required layer 2 processing and 
     * hands a Layer 2 frame to Layer 1. 
     * @param frame The layer 3 IP packet to be sent
     * @param nextHopAddress - IP address of the next hop node in the path
     * @param host - current host machine information processing this packet 
     */
    public static void recieveFromLayer3(Layer3Packet frame, byte[] nextHopAddress, Host host) {
        System.out.printf("%s: layer 2: received data from layer 3\n", host.getHostName());
        // create a Layer2Frame and add the Layer3Frame as part of the body
        Layer2Frame l2 = new Layer2Frame();
        l2.setBody(frame.toByteArray());
        
        // look up ARP table for destination MAC address 
        if(!ARPTable.containsKey(new IpAddWrapper(nextHopAddress)))
            System.out.println("Error: nextHopAddress ip address not found in ARPTable!\n");
            
        byte[] destMAC = ARPTable.get(new IpAddWrapper(nextHopAddress));
        // Set destination mac address
        l2.setDestAddr(destMAC);
        
        // Set source MAC address
        l2.setSrcAddr(host.getMACAddress());
        
        // call calculate CRC32 method of Layer2Frame 
        l2.calculateAndSetCRC();
        
        // pass the Layer2Frame object to Layer 1
        System.out.printf("%s: layer 2: passing data to layer 1\n", host.getHostName());
        Layer1.receiveFromLayer2(l2, host, nextHopAddress);
    }

    /***
     * Receives a Layer2 frame from layer 1, performs the required processing and 
     * passes a Leyer3 frame to Layer3
     * @param frame - layer 2 frame
     * @param host - current host machine information processing this packet
     */
    public static void recieveFromLayer1(Layer2Frame frame, Host host) {
        System.out.printf("%s: layer 2: received data from layer 1\n", host.getHostName());
        
        // check crc 
        if(!frame.verifyCRC()) {
            System.out.printf("%s: layer 2: ERROR invalid CRC!\n",host.getHostName());
            return;
        }
        
        // create a layer 3 frame
        Layer3Packet l3 = new Layer3Packet(frame.getBody());
        
        // pass layer 3 frame to layer 3
        System.out.printf("%s: layer 2: passing data to layer 3\n", host.getHostName());
        Layer3.receiveFromLayer2(l3, host);
    }
    
}
