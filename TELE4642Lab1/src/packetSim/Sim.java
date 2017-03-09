package packetSim;

import java.util.LinkedList;

public class Sim {

	final static int meanPacketSize = 10000; //10000 bits
	
	double runTime = 0.0;
	double waitTime = 0.0;
	
	double currentTime;
	
	public static void main(String[] args) {
		
		
		int npkts = Integer.parseInt(args[0]);
		double lambda = Double.parseDouble(args[1]);
		
		LinkedList<Packet> event = poissonPacketGenerator(npkts, lambda);
		
		for(Packet p: event)
			System.out.println("pkt: " + p.getNum() + " " + p.getArrivalTime());
		
	}
	
	public static LinkedList<Packet> poissonPacketGenerator(int npkts, double lambda){
		double time = 0.0;
		LinkedList<Packet> lst = new LinkedList<Packet>();
		for(int i = 1; i <= npkts; i++){
			double interval = exponentialNumberGenerator(lambda);
			time += interval;
			Packet p = new Packet(i, (int) exponentialNumberGenerator(meanPacketSize), time);
			lst.add(p);
		}
		
		return lst;
	}

	public static double exponentialNumberGenerator(double lambda){
		return Math.log(1 - Math.random()) /( -lambda);	
	}
}
