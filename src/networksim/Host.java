package networksim;

public class Host {
    
    private String ipAddress;
    private String macAddress;
    private String hostName;
    private String subnetMask;
    
    public Host(String ipAddress, String subnetMask, String macAddress, String hostName){
        this.hostName = hostName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
    }
    
    public String getIPAddress(){
        return this.ipAddress;
    }
    
    public String getSubnetMask(){
        return this.subnetMask;
    }
    
    public String getMACAddress(){
        return this.macAddress;
    }
    public String getHostName(){
        return this.hostName;
    }
    
    public String toString(){
        return this.hostName;
    }
    
    public void send(Byte [] packet){
        
    }
    
    public void receive (Byte [] packet){
        Layer1.processReceivedPacket (packet, this);
    }
    
}
