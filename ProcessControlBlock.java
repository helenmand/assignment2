import java.util.ArrayList;

public class ProcessControlBlock {
    
    private final int pid;
    private ProcessState state;
    // the following two ArrayLists should record when the process starts/stops
    // for statistical purposes
    private ArrayList<Integer> startTimes; // when the process starts running
    private ArrayList<Integer> stopTimes;  // when the process stops running

    private ArrayList<Integer[]> timeline;
    private int startIndex;
    private int stopIndex;
    private int timelineIndex;
    
    private static int pidTotal= 0;
    
    public ProcessControlBlock() {
        this.state = ProcessState.NEW;
        this.startTimes = new ArrayList<Integer>();
        this.stopTimes = new ArrayList<Integer>();
        /* TODO: you need to add some code here
         * Hint: every process should get a unique PID */
        this.pid = 0; // change this line
        
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
                 stopTimes.add(currentClockTime);
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

    private void updateTimeline() {
        while(startIndex<startTimes.size() && stopIndex<stopTimes.size()) {
            if(startTimes.get(startIndex) < stopTimes.get(stopIndex)) {
                timeline.set(timelineIndex, new Integer[] {startTimes.get(startIndex), 1});

                startIndex++;
                timelineIndex++;
            } else {
                timeline.set(timelineIndex, new Integer[] {stopTimes.get(startIndex), 0});

                stopIndex++;
                timelineIndex++;
            }
        }
    }

    public ArrayList<Integer[]> getTimeline() {
        updateTimeline();
        return timeline;
    }
    
}
