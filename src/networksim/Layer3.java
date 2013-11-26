package networksim;

import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    HashMap<String, String> nextHop;

    public Layer3() {
        nextHop = new HashMap<String, String>();
        nextHop.put("10.10.20.1", "10.10.20.2");
    }

    @Override
    public void receiveFromLayer2(Layer2 frame) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer4() {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveFromLayer4(Layer3 frame) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer2() {
        // TODO Auto-generated method stub

    }

    // public String getNexHop(String host) {
    //
    // }

}
