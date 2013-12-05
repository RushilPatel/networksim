package networksim;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Layer4.java: This class simulates the functionalities of the Transport Layer (Layer4). 
 * It basically receives a file from the host, split it to an specific number of chunks, inserts each chunk into a layer3 frame
 * which in fact is an IP packet. Moreover, some IP packet fields are filled at this layer such as Source IP (Host), Destination IP, Flag and Offset.
 *   
 * @author Ebad
 *
 */
public class Layer4 {
	//public Layer3 layer3;
	//public Layer2 layer2;
	/**
	 * 
	 * @param fileToSend a handle to the file that should be sent
	 * @param host Host address
	 * @param destIPAddress Destination IP Address
	 * @throws IOException 
	 */
    public static void receiveFromHost(File fileToSend, Host host, byte [] destIPAddress) throws IOException{
    	sendToLayer3(fileToSend, host, destIPAddress);
    	
	}
    /**
     * This functionality is designed for when a node wants to initiate sending a file, receiving it from the host and sending to an specific destination IP address.
     * 
     * @param fileToSend A handle to the file that should be sent
     * @param host Host Address
     * @param destIPAddress Destination IP Address
     * @param chunkSize The applicable size of a chunk which equals to the MTU subtracted from layer3's header size and layer2's header size.
     * @param tmp is there to check when the reading process should be considered done, where tmp becomes negative.
     */
    public static void sendToLayer3(File fileToSend, Host host, byte [] destIPAddress) throws IOException{
    	
    	int chunkSize = 1400 - Layer3Frame.HEADER_SIZE - Layer2Frame.HEADER_N_CRC_SIZE;
    	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
        FileOutputStream out;
        int partCounter = 1;
        //int sizeOfFiles = 1024;// 1KB
        int sizeOfFiles = chunkSize;
        byte[] buffer = new byte[sizeOfFiles];
        int tmp = 0;
        boolean first = true;
        while ((tmp = bis.read(buffer)) > 0) {
        	Layer3Frame frame = new Layer3Frame();
        	frame.sourceAddr = host.getIPAddress();
        	frame.destinationAddr = destIPAddress;
        	
        	if(first) {    // first fragment
        	    first = false;
        	    frame.flagsOffset = intToByteArr(0);
        	} else {
        	    frame.flagsOffset = intToByteArr(buffer.length);
        	}
        	if (tmp < buffer.length){
        		frame.flagsOffset[1] &= (byte)0xDF; // last fragment, so clear flagsOffset flag
        	}
        	else {
        		frame.flagsOffset[1] |= (byte)0x20; // part of fragment, so set flagsOffset flag
        	}
        	System.out.println("Sent Offset = " + byteArrToInt13(frame.flagsOffset)); 
        	frame.body = buffer;
        	
        	Layer3.receiveFromLayer4(frame, destIPAddress, host);
        }

    }
    /**
     * This function converts integer to byte array.
     * @param n is an input integer which is supposed to be converted to byte array
     * @return the byte array is returned
     */
    public static byte[] intToByteArr(int n) {
    	byte[] arr = new byte[2];
    	int i = 0;
    	while(n > 0 && i < 2) {
    		arr[i] =(byte) (n % 256);
    		n = n >> 8;
    		i ++;
    	}
    	
    	return arr;
    }
    /**
     * The main purpose of this function is to extract the offset bits in the IP packet.
     * @param arr input byte array. 
     * @return integer value is returned 
     */
    public static int byteArrToInt13(byte[] arr) {
    	int i = arr[1] & 0x1F;
    	i = i << 8;
    	i |= arr[0];
    	return i;
    }
    /**
     * 
     * @param ip IP of type byte array
     * @return returns string value of the IP address
     */
    public static String ipToString(byte[] ip){
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < ip.length; i++) {
    		int tmp = 0;
    		tmp |= ip[i];
    		sb.append(tmp);
    		if(i != ip.length-1)
    			sb.append(".");
    	}
    	return sb.toString();
    }

    /**
     * 
     * @param frame A layer3 frame which in fact is an IP Packet
     * @param host Host Address
     * @throws IOException
     */
	public static void receiveFromLayer3(Layer3Frame frame, Host host) throws IOException{
		
		byte[] flagsOffset = frame.flagsOffset;
		
		//Convert the Byte array to decimal value
		//long offsetDec = 0;
		boolean flag = false;
		if ((int)(flagsOffset[1] & (byte)0x20) != 0){
			//Flag is set
			flag = true;
		}
		int offset = byteArrToInt13(flagsOffset);
		System.out.println("Recv Offset = " + offset);	
		String filename = ipToString(frame.sourceAddr) + ".tmp";
		
		if (flag == false && offset == 0){	//Unfragmented Packet
			writeChunkToFile(frame, filename, false);
		}
		if (flag == true && offset == 0){	//The 1st packet (Fragmented)
			//create the file, and write the 1st packet (append)
			writeChunkToFile(frame, filename, false);
			
		}
		if (flag == true && offset > 0){	//The Nth packet (Fragmented)
			//open the file, and write the Nth packet (append)
			writeChunkToFile(frame, filename, true);
		}
		if (flag == false && offset > 0){	//The last Packet (Fragmented)
			//open the file, and write the last packet (append)
			writeChunkToFile(frame, filename, true);
		}
		
		
		//return payload;
	}
	/**
	 * 
	 * @param frame Layer3 frame which in fact is an IP packet
	 * @param filename File name
	 * @param append A boolean that identifies if the file should be written from the beginning or be appended.
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	private static void writeChunkToFile(Layer3Frame frame, String filename, boolean append)
			throws IOException, FileNotFoundException {
		File file = new File(filename);
		FileOutputStream  out;
		if (append == false){
			//file.deleteOnExit(); not required
			file.createNewFile();
			out = new FileOutputStream(file);
		}else{
			out = new FileOutputStream(file, true);
		}
		
		out.write(frame.body);
	//	System.out.println(Arrays.toString(frame.body));
		out.close();
	}
	
}
