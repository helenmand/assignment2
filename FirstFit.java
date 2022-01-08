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

        ArrayList<Integer> startFlag = new ArrayList<Integer>();
        ArrayList<Integer> endFlag = new ArrayList<Integer>();
        int currentBlockStart=0;
        int currentBlockEnd=0;

        //Check every block, quit if process fits
        for(int i=0;i<availableBlockSizes.length && !fit;i++) {

            //Calculate first and last address of block
            currentBlockStart=0;
            for(int j=0;j<i;j++) {
                currentBlockStart+=availableBlockSizes[j];
            }
            currentBlockEnd = currentBlockStart+availableBlockSizes[i]-1;

         

            //get start and end addresses for every slot in current block
            for(int j=0;j<currentlyUsedMemorySlots.size();j++) {
                if(currentlyUsedMemorySlots.get(j).getStart()>=currentBlockStart && currentlyUsedMemorySlots.get(j).getEnd()<=currentBlockEnd) {
                    startFlag.add(currentlyUsedMemorySlots.get(j).getStart());
                    endFlag.add(currentlyUsedMemorySlots.get(j).getEnd());
                }
            }
            
            //All following ifs check if process fits in current block


            //checks if process fits in block, only if there are no other slots in block
            if(startFlag.isEmpty()) {
                if(currentBlockEnd-currentBlockStart+1>=p.getMemoryRequirements()) {
                    fit = true;
                    address = currentBlockStart;
                }
            }
            
            //checks if process fits between start of block and start of first slot
            if(!fit && startFlag.isEmpty()) {
                if(startFlag.get(0)-currentBlockStart>=p.getMemoryRequirements()) {
                    fit = true;
                    address = currentBlockStart; 
                }
            }
            

            //checks if process fits between any slot in block
            if(!fit && startFlag.isEmpty()) {
                for(int j=1;j<startFlag.size() && !fit;j++) {
                    if(startFlag.get(j)-endFlag.get(j-1)-1>=p.getMemoryRequirements()) {
                        fit = true;
                        address = endFlag.get(j-1)+1;
                    }
                }
            }

            //checks if process fits between end of last slot and end of block
            if(!fit && startFlag.isEmpty()) {
                if(currentBlockEnd-endFlag.get(endFlag.size()-1)>=p.getMemoryRequirements()) {
                    fit = true;
                    address = endFlag.get(endFlag.size()-1)+1;
                }
            }
            startFlag.clear();
            endFlag.clear();
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
