import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    private ArrayList<Process> loadedProcesses = new ArrayList<>();

    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    /**
     * Getter 
     * @return loaded processes
     */
    public ArrayList<Process> getLoadedProcesses(){
        return this.loadedProcesses;
    }   

    public ArrayList<MemorySlot> getCurrentlyUsMemorySlots(){
        return this.currentlyUsedMemorySlots;
    }

    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */
       
		
        // gets the memory address 
        p.setMemoryLocation(algorithm.fitProcess(p, currentlyUsedMemorySlots));
        
        // Checks if space was found in the memory
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
        	
        	// Checks if the memory is empty
        	if(currentlyUsedMemorySlots.size()!=0) {
		        found= false;
		        index=0;
		        // finds the specific position in the array in ascending order of indicators
		        while(index<currentlyUsedMemorySlots.size() && !found) {
		        	// checks if it is in the correct position
		        	if(slot.getStart()<currentlyUsedMemorySlots.get(index).getStart()) {
		        		currentlyUsedMemorySlots.add(index,slot);
		        		found=true;
		        	}	
		        	index+=1;
		        }
		        
		        // checks if the position was not found and added at the end of the array
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
