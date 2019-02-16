package seminar_test;

class DiningPhilosophers {
	
    public static void main(String[] args)  {
        Fork f1 = new Fork ();
        Fork f2 = new Fork ();
        Fork f3 = new Fork ();
        Fork f4 = new Fork ();
        Fork f5 = new Fork ();
        
        Philosopher p1 = new Philosopher("P1",f1,f2);
        Philosopher p2 = new Philosopher("P2",f2,f3);
        Philosopher p3 = new Philosopher("P3",f3,f4);    
        Philosopher p4 = new Philosopher("P4",f4,f5);
        Philosopher p5 = new Philosopher("P5",f1,f5);
        
        (new Thread(p1)).start();
        (new Thread(p2)).start();
        (new Thread(p3)).start();
        (new Thread(p4)).start();
        (new Thread(p5)).start();
    }
}

class State {
    protected String state;
    String name;
    protected void set_state(String s){
    	state = s;
    	try {
    	if (s.equals("T")) {
        		System.out.println ("Philosopher " + name + " is thinking");
                Thread.sleep((int)(25*Math.random()));
        }
    	if (s.equals("H")) {
    		System.out.println ("Philosopher " + name +" got left fork");
            Thread.sleep((int)(25*Math.random()));
    	}
    	if (s.equals("E")) {
            System.out.println ("Philosopher " + name +" got right fork and eating."); 
    		Thread.sleep((int)(25*Math.random())); // simulate eating
            System.out.println ("Philosopher " + name +" put down left and right fork");
    	}
    	}
    	catch (Exception e) {}
    }
}

class Philosopher extends State implements Runnable  {
	
    Fork left, right;
    
    Philosopher(String name,Fork left,Fork right) {   
    	this.name = name;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public void run() {
        try {
          set_state("T");  
          for(int i=0; i<400; i++) {
            left.pickup();
            set_state("H");  
            right.pickup();
            set_state("E");  
            set_state("T"); 
            left.drop();
            right.drop();
          }
        } 
        catch(Exception e){}
    }
}

class Fork {
    boolean taken = false;
    synchronized void pickup() throws Exception {
        while(taken)
            wait();
        taken = true; 
    }
    synchronized void drop() throws Exception {
        taken = false;
        notify();
    }
}   

