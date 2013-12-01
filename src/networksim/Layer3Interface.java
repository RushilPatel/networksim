package networksim;

public interface Layer3Interface {
    public void receiveFromLayer2(Layer3Frame frame, byte[] finalDestination,
            Host host);

    public void sendToLayer4(Layer3Frame frame);

    public void sendToLayer2();

    void receiveFromLayer4(Layer3Frame frame, byte[] finalDestination, Host host);
}
