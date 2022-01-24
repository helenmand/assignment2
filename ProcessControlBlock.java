import java.util.ArrayList;

public class ProcessControlBlock {
    
    private final int pid;
    private ProcessState state;
    // the following two ArrayLists should record when the process starts/stops
    // for statistical purposes
    private ArrayList<Integer> startTimes; // when the process starts running
    private ArrayList<Integer> stopTimes;  // when the process stops running
    private int initialReadyTime;
    
    private static int pidTotal= 0;
    
    public ProcessControlBlock() {
        this.state = ProcessState.NEW;
        this.startTimes = new ArrayList<Integer>();
        this.stopTimes = new ArrayList<Integer>();
        this.initialReadyTime = -1;
        /* TODO: you need to add some code here
         * Hint: every process should get a unique PID */
        this.pid = createID(); // change this line
        this.pidTotal += 1;
    }


    public ProcessState getState() {
        return this.state;
    }
    
    public void setState(ProcessState state, int currentClockTime) {
        /* TODO: you need to add some code here
         * Hint: update this.state, but also include currentClockTime
         * in startTimes/stopTimes */

         this.state = state;

         switch (state) {
            case READY:
                 if(initialReadyTime == -1) {
                    initialReadyTime = currentClockTime;
                 } else {
                    stopTimes.add(currentClockTime);
                 }
                 break;
            case RUNNING:
                startTimes.add(currentClockTime);
                break;
            case TERMINATED:
                stopTimes.add(currentClockTime);
                break;
            
         }
        
    }
    
    public int getPid() { 
        return this.pid;
    }
    
    public ArrayList<Integer> getStartTimes() {
        return startTimes;
    }
    
    public ArrayList<Integer> getStopTimes() {
        return stopTimes;
    }

    public int getInitialReadyTime() {
        return initialReadyTime;
    }

    private int createID(){
        //return (this.hashCode() % 100000 > 0) ? this.hashCode() % 100000 : (this.hashCode() % 100000)*(-1);

        int id = this.hashCode() % 100000;

        return (id < 0) ? id*(-1) : id;
    }

}
