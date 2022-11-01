import java.text.DecimalFormat; // to round utilization

/*
 * Arda Erol
 * 
 */



/*
 * basic LinkedList implementation
 * 
 * designed not in generic type  
 * since only type 'job' is assigned
 * in the project
 * 
 */


class JobList {
	protected static class Node{
		private job element;
		private Node next;
		
		public Node(job element, Node next) {
			this.element = element;
			this.next = next;
		}
		
		public job getElement() {return this.element;}
		
		public Node getNext() {return this.next;}
		
		public void setNext(Node next) {this.next = next;}
	}
	
	protected Node head;
	protected Node tail;
	protected int size;
	
	public JobList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}
	
	public int size() {return size;}
	
	public boolean isEmpty() {return this.size == 0;}
	
	public job get(int index) {
		
		if(index < 0 || index >= size)return null;
		
		Node cur = this.head;
		while(index != 0) {
			cur = cur.getNext();
			index--;
		}
		
		return cur.getElement();		
	}
	
	public job first() {
		if(isEmpty())return null;
		return head.getElement();
	}
	
	public job last() {
		if(isEmpty())return null;
		return tail.getElement();
	}
	
	public void addFirst(job element) {
		head = new Node(element, head);
		if(isEmpty())tail = head;
		size++;
	}
	
	public void addLast(job element) {
		Node newest = new Node(element, null);
		if(isEmpty())head = newest;
		else tail.setNext(newest);
		tail = newest;
		size++;
	}
	
	/* void insert(Node prev, job element)
	 * 
	 * helps WaitList
	 * to sort by
	 * arriving time
	 */
	
	public void insert(Node prev, job element) { 
		Node newest = new Node(element, null);
		if(prev.getNext() != null) {
			Node next = prev.getNext();
			newest.setNext(next);
			prev.setNext(newest);
		}
		else {
			prev.setNext(newest);
			tail = newest;
		}
		size++;
	}
	
	public job removeFirst() { 
		if(isEmpty())return null;
		job answer = head.getElement();
		head = head.getNext();
		if(isEmpty())tail = null;
		size--;
		return answer;
	}
	
	// optional code fragment to print list
	
	/*
	 public void print() {
		Node cur = head;
		while(cur != null) {
			System.out.println(cur.getElement().arrivalTime);
			cur = cur.getNext();
		}
	}
	
	// optional code fragment to print list
	
	*/
}

/*
 * implementation of WaitList
 * 
 * created jobs are stored in WaitList
 * 
 * jobs are sorted by arriving time
 * in WaitList automatically
 */

class WaitList extends JobList{
	
	/*
	 * void add(job j)
	 * 
	 * adding job j
	 * by sorting
	 * arrival time
	 * 
	 */
	
	public void add(job j) { 
		Node cur = head;
		if(isEmpty()) {addFirst(j);}
		
		else if(cur.getNext() == null) {
			if(j.arrivalTime < head.getElement().arrivalTime)addFirst(j);
			else addLast(j);
		}
				
		else {			
			while(cur.getNext() != null && cur.getNext().getElement().arrivalTime <= j.arrivalTime)cur = cur.getNext();
			insert(cur, j);
		}
	}	
}

/*
 * implementation of Queue
 * 
 * jobs are taken from WaitList
 * and placed in relevant queue
 * 
 */

class PQueue extends JobList{
	
	public void enqueue(job j) {
		addLast(j);
	}
	
	public job dequeue() {
		return removeFirst();
	}
}

public class Scheduler {
	
	/*
	-----------------
	 * inner class
	 * core
	 */
	
	class core{
		int id; 
		int totalExecutionTime;                 // total execution time of the core in a lifetime
		int executionTime;                      // execution time for each job core assigned
		int freeTime;                           // total free time of the core in a lifetime
		boolean status;                         // true if core is sleeping, false if executing
		job executing;                          // reference of job core assigned
		JobList handledJobs = new JobList();    //jobs are located handledJobs after executed and finished successfully
		
		public core(int id) {
			this.id = id;
			this.totalExecutionTime = 0; 
			this.executionTime = 0;
			this.freeTime = 0;
			status = true; 
			this.executing = null;
		}
		
		public boolean isAvailable() {
			return status;
		}
		
		public void setStatus(boolean status) {
			this.status = status;
		}
		
		/*
		 * void take()
		 * 
		 * assigns given job
		 * to the core
		 * 
		 * sets availability false
		 * 
		 */
		
		private void take(job executing) {
			this.executing = executing;
			setStatus(false);
		}
		
		/*
		 * void release()
		 * 
		 * releases current job
		 * from the core
		 * 
		 * adds job to the handledJobs
		 * 
		 * do not sets availability true
		 * it is handled in done() line 317
		 * 
		 */
		
		private void release() {
			handledJobs.addLast(executing);
			strend[executing.id][1] = time;
			this.executing = null;
		}
		
		/*
		 * void execute()
		 * 
		 * increments execution time
		 * in per unit time if
		 * core is assigned
		 * to a job
		 */
		
		public void execute() { 
			if(executionTime == 0)strend[executing.id][0] = time;
			if(!done()) {
				executionTime++;
				totalExecutionTime++;
			}
			
			done();
		}
		
		/*
		 * void free() will be used to update Scheduler, later
		 * 
		 * increments free time
		 * in per unit time if
		 * core is not assigned
		 * to a job
		 */
		
		public void free() {
			freeTime++;
		}
		
		/*
		 * double performance()
		 * 
		 * helps utilization()
		 * defined in Scheduler Class
		 * 
		 */
		
