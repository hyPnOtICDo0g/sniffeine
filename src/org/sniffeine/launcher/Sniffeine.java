package org.sniffeine.launcher;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapHandle.Builder;
import org.pcap4j.core.PcapNativeException;
import org.sniffeine.handler.PacketUtilsHandler;
import org.sniffeine.handler.PacketCaptureHandler;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;

public class Sniffeine{
    // timeout in ms
	private static final String READ_TIMEOUT_KEY = PacketUtilsHandler.class.getName() + ".readTimeout";
	private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10);
    // snap length in bytes
	private static final String SNAPLEN_KEY = PacketUtilsHandler.class.getName() + ".snaplen";
	private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536);

    public static void main(String[] args){
        try{
            PacketUtilsHandler utils = new PacketUtilsHandler();
            PacketCaptureHandler capture = new PacketCaptureHandler();
            // create a live capture handle
            PcapHandle handle = new Builder(utils.GetNifName())
                .snaplen(SNAPLEN)
                .promiscuousMode(PromiscuousMode.PROMISCUOUS)
                .timeoutMillis(READ_TIMEOUT)
                .build();
            // start capturing
            capture.StartPacketCapture(handle);
            // display stats once GUI window is closed
            utils.DisplayPacketStats(handle.getStats());
            handle.close();
        }
        catch(PcapNativeException e){
            System.out.println("Sniffeine requires root privileges.");
        }
        catch(UnsatisfiedLinkError e){
            System.out.println("Sniffeine requires libpcap, WinPcap or Npcap to be installed.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
