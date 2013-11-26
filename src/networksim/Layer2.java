package networksim;

public class Layer2 implements Layer2Interface {
    
    //static ARP table as hashmap
    
    public static void recieveFromLayer3(Layer3Frame frame, byte[] nextHopAddress, Host host) {
        // TODO Auto-generated method stub
        
        // create a Layer2Frame and add the Layer3Frame as part of the body
        
        // look up ARP table for destination MAC address and Set destination mac address
        
        // Set source MAC address
        
        // call calculate CRC32 method of Layer2Frame
        
        // pass the Layer2Frame object to Layer 1
    }

    public static void recieveFromLayer1(Layer2Frame frame, Host host) {
        // TODO Auto-generated method stub
        
    }
}
