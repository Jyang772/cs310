package cs310;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;



/**
 * This is an example of the PCap4j library for reading PCAP files
 * and extracting the IPv4 addresses and other packet information.
 * 
 * Licensed under CC-BY-SA
 * 
 * @author Joseph Paul Cohen
 *
 */
public class PCap4jExample {

	public static void main(String[] args) throws PcapNativeException, NotOpenException {
		
		//silence Logger
		Logger rootLogger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.toLevel("ERROR"));
		
		//open pcap
	    PcapHandle handle = Pcaps.openOffline("dumps/echoAndEchoReply.pcap");
	    
	    // loop through 10000 packets
	    for (int i = 0; i < 10000; i++) {
	      try {
	        Packet packet = handle.getNextPacketEx();

	        // If packet has IP addresses print it
	        if (packet.contains(IpV4Packet.class)){
	        	String src = packet.get(IpV4Packet.class).getHeader().getSrcAddr().getHostAddress();
	        	String dst = packet.get(IpV4Packet.class).getHeader().getDstAddr().getHostAddress();
	        	int ttl = packet.get(IpV4Packet.class).getHeader().getTtlAsInt();
	        	String proto = packet.get(IpV4Packet.class).getHeader().getProtocol().name();
	        	int port = packet.get(IpV4Packet.class).getHeader().getProtocol().value();
	        	
	        	System.out.println(src + "->" + dst + ", TTL:" + ttl + ", Prot:" + proto + ", Port:" + port);
	        }

	      } catch (TimeoutException e) {
	      } catch (EOFException e) {
	        System.out.println("Reached end of file.");
	        break;
	      }
	    }
		
	}
	
}
