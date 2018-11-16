package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttException;

import no.hvl.dat159.roomcontrol.tests.SimpleController;

public class RoomDevice {

	public static void main(String[] args) throws MqttException {
		
		Room room = new Room(20);
		Heating heater = new Heating(room);
				
		TemperatureSensor sensor = new TemperatureSensor(room);
		Display display = new Display();
		MQTTPubTemperature sensorpub = new MQTTPubTemperature(sensor);
		MQTTSubHeater heatersub = new MQTTSubHeater(heater, display);
		
		
		try {
			
			Thread temppublisher = new Thread(sensorpub);
			Thread heatsubscriber = new Thread(heatersub);
			
			
			temppublisher.start();
			heatsubscriber.start();
			temppublisher.join();
			heatsubscriber.join();
			
		} catch (Exception ex) {
			
			System.out.println("RoomDevice: " + ex.getMessage());
			ex.printStackTrace();
		}
		


	}

}
