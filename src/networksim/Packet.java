package networksim;

public class Packet implements Comparable<Packet>{

    private byte[] data;
    private int priority;
    public Packet(byte [] data , int priority){
        this.data = data;
        this.priority = priority;
    }
    
    public byte [] getData(){
        return this.data;
    }
    
    public int getPriority(){
        return this.priority;
    }

    @Override
    public int compareTo (Packet packet) {
        return this.getPriority () - packet.getPriority ();
    }
}
