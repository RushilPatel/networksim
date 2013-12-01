package networksim;

import java.util.Arrays;
import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    HashMap<byte[], byte[]> hostANextHop;
    HashMap<byte[], byte[]> hostBNextHop;
    HashMap<byte[], byte[]> hostCNextHop;
    HashMap<byte[], byte[]> routerNextHop;

    @Override
    public void receiveFromLayer2(Layer3Frame frame, byte[] finalDestination,
            Host host) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer4(Layer3Frame frame) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveFromLayer4(Layer3Frame frame, byte[] finalDestination,
            Host host) {

    }

    @Override
    public void sendToLayer2() {
        // TODO Auto-generated method stub,
    }

    public byte[] getNextHop(byte[] finalDestAddress, Host host) {

        boolean isDestClassC = (int) finalDestAddress[0] < 0;
        boolean isHostClassC = (int) host.getIPAddress()[0] < 0;

        if (isDestClassC && isHostClassC) {
            if (Arrays.equals(finalDestAddress, host.getIPAddress())) {
                // TODO waiting for receive from layer 3 method
                Layer4.
            } else {
                if (host.getHostName().equals("hostB")) {
                    byte[] c = { (byte) 192, (byte) 168, (byte) 25, (byte) 15 };
                    return c;
                } else {
                    byte[] b = { (byte) 192, (byte) 168, (byte) 25, (byte) 20 };
                    return b;
                }
            }
        } else {
            byte[] router = { (byte) 192, (byte) 168, (byte) 25, (byte) 10 };
            return router;
        }

    }
}
