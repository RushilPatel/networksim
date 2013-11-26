package networksim;

public class Layer3Frame implements FrameInterface {
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
    final byte[] versionHLen = new byte[VERSION_HLEN_SIZE];
    final byte[] tos = new byte[TOS_SIZE];
    final byte[] length = new byte[LENGTH];
    final byte[] ident = new byte[IDENT_SIZE];
    final byte[] flagsOffset = new byte[FLAGS_OFFSET_SIZE];
    final byte[] ttl = new byte[TTL_SIZE];
    final byte[] protocol = new byte[PROTOCOL_SIZE];
    final byte[] checksum = new byte[CHECKSUM_SIZE];
    final byte[] sourceAddr = new byte[SOURCE_ADDRESS_SIZE];
    final byte[] destinationAddr = new byte[DESTINATION_ADDRESS_SIZE];
    byte[] body = new byte[MIN_BODY_SIZE];

    public static final int HEADER_SIZE = 1 + 1 + 2 + 2 + 2 + 1 + 1 + 2 + 4 + 4;

    public Layer3Frame() {

    }

    public Layer3Frame(byte[] frameByteSequence) {
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

    @Override
    public byte[] toByteArray() {
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

        return byteArr;
    }
}
