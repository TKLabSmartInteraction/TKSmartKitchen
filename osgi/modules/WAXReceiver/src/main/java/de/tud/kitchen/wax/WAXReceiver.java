package de.tud.kitchen.wax;
import java.io.DataInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.Runnable;
import java.util.Date;

public class WAXReceiver implements Runnable{
	
	public WAXReceiver(DataInputStream instream){	// accepts DataInputStream provided by SerialProvider 
		this.instream = instream;
		
		startReceiving();
		this.verbose = false;
	}

	//private NRSerialPort serial;
	private DataInputStream instream;
	private Thread readSerial;
	private List<WAXListener> listeners = new ArrayList<WAXListener>();
	private boolean verbose;
	private boolean isRunning = false;
	private int listenerCount = 0;
	
	private int convertToUInt16(byte lowerByte, byte upperByte){
		int lowerByteI = 0xFF & lowerByte;
		int upperByteI = 0xFF & upperByte;
		
		return 0xFFFF & ((upperByteI << 8) | lowerByteI);
	}
	
	private short convertToInt16(byte lowerByte, byte upperByte){
		int lowerByteI = 0xFF & lowerByte;
		int upperByteI = 0xFF & upperByte;
		
		return (short)((upperByteI << 8) | lowerByteI);
	}
	
	private void verbosePrintln(String line){
		if(verbose)
			System.err.println(line);
	}
	
	private void broadcastToListeners(WAXPacket packet){
		for(WAXListener listener: listeners){
			listener.receiveWAX(packet);
		}
	}
	
	public int addListener(WAXListener listener){
		listeners.add(listener);
		return listenerCount++;
	}
	
	public void startReceiving(){
		if(!isRunning){
			readSerial = new Thread(this);
			readSerial.start();
			isRunning = true;
		}
		else {
			verbosePrintln("Can not start receiver (receiver already running)");
		}
	}
	
	private void stopReceiving(){
		if(isRunning){
			readSerial.interrupt();
			this.isRunning = false;
		}
		else {
			verbosePrintln("Can not stop receiver (receiver currently not running)");
		}
	}

	public void terminateReceiver(){
		stopReceiving();
		//serial.disconnect(); //TODO: release SP-binding
	}
	
