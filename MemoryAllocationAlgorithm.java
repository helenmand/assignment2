import java.util.ArrayList;

public abstract class MemoryAllocationAlgorithm {

    protected final int[] availableBlockSizes;
    
    public MemoryAllocationAlgorithm(int[] availableBlockSizes) {
        this.availableBlockSizes = availableBlockSizes;
    }

    public abstract int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots);

    protected ArrayList<Integer[]> getFreeSpaces(ArrayList<MemorySlot> currentlyUsedMemorySlots, int blockStart, int blockEnd) {
        
        ArrayList<Integer[]> freeSpaces = new ArrayList<Integer[]>();
        Integer[] set;

        ArrayList<Integer> startFlag = new ArrayList<Integer>();
        ArrayList<Integer> endFlag = new ArrayList<Integer>();
        
        //if there are no slots in use - the whole block is free. If there are slots in use, find all slots in block
        if(currentlyUsedMemorySlots.isEmpty()) {
            set = new Integer[2];
            set[0] = blockStart;
            set[1] = blockEnd;
        } else {

            //find all slots in block
            for(int i=0;i<currentlyUsedMemorySlots.size() && currentlyUsedMemorySlots.get(i).getStart() <= blockEnd;i++) {
                if(currentlyUsedMemorySlots.get(i).getEnd()>=blockStart) {
                    startFlag.add(currentlyUsedMemorySlots.get(i).getStart());
                    endFlag.add(currentlyUsedMemorySlots.get(i).getEnd());
                }
            }
            
            //if none of the currenly used slots are in this block, the whole block is free
            if(startFlag.isEmpty()) {
                set = new Integer[2];
                set[0] = blockStart;
                set[1] = blockEnd;
            } else {
                //add free space that exists between block start and first slot
                if(startFlag.get(0)>blockStart) {
                    set = new Integer[2];
                    set[0] = blockStart;
                    set[1] = startFlag.get(0)-1;
                }
                
                //add free space that exists between each slot
                for(int i=1;i<startFlag.size();i++) {
                    if (startFlag.get(i)- endFlag.get(i-1) > 1) {
                        set = new Integer[2];
                        set[0] = endFlag.get(i-1) + 1;
                        set[1] = startFlag.get(i) - 1;
                    }
                }
        
                //add free space that exists between last slot and block end
                if(endFlag.get(endFlag.size()-1)<blockEnd) {
                    set = new Integer[2];
                    set[0] = endFlag.get(endFlag.size()-1)+1;
                    set[1] = blockEnd;
                }
            }
        }
        return freeSpaces;
    }
}
