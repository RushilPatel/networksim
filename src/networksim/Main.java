package networksim;

import java.io.File;
import java.util.PriorityQueue;

public class Main {

    public static byte[] hostAIP = new byte[] {(byte) 10, (byte) 10, (byte) 20, (byte) 1};
    public static byte[] hostASubnet = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 0};;
    public static byte[] hostAMAC = new byte[] {(byte) 11, (byte) 22, (byte) 33 , (byte) 44, (byte) 55, (byte) 66};
    
<<<<<<< HEAD
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
=======
    public static void main(String[] args) {
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> 1cb251f7252d678fe4770a65c78d8ebdc3e6c14c
        Host hostA = new Host ("10.10.20.1", "255.255.255.0", "00:11:22:33:44:55", "HostA");
        Host router = new Host ("0.0.0.0", "255.255.255.0", "11:22:33:44:55:66", "Router");
        Host hostB = new Host ("192.168.25.20", "255.255.255.0", "22:33:44:55:66:77", "HostB");
        Host hostC = new Host ("192.168.25.15", "255.255.255.0", "22:33:44:55:66:77", "HostC");
<<<<<<< HEAD
=======

>>>>>>> 14ceb802517a7362592de129b62f1875cd51729a
=======

>>>>>>> 1cb251f7252d678fe4770a65c78d8ebdc3e6c14c
>>>>>>> 38c90778344ff3c0622a1607a75994a84d050b58
    }
}
