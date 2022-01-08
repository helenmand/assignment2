import java.util.ArrayList;

public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess;
    private int process_timer = 0;
    private final ArrayList<Process> ReadyQueue = new ArrayList<>();

    public RoundRobin() {
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */
    }
    
    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        processes.add(p);
        ReadyQueue.add(p);
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if (processes.isEmpty())
            return null;
        if (currentProcess == null || (process_timer >= quantum && processes.size() != 1) || currentProcess.getPCB().getState() == ProcessState.TERMINATED) {
            if (currentProcess != null && currentProcess.getPCB().getState() != ProcessState.TERMINATED)
                ReadyQueue.add(currentProcess);
            currentProcess = ReadyQueue.get(0);
            ReadyQueue.remove(0);
            process_timer = 0;
        }
        else if (currentProcess.getPCB().getState() == ProcessState.RUNNING)
            process_timer++;
        else if (currentProcess.getPCB().getState() != ProcessState.READY)
            return null;
        return currentProcess;
    }
}
