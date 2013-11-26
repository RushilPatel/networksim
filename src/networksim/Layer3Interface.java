package networksim;

public interface Layer3Interface {
    public void receiveFromLayer2(Layer3Frame frame);

    public void sendToLayer4();

    public void receiveFromLayer4(Layer3Frame frame, String finalDestination);

    public void sendToLayer2();

    void receiveFromLayer4(Layer3 frame, String finalDestination, Host host);
}
