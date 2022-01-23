
public class Process {
    private ProcessControlBlock pcb;
    private int arrivalTime;
    private int burstTime;
    private int memoryRequirements;
    private int memoryLocation=-1;

    private int clock;

    
    
    public Process(int arrivalTime, int burstTime, int memoryRequirements) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock();
    }

    //testing
    public Process(int arrivalTime, int burstTime, int memoryRequirements, int pid) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock(pid);
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public int getMemoryRequirements() {
    	return memoryRequirements;
    }
    
    public int getMemoryLocation() {
    	return memoryLocation;
    }
    
    public void setMemoryLocation(int location) {
    	memoryLocation=location;
    }
    
    public ProcessControlBlock getPCB() {
        return this.pcb;
    }
   
    public void run() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process starts running */
        pcb.setState(ProcessState.RUNNING, clock);
        
    }
    
    public void waitInBackground() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */
        pcb.setState(ProcessState.READY, clock);
        
    }

    public double getWaitingTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        int sum = pcb.getStartTimes().get(0)+1 - pcb.getInitialReadyTime();
        for(int i=0;i<pcb.getStartTimes().size()-1 && i<pcb.getStopTimes().size();i++) {
            sum += pcb.getStartTimes().get(i+1)+1 - pcb.getStopTimes().get(i);
        }
        return sum;
    }
    
    public double getResponseTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return pcb.getStartTimes().get(0) - pcb.getInitialReadyTime();
    }
    
    public double getTurnAroundTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return pcb.getStopTimes().get(pcb.getStopTimes().size()-1) - pcb.getInitialReadyTime() + 1;
    }

    

    
}
