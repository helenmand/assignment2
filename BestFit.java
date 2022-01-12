import java.util.ArrayList;

public class BestFit extends MemoryAllocationAlgorithm {
    
    public BestFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        int minMax = 2147483647;
        int block_index = 0;

        ArrayList<Integer[]> available_slots = new ArrayList<Integer[]>();
        Integer[] best_slots = new Integer[2];

        for (int block : availableBlockSizes){
            
            int block_start = 0;
            for (int i = 0; i < block_index; i++){ block_start += availableBlockSizes[i]; } 
            int block_end = block_start + block - 1;

            available_slots = getFreeSpaces(currentlyUsedMemorySlots, block_start, block_end);

            for (Integer[] slot : available_slots){
                if ((slot[1] - slot[0] + 1 >= p.getMemoryRequirements()) && (slot[1] - slot[0] + 1 < minMax)) {
                    minMax = slot[1] - slot[0] + 1;
                    best_slots = slot;
                    fit = true;
                } 
            }
            ++block_index;
        }
        if(fit){ address = best_slots[0]; }
        return address;
    }

    
    
}
