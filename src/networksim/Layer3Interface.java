package networksim;

public interface Layer3Interface {
    public void receiveFromLayer2(Layer3Frame frame, Host host);

    public void receiveFromLayer4(Layer3Frame frame, byte[] finalDestination,
            Host host);
}
