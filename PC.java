
public class PC {

    public static void main(String[] args) {
        /* TODO: You may change this method to perform any tests you like */
        
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(5, 21, 26, 0),
                new Process(12, 12, 40, 1),
                new Process(20, 19, 42, 2),
                new Process(31, 9, 30, 3),
                new Process(32, 13, 13, 4),
                new Process(32, 15, 24, 5),
                new Process(32, 19, 39, 6),
                new Process(32, 8, 6, 7),
                new Process(32, 9, 3, 8)
        };
        final int[] availableBlockSizes = {15, 40, 50, 25}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new FirstFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);        
        Scheduler scheduler = new FCFS();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();
    }

}
