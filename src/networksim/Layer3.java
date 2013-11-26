package networksim;

import java.util.Arrays;
import java.util.HashMap;

public class Layer3 implements Layer3Interface {
    HashMap<byte[], byte[]> hostANextHop;
    HashMap<byte[], byte[]> hostBNextHop;
    HashMap<byte[], byte[]> hostCNextHop;
    HashMap<byte[], byte[]> routerNextHop;
    String defaultGateway;

    @Override
    public void receiveFromLayer2(Layer3 frame) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToLayer4() {
        // TODO Auto-generated method stub

    }

    public void receiveFromLayer4(Layer3Frame frame, byte[] finalDestination,
            Host host) {

    }

    @Override
    public void sendToLayer2() {
        // TODO Auto-generated method stub,
    }

    public byte[] getNexHop(byte[] finalDestAddress, Host host) {
        
        boolean isDestClassC = (int)finalDestAddress[0] < 0;
        boolean isHostClassC = (int)host.getIPAddress()[0] < 0;        
        
        if(isDestClassC && isHostClassC){
            if(Arrays.equals(finalDestAddress, host.getIPAddress())){
                //send it to layer 4
            }else{
                if(host.getHostName().equals("hostB")){
                    //send it to HOST C
                }else{
                    //send it to Host B
                }
            }
        }else{
            //send it to Router
        }
        
        boolean isHostA = (int) hostAddress[0] < 0;
        boolean isHostB;
        boolean isHostC;
        boolean isRouter;
        if (isHostA) {
            return hostANextHop.get(finalDestAddress);
        } else if (isHostB) {
            return hostBNextHop.get(finalDestAddress);
        } else if (isHostC) {
            return hostCNextHop.get(finalDestAddress);
        } else if (isRouter) {
            return routerNextHop.get(finalDestAddress);
        }

        // byte[] result = null;
        // if (nextHop.containsKey(finalDestAddress)) {
        // result = nextHop.get(host).getBytes();
        // } else {
        // result = nextHop.get(defaultGateway).getBytes();
        // }

        return result;
    }

}
