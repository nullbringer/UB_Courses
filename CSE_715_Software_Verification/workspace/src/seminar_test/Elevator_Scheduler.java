// Requires no input
// Each person does 100000 iteratios.  Ten people.
// Select current_floor and direction in class Elevator
// for state diagram,

package seminar_test;

import java.util.LinkedList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;


public class Elevator_Scheduler {

	public static void main(String[] args) {
		
		Elevator E = new Elevator();
			
 		Scheduler S = new Scheduler(E,10);	
 		
 		E.set_Scheduler(S);
 				
		Person p1 = new Person("A",S);
		Person p2 = new Person("B",S);
		Person p3 = new Person("C",S);
		Person p4 = new Person("D",S);
	    Person p5 = new Person("E",S);
   	    Person p6 = new Person("F",S);
		
		
		p1.start();
		p2.start();
		p3.start();
		p4.start();
		p5.start();
		p6.start();
		
		S.start();
		
		}
}


class Elevator {
	
	int current_floor;
	String direction;
	Scheduler S;    // added this line for state maintenance
	
	String state;   // for query-based debugging
	String up;
	String down;
	
	public Elevator() {
		current_floor = 0;
		direction = "up";
	}
	
	public void set_Scheduler(Scheduler S) {
		this.S = S;
	}
	
	synchronized void go(int floor, String dir) {
		 up = S.up.toString().replaceAll(",", " ");
		 down = S.down.toString().replaceAll(",", " ");
		
		if (direction != dir || current_floor != floor)
			state = floor + "-" + dir + "-" + up + "-" + down;
		
		if (direction != dir)
			direction = dir;
		if (current_floor != floor)
			current_floor = floor;

	}
}

class Person extends Thread {
	Scheduler S;
	String name;
	int from = 0;
	int to = 0;
	public Person(String name, Scheduler S) {
        this.name = name;
        this.S = S;
    }

    public void run() {
        try {
          for (;true;) {
        	  if (to > from) 
         		  S.up(name,from, to);
        	  if (from > to) 
        		  S.down(name,from, to);
              from = to;
    		  to = (from + new Random().nextInt(97*S.max)) % S.max;
        }
        }catch(Exception e){}
    }
}



class Scheduler extends Thread {
	int max;
	int current_floor = 0;
	String direction = "up";
	Elevator elevator;
	SortedSet<Integer> up = new TreeSet<Integer>();
	SortedSet<Integer> down = new TreeSet<Integer>();

	public Scheduler(Elevator e, int floors) {
	    elevator = e;
	    this.max = floors;
	}
	
	synchronized void up(String name, int from, int to) {
		try {
			up.add(from);   // press the up button at floor 'from'
		    notifyAll();
		    while (current_floor != from)
			    wait();
		    //up.remove(from); 
		    up.add(to);    // get into elevator and press up button to 'to'
		    notifyAll();
		    while (current_floor != to)
			    wait();
		    //up.remove(to); 
		    // reach floor 'to'
		}
		catch (Exception e) {}

	}
	synchronized void down(String name, int from, int to) {
		try {
			down.add(-from); // press the down button at floor 'from'
		    notifyAll();
		    while (current_floor != from)
			    wait();
		    //down.remove(-from);
		    down.add(-to);	// get into elevator and press down button to 'to'
		    notifyAll();
		    while (current_floor != to)
			    wait();     // reach floor 'to'
		    //down.remove(-to); 
		}
		catch (Exception e) {}
	}
	
