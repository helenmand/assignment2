import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {
    
	private int block=0;
	private int address=0; 
	private int s=availableBlockSizes[block];
	
    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }
     
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
        // where the address will stop looking for free space
        int endAddress=this.address+sumAllBlock;
        // 
        int countEndAddress=this.address;
        // 
        boolean memoryIsChecked=false;
        ArrayList<Integer[]> freePositions= new ArrayList<>();
        // takes the free spaces of the block
        freePositions=getFreeSpaces(currentlyUsedMemorySlots, this.address, s-1);
        int index=0;
        
        do {
        	// checks if the block has free spaces 
        	if(index<freePositions.size()) { 
        		// checks if the process can be entered to this free space
        		//        end free space      -     start free space   (+1 because the same space counts for a free space)
        		if(freePositions.get(index)[1]-freePositions.get(index)[0]+1>=p.getMemoryRequirements()) {
        			
        			address=freePositions.get(index)[0];
        			fit=true;
        			this.address=freePositions.get(index)[0]+p.getMemoryRequirements()-1;
        		}
        		else { 
    				// the address moves to the end of this free space
    				countEndAddress+=(freePositions.get(index)[1]-this.address);
    				// checks the address if was start from the middle of the block
    				// If it has exceeded the end index
    				if(endAddress<=countEndAddress) {
    			        // returns the address to the specific position that was in from the beginning
    					this.address=endAddress-sumAllBlock;
    					memoryIsChecked=true;
    				}
    				else { // moves the address position to the next place
    					this.address=freePositions.get(index)[1];
    					index+=1;
    				}
    			}
        	}
        	else {// the block not has free spaces
        		countEndAddress+=s-this.address;
        		
        		// checks the address if was start from the middle of the block
				// If it has exceeded the end index
				if(endAddress<=countEndAddress) {
			        // returns the address to the specific position that was in from the beginning
					this.address=endAddress-sumAllBlock;
					memoryIsChecked=true;
				}
				else {				
	        		// checks if it was the last block for to start from the first memory block
	        		if(block+1==this.availableBlockSizes.length) {
	        			block=0;
	        			s=availableBlockSizes[block];
	        			this.address=0;
	        		}
	        		else {// not was the last block, next block
	        			block+=1;
	        			this.address=s;
	        			s+=availableBlockSizes[block];
	        		}
	        		// takes the free spaces of the block
	        		freePositions=getFreeSpaces(currentlyUsedMemorySlots, this.address, s-1);
	        		index=0;
				}
        	}
        	
        }while(!memoryIsChecked && !fit);
       
        return address;
    }

}
