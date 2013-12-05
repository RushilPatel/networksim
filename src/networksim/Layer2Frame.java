package networksim;

import java.util.Random;
import java.util.zip.CRC32;

public class Layer2Frame implements FrameInterface {
    private static final int CRC_SIZE_4 = 4;
    //the minimum allowed size of the body is 46bytes.
    private static final int MIN_BODY_SIZE_46 = 46;
    private static final int TYPE_SIZE_2 = 2;
    private static final int ADDRESS_SIZE_6 = 6;
    private static final int PREAMBLE_SIZE_8 = 8;
    // preamble is an alternating sequence of 0 and 1 bits 
    final byte[] preamble = new byte[PREAMBLE_SIZE_8];
    byte[] destAddr = new byte[ADDRESS_SIZE_6];
    byte[] srcAddr = new byte[ADDRESS_SIZE_6];
    final byte[] type =  new byte[TYPE_SIZE_2];
    //the minimum allowed size of the body is 46bytes.
    byte[] body = new byte[MIN_BODY_SIZE_46];
    byte[] crc =  new byte[CRC_SIZE_4];
    
    public static final int HEADER_N_CRC_SIZE = 8 + 6 + 6 + 2 + 4;
    
    public Layer2Frame() {
        // initialize preamble to an alternating 0 1 sequence 
        for(int i = 0; i < preamble.length; i++)
            preamble[i] = (byte) 0xAA;
    }
    
    public Layer2Frame(byte[] frameByteSequence) {
        this();
        // TODO what should be done in the case where frameByteSequence.length is less that HEADER_N_CRC_SIZE
        
        int i = 0;
      //Initialize preamble
        for(int j = 0; j < PREAMBLE_SIZE_8; i++, j++)
            preamble[j] = frameByteSequence[i];
      //Initialize destination address
        for(int j = 0; j < ADDRESS_SIZE_6; i++, j++)
            destAddr[j] = frameByteSequence[i];
      //Initialize destination address
        for(int j = 0; j < ADDRESS_SIZE_6; i++, j++)
            srcAddr[j] = frameByteSequence[i];
      //Initialize destination address
        for(int j = 0; j < TYPE_SIZE_2; i++, j++) 
            type[j] = frameByteSequence[i];
      //Initialize body 
        int bodySize = frameByteSequence.length - HEADER_N_CRC_SIZE > 0 ? frameByteSequence.length - HEADER_N_CRC_SIZE : 0;
        body = new byte[bodySize];
        for(int j = 0; j < body.length; i++, j++)
            body[j] = frameByteSequence[i];
        // Initialize crc
        for(int j = 0; j < CRC_SIZE_4; i++, j++)
            crc[j] = frameByteSequence[i];
    }
    
    /***
     * Accepts byte array representation of a MAC or an IP address and returns string representation.
     * @param arr byte array
     * @return string representation of a byte array MAC or an IP address. 
     */
    public static String byteAddressToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        
        if(arr.length == 4) {
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
                sb.append(String.format("%02x", tmp));
                if (i != arr.length - 1)
                    sb.append(":");
            }
        }
        return sb.toString();
    }
    
    /***
     * To string method for Layer 2 frames. 
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("SrcMAC %s, ", byteAddressToString(srcAddr)));
        sb.append(String.format("DestMAC %s", byteAddressToString(destAddr)));
        
        return sb.toString();
    }
    
    /***
     * converts the frame into a byte array.
     */
    @Override
    public byte[] toByteArray() {
        byte[] byteArr = new byte[HEADER_N_CRC_SIZE + body.length]; 
        
        int i = 0;
        //Initialize preamble
          for(int j = 0; j < preamble.length; i++, j++)
              byteArr[i] = preamble[j];
          for(int j = 0; j < destAddr.length; i++, j++)
              byteArr[i] = destAddr[j];
          for(int j = 0; j < srcAddr.length; i++, j++)
              byteArr[i] = srcAddr[j];
          for(int j = 0; j < type.length; i++, j++) 
              byteArr[i] = type[j];
          for(int j = 0; j < body.length; i++, j++)
              byteArr[i] = body[j];
          for(int j = 0; j < crc.length; i++, j++)
              byteArr[i] = crc[j];
        
        return byteArr;
    }

    /***
     * Byte array representation of the frame excluding CRC.
     * @return
     */
    private byte[] toByteArrayWithoutCRC() {
        byte[] byteArr = new byte[HEADER_N_CRC_SIZE - CRC_SIZE_4 + body.length]; 
        
        int i = 0;
        //Initialize preamble
          for(int j = 0; j < preamble.length; i++, j++)
              byteArr[i] = preamble[j];
          for(int j = 0; j < destAddr.length; i++, j++)
              byteArr[i] = destAddr[j];
          for(int j = 0; j < srcAddr.length; i++, j++)
              byteArr[i] = srcAddr[j];
          for(int j = 0; j < type.length; i++, j++) 
              byteArr[i] = type[j];
          for(int j = 0; j < body.length; i++, j++)
              byteArr[i] = body[j];
        return byteArr;
    }
    
    public byte[] getDestAddr() {
        return destAddr;
    }
    
    public void setDestAddr(byte[] destAddr) {
        this.destAddr = destAddr;
    }

    public byte[] getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(byte[] srcAddr) {
        this.srcAddr = srcAddr;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getCrc() {
        return crc;
    }

    private void setCrc(byte[] crc) {
        this.crc = crc;
    }
    
    /***
     * Calculates the CRC for the frame and sets CRC member variable.
     */
    public void calculateAndSetCRC() {
        setCrc(calculateCRC());
    }
    
    /***
     * Calculates the CRC for the frame and returns the result as byte array.
     * @return byte array representation of the calculated CRC.
     */
    private byte[] calculateCRC() {
        CRC32 crc32 = new CRC32();
        crc32.update(this.toByteArrayWithoutCRC());
        long c = crc32.getValue();
        // long to 4 byte, since crc32 uses only LSB 4 bytes of long.
        byte[] crcArr = new byte[CRC_SIZE_4];
        
        // MSB out of 4 bytes in c stored in crcArr[0]
        for(int i = crcArr.length-1; i >= 0; i--) {
            crcArr[i] = (byte) c;
            c = c >> 8;
        }
        
        return crcArr;
    }

    /***
     * Verifies if the CRC that is stored in the frame is a correct.
     * @return true if CRC is correct.
     */
    public boolean verifyCRC() {
        byte[] calc = calculateCRC();
        
        if(calc.length != crc.length)
            return false;
        
        for(int i = 0; i < CRC_SIZE_4; i++)
            if(calc[i] != crc[i])
                return false;
        
        return true;
    }
    
    public static void main(String[] args) {
        Random rng  = new Random();
        Layer2Frame l = new Layer2Frame();
        byte[] randBody = new byte[50];
        rng.nextBytes(randBody);
        byte[] src = new byte[Layer2Frame.ADDRESS_SIZE_6];
        rng.nextBytes(src);
        byte[] dst = new byte[Layer2Frame.ADDRESS_SIZE_6];
        rng.nextBytes(dst);
        
        l.setSrcAddr(src);
        l.setDestAddr(dst);
        l.setBody(randBody);
        l.calculateAndSetCRC();
        
        Layer2Frame f = new Layer2Frame(l.toByteArray());
        System.out.println(f.verifyCRC());
    }
    
}
