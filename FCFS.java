
public class FCFS extends Scheduler {

    public FCFS() {
        /* TODO: you _may_ need to add some code here */
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        this.processes.add(p);
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if (processes.size() > 0 &&
           (processes.get(0).getPCB().getState().equals(ProcessState.READY) || processes.get(0).getPCB().getState().equals(ProcessState.RUNNING))) 
        {
            return processes.get(0); // returns a READY/RUNNING process, if the process list is not empty
        }
        return null; // or else, returns null
    }
}
