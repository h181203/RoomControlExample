package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSubHeater implements MqttCallback, Runnable {

	private String message;
	private Display display;
	private Boolean heat;
	private Heating heater;
	public MQTTSubHeater(Heating heater, Display display) throws MqttException {
		this.heater = heater;
		this.display = display;
		String topic = "Heat";
		int qos = 1; // 1 - This client will acknowledge to the Device Gateway that messages are
						// received
		String broker = "tcp://m23.cloudmqtt.com:19534";
		String clientId = "MQTT_Heater_SUB";
		String username = "kwpsdzfj";
		String password = "aH_BOySAuB8s";


		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());

		System.out.println("Connecting to broker: " + broker);

		MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
		client.setCallback(this);
		client.connect(connOpts);
		System.out.println("Connected");

		client.subscribe(topic, qos);
		System.out.println("Subscribed to message");

	}

	/**
	 * @see MqttCallback#connectionLost(Throwable)
	 */
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);

	}

	/**
	 * @see MqttCallback#messageArrived(String, MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String msg = "venter";
		if(new String(message.getPayload()) != null) {
			Boolean bool = Boolean.parseBoolean(new String(message.getPayload()));
			if(bool) {
				heat = bool;
				msg = "på";
			} else {
				heat = bool;
				msg = "av";
			}
				heater.write(heat);
			
		}
		
		String dismessage = String.format("[%s] %s", topic, msg);
		
		display.write(dismessage);

		this.setMessage(msg);
	}

	/**
	 * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void run() {
		
	}
}