	synchronized void schedule()  {
		try {
				
		while (up.isEmpty() && down.isEmpty())
			wait();
				
		elevator.go(current_floor,direction);

		while (up.isEmpty() && down.isEmpty())
			wait();
				
		Log.print_queues("Floor " + current_floor + ", dir = " + direction + ", ", up, down);

		if (current_floor==(max-1)) 
			 direction = "down";
		
	    if (current_floor == 0) 
				 direction = "up";
	    
		elevator.go(current_floor,direction);  // needed because direction was assigned above
			
	    if (direction == "up")
					if (!up.isEmpty())
						if (current_floor > up.last()) {  
							  if (!down.isEmpty()) {
								  if (current_floor < -down.first()) { 
									  // keep going up, then change direction
									  elevator.go(-down.first(),direction);
								      current_floor = -down.first();
								      direction = "down"; 
									  elevator.go(current_floor,direction);
									  down.remove(down.first());
								  }
								  else {  // current_floor >= -down.first()
									  // change direction, go down
									  direction = "down";
								      elevator.go(current_floor,direction); 
									  current_floor = -down.first(); 
								      elevator.go(current_floor,"down"); 
									  down.remove(down.first());
								  }
						      } // down is empty
							  else { // change direction, go down and then go up
							         elevator.go(current_floor,"down");
							         current_floor = up.first(); 
							         elevator.go(current_floor,"down"); 
							         direction = "up";
							         elevator.go(current_floor,"up");
							         up.remove(up.first());
							  }
							}
						else // current_floor <= up.last()
							{for (int f: up) // find next request above current floor
							     if (f >= current_floor) {
								     current_floor = f; 
								     elevator.go(current_floor,direction);
								     up.remove(current_floor);
								     // don't break; serve all up requests
							     }
						     }
	    
				    else // up is empty
				    	if (!down.isEmpty()) {
				    		 if (current_floor <= -down.first()) {
				    			// keep going up
				    		  	 current_floor = -down.first();
	    					     elevator.go(current_floor,direction);
	    					     down.remove(current_floor);
	    					 }
				    		 direction = "down";
				    		 elevator.go(current_floor,direction);
				    		 current_floor = -down.first();
				    		 elevator.go(current_floor,direction);
				    		 down.remove(current_floor);
				    		}
				    	else { // up is empty and down is empty - already considered
				    	     }
	    
         else // direction == "down"
		  			if (!down.isEmpty())
						if (current_floor < -down.last()) {  
							  if (!up.isEmpty()) {
							     if (current_floor >= up.first()) {
									 // keep going down, then change direction
								     elevator.go(up.first(),direction);
							         current_floor = up.first();
							         direction = "up";
									 elevator.go(current_floor,direction);
									 up.remove(current_floor);
							     }
							     else { // current_floor < up.first()
									 // change direction, go up
									 direction = "up";
									 elevator.go(current_floor,direction);
							    	 current_floor = up.first(); 
							    	 elevator.go(current_floor,"up"); 
							    	 up.remove(up.first());
							    	 }
							  } 
							  else  { //up.isEmpty()
							         // change direction, go up and then go down
							         elevator.go(current_floor,"up");
								     current_floor = -down.first();  
								     elevator.go(current_floor,"up"); 
								     direction = "down";
								     elevator.go(current_floor,"down"); 
								     down.remove(down.first());
								  }
							}
	  
						else // current_floor >= -down.last()
							{for (int f: down) // find next request below current floor
								 if (-f <= current_floor) {
									 current_floor = -f;  
									 elevator.go(current_floor,direction);
									 down.remove(-current_floor);
									 //don't break; serve all down requests
								 }
							 }
	    
					else // down is empty
	    
						 if (!up.isEmpty()) {
		    				  if (current_floor > up.first()) {
		    					  // keep going down
		    					  current_floor = up.first();
		    					  elevator.go(current_floor,direction);
		    					  up.remove(current_floor);
		    				  }
		    				  direction = "up";
		    				  elevator.go(current_floor,direction);
		    				  current_floor = up.first(); 
		    				  elevator.go(current_floor,direction);
		    				  up.remove(current_floor);
		    				  }
						 else {// up is empty and down is empty - already considered
							  }
		
		Log.print_queues("Floor " + current_floor + ", dir = " + direction + " ", up, down);

		notifyAll();
		
		sleep(new Random().nextInt(10));
		}
		catch (Exception e) {}
	}

    /*int abs(int x) { 
    	if (x > 0) return x;
    	else return -x;
    }*/
    
	public void run() {
		for (;true;) {
			schedule();
		}
	}
}


// logging output, currently using just print_queues

class Log {
	
	synchronized static void print_queues(String action, SortedSet<Integer> up, SortedSet<Integer> down) {
		System.out.print(action + "up:[ ");
		for (int f: up) 
			System.out.print(f + " ");
		System.out.print("] down:[ ");
		for (int f: down) 
			System.out.print((0-f) + " ");
		System.out.println("]");
	}

}