		private double performance() {
			DecimalFormat df = new DecimalFormat("#.##");
			return Double.valueOf(df.format((double)(totalExecutionTime) / (double)time));
		}
		
		/*
		 * boolean done()
		 * 
		 * returns true if job
		 * assigned core 
		 * is completed
		 * 
		 * releases job
		 * sets core available again
		 * 
		 * otherwise returns false
		 * 
		 */
		
		private boolean done() {
			if(executionTime == executing.duration) {
				this.executionTime = 0;
				release();
				setStatus(true);
				return true;
			}
			
			return false;
		}
	}
	
	/*
	 * inner class
	 * core 
	 -----------------
	 */
	
	private WaitList waitlist;    // jobs are wait in waitlist until execution time
	private PQueue highPQueue;    // cores take jobs from highPqueue immediately
	private PQueue midPQueue;     // if cores are available after highPQueue, take jobs from midPqueue
	private PQueue lowPQueue;     // if cores are available after midPQueue, take jobs from lowPqueue
	private core[] processors;    // cores are stored in processors array
	int[][] strend;               // stores taking and releasing time of each job by cores
	private int jobs = 0;         // number of jobs 
	private int time = 0;         // system time
	
	public Scheduler() {
		this.waitlist = new WaitList();
		this.highPQueue = new PQueue();
		this.midPQueue = new PQueue();
		this.lowPQueue = new PQueue();
	}
	
	public void setResourcesCount(int resource) {
		processors = new core[resource];
		generateCore();
	}
	
	private void generateCore() {
		for(int i=0; i<processors.length; i++)
			processors[i] = new core(i);
	}
	
	/*
	 * void locateJob(job j)
	 * 
	 * puts jobs to
	 * relevant queues from 
	 * WaitList
	 * 
	 */
	
	private void locateJob(job j) {	 
		if(j.priority.equals("H")) 
			highPQueue.enqueue(j);
		
		else if(j.priority.equals("M")) 
			midPQueue.enqueue(j);
		
		else 		
			lowPQueue.enqueue(j);		
	}
	
	/*
	 * int freeResource()
	 * 
	 * returns min_id available core
	 * returns -1 if cores are on
	 * execution
	 * 
	 */
	
	private int freeResource() {
		for(int i=0; i<processors.length; i++)
			if(processors[i].isAvailable())return i;
		return -1;
	}
	
	public void add(job j) {
		jobs++;
		waitlist.add(j);
	}
	
	public void utilization(int core_id) {
		core c = processors[core_id-1];
		System.out.println("R" + core_id + " verim: " + c.performance());
	}
	
	public void resourceExplorer(int core_id) {
		core c = processors[core_id-1];
		
		String output = "R" + core_id + ": ";
		for(int i=0; i<c.handledJobs.size(); i++) 
			output += ("(" + c.handledJobs.get(i).id + ", " + strend[c.handledJobs.get(i).id][1] + ", " + (strend[c.handledJobs.get(i).id][0] - c.handledJobs.get(i).arrivalTime) + "), ");
		
		System.out.println(output.substring(0, output.length()-2));		
	}
	
	public void jobExplorer(job j) {		
		int core_id = 0;
		
		for(core c: processors)
			for(int i=0; i<c.handledJobs.size(); i++)
				if(c.handledJobs.get(i).id == j.id)core_id = c.id;
		
		String output = "islemno\tkaynak\tbaslangic\tbitis\tgecikme\n" 
				+ j.id + "\t" + "R" + (core_id+1) + "\t" + strend[j.id][0] + "\t\t" + strend[j.id][1] + "\t" + (strend[j.id][0] - j.arrivalTime);
		
		System.out.println(output);
	}
	
	/*
	 * boolean controller()
	 * 
	 * controls outer while-loop	 
	 */
	
	private boolean controller() {
		boolean b = true;
		for(int i=0; i<processors.length; i++) {
			if(!processors[i].isAvailable())b = false;
		}
		return (waitlist.size() == 0 && highPQueue.size() == 0 && midPQueue.size() == 0 && lowPQueue.size() == 0 && b);
	}
	
	/*
	 * String console()
	 * 
	 * helps to print
	 * time flow to
	 * the console
	 */
	
	private String console() {
		
		String out = "Zaman\t";
		for(int i=0; i<processors.length; i++)
			out += "R" + (i+1) + "\t";
		
		return out;		
	}
	
	public void run() {
		
		strend = new int[jobs][2];
		
		System.out.println(console());
		
		while(!controller()) {
						
			for(int i=0; i<processors.length; i++) 
				if(waitlist.size() != 0 && processors[i].isAvailable()) {
					job j = waitlist.first();
					if(time >= j.arrivalTime){
						locateJob(j);
						waitlist.removeFirst();
					}
				}
			
			
			while(highPQueue.size() != 0 && freeResource() != -1) {
				core c = processors[freeResource()];
				c.take(highPQueue.dequeue());
			}
			
			while(midPQueue.size() != 0 && freeResource() != -1) {
				core c = processors[freeResource()];		
				c.take(midPQueue.dequeue());					
			}
			
			while(lowPQueue.size() != 0 && freeResource() != -1) {
				core c = processors[freeResource()];
				c.take(lowPQueue.dequeue());		
			}
			
			System.out.print(time + "\t");
			for(int i=0; i<processors.length; i++)
				if(!processors[i].isAvailable()) {
					System.out.print("J"+processors[i].executing.id + "\t");
					processors[i].execute();
				}
				else {
					System.out.print("\t");
					processors[i].free();
				}
			
			System.out.println();
			
			time++;
			
			//optional code fragment to observe steps easily
			
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			//optional code fragment to observe steps easily
		}
	}	
}
