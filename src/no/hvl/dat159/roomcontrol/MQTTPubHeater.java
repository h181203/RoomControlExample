package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPubHeater implements Runnable {
	private String topic = "Heat";
	Boolean heat = null;
	private int qos = 1;
	private String broker = "tcp://m23.cloudmqtt.com:19534";
	private String clientId = "MQTT_Heater";
	private String username = "kwpsdzfj";
	private String password = "aH_BOySAuB8s";

	private MqttClient publisherClient;

	TemperatureSensor sensor;


	public Boolean getHeat() {
		return heat;
	}

	public void setHeat(Boolean heat) {
		this.heat = heat;
	}

	private void publish() throws MqttPersistenceException, MqttException, InterruptedException {
		
		for (int i = 0; i < 100; i++) {
			if(heat != null) {
				System.out.println("Publishing message: " + heat.toString());
	
				MqttMessage message = new MqttMessage(heat.toString().getBytes());
				message.setQos(qos);
	
				publisherClient.publish(topic, message);
			}
			Thread.sleep(10000);
		}

	}

	private void connect() {

		MemoryPersistence persistence = new MemoryPersistence();

		try {
			publisherClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());
			System.out.println("Connecting to broker: " + broker);
			publisherClient.connect(connOpts);
			System.out.println("Connected");

		} catch (MqttException e) {
			System.out.println("reason " + e.getReasonCode());
			System.out.println("msg " + e.getMessage());
			System.out.println("loc " + e.getLocalizedMessage());
			System.out.println("cause " + e.getCause());
			System.out.println("excep " + e);
			e.printStackTrace();
		}
	}

	private void disconnect() throws MqttException {

		publisherClient.disconnect();

	}

	public void run() {

		try {

			System.out.println("Sensor publisher running");

			connect();

			publish();

			disconnect();

			System.out.println("Sensor publisher stopping");

		} catch (Exception ex) {
			System.out.println("Sensor publisher: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
