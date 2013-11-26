package networksim;

import java.io.File;
import java.util.PriorityQueue;

public class Main {

    public static byte[] hostAIP = new byte[] {(byte) 10, (byte) 10, (byte) 20, (byte) 1};
    public static byte[] hostASubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};;
    public static byte[] hostAMAC = new byte[] {(byte) 11, (byte) 22, (byte) 33 , (byte) 44, (byte) 55, (byte) 66};
    
    public static byte[] hostBIP = new byte[] {(byte) 192, (byte) 168, (byte) 25, (byte) 20};
    public static byte[] hostBSubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};;
    public static byte[] hostBMAC = new byte[] {(byte) 22, (byte) 33 , (byte) 44, (byte) 55, (byte) 66 , (byte) 77};;

    public static byte[] hostCIP = new byte[] {(byte) 192, (byte) 168, (byte) 25, (byte) 15};
    public static byte[] hostCSubnet  = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};;
    public static byte[] hostCMAC = new byte[] {(byte) 33 , (byte) 44, (byte) 55, (byte) 66, (byte) 77, (byte) 88};;
    
    public static byte[] routerIP = new byte[] {(byte) 192, (byte) 0, (byte) 0, (byte) 0};
    public static byte[] routerSubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};;
    public static byte[] routerMAC = new byte[] {(byte) 44, (byte) 55, (byte) 66 , (byte) 77, (byte) 88, (byte) 99};;
    
    public static PriorityQueue<Packet> classABroadcast = new PriorityQueue<> ();
    public static PriorityQueue<Packet> classCBroadcast = new PriorityQueue<> ();
    
    private static boolean transferring;
    public static void main(String[] args) throws InterruptedException {
                        
        final Host hostA = new Host (hostAIP, hostASubnet, hostAMAC, "HostA");
        final Host router = new Host (routerIP, routerSubnet, routerMAC, "Router");
        final Host hostB = new Host (hostBIP, hostBSubnet, hostBMAC, "HostB");
        final Host hostC = new Host (hostCIP, hostCSubnet, hostCMAC, "HostC");

        Thread hostAThread = new Thread (hostA);
        Thread routerThread = new Thread (router);
        Thread hostBThread = new Thread (hostB);
        Thread hostCThread = new Thread (hostC);        
        transferring = true;
        new Thread (new Runnable() {
            public void run () {
                hostB.sendFile (hostC.getIPAddress (), new File ("Test"));         
                transferring = false;
            }
        }).start ();
        
        while(transferring || !classABroadcast.isEmpty () || !classCBroadcast.isEmpty ()){
            hostAThread.run ();
            hostAThread.join ();
            
            routerThread.run ();
            routerThread.join ();
            
            hostBThread.run ();
            hostBThread.join ();
            
            hostCThread.run ();
            hostCThread.join ();
            
        }        
    }
}
