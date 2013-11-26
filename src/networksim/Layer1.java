package networksim;

public class Layer1 {

    public static void receiveFromLayer2(Layer2Frame frame, Host host){
        host.send (frame.toByteArray ());
    }
    
//    public static void receiveFromLayer2(byte[] frame, Host host){
//        host.send (frame);
//    }
    
    public static void processReceivedPacket(Packet packet , Host host){
        Layer2Frame frame = new Layer2Frame(packet.getData ());
        if(host.getMACAddress ().equals (frame.getDestAddr ())){
            Layer2.recieveFromLayer1 (frame);
        }else{
            host.discardPacket(packet);
        }
        
        
//        if(host.getIPAddress ().equals (packet.getData ())){
//            System.out.println (host.getHostName () + " accepted packet");
//        }else{
//            System.out.println (host.getHostName () + " rejected packet");
//            host.discardPacket(packet);
//        }
        
    }
}
