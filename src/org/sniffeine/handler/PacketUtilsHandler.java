package org.sniffeine.handler;

import java.io.IOException;
import org.pcap4j.core.PcapStat;
import org.pcap4j.util.NifSelector;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

public class PacketUtilsHandler{
	// returns the selected network interface
	public String GetNifName(){
		PcapNetworkInterface nif = null;
		try{
			// print Nif menu
			nif = new NifSelector().selectNetworkInterface();
			if (nif == null){
				System.out.println("No device chosen. Exiting.");
				throw new IOException();
			}
		}
		catch(IOException e){
			System.exit(1);
		}
		System.out.print(nif.getName().replace("\\Device\\", "") + " | " + nif.getDescription() + " Selected.\n");
		return nif.getName();
	}

	public void DisplayPacketStats(PcapStat ps) throws NotOpenException, PcapNativeException{
		System.out.println("\nPackets Received: " + ps.getNumPacketsReceived());
		System.out.println("Packets Dropped: " + ps.getNumPacketsDropped());
		System.out.println("Packets Dropped By Intf: " + ps.getNumPacketsDroppedByIf());
	}
}
