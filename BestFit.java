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

        // stores size of candidate blocks. 
        // candidate blocks are those which are bigger or equal than the processes memory requirements.
        ArrayList<Integer> candidate_blocks = new ArrayList<Integer>();
        candidate_blocks = find_candidate_blocks(availableBlockSizes, p.getMemoryRequirements());

        // if there are no candidate blocks, the process does not fit
        if (candidate_blocks.size() == 0) { return address; }

        /*FOR WORST FIT ADD
        candidate_blocks = reverseArrayList(candidate_blocks);*/
        
        for (Integer block : candidate_blocks){
            // fininding start and end of the block
            int block_index = find_index(availableBlockSizes, block);
            int block_start = 0;
            
            for (int i = 0; i < block_index; i++){ block_start += availableBlockSizes[i]; }
            
            int block_end = block_start + block - 1;

            // fit code
        }

        return address;
    }


    /**
     * finding every block that could fit the process. Adding the sizes of these blocks
     * in an array list and sorting the list in ascending order
     * 
     * @param blocks every available block
     * @param size the size of the process
     * @return final_blocks, the blocks that fit the process
     */
    private ArrayList<Integer> find_candidate_blocks(int[] blocks, int size){
        ArrayList<Integer> final_blocks = new ArrayList<Integer>();

        for (int i = 0; i < blocks.length; i++){ if ( final_blocks.get(i) >= size ){ final_blocks.add(blocks[i]);} }
        final_blocks.sort(null);

        // returns all candidate memory blocks in asceding order.
        return final_blocks; 
    }

    /**
     * returns the index of the given element in the given array.
     * 
     * @param arr the array 
     * @param element the element
     * @return the index of the element or -1 if it does not exist.
     */
    private int find_index(int[] arr, int element){
        for (int i = 0; i < arr.length; i++){ if (arr[i] == element) { return i; } }   
        return -1;
    }
}
