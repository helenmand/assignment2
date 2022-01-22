import java.util.ArrayList;

public class WorstFit extends MemoryAllocationAlgorithm {
    
    public WorstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    /**
     * Tries to fit a process in memory according to Best Fit algorithm.
     * Searches every available memory slot to find the biggest available memory slot
     * 
     * @param p procces to fit in memory
     * @param currentlyUsedMemorySlots the memory slots that are already in use
     * @return the address the process fits in or -1 in case the process cannot fit in the memory
     */
    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        int max = -1;
        int block_start = 0; int block_end = 0; // start/end of the current block

        ArrayList<Integer[]> available_slots = new ArrayList<Integer[]>();
        Integer[] worst_slots = new Integer[2];

        for (int block : availableBlockSizes){
            block_end = block_start + block - 1; // updating current block end
            
            // if the block is smaller than the size of the process there is no need to check it
            if (block < p.getMemoryRequirements()){ block_start += block; continue; }

            // getting the avaible memory slots of the current block
            available_slots = getFreeSpaces(currentlyUsedMemorySlots, block_start, block_end);

            for (Integer[] slot : available_slots){
                // the slot must be greater or equal to the process's size and to meet the requirements of worstfit
                // the slot must be greater than the max, and not equal to select the first available slot
                if ((slot[1] - slot[0] + 1 >= p.getMemoryRequirements()) && (slot[1] - slot[0] + 1 > max)) {
                    max = slot[1] - slot[0] + 1;
                    worst_slots = slot; // update when a slot that meets requirements is found
                    fit = true; // the process fits in memory
                } 
            }
            block_start += block; // update next block's start
        }
        if(fit){ address = worst_slots[0]; } // changing the address when the process fits
        return address;
    }
    
}
