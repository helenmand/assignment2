import java.util.ArrayList;
public class SRTF extends Scheduler {

			
    public SRTF() {
        /* TODO: you _may_ need to add some code here */
    	
    	}

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
    	
    	int index=0; // array index
    	boolean found=false; // checks if found the position in array
    	
    	// find the specific index to add the process
    	while(index<processes.size() && !found) {
    		// Checks if the process have shorter burst time 
    		if(processes.get(index).getBurstTime()>p.getBurstTime()) {
    			// add the process
    			processes.add(index,p);
    			// the position in array found
    			found=true;
    		}
    		index+=1;
    	}
    		
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
    	
    	// return the first process from array 
    	// that has the shortest remaining time first
        return processes.get(0);
    }
}




