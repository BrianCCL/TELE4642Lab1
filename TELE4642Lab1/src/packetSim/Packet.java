package packetSim;

public class Packet {
	private String type;
	private int size;
	
	private final int num;
	private final double arrivalTime;
	
	private double nxtProcessTime;
	
	
	public Packet(int num, int size, double arrivalTime){
		super();
		
		this.num = num;
		this.size = size;
				
		this.arrivalTime = arrivalTime;
		nxtProcessTime = arrivalTime;
		type = "Arrival";
	}

	public int getNum() {
		// TODO Auto-generated method stub
		return num;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}
}
