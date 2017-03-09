package packetSim;

public class Packet {
	private String type;
	private int size;
	private double arrivalTime;
	private double nxtProcessTime;
	private int num;
	
	public Packet(int num, int size, double arrivalTime){
		super();
		
		this.num = num;
		this.size = size;
				
		this.arrivalTime = arrivalTime;
		nxtProcessTime = arrivalTime;
		type = "Arrival";
	}
}