	public void run(){
		
		try {
				byte[] packetdescriptor = new byte[12];
				WAXPacket packet = new WAXPacket();
				int payloadSize;
				long valuesRaw;
				boolean truncated;
				
				while(!Thread.currentThread().isInterrupted()){
						int available = 0;
						try {
							available = instream.available();
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						while(available < 40){ //TODO: adaption by shortest valid packet (sawtooth-approach)
							Thread.sleep(20); // green command for optimizing CO2-footprint (thus reducing I/O & CPU load) //TODO: constant response time (considering highest sampling rate)
							try {
								available = instream.available();
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						
						if(Thread.currentThread().isInterrupted())		// immediately stop serial reading
							break;
					
						try {
							instream.read(packetdescriptor,0,2); 		// match first part of packet descriptor (2 bytes)
						} catch (Exception e) {
							// TODO: handle exception
						}
							
							if(packetdescriptor[1] == 0x12){			// load additional byte if header signature is truncated
								packetdescriptor[0] = packetdescriptor[1];
								try {
									packetdescriptor[1] = instream.readByte();
								} catch (Exception e) {
									// TODO: handle exception
								}
								
							}
							
							if(packetdescriptor[0] == 0x12 && packetdescriptor[1] == 0x78){ 
								int availableBytes;
								byte headerSize;
								try {
									headerSize = (byte) instream.read(packetdescriptor,2,10);
								} catch (Exception e) {
									headerSize = 0;
									// TODO: handle exception
								}
								 
								if(headerSize == 10){ // read second part of packet descriptor from serial line (10 bytes)
									packet.setDeviceID(convertToUInt16(packetdescriptor[2], packetdescriptor[3]));
									byte deviceStatusRaw = packetdescriptor[4];
									packet.setBatteryLow(((deviceStatusRaw & 0x01) == 1));
									byte dataFormatRaw = packetdescriptor[7];
									packet.setSensorRange((byte) ((dataFormatRaw & 0xC0) >> 6));
									packet.setDataFormat((byte) ((dataFormatRaw & 0x30) >> 4));
									packet.setSamplingFrequency(3200 / (1 << (15 - (dataFormatRaw & 0x0F))));
									packet.setSequenceID(convertToUInt16(packetdescriptor[8], packetdescriptor[9]));
									packet.setSendbufferSize(0xFF & packetdescriptor[10]);
									packet.setContainedSamples(0xFF & packetdescriptor[11]);
									
									if(packet.getDataFormat() == 2){
										payloadSize = packet.getContainedSamples() * 3 * 2 + 1; 		// [bytes] 3-axis 16bit-mode + SLIP-END
									}
									else if (packet.getDataFormat() == 0){
										payloadSize = packet.getContainedSamples() * 4 + 1; 			// [bytes] 3-axis 10bit-mode + SLIP-END
									}
									else {
										payloadSize = 0;
									}
									
									
									try {
										availableBytes = instream.available();
									} catch (Exception e) {
										availableBytes = 0;
										// TODO: handle exception
									}
									if(availableBytes >= payloadSize){
										
										byte[] payloadRaw = new byte[120]; 					// initialize payload buffer (max. 20 samples * 3 axis * 16bit resolution)
										Arrays.fill(payloadRaw, (byte)0x00); 				// clear payload buffer //TODO: remove this line
										int payloadReadBytes;
										try {
											payloadReadBytes = instream.read(payloadRaw, 0, payloadSize); // read expected amount of bytes into buffer	
										} catch (Exception e) {
											payloadReadBytes = 0;
											// TODO: handle exception
										}
										payloadRaw = Arrays.copyOfRange(payloadRaw, 0, payloadReadBytes); // trim payload
										SLIPChunk payload = new SLIPChunk(payloadRaw);		// rock'n'roll !!!!!!!!!
										
										truncated = false;
										while(payload.getOutstanding() != 0){
											byte[] overlap = new byte[payload.getOutstanding()];
											
											try {
												payloadReadBytes = instream.read(overlap,0,payload.getOutstanding());
											} catch (Exception e) {
												payloadReadBytes = 0;
												// TODO: handle exception
											}
											
											if(payloadReadBytes != payload.getOutstanding()){
												truncated = true;
												break; // additional bytes could not be read -> ignore truncated packet
											}
											payload.appendSLIP(overlap);
											//payloadRaw = chunk.getChunk();
										}
										if(payload.getChunk()[payload.getChunkSize()-1] != (byte)0xC0){
											truncated = true;
											verbosePrintln("Dropping package: SLIP-END did not sync.");
										}
										
										if(truncated == false){ // all additional bytes could be retrieved and synced on SLIP-END
											WAXSample[] samples = new WAXSample[packet.getContainedSamples()];
											short x = 0, y = 0, z = 0;
											for(int i=0; i<packet.getContainedSamples(); i++){
												if(packet.getDataFormat() == 2){
													x = convertToInt16(payload.getChunk()[i*6+0], payload.getChunk()[i*6+1]);
													y = convertToInt16(payload.getChunk()[i*6+2], payload.getChunk()[i*6+3]);
													z = convertToInt16(payload.getChunk()[i*6+4], payload.getChunk()[i*6+5]);
												}
												else if(packet.getDataFormat() == 0){
													valuesRaw = (0xFFL & payload.getChunk()[4*i+0]) | ((0xFFL & payload.getChunk()[4*i+1]) << 8) | ((0xFFL & payload.getChunk()[4*i+2]) << 16) | ((0xFFL & payload.getChunk()[4*i+3]) << 24); //prefinal
													int exponent = (int)(0x3L & (valuesRaw >> 30)); // first 2 bits representing e
													int exponentShifting = 6 - exponent;
													
													x = (short)(((short)(0xffc0L & (valuesRaw << 6))) >> exponentShifting); //TODO: replace valuesRaw -> payload.getChunk()
													y = (short)(((short)(0xffc0L & (valuesRaw >> 4))) >> exponentShifting);
													z = (short)(((short)(0xffc0L & (valuesRaw >> 14))) >> exponentShifting);
												}
												samples[i] = new WAXSample(new AccerlerationVector(x / 256.0f, y / 256.0f, z / 256.0f), new Date().getTime() - ( (packet.getSendbufferSize() + packet.getContainedSamples() - i - 1) * 1000L / packet.getSamplingFrequency()) );
											}
											packet.setSamples(samples);
											broadcastToListeners(packet);
										}
										else {
											verbosePrintln("Dropping package: payload completion failed (expected: " + payloadSize + " bytes, " + "available: " + payload.getChunk().length + ")");
										}
									}
									else {
										try {
											verbosePrintln("Dropping package: payload incomplete (expected: " + payloadSize + " bytes, " + "available: " + instream.available() + ")");
										} catch (Exception e) {
											// TODO: handle exception
										}
									}
								}
							} 
					 	} 
			} catch (InterruptedException interrupted){
				readSerial.interrupt(); // flag cleared by exception -> set it again
		   }
	}
}
