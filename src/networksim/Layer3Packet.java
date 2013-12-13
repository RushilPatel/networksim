package networksim;

public class Layer3Packet implements FrameInterface {
    // IPv4 Header Field sizes
    // All values are in bytes
    private static final int VERSION_HLEN_SIZE = 1;
    private static final int TOS_SIZE = 1;
    private static final int LENGTH = 2;
    private static final int IDENT_SIZE = 2;
    private static final int FLAGS_OFFSET_SIZE = 2;
    private static final int TTL_SIZE = 1;
    private static final int PROTOCOL_SIZE = 1;
    private static final int CHECKSUM_SIZE = 2;
    private static final int SOURCE_ADDRESS_SIZE = 4;
    private static final int DESTINATION_ADDRESS_SIZE = 4;
    private static final int MIN_BODY_SIZE = 46;
    // Omitted Options and Pad field as I didn't see them necessary
    // Actual IPv4 Header Fields
    byte[] versionHLen = new byte[VERSION_HLEN_SIZE];
    byte[] tos = new byte[TOS_SIZE];
    byte[] length = new byte[LENGTH];
    byte[] ident = new byte[IDENT_SIZE];
    byte[] flagsOffset = new byte[FLAGS_OFFSET_SIZE];
    byte[] ttl = new byte[TTL_SIZE];
    byte[] protocol = new byte[PROTOCOL_SIZE];
    byte[] checksum = new byte[CHECKSUM_SIZE];
    byte[] sourceAddr = new byte[SOURCE_ADDRESS_SIZE];
    byte[] destinationAddr = new byte[DESTINATION_ADDRESS_SIZE];
    byte[] body = new byte[MIN_BODY_SIZE];

    // Guaranteed IPv4 Header size (Excludes the Body)
    public static final int HEADER_SIZE = 1 + 1 + 2 + 2 + 2 + 1 + 1 + 2 + 4 + 4;

    /**
     * This method serves as the generic constructor so that a Layer 3 Frame can
     * be blankly constructed
     */
    public Layer3Packet() {

    }

    /**
     * This method serves as a copy constructor for Layer 3 Frame
     * 
     * @param frameByteSequence
     *            The information to be transformed into a Layer 3 Frame
     */
    public Layer3Packet(byte[] frameByteSequence) {
        this();

        int i = 0;
        // Initialize version and header length
        for (int j = 0; j < VERSION_HLEN_SIZE; i++, j++)
            versionHLen[j] = frameByteSequence[i];
        // Initialize TOS
        for (int j = 0; j < TOS_SIZE; i++, j++)
            tos[j] = frameByteSequence[i];
        // Initialize Length field
        for (int j = 0; j < LENGTH; i++, j++)
            length[j] = frameByteSequence[i];
        // Initialize Ident field
        for (int j = 0; j < IDENT_SIZE; i++, j++)
            ident[j] = frameByteSequence[i];
        // Initialize Flags and Offset fields
        for (int j = 0; j < FLAGS_OFFSET_SIZE; i++, j++)
            flagsOffset[j] = frameByteSequence[i];
        // Initialize TTL field
        for (int j = 0; j < TTL_SIZE; i++, j++)
            ttl[j] = frameByteSequence[i];
        // Initialize Protocol field
        for (int j = 0; j < PROTOCOL_SIZE; i++, j++)
            protocol[j] = frameByteSequence[i];
        // Initialize Checksum field
        for (int j = 0; j < CHECKSUM_SIZE; i++, j++)
            checksum[j] = frameByteSequence[i];
        // Initialize Source Address field
        for (int j = 0; j < SOURCE_ADDRESS_SIZE; i++, j++)
            sourceAddr[j] = frameByteSequence[i];
        // Initialize Destination Address field
        for (int j = 0; j < DESTINATION_ADDRESS_SIZE; i++, j++)
            destinationAddr[j] = frameByteSequence[i];

        // Initialize body
        body = new byte[frameByteSequence.length - i > 0 ? frameByteSequence.length
                - i
                : 0];
        for (int j = 0; i < frameByteSequence.length && j < body.length; i++, j++)
            body[j] = frameByteSequence[i];
    }

    /*
     * (non-Javadoc)
     * @see networksim.FrameInterface#toByteArray()
     */
    @Override
    public byte[] toByteArray() {
        // The continuous byte array that will be eventually written on the line
        byte[] byteArr = new byte[HEADER_SIZE + body.length];

        int i = 0;
        for (int j = 0; j < versionHLen.length; i++, j++)
            byteArr[i] = versionHLen[j];
        for (int j = 0; j < tos.length; i++, j++)
            byteArr[i] = tos[j];
        for (int j = 0; j < length.length; i++, j++)
            byteArr[i] = length[j];
        for (int j = 0; j < ident.length; i++, j++)
            byteArr[i] = ident[j];
        for (int j = 0; j < flagsOffset.length; i++, j++)
            byteArr[i] = flagsOffset[j];
        for (int j = 0; j < ttl.length; i++, j++)
            byteArr[i] = ttl[j];
        for (int j = 0; j < protocol.length; i++, j++)
            byteArr[i] = protocol[j];
        for (int j = 0; j < checksum.length; i++, j++)
            byteArr[i] = checksum[j];
        for (int j = 0; j < sourceAddr.length; i++, j++)
            byteArr[i] = sourceAddr[j];
        for (int j = 0; j < destinationAddr.length; i++, j++)
            byteArr[i] = destinationAddr[j];
        for (int j = 0; j < body.length; i++, j++)
            byteArr[i] = body[j];

        return byteArr;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getVersionHLen() {
        return versionHLen;
    }

    public byte[] getTos() {
        return tos;
    }

    public byte[] getLength() {
        return length;
    }

    public byte[] getIdent() {
        return ident;
    }

    public byte[] getFlagsOffset() {
        return flagsOffset;
    }

    public byte[] getTtl() {
        return ttl;
    }

    public byte[] getProtocol() {
        return protocol;
    }

    public byte[] getChecksum() {
        return checksum;
    }

    public byte[] getSourceAddr() {
        return sourceAddr;
    }

    public byte[] getDestinationAddr() {
        return destinationAddr;
    }

    /***
     * Accepts byte array representation of a MAC or an IP address and returns
     * string representation.
     * 
     * @param arr
     *            byte array
     * @return string representation of a byte array MAC or an IP address.
     */
    public static String byteAddressToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();

        if (arr.length == 4) {
            for (int i = 0; i < arr.length; i++) {
                int tmp = 0;
                tmp |= arr[i];
                sb.append(tmp);
                if (i != arr.length - 1)
                    sb.append(".");
            }
        } else {
            for (int i = 0; i < arr.length; i++) {
                int tmp = 0;
                tmp |= arr[i];
                sb.append(String.format("%x", tmp));
                if (i != arr.length - 1)
                    sb.append(":");
            }
        }
        return sb.toString();
    }

    /***
     * To string method for Layer 3 frames.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("SrcIP %s, ", byteAddressToString(sourceAddr)));
        sb.append(String.format("DestIP %s",
                byteAddressToString(destinationAddr)));

        return sb.toString();
    }
}
