package networksim;

import java.util.Arrays;
import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    HashMap<IpAddWrapper, byte[]> hostANextHop;
    HashMap<IpAddWrapper, byte[]> hostBNextHop;
    HashMap<IpAddWrapper, byte[]> hostCNextHop;
    HashMap<IpAddWrapper, byte[]> routerNextHop;

    /***
     * Wrapper class for byte array. The class implements equals and hashCode
     * method for the byte array.
     * 
     * @author meha
     */
    public static class IpAddWrapper {
        byte[] ip = new byte[6];

        public IpAddWrapper(byte[] ip) {
            this.ip = ip;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null)
                return false;
            if (!(other instanceof IpAddWrapper))
                return false;

            IpAddWrapper obj = (IpAddWrapper) other;
            if (obj == this)
                return true;
            if (ip.length != obj.ip.length)
                return false;

            for (int i = 0; i < ip.length; i++) {
                if (ip[i] != obj.ip[i])
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hashcode = 0;

            for (byte b : ip) {
                int tmp = (int) b;
                if (tmp < 0)
                    tmp = 256 + tmp;
                hashcode += 31 * b;
            }

            return hashcode;
        }
    }

    @Override
    public void receiveFromLayer2(Layer3Frame frame, Host host) {
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
