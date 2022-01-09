import java.util.ArrayList;

public class FirstFit extends MemoryAllocationAlgorithm {
    
    public FirstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        int currentBlockStart=0;
        int currentBlockEnd=0;
        ArrayList<Integer[]> freeSpaces;

        //Check every block, quit if process fits
        for(int i=0;i<availableBlockSizes.length && !fit;i++) {

            //Calculate first and last address of block
            currentBlockStart=0;
            for(int j=0;j<i;j++) {
                currentBlockStart+=availableBlockSizes[j];
            }
            currentBlockEnd = currentBlockStart+availableBlockSizes[i]-1;

            //get free space in block
            freeSpaces = getFreeSpaces(currentlyUsedMemorySlots, currentBlockStart, currentBlockEnd);

            //check if block fits in any free space
            for(int j=0;j<freeSpaces.size() && !fit;j++) {
                if(freeSpaces.get(j)[1] - freeSpaces.get(j)[0] + 1 >= p.getMemoryRequirements()) {
                    address = freeSpaces.get(j)[0];
                    fit = true;
                }
            }   
        }

        //TEMPORARY TESTING CODE MEMORYSLOT ADDING
        if(fit) {
            MemorySlot slot = new MemorySlot(address, address+p.getMemoryRequirements()-1, currentBlockStart, currentBlockEnd);
            if(currentlyUsedMemorySlots.isEmpty()) {
                currentlyUsedMemorySlots.add(slot);
            } else {
                boolean inserted = false;
               
                for(int i=0;i<currentlyUsedMemorySlots.size() && !inserted;i++) {
                    if(address<currentlyUsedMemorySlots.get(i).getStart()) {
                        currentlyUsedMemorySlots.add(i, slot);
                        inserted = true;
                    }
                }
                if(!inserted) {
                    currentlyUsedMemorySlots.add(slot);
                }
            }
            
        }
        

        return address;
    }

}
