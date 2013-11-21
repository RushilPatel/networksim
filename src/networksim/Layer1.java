package networksim;

public class Layer1 {

    public static void receiveFromLayer2(Layer2Frame frame){
        Main.send (frame.toBytesArray());
    }
    
    public static void processReceivedPacket(Byte [] packet , Host host){
        Layer2Frame frame = new Layer2Frame(packet);
        
    }
}
