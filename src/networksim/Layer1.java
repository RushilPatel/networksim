package networksim;

import java.util.concurrent.BlockingQueue;

public class Layer1 {

    /**
     * Sends packet to the next hop. 
     * It places the packet into proper queue based on the IP 
     * Address of next hop
     * @param frame - frame to send
     * @param host - current host sending the packet
     * @param nextHopIP - IP address of next hop
     */
    public static void receiveFromLayer2(Layer2Frame frame, Host host, byte[] nextHopIP){
        Packet packetToSend = new Packet (frame.toByteArray (), Packet.getNextPriority ());
        //if next hop is class A network, put the packet into class A broadcast queue
        //else, put it into classCBroadcast queue
        boolean isClassA = nextHopIP[0] > 0;
        if(isClassA){
            try {
                Main.classABroadcast.put (packetToSend);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Main.classCBroadcast.put (packetToSend);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println (host.getHostName () + " sent packet with priority " + packetToSend.getPriority ());

    }
    
    
    /**
     * Process a packet when a host receives it.
     * Checks destination MAC address of the packet,
     * if it is equal to host's MACaddress, then it accepts the packets.
     * Else, it rejects the packet/
     * @param packet - packet data
     * @param host - host that removed the packet
     * @param queue - queue the packet was removed from
     */
    public static void processReceivedPacket(Packet packet , Host host, BlockingQueue<Packet> queue){
        Layer2Frame frame = new Layer2Frame(packet.getData ());
        //if the host that received the packet is router, then match both MAC addresses
        if(host.isRouter){
            if(compareMacs (host.getMACAddress (), frame.getDestAddr ()) || compareMacs (host.getMACAddress2 (), frame.getDestAddr ())){
                System.out.println (host.getHostName () + " accepted packet with priority " + packet.getPriority ());
                acceptPacket (frame, host);
            }else{
                //System.out.println (host.getHostName () + " discarded packet with priority " + packet.getPriority ());
                discardPacket(packet , queue);
            }
            
        }else{
            if(compareMacs (host.getMACAddress (), frame.getDestAddr ())){
                System.out.println (host.getHostName () + " accepted packet with priority " + packet.getPriority ());
                acceptPacket (frame, host);
            }else{
                //System.out.println (host.getHostName () + " discarded packet with priority " + packet.getPriority ());
                discardPacket(packet , queue);
            }
        }
    }
    
    /**
     * Accepts the packet, meaning sends it to layer2
     * @param frame - layer 2 frame
     * @param host - host accepting the packet
     */
    private static void acceptPacket(Layer2Frame frame, Host host){
        Layer2.recieveFromLayer1 (frame, host);
    }
    
    /**
     * Rejects the packet, meaning it puts it back in the queue,
     * so other host can get the packet
     * @param packet - packet to discard
     * @param queue - queue to put packet back into
     */
    private static void discardPacket(Packet packet , BlockingQueue<Packet> queue){
        queue.add (packet);
    }
    
    
    /**
     * Compares two MAC address
     * @param mac1
     * @param mac2
     * @return -true, if equal
     *         -false, otherwise
     */
    private static boolean compareMacs(byte [] mac1, byte [] mac2){
        for(int i = 0; i < mac1.length; i++){
            if((int)mac1[i] != (int) mac2[i]){
                return false;
            }
        }
        return true;
    }
}

