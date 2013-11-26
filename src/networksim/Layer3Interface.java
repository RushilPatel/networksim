package networksim;

public interface Layer3Interface {
    public void receiveFromLayer2(Layer3 frame);

    public void sendToLayer4();

    public void receiveFromLayer4(Layer3 frame, String finalDestination);

    public void sendToLayer2();

    void receiveFromLayer4(Layer3 frame, String finalDestination, Host host);
}
