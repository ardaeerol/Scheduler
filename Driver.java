public class Driver {
    public static void main(String[] args) {
    job j0 = new job(0,0,"H",3);
    job j1 = new job(0,1,"H",2);
    job j2 = new job(0,2,"M",3);
    job j3 = new job(3,3,"H",3);

    Scheduler programmer = new Scheduler();
    programmer.setResourcesCount(2);
    programmer.add(j0);
    programmer.add(j1);
    programmer.add(j2);
    programmer.add(j3);
    programmer.run();
    System.out.println("-------");
    programmer.utilization(1); 
    System.out.println("-------");
    programmer.resourceExplorer(2);
    System.out.println("-------");
    programmer.jobExplorer(j3);



    }
}
