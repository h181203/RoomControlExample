package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Controller implements Runnable {
	
	Display display;
	MQTTSubTemperature mqttsubtemp;
	MQTTPubHeater mqttpubheat;
	
	
	Controller(MQTTPubHeater mqttpubheat) throws MqttException{
		this.mqttpubheat = mqttpubheat;
		display = new Display();
		mqttsubtemp = new MQTTSubTemperature(display);
		
	}
	
	
	public static void main(String[] args) throws MqttException, InterruptedException {
		MQTTPubHeater mqttheat = new MQTTPubHeater();
		Controller control = new Controller(mqttheat);
		Thread subtemp = new Thread(control);
		Thread pubheat = new Thread(mqttheat);
		
		subtemp.start();
		pubheat.start();
		subtemp.join();
		pubheat.join();
		
	}

	public void run(){
		String msg;
		double temperature = 0;
		for(int i = 0; i < 100; i++) {
			msg = mqttsubtemp.getMessage();
			if(msg!= null) {
				temperature = Double.parseDouble(msg);
				if(temperature < 15) {
					mqttpubheat.setHeat(true);
				} else if (temperature > 20) {
					mqttpubheat.setHeat(false);
				}
			}
			
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
			
				}
		}
	}
}

