import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    private ArrayList<Process> loadedProcesses = new ArrayList<>();

    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    /**
     * Getter 
     * @return loaded processes
     */
    public ArrayList<Process> getLoadedProcesses(){
        return this.loadedProcesses;
    }   

    public ArrayList<MemorySlot> getCurrentlyUsMemorySlots(){
        return this.currentlyUsedMemorySlots;
    }

    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */
        int address = algorithm.fitProcess(p, currentlyUsedMemorySlots);
        
        if(address != -1) { 
            loadedProcesses.add(p);
            fit = true;
        }

        return fit;
    }
}
