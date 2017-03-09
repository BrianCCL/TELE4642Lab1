package packetSim;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.stream.IntStream;

public class Sim {
	static String mu = "\u03bc";
	final static double meanPacketSize = 10000.0;	//10000 bits
	final static double capacity = 100000.0;		//10Gbps -> 100000 bit per us
	
	static double runTime = 0.0;
	static double waitTime = 0.0;
	
	static int length_count[] = new int[11] ;

	public static void main(String[] args) {
		
		int npkts = Integer.parseInt(args[0]);
		double lambda = Double.parseDouble(args[1]);
		
		System.out.println("===\tGenerating " + npkts + " Packet(s)\t   ===");	
		LinkedList<Packet> event = poissonPacketGenerator(npkts, lambda);
		System.out.println("\n===\tPacket Generated\t   ===\n");
		
		System.out.println("===\tStarting Simulation\t   ===");		
		long startTime = System.currentTimeMillis();
		simulate(event);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("\n===\tSimulation Ended\t   ===");		
		System.out.println("===\tTotal Time Used: " + elapsedTime/1000.0 + "s\t   ===\n");
		
		System.out.println("Total Number of Packet(s)  " + npkts);
		System.out.println("Average Wait Time \t" + String.format("%10.5f", Math.round(waitTime/npkts*10000.0)/10000.0) + " us");
		System.out.println("Average Run  Time \t" + String.format("%10.5f", Math.round(runTime/npkts*10000.0)/10000.0) + " us");
		System.out.println("Average Stay Time \t" + String.format("%10.5f", Math.round((runTime+waitTime)/npkts*10000.0)/10000.0) + " us\n");

		double total = IntStream.of(length_count).parallel().sum();
		for(int i = 0; i <= 10; i++)
			System.out.println("P(" + String.format("%2d", i) + "): " + String.format("%2.2f", Math.round(length_count[i]/total*10000.0)/100.0) + "%");
		
		//System.out.println(mu);
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
					int size = buffer.size();
					try{
						length_count[size]++;
					}catch(Exception e){
						
					}
					System.out.print(" finds " + size + " packets in the queue. ");
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
