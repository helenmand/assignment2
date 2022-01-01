import java.util.ArrayList;
public class SRTF extends Scheduler {

	ArrayList<Integer> BurstTimes;
    public SRTF() {
        /* TODO: you _may_ need to add some code here */
    	  	
    	BurstTimes= new ArrayList<>();	    	
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
    	
    	// Checks if the array of processes is empty
    	if(processes.size()==0) {
    		processes.add(p);
    		BurstTimes.add(p.getBurstTime());
    	}
    	else { // Is not empty 
	    	int index=0; // array index
	    	boolean found=false; // checks if found the position in array
	    	
	    	// find the specific index to add the process
	    	while(index<processes.size() && !found) {
	    		// Checks if the process have the shorter burst time 
	    		if(BurstTimes.get(index)>p.getBurstTime()) {
	    			// add the process
	    			processes.add(index,p);
	    			BurstTimes.add(index,p.getBurstTime());
	    			// the specific index found
	    			found=true;
	    		}
	    		index+=1;
	    	}
	    	// Checks if the process has not found a place to add to the array
	    	if(!found) {
	    		// the process has the biggest burst time
	    		processes.add(index,p); // Added at the end of the array
	   			BurstTimes.add(index,p.getBurstTime());
	   		}
    	}
    		
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
    	
    	// Checks if the array of processes is empty
    	if(processes.size()==0) {
    		return null;
    	}
    	else {
    		// checks if the process is in state READY or RUNNING
    		if(processes.get(0).getPCB().getState().equals(ProcessState.READY) || processes.get(0).getPCB().getState().equals(ProcessState.RUNNING)) {
    			
    			// Checks if the process is over
	    		if(BurstTimes.get(0)==0) {
	    			// remove the first place of array 
	    			BurstTimes.remove(0);
	    		}
	    		
	    		// Update the burst time of a process to be executed 
    			int newBurstTime=BurstTimes.get(0)-1;
	    		
		    	// Remove the old time
		    	BurstTimes.remove(0);
		    	// Add the new time 
		    	BurstTimes.add(0,newBurstTime);
	    		
		    	// return the first process from array 
		    	// that has the shortest remaining time first
		        return processes.get(0);
    		}
    		else {
    			return null;
    		}
    	}
    }
}




