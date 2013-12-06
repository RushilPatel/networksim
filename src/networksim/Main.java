package networksim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    private static final int QUEUE_SIZE_100 = 100;
    public static byte[] hostAIP = new byte[] {(byte) 10, (byte) 10, (byte) 20, (byte) 1};
    public static byte[] hostASubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};
    public static byte[] hostAMAC = new byte[] {(byte) 11, (byte) 22, (byte) 33 , (byte) 44, (byte) 55, (byte) 66};
    
    public static byte[] hostBIP = new byte[] {(byte) 192, (byte) 168, (byte) 25, (byte) 20};
    public static byte[] hostBSubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};
    public static byte[] hostBMAC = new byte[] {(byte) 22, (byte) 33 , (byte) 44, (byte) 55, (byte) 66 , (byte) 77};

    public static byte[] hostCIP = new byte[] {(byte) 192, (byte) 168, (byte) 25, (byte) 15};
    public static byte[] hostCSubnet  = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};
    public static byte[] hostCMAC = new byte[] {(byte) 33 , (byte) 44, (byte) 55, (byte) 66, (byte) 77, (byte) 88};;
    
    public static byte[] routerIP = new byte[] {(byte) 192, (byte) 168, (byte) 25, (byte) 10};
    public static byte[] routerSubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};
    public static byte[] routerMAC = new byte[] {(byte) 44, (byte) 55, (byte) 66 , (byte) 77, (byte) 88, (byte) 99};
    
    public static byte[] routerIP2 = new byte[] {(byte) 10, (byte) 10, (byte) 20, (byte) 2};
    public static byte[] routerSubnet2 = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};
    public static byte[] routerMAC2 = new byte[] {(byte) 33, (byte) 55, (byte) 66 , (byte) 77, (byte) 88, (byte) 99};
    
    public static BlockingQueue<Packet> classABroadcast = new PriorityBlockingQueue<Packet> (QUEUE_SIZE_100);
    public static BlockingQueue<Packet> classCBroadcast = new PriorityBlockingQueue<Packet> (QUEUE_SIZE_100);
    
    public static AtomicBoolean transferring = new AtomicBoolean(true);
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, IOException {
        
                        
        final Host hostA = new Host (hostAIP, hostASubnet, hostAMAC, "HostA");
        final Host router = new Host (routerIP, routerSubnet, routerMAC, "Router", routerIP2, routerSubnet2, routerMAC2);
        final Host hostB = new Host (hostBIP, hostBSubnet, hostBMAC, "HostB");
        final Host hostC = new Host (hostCIP, hostCSubnet, hostCMAC, "HostC");
        Layer2.init(hostAIP, hostAMAC);
        Layer2.init(hostBIP, hostBMAC);
        Layer2.init(hostCIP, hostCMAC);
        Layer2.init(routerIP, routerMAC);
        Layer2.init(routerIP2, routerMAC2);
        Layer3.init ();
        Thread hostAThread = new Thread (hostA, "Thread A");
        Thread routerThread = new Thread (router, "Thread R");
        Thread hostBThread = new Thread (hostB, "Thread B");
        Thread hostCThread = new Thread (hostC, "Thread C"); 
        final File filetoSend = new File ("C:\\Users\\rpatel2\\Documents\\Project\\networksim\\Atlas.mp3");
        transferring.set(true);
        new Thread (new Runnable() {
            public void run () {
                try {
                    hostA.sendFile (hostC.getIPAddress (), filetoSend);
                } catch (IOException e) {
                    System.out.println ("There was an error parsing the file.");
                }
                transferring.set(false);//transferring = false;
            }
        }, "Thread P").start ();
        
        while(transferring.get() || !classABroadcast.isEmpty () || !classCBroadcast.isEmpty ()){
            hostAThread.run ();hostAThread.join();
            routerThread.run ();routerThread.join();
            hostBThread.run ();hostBThread.join();
            hostCThread.run ();hostCThread.join();
        }
        System.out.println("Done!");  
        String inputFileCheckSum = checksum (filetoSend.getAbsolutePath ());
        String outputFileCheckSum = checksum ("C:\\Users\\rpatel2\\Documents\\Project\\networksim\\outputfile");
        
        System.out.println ("Match Checksum Result: " + inputFileCheckSum.equals (outputFileCheckSum));
    }
    
    public static String checksum(String datafile) throws NoSuchAlgorithmException, IOException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(datafile);
        byte[] dataBytes = new byte[1024];
        int nread = 0; 
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();
        //convert the bytes to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        fis.close ();
        return sb.toString();
    }
}
