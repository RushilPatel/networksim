package networksim;

import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    HashMap<String, String> nextHop;

    public Layer3() {
        nextHop = new HashMap<String, String>();
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
    public void receiveFromLayer4( frame) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer2() {
        // TODO Auto-generated method stub

    }

}
