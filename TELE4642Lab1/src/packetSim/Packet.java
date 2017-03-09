package packetSim;

public class Packet {
	
	
	private final int num;
	private final int size;	
	private final double arrivalTime;
	
	private String type;	
	private double processTime;
	
	
	public Packet(int num, int size, double arrivalTime){
		super();
		
		this.num = num;
		this.size = size;
				
		this.arrivalTime = arrivalTime;
		processTime = arrivalTime;
		type = "Arrival";
	}

	public int getNum() {
		// TODO Auto-generated method stub
		return num;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public int getSize() {
		return size;
	}
	
	public void setProcessTime(double processTime){
		this.processTime = processTime;
	}
	
	public double getProcessTime(){
		return processTime;
	}

	public double compareTo(Packet p) {		
		return p.getProcessTime() - processTime;
	}
}
