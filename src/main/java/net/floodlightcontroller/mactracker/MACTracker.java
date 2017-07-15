package net.floodlightcontroller.mactracker;

import java.util.Collection;
import net.floodlightcontroller.core.IFloodlightProviderService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Set;

import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPacket;
import net.floodlightcontroller.packet.IPv4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.VlanVid;

import net.floodlightcontroller.mactracker.JsonReader;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;

public class MACTracker implements IOFMessageListener, IFloodlightModule {
	
	
	JsonReader js = new JsonReader();
	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stubIFloodlightModule
		 return MACTracker.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
	    Collection<Class<? extends IFloodlightService>> l =
	        new ArrayList<Class<? extends IFloodlightService>>();
	    l.add(IFloodlightProviderService.class);
	    return l;
	}
	
	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
	    floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    macAddresses = new ConcurrentSkipListSet<Long>();
	    logger = LoggerFactory.getLogger(MACTracker.class);
	    js.initialize();
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);

	}
	
	@Override
	   public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
	  
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);

		Long sourceMACHash = eth.getSourceMACAddress().getLong();

		if (!macAddresses.contains(sourceMACHash)) {
			
			
			String tmp = js.getLocation(sw.getId().toString());
			if (tmp.equals("0")) {
				js.putToList(sw.getId().toString());
			}
			js.putMacToList(eth.getSourceMACAddress().toString(),tmp);
			macAddresses.add(sourceMACHash);
			logger.info("MAC Address: {} seen on switch: {}", eth.getSourceMACAddress().toString(),
					tmp.equals("0") ? "unknown" : tmp);
		}

	
	        
	        switch (msg.getType()) {
	        case PACKET_IN:
	            /* Retrieve the deserialized packet in message */
	     
	            /* Various getters and setters are exposed in Ethernet */
	            MacAddress srcMac = eth.getSourceMACAddress();
	            VlanVid vlanId = VlanVid.ofVlan(eth.getVlanID());
	     
	            /*
	             * Check the ethertype of the Ethernet frame and retrieve the appropriate payload.
	             * Note the shallow equality check. EthType caches and reuses instances for valid types.
	             */
	            if (eth.getEtherType() == EthType.IPv4) {
	                /* We got an IPv4 packet; get the payload from Ethernet */
	                IPv4 ipv4 = (IPv4) eth.getPayload();
	                js.putIpToList(ipv4.getSourceAddress().toString(),js.getSwitchLocationFromMac(srcMac.toString()));
	                 
	                /* Various getters and setters are exposed in IPv4 */
	                byte[] ipOptions = ipv4.getOptions();
	                IPv4Address dstIp = ipv4.getDestinationAddress();
	                 
	                /* Still more to come... */
	     
	            } else if (eth.getEtherType() == EthType.ARP) {
	                /* We got an ARP packet; get the payload from Ethernet */
	                ARP arp = (ARP) eth.getPayload();
	     
	                /* Various getters and setters are exposed in ARP */
	                boolean gratuitous = arp.isGratuitous();
	     
	            } else {
	                /* Unhandled ethertype */
	            }
	            break;
	        default:
	            break;
	        }
	        return Command.CONTINUE;
	    }

}
