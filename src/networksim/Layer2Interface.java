package networksim;

public interface Layer2Interface {
    public static void recieveFromLayer3(Layer3Frame frame, byte[] nextHopAddress);
    public static void recieveFromLayer1(Layer2Frame frame);
}
