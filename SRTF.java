import java.util.ArrayList;
public class SRTF extends Scheduler {

	ArrayList<Integer> BurstTimes; // Burst times of processes are stored
    public SRTF() {
        /* TODO: you _may_ need to add some code here */
    	  	
    	BurstTimes= new ArrayList<>();	    	
    }
    
    /**
     * This method adds processes sorted by their burst time
     * 
     * @param p is the process that will be added and sorted based on its burst time 
     */
    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
    	
    	// Checks if the array of processes is empty
    	if(processes.size()==0) {
    		processes.add(p);
    		BurstTimes.add(p.getBurstTime());
    	}
    	else { // Is not empty 
	    	int index=0; // array index
	    	boolean found=false; // checks if the position in the array has been found
	    	
	    	// finds the specific index to adds the process
	    	while(index<processes.size() && !found) {
	    		// Checks if the process has the shorter burst time 
	    		if(BurstTimes.get(index)>p.getBurstTime()) {
	    			// add the process
	    			processes.add(index,p);
	    			BurstTimes.add(index,p.getBurstTime());
	    			// the specific index has been found
	    			found=true;
	    		}
	    		index+=1;
	    	}
	    	// Checks if the process has not found a place to add to the array
	    	if(!found) {
	    		// the process has the biggest burst time
	    		processes.add(p); // Added at the end of the array
	   			BurstTimes.add(p.getBurstTime());
	   		}
    	}
    }
    /**
     * This method returns the process with the shortest remaining burst time
     * 
     * @return the process that has the shortest remaining burst time
     */
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
    	
    	// Checks if the array of processes is empty
    	if(processes.size()==0) {
    		return null;
    	}
    	else {
    		// checks if the process is in state READY
    		if(processes.get(0).getPCB().getState().equals(ProcessState.READY)){
    			return processes.get(0);
    		}
    		else {
    			// checks if the process is in state RUNNING
    			if(processes.get(0).getPCB().getState().equals(ProcessState.RUNNING)) {
	    			// Checks if the process is the last time that executed
		    		if(BurstTimes.get(0)==1) {
		    			// removes the first place of array 
		    			BurstTimes.remove(0);
		    		}
		    		else {
			    		// Updates the burst time of the process to be executed 
		    			int newBurstTime=BurstTimes.get(0)-1;
			    		
				    	// Removes the old time
				    	BurstTimes.remove(0);
				    	// Adds the new time 
				    	BurstTimes.add(0,newBurstTime);
		    		}
		    		
			    	// returns the first process from array 
			    	// that has the shortest remaining time first
			        return processes.get(0);
	    			}
	    		else {
	    			return null;
	    		}
    		}
    	}
    }
}




