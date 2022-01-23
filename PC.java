public class PC {

    public static void main(String[] args) {
        /* TODO: You may change this method to perform any tests you like */
        
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(5, 9, 10, 1),
                new Process(12, 12, 40, 2),
                new Process(20, 7, 25, 3),
                new Process(31, 8, 30, 4)
        };
        final int[] availableBlockSizes = {15, 40, 50, 60}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new NextFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);        
        Scheduler scheduler = new FCFS();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();

        for(Process p : processes) {
            System.out.println("");
            System.out.println("--PROCESS " + p.getPCB().getPid() + "---");
            System.out.println("Arrival Time: " + p.getArrivalTime());
            System.out.println("Burst Time: " + p.getBurstTime());
            System.out.println("Memory Requirements: " + p.getMemoryRequirements());
            System.out.println("Memory Location: " + p.getMemoryLocation());
            System.out.println("Waiting Time: " + p.getWaitingTime());
            System.out.println("Response Time: " + p.getResponseTime());
            System.out.println("Turnaround Time: " + p.getTurnAroundTime());
            System.out.println("");
        }
    }

}
