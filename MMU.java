import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    

    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

	
	public int[] getAvailableBlockSizes(){
		return availableBlockSizes;
	}  

    public ArrayList<MemorySlot> getCurrentlyUsMemorySlots(){
        return this.currentlyUsedMemorySlots;
    }
    /**
     * This method creates a memory slot for the process in case it can fit in the memory
     * 
     * @param p is the process that will check if can fit in the memory
     * 
     * @return a boolean value true, in case it can fit the process and creates the slot 
	 *			otherwise false means it can not fit the process in memory and the slot will not be created
     */
    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */
       
		
        // gets the memory address 
        p.setMemoryLocation(algorithm.fitProcess(p, currentlyUsedMemorySlots));
        
        // Checks if found space in the memory
        if(p.getMemoryLocation()!=-1) {
        	// Calculates BlockStart and BlockEnd
        	boolean found=false;
        	int index=0;
    		int blockStart=0;
    		int blockEnd=availableBlockSizes[0]-1;
    		while(!found) {
    			// Checks if it is on the block
    			if(p.getMemoryLocation()>=blockStart && p.getMemoryLocation()<=blockEnd) {
    				found=true;
    			}
    			else {
    				index+=1;
    				blockStart=blockEnd+1;
    				blockEnd+=availableBlockSizes[index];
    			}
    		}
    		// Creates slot
    		MemorySlot slot=new MemorySlot(p.getMemoryLocation(),p.getMemoryLocation()+p.getMemoryRequirements()-1,blockStart,blockEnd);
        	
        	// Checks if the memory is not empty
        	if(currentlyUsedMemorySlots.size()!=0) {
		        found= false;
		        index=0;
		        // finds the position in the array according to ascending order of start addresses
		        while(index<currentlyUsedMemorySlots.size() && !found) {
		        	// checks if it is in the correct position
		        	if(slot.getStart()<currentlyUsedMemorySlots.get(index).getStart()) {
		        		currentlyUsedMemorySlots.add(index,slot);
		        		found=true;
		        	}	
		        	index+=1;
		        }
		        
		        // checks if the position has not found so the slot will add at the end of the array
		        if(!found) {
		        	currentlyUsedMemorySlots.add(slot);
		        }
		    }
        	else {
        		// There is no other slot in the memory
        		currentlyUsedMemorySlots.add(slot);
        	}
        	fit=true;
        }
        	
        return fit;
    }
}
