package networksim;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Layer3 {
    // The forwarding tables for each node in the network
    static HashMap<IpAddWrapper, byte[]> hostANextHop;
    static HashMap<IpAddWrapper, byte[]> hostBNextHop;
    static HashMap<IpAddWrapper, byte[]> hostCNextHop;
    static HashMap<IpAddWrapper, byte[]> routerNextHop;

    /***
     * Wrapper class for byte array. The class implements equals and hashCode
     * method for the byte array.
     * 
     * @author Mehadi
     */
    public static class IpAddWrapper {
        byte[] ip = new byte[6];

        public IpAddWrapper(byte[] ip) {
            this.ip = ip;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
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

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
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

    /**
     * This method will receive from Layer 2 and determine if the packet is for
     * this host
     * 
     * @param frame
     *            The frame to be processed
     * @param host
     *            The host that the packet is currently at
     */
    public static void receiveFromLayer2(Layer3Frame frame, Host host) {
        // This method will see if the packet is meant for this host or not. If
        // it is, it will send the packet up to layer 4. If not,
        // Layer 3 will attach a next hop and send it back down to layer 2
        // This checks if the packet is meant for this host.
        if (new IpAddWrapper(frame.destinationAddr).equals(new IpAddWrapper(
                host.getIPAddress()))) {
            System.out.println("Host: " + host.getHostName()
                    + " received a packet... ");
            try {
                Layer4.receiveFromLayer3(frame, host);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Not meant for this host so assign next hop and send back down to
            // layer 2
            byte[] nextHop = getNextHop(frame.destinationAddr, host);
            Layer2.recieveFromLayer3(frame, nextHop, host);
        }

    }

    /**
     * This method will receive a packet from layer 4, assign a next hop and a
     * checksum and send it down to layer 2
     * 
     * @param frame
     *            The frame to be processed
     * @param finalDestination
     *            The byte array representation of the final host
     * @param host
     *            The current host (Location)
     */
    public static void receiveFromLayer4(Layer3Frame frame,
            byte[] finalDestination, Host host) {
        // Determine the next hop and then send to layer 2
        System.out.println("Host: " + host.getHostName()
                + " received a packet... ");
        byte[] nextHop = getNextHop(finalDestination, host);
        frame.checksum = calculateChecksum(frame.body);
        Layer2.recieveFromLayer3(frame, nextHop, host);

    }

    /**
     * This method will look at a forwarding table and determine the next hop
     * 
     * @param finalDestAddress
     *            The address of the final host
     * @param host
     *            the current host
     * @return a byte array representation of the IP address of the next hop
     */
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

    /**
     * This method will hard code the forwarding tables based on the projects
     * given topology
     */
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

    /**
     * This method will take the body of a frame and calculate the IPv4 checksum
     * and insert it into the IPv4 Header
     * 
     * @param buf
     *            The body of the frame to be processed
     * @return a byte array representation of the IPv4 Checksum
     */
    public static byte[] calculateChecksum(byte[] buf) {
        int length = buf.length;
        int i = 0;

        long sum = 0;
        long data;

        // Handle all pairs
        while (length > 1) {
            data = (((buf[i] << 8) & 0xFF00) | ((buf[i + 1]) & 0xFF));
            sum += data;
            // 1's complement carry bit correction in 16-bits (detecting sign
            // extension)
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum += 1;
            }

            i += 2;
            length -= 2;
        }

        // Handle remaining byte in odd length buffers
        if (length > 0) {
            sum += (buf[i] << 8 & 0xFF00);
            // 1's complement carry bit correction in 16-bits (detecting sign
            // extension)
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum += 1;
            }
        }

        // Final 1's complement value correction to 16-bits
        sum = ~sum;
        sum = sum & 0xFFFF;
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(sum);
        return buffer.array();
    }
}
