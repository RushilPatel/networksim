package networksim;

import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    // The forwarding tables for each node in the network
    static HashMap<IpAddWrapper, byte[]> hostANextHop;
    static HashMap<IpAddWrapper, byte[]> hostBNextHop;
    static HashMap<IpAddWrapper, byte[]> hostCNextHop;
    static HashMap<IpAddWrapper, byte[]> routerNextHop;

    /***
     * Wrapper class for byte array. The class implements equals and hashCode
     * method for the byte array.
     * 
     * @author mehadi
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

    /**
     * This method initializes the forwarding tables and populates them
     * This should be called before any Layer 3 methods
     */
    public static void init() {
        hostANextHop = new HashMap<IpAddWrapper, byte[]>();
        hostBNextHop = new HashMap<IpAddWrapper, byte[]>();
        hostCNextHop = new HashMap<IpAddWrapper, byte[]>();
        routerNextHop = new HashMap<IpAddWrapper, byte[]>();
        // Create the forwarding tables
        createTables();
    }

    @Override
    public void receiveFromLayer2(Layer3Frame frame, Host host) {
        // No processing is done in the method. Layer 3 simply sends the frame
        // along to layer 4
        Layer4.receiveFromLayer3(frame.body, host);

    }

    @Override
    public void receiveFromLayer4(Layer3Frame frame, byte[] finalDestination,
            Host host) {
        // Determine the next hop and then send to layer 2
        byte[] nextHop = getNextHop(finalDestination, host);
        Layer2.recieveFromLayer3(frame, nextHop, host);

    }

    public static byte[] getNextHop(byte[] finalDestAddress, Host host) {
        createTables();
        // Default gateways
        byte[] def1 = { (byte) 10, (byte) 10, (byte) 20, (byte) 2 };
        byte[] def2 = { (byte) 192, (byte) 168, (byte) 25, (byte) 10 };
        // Figure out the host based on the host object. Use that to get the
        // next hop
        if (host.getHostName().equals("HostA")) {
            if (hostANextHop.containsKey(new IpAddWrapper(finalDestAddress))) {
                return hostANextHop.get(new IpAddWrapper(finalDestAddress));
            } else {
                return def1;
            }
        } else if (host.getHostName().equals("HostB")) {
            if (hostBNextHop.containsKey(new IpAddWrapper(finalDestAddress))) {
                return hostBNextHop.get(new IpAddWrapper(finalDestAddress));
            } else {
                return def2;
            }
        } else if (host.getHostName().equals("HostC")) {
            if (hostCNextHop.containsKey(new IpAddWrapper(finalDestAddress))) {
                return hostCNextHop.get(new IpAddWrapper(finalDestAddress));
            } else {
                return def2;
            }
        } else {
            return routerNextHop.get(new IpAddWrapper(finalDestAddress));
        }
    }

    public static void createTables() {
        byte[] dest1A = { (byte) 10, (byte) 10, (byte) 20, (byte) 1 };
        byte[] dest2B = { (byte) 192, (byte) 168, (byte) 25, (byte) 20 };
        byte[] dest3C = { (byte) 192, (byte) 168, (byte) 25, (byte) 15 };
        byte[] destR1 = { (byte) 10, (byte) 10, (byte) 20, (byte) 2 };
        byte[] destR2 = { (byte) 192, (byte) 168, (byte) 25, (byte) 10 };
        hostANextHop.put(new IpAddWrapper(dest2B), destR1);
        hostANextHop.put(new IpAddWrapper(dest3C), destR1);
        routerNextHop.put(new IpAddWrapper(dest1A), dest1A);
        routerNextHop.put(new IpAddWrapper(dest2B), dest2B);
        routerNextHop.put(new IpAddWrapper(dest3C), dest3C);
        hostBNextHop.put(new IpAddWrapper(dest1A), destR2);
        hostBNextHop.put(new IpAddWrapper(dest3C), dest3C);
        hostCNextHop.put(new IpAddWrapper(dest1A), destR2);
        hostCNextHop.put(new IpAddWrapper(dest2B), dest2B);
    }

}
