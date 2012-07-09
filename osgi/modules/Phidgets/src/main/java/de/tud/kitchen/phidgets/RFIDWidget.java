package org.mundo.context.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import org.mundo.context.CtxManager;
import org.mundo.context.CtxSource;
import org.mundo.context.CtxWidget;
import org.mundo.context.Prop;
import org.mundo.context.datatype.CtxItem;
import org.mundo.context.datatype.CtxItemFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;

public class RFIDWidget extends CtxWidget implements CtxSource, TagGainListener, TagLossListener{
	
	@Prop protected String mappingFile;
	@Prop protected int timeout = 3000;
	@Prop protected int serial = -1;
	@Prop protected int logging = 0;
	
		
	
	private static final Logger _logger = LoggerFactory.getLogger(RFIDWidget.class);
	
	
	private RFIDPhidget rfid;
	
	private Properties config;
	private boolean phidgetAttached = false;
	
	public void start(){
		CtxManager cs = CtxManager.getManager();
		if ("".equals(outputContextType)) outputContextType = "ctx:rfid";
		cs.registerWidget(outputContextType, this);
		storeIdentifier="tagId";
		
		try {
			rfid = new RFIDPhidget();
			if (logging>0) {
				File logFolder = new File("log");
				if(!logFolder.exists()|| !logFolder.isDirectory()){
					logFolder.mkdir();
				}
				rfid.enableLogging(logging, "log/rfidPhidget.log");
			}
			if (serial<=0)
				rfid.openAny();
			else 
				rfid.open(serial);
			rfid.waitForAttachment(timeout);				
			rfid.addTagGainListener(this);	
			rfid.addTagLossListener(this);
			rfid.setLEDOn(true);
			rfid.setAntennaOn(true);
			phidgetAttached = true;
			serial = rfid.getSerialNumber();
		} catch (PhidgetException e) {
			if (serial <=0)
				System.err.println("No RFID Phidget attached");
			else 
				System.err.println("No RFID Phidget with serial No. "+serial +" attached");
		}
		config = new Properties();
			try {
				config.load(new FileInputStream(CtxManager.getOSGiCompatiblePath(mappingFile)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		super.start();
	}
	

	
	

	
	public void stop(){
		try {
			if (phidgetAttached){
				rfid.setLEDOn(false);
				rfid.close();
			}
		} catch (PhidgetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		rfid = null;
		super.stop();
	}
	
	@Override
	public void tagGained(TagGainEvent arg0) {
		CtxItem x = new CtxItem();
		String tag = config.getProperty(arg0.getValue());
		x.put("name", tag);
		x.put("tagId", arg0.getValue());
		x.put("available", true);
		x.setId(serial+"");
		x.setProduct(tag);
		processResult(x);		
	}

	@Override
	public void tagLost(TagLossEvent arg0) {
		CtxItem x = new CtxItem();
		String tag = config.getProperty(arg0.getValue());
		x.put("name", tag);
		x.put("tagId", arg0.getValue());
		x.put("available", false);
		x.setId(serial+"");
		x.setProduct(tag);
		processResult(x);		
	}
			
	
}
