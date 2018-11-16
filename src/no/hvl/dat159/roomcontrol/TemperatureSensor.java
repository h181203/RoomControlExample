package no.hvl.dat159.roomcontrol;

public class TemperatureSensor implements Runnable {

	private Room room;

	public TemperatureSensor(Room room) {

		this.room = room;
	}

	public double read() {

		return room.sense();
	}

	public void run() {
		read();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
