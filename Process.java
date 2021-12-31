
public class Process {
    private ProcessControlBlock pcb;
    private int arrivalTime;
    private int burstTime;
    private int memoryRequirements;

    
    
    public Process(int arrivalTime, int burstTime, int memoryRequirements) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock();
    }
    
    public ProcessControlBlock getPCB() {
        return this.pcb;
    }
   
    public void run() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process starts running */
        
    }
    
    public void waitInBackground() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */
        
    }

    public double getWaitingTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        int sum = 0;
        for(int i=1;i<pcb.getTimeline().size();i++) {
            if(pcb.getTimeline().get(i-1)[1] == 0 && pcb.getTimeline().get(i)[1] == 1) {
                sum = pcb.getTimeline().get(i)[0] - pcb.getTimeline().get(i-1)[0];
            }
        }

        return sum;
    }
    
    public double getResponseTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return pcb.getStartTimes().get(0) - pcb.getStopTimes().get(0);
    }
    
    public double getTurnAroundTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return pcb.getStopTimes().get(0) - pcb.getStopTimes().get(-1);
    }

    public int getBurstTime() {
        return burstTime;
    }

    
}
