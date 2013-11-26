package networksim;

import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    HashMap<String, String> nextHop;
    String defaultGateway;

    public Layer3() {
        nextHop = new HashMap<String, String>();
        this.defaultGateway = nextHop.get(defaultGateway);
    }

    @Override
    public void receiveFromLayer2(Layer3 frame) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer4() {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveFromLayer4(Layer3 frame, String finalDestination) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer2() {
        // TODO Auto-generated method stub

    }

    public String getNexHop(String host) {
        if (nextHop.containsKey(host)) {
            return nextHop.get(host);
        } else {
            return defaultGateway;
        }
    }

}
