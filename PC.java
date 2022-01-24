public class PC {

    public static void main(String[] args) {
        /* TODO: You may change this method to perform any tests you like */
        
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(5, 9, 10),
                new Process(12, 12, 40),
                new Process(20, 7, 25),
                new Process(31, 8, 30)
        };
        final int[] availableBlockSizes = {15, 40, 50, 60}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new NextFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);        
        Scheduler scheduler = new RoundRobin(1);
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();
    }

}
