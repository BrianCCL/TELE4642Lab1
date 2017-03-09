package packetSim;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

public class Sim {

	final static double meanPacketSize = 10000.0;//10000 bits
	final static double capacity = 100000.0;		//10Gbps -> 100000 bit per us
	
	static double runTime = 0.0;
	static double waitTime = 0.0;

	public static void main(String[] args) {
		
		int npkts = Integer.parseInt(args[0]);
		double lambda = Double.parseDouble(args[1]);
		
		System.out.println("===\tGenerating " + npkts + " Packet(s)\t   ===");	
		LinkedList<Packet> event = poissonPacketGenerator(npkts, lambda);
		System.out.println("===\tPacket(s) Generated\t   ===");
		
		simulate(event);
		
		System.out.println(waitTime);
		/*
		//Testing Bellow 
		for(Packet p: event)
			System.out.println("pkt: " + p.getNum() + " " + p.getArrivalTime());
		*/
		//System.out.println(exponentialNumberGenerator(1/meanPacketSize));
		
	}
	
	private static void simulate(LinkedList<Packet> event){
		
		Queue<Packet> buffer = new LinkedList<Packet>();
		boolean run_state = false;
		
		while(!event.isEmpty()){
			
			Packet p = event.poll();
			double currentTime = p.getProcessTime();
			System.out.print("[" + String.format("%10.5f", Math.round(currentTime*10000.0)/10000.0) + "]: Packet " + String.format("%5d", Math.round(p.getNum())));
			
			if(p.getType().equals("Arrival")){
				System.out.print(" arrives with size " + p.getSize() + " and");
				if(!run_state){	
					System.out.print(" runs. ");
					scheduleDeparture(currentTime, p, event);
					run_state = true;
				}else{
					System.out.print(" finds " + buffer.size() + " packets in the queue. ");
					buffer.add(p);
				}
			}else if(p.getType().equals("Run")){
				System.out.print(" departs having spent " + String.format("%2.5f", Math.round((currentTime - p.getArrivalTime())*10000.0)/10000.0) + " us in the System. ");
				if(!buffer.isEmpty()){	
					waitTime += currentTime;	//wait time = Current Time - Arrival Time
					p = buffer.poll();
					waitTime -= p.getArrivalTime();					
					System.out.print("\n[" + String.format("%10.5f", Math.round(currentTime*10000.0)/10000.0) + "]: Packet " + String.format("%5d", Math.round(p.getNum())) + " departs from buffer and runs. ");
					scheduleDeparture(currentTime, p, event);
				}else{
					run_state = false;
				}
			}
			System.out.println();		
		}	
	}	

	private static void scheduleDeparture(double time, Packet p, LinkedList<Packet> event) {
		   
        ListIterator<Packet> itr = event.listIterator();
        double t = p.getSize()/capacity;
        runTime += t;
        p.setType("Run");
        p.setProcessTime(t + time);	
        
        while(true) {
            if (!itr.hasNext()){
                itr.add(p);
                break;
            }

            Packet elementInList = itr.next();
            if (elementInList.compareTo(p) < 0){
                itr.previous();
                itr.add(p);
                break;
            }
        }
	    
	}

	public static LinkedList<Packet> poissonPacketGenerator(int npkts, double lambda){
		double time = 0.0;
		LinkedList<Packet> lst = new LinkedList<Packet>();
		for(int i = 1; i <= npkts; i++){
			double interval = exponentialNumberGenerator(lambda);
			time += interval;
			Packet p = new Packet(i, (int) exponentialNumberGenerator(1/meanPacketSize), time);
			lst.add(p);
		}
		
		return lst;
	}

	public static double exponentialNumberGenerator(double lambda){
		return (Math.log(1 - Math.random()) /( -lambda));	
	}
}
