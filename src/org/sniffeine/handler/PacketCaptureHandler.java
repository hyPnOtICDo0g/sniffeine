package org.sniffeine.handler;

import org.pcap4j.packet.Packet;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.sniffeine.ui.DrawInfoTable;
import org.pcap4j.core.NotOpenException;

public class PacketCaptureHandler{
    private int PacketCount = 0;
    private int PacketLength = 0;
    private String Protocol = null;
    private String SourceIP = null;
    private String HexStream = null;
    private String SourcePort = null;
    private IpPacket ipPacket = null;
    private String DestinationIP = null;
    private String DestinationPort = null;

    public DrawInfoTable Table = null;
    public static boolean RunUntilClosed = true;

    // returns a record
    public String[] GenerateRecord(String Timestamp){
        return new String[]{
            Integer.toString(++PacketCount),
            Timestamp,
            SourceIP,
            DestinationIP,
            SourcePort,
            DestinationPort,
            Protocol,
            Integer.toString(PacketLength),
            HexStream
        };
    }

    // detect the proper protocol and assign information accordingly
    public void SwitchProtocol(Packet packet){
        PacketLength = packet.length();
        ipPacket = packet.get(IpPacket.class);
        HexStream = ByteArrays.toHexString(packet.getRawData(), " ");

        if(ipPacket == null){ return; }

        Protocol = ipPacket.getHeader().getProtocol().name();
        SourceIP = ipPacket.getHeader().getSrcAddr().getHostAddress().toString();
        DestinationIP = ipPacket.getHeader().getDstAddr().getHostAddress().toString();

        switch(Protocol){
            case "TCP":
                TcpPacket TCPpacket = packet.get(TcpPacket.class);
                SourcePort = TCPpacket.getHeader().getSrcPort().valueAsString();
                DestinationPort = TCPpacket.getHeader().getDstPort().valueAsString();
                break;

            case "UDP":
                UdpPacket UDPpacket = packet.get(UdpPacket.class);
                SourcePort = UDPpacket.getHeader().getSrcPort().valueAsString();
                DestinationPort = UDPpacket.getHeader().getDstPort().valueAsString();
                break;

            default:;
        }

        if(SourcePort != null && DestinationPort != null){
            if (SourcePort.equals("80") || SourcePort.equals("8080") || DestinationPort.equals("80") || DestinationPort.equals("8080")){
                Protocol = "HTTP";
            }
            else if(SourcePort.equals("443") || DestinationPort.equals("443")){
                Protocol = "HTTPS";
            }
        }
    }

    // capture packets until the GUI window is closed
    public void StartPacketCapture(PcapHandle handle) throws NotOpenException{
        Table = new DrawInfoTable(new String[][]{});
        while(RunUntilClosed){
            Packet packet = handle.getNextPacket();
            if(packet != null){
                SwitchProtocol(packet);
                Table.model.add(GenerateRecord(handle.getTimestamp().toString()));
            }
        }
    }
}
