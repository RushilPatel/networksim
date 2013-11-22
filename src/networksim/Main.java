package networksim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void send(Byte packet){
        
    }
    
    public static void main(String[] args) {

        Host hostA = new Host ("10.10.20.1", "255.255.255.0", "00:11:22:33:44:55", "HostA");
        Host router = new Host ("0.0.0.0", "255.255.255.0", "11:22:33:44:55:66", "Router");
        Host hostB = new Host ("192.168.25.20", "255.255.255.0", "22:33:44:55:66:77", "HostB");
        Host hostC = new Host ("192.168.25.15", "255.255.255.0", "22:33:44:55:66:77", "HostC");
        BufferedReader B = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("C:\\testing.txt"));
 
			while ((sCurrentLine = B.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (B != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

    }
}
