package networksim;

public interface Layer3Interface {
    public void receiveFromLayer2(Layer2 frame);

    public void sendToLayer4();

    public void receiveFromLayer4(Layer3 frame);

    public void sendToLayer2();
}
