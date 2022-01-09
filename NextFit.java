import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {
    
	private int block=0;
	private int address=0; 
	private int s=availableBlockSizes[block];
	private int sumAllBlock=0;
	
    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }
    
    /*
     * This method finds the slots in a specific block
     * 
     * @param blockSlots is an array that contains all the memory slots
     * @param endBlock is an integer that indicates which of the blocks we want the slots for
     * 
     * @return an array that includes the slots of the block
     */
    private ArrayList<MemorySlot> getSlots(ArrayList<MemorySlot> blockSlots, int endBlock){
		ArrayList<MemorySlot> slots= new ArrayList<>();
		int slotSize=blockSlots.size();
		// There is not slots
		if(slotSize==0) {
			return null;
		}
		else {// there is some slots
			int index=0;
			MemorySlot slot;
			while(index<slotSize) {
				slot=blockSlots.get(index);
				// checks if the slot is in the specific block and if the address is behind of the slot
				if(slot.getBlockEnd()==endBlock && this.address<=slot.getEnd()) {
					slots.add(slot);
					}
				index+=1;
				}
				
			}
			return slots;
    }

    
    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */
        
        // finds the total memory of the blocks
        for(int i=0;i<availableBlockSizes.length;i++) {
    		sumAllBlock+=availableBlockSizes[i];
    	}
        
        // where the address starts
        int startAddress=this.address;
        
        ArrayList<MemorySlot> blockSlots;
        // takes the slots of the block we are
        blockSlots=getSlots(currentlyUsedMemorySlots, s-1);
        int index=0;
        
        do {
        	// This block has no slots or the address is in front of the slots
        	if(blockSlots.size()==0) {
        		// checks if the process can be entered into memory
        		if(s-this.address>=p.getMemoryRequirements()) {
        			// the address at which the process space begins
        			address=this.address;
        			fit=true;
        		}	
        	}
        	// That block has slots
        	else {
        		MemorySlot slot=blockSlots.get(index);
        		
        		// checks if the slot is finished and if there is another slot 
    			if(this.address==slot.getEnd() && blockSlots.size()>index+1) {
    				index+=1;
    			}
    			else {// checks if the address does not point to used space
    				// behind of slot
    				if(this.address<slot.getStart()) {
    					if(slot.getStart()-this.address>=p.getMemoryRequirements()) {
    						// the address at which the process space begins
    	        			address=this.address;
    	        			fit=true;
    					}
    				}
    				else {
    					// front of slot
    	        		if(this.address>slot.getEnd()) {
    	        			// checks the empty space if the process can be entered
    	        			if(s-this.address>=p.getMemoryRequirements()) {
    	        				address=this.address;
    	        				fit=true;
    	        			}
    	        		}
    				}
    			}
        	}
        	
        	// Updates the address
        	this.address=(this.address+1)%this.sumAllBlock;
        	// return to the first block
    		if(this.address==0) {
    			block=0;
    			s=availableBlockSizes[block];
    			blockSlots=getSlots(currentlyUsedMemorySlots, s-1);
    			index=0;
    		}
    		else {
    			// next block
    			if(this.address==this.availableBlockSizes[block]) {
	    			block+=1;
	    			s+=availableBlockSizes[block];
	    			blockSlots=getSlots(currentlyUsedMemorySlots, s-1);
	    			index=0;
    			}
    			
    		}
        }while(startAddress!=this.address && !fit);

        return address;
    }

}
