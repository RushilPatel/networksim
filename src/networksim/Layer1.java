package networksim;

import java.util.PriorityQueue;

public class Layer1 {

    public static void receiveFromLayer2(Layer2Frame frame, Host host, byte[] nextHopIP){
        Packet packetToSend = new Packet (frame.toByteArray (), Packet.getPriorityCounter ());
        boolean isClassA = nextHopIP[0] < 0;
        if(isClassA){
            Main.classABroadcast.add (packetToSend);
        }else{
            Main.classCBroadcast.add (packetToSend);
        }
    }
    
//    public static void receiveFromLayer2(byte[] frame, Host host){
//        host.send (frame);
//    }
    
    public static void processReceivedPacket(Packet packet , Host host, PriorityQueue<Packet> queue){
        Layer2Frame frame = new Layer2Frame(packet.getData ());
        if(host.getMACAddress ().equals (frame.getDestAddr ())){
            acceptPacket (frame, host);
        }else{
            discardPacket(packet , queue);
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
    
    private static void discardPacket(Packet packet , PriorityQueue<Packet> queue){
        queue.add (packet);
    }
}
