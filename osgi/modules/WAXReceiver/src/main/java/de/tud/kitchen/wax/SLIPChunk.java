package de.tud.kitchen.wax;
import java.util.Arrays;


public class SLIPChunk {

	public SLIPChunk(byte[] chunk, int outstanding){
		this.chunk = chunk;
		this.outstanding = outstanding;
	}
	
	public SLIPChunk(byte[] encodedSLIP){
		SLIPChunk decodedChunk = decodeSLIP(encodedSLIP);
		this.chunk = decodedChunk.getChunk();
		this.outstanding = decodedChunk.getOutstanding();
	}
	
	private int outstanding;
	private byte[] chunk = null;
	
	public void appendSLIP(byte[] encodedSLIP){
		SLIPChunk decodedChunk = decodeSLIP(concat(this.chunk,encodedSLIP));
		this.chunk = decodedChunk.getChunk();
		this.outstanding = decodedChunk.getOutstanding();
	}
	
	public byte[] getChunk(){
		return this.chunk;
	}
	
	public int getChunkSize(){
		return this.chunk.length;
	}
	
	public int getOutstanding(){
		return this.outstanding;
	}
	
	private SLIPChunk decodeSLIP(byte[] escapedSLIP){
		byte[] unescapedSLIP = new byte[escapedSLIP.length];
		int j=0;
		int skip=0;
		
		for(int i=0; i+skip<escapedSLIP.length; i++){
			if((byte)escapedSLIP[i+skip] == (byte)0xDB && i+skip+1 < escapedSLIP.length){ // SLIP escape character found and escape sequence not truncated
				if((byte)escapedSLIP[i+skip+1] == (byte)0xDC){ 			// escaped END found
					unescapedSLIP[j++] = (byte)0xC0; 					// replacing by unescaped END
					skip++;
				}
				else if ((byte)escapedSLIP[i+skip+1] == (byte)0xDD){ 	// escaped ESC found
					unescapedSLIP[j++] = (byte)0xDB; 					// replacing by unescaped ESC
					skip++;
				}	
			}
			else { // also passing truncated escape-sequences for reloading mechanism
				unescapedSLIP[j++] = escapedSLIP[i+skip];
			}
		}
		
		return new SLIPChunk(Arrays.copyOfRange(unescapedSLIP, 0, escapedSLIP.length - skip), skip);
	}
	
	private byte[] concat(byte[] first, byte[] second) {
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
		}	
}

