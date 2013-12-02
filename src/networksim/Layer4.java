
package networksim;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Layer4 {
	//public Layer3 layer3;
	//public Layer2 layer2;
	
    public static void receiveFromHost(File fileToSend, Host host, byte [] destIPAddress){
    	sendToLayer3(fileToSend, host, destIPAddress);
    	
	}
    
    public static void sendToLayer3(File fileToSend, Host host, byte [] destIPAddress) {
    	int chunkSize = 1400 - Layer3Frame.HEADER_SIZE - Layer2Frame.HEADER_N_CRC_SIZE;
    	
    	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
        FileOutputStream out;
        int partCounter = 1;
        //int sizeOfFiles = 1024;// 1KB
        int sizeOfFiles = chunkSize;
        byte[] buffer = new byte[sizeOfFiles];
        int tmp = 0;
        while ((tmp = bis.read(buffer)) > 0) {
        	Layer3Frame frame = new Layer3Frame();
        	frame.sourceAddr = host.getIPAddress();
        	frame.destinationAddr = destIPAddress;
        	
        	frame.flagsOffset = intToByteArr(buffer.length);
        	if (tmp < buffer.length){
        		frame.flagsOffset[1] &= (byte)0xDF;
        	}
        	else {
        		frame.flagsOffset[1] |= (byte)0x20;
        	}
        	
        	frame.body = buffer;
        	
        	Layer3.receiveFromLayer4(frame, destIPAddress, host);
        }

    }
    
    public static byte[] intToByteArr(int n) {
    	byte[] arr = new byte[2];
    	int i = 0;
    	while(n > 0 && i < 2) {
    		arr[i] =(byte) (n % 256);
    		n = n >> 8;
    	}
    	
    	return arr;
    }
    
    public static int byteArrToInt13(byte[] arr) {
    	int i = arr[1] & 0x1F;
    	i = i << 8;
    	i |= arr[0];
    	return i;
    }
    
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

    
	public static void receiveFromLayer3(Layer3Frame frame, Host host) throws IOException{
		
		byte[] flagsOffset = frame.flagsOffset;
		
		//Convert the Byte array to decimal value
		long offsetDec = 0;
		boolean flag = false;
		if ((int)(flagsOffset[1] & (byte)0x20) != 0){
			//Flag is set
			flag = true;
		}
		int offset = byteArrToInt13(flagsOffset);
			
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

	private static void writeChunkToFile(Layer3Frame frame, String filename, boolean append)
			throws IOException, FileNotFoundException {
		File file = new File(filename);
		FileOutputStream  out;
		if (append == false){
			file.deleteOnExit();
			file.createNewFile();
			out = new FileOutputStream(file);
		}else{
			out = new FileOutputStream(file, true);
		}
		
		out.write(frame.body);
		out.close();
	}
	
}
