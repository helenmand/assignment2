import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {
    
	private int block=0;	// The block we are
	private int address=0;  // We store the address we stopped so we can start now from this address
	private int sumcurrentBlock=availableBlockSizes[block]; // Maintains the sum of the blocks where the address is
	
    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }
    
    /**
     * This method checks if can fit a process in the memory
     * 
     * @param p is the process that will check if can be fit in the memory
     * @param currentlyUsedMemorySlots is the array that contains the used slots
     * 
     * @return the memory address of the process which was stored in memory
     * 		   otherwise -1 which means that no memory space was found
     */
    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */
        
        // finds the total memory of the blocks
        int sumAllBlock=0;
        for(int i=0;i<availableBlockSizes.length;i++) {
    		sumAllBlock+=availableBlockSizes[i];
    	}
        // the address stops looking for free space
        int endAddress=this.address+sumAllBlock;
        // counts the movements of the address to search a position
        int countEndAddress=this.address;
        
        boolean memoryIsChecked=false;
        ArrayList<Integer[]> freePositions= new ArrayList<>();
        // takes the free spaces of the block
        freePositions=getFreeSpaces(currentlyUsedMemorySlots, this.address, sumcurrentBlock-1);
        int index=0;
        
        do {
        	// checks if the block has free spaces 
        	if(index<freePositions.size()) { 
        		// checks if the process can be entered in this free space
        		//        end free space      -     start free space   (+1 because the same space counts as a free space
        		if(freePositions.get(index)[1]-freePositions.get(index)[0]+1>=p.getMemoryRequirements()) {
        			// the position of start address that fits in this process
        			address=freePositions.get(index)[0];
        			fit=true;
        			// updates the address to the new position
        			this.address=freePositions.get(index)[0]+p.getMemoryRequirements()-1;
        		}
        		else { 
    				// counts the positions that the address will move at the end of free space
    				countEndAddress+=(freePositions.get(index)[1]-this.address);
    				// checks if it has exceeded the end index
    				if(endAddress<=countEndAddress) {
    			        // returns the address to the original position
    					this.address=endAddress-sumAllBlock;
    					// all memory was checked
    					memoryIsChecked=true;
    				}
    				else { // moves the position of the address to the next place
    					this.address=freePositions.get(index)[1];
    					index+=1;
    				}
    			}
        	}
        	else {// the block has no free spaces
        		countEndAddress+=sumcurrentBlock-this.address;
				// checks if it has exceeded the end index
				if(endAddress<=countEndAddress) {
			        // returns the address to the original position
					this.address=endAddress-sumAllBlock;
					// all memory was checked
					memoryIsChecked=true;
				}
				else {				
	        		// checks if it was the last block and then starts again from the first memory block
	        		if(block+1==this.availableBlockSizes.length) {
	        			block=0;
	        			sumcurrentBlock=availableBlockSizes[block];
	        			this.address=0;
	        		}
	        		else {// it was not the last block, next block
	        			block+=1;
	        			this.address=sumcurrentBlock;
	        			sumcurrentBlock+=availableBlockSizes[block];
	        		}
	        		// takes the free spaces of the block
	        		freePositions=getFreeSpaces(currentlyUsedMemorySlots, this.address, sumcurrentBlock-1);
	        		index=0;
				}
        	}
        	
        }while(!memoryIsChecked && !fit);
       
        return address;
    }

}
