package networksim;

import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;

public class Layer1 {

    public static void receiveFromLayer2(Layer2Frame frame, Host host, byte[] nextHopIP){
        Packet packetToSend = new Packet (frame.toByteArray (), Packet.getPriorityCounter ());
        boolean isClassA = nextHopIP[0] > 0;
        if(isClassA){
            try {
                Main.classABroadcast.put (packetToSend);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try {
                Main.classCBroadcast.put (packetToSend);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println (host.getHostName () + " sent packet with priority " + packetToSend.getPriority ());

    }
    
//    public static void receiveFromLayer2(byte[] frame, Host host){
//        host.send (frame);
//    }
    
    public static void processReceivedPacket(Packet packet , Host host, BlockingQueue<Packet> queue){
        Layer2Frame frame = new Layer2Frame(packet.getData ());
        
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
        
        
        
        
//        if(host.getIPAddress ().equals (packet.getData ())){
//            System.out.println (host.getHostName () + " accepted packet");
//        }else{
//            System.out.println (host.getHostName () + " rejected packet");
//            host.discardPacket(packet);
//        }
        
    }
    
    private static void acceptPacket(Layer2Frame frame, Host host){
        Layer2.recieveFromLayer1 (frame, host);
    }
    
    private static void discardPacket(Packet packet , BlockingQueue<Packet> queue){
        queue.add (packet);
    }
    private static boolean compareMacs(byte [] mac1, byte [] mac2){
        for(int i = 0; i < mac1.length; i++){
            if((int)mac1[i] != (int) mac2[i]){
                return false;
            }
        }
        return true;
    }
}

