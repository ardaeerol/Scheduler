public class Surucu {
    public static void main(String[] args) {
    job j0 = new job(0,0,"H",3);
    job j1 = new job(0,1,"H",2);
    job j2 = new job(0,2,"M",3);
    job j3 = new job(3,3,"H",3);

    Scheduler programlayici = new Scheduler();
    programlayici.setResourcesCount(2);
    programlayici.add(j0);
    programlayici.add(j1);
    programlayici.add(j2);
    programlayici.add(j3);
    programlayici.run();
    System.out.println("-------");
    programlayici.utilization(1); // kaynak 1, toplam 6 birim çalışmış ve arada boş kaldığı zaman              // olmamış, 6/6 = 1
    System.out.println("-------");
    programlayici.resourceExplorer(2);
    System.out.println("-------");
    programlayici.jobExplorer(j3);



    }
}