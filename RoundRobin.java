import java.util.ArrayList;

public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess; //The process that's currently running.
    private int process_timer = 0; //Quantum timer.
    private final ArrayList<Process> ReadyQueue = new ArrayList<>(); //Processes that wait their turn to run.

    public RoundRobin() {
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */
    }
    
    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    /*
    Called when a new process must be added to the scheduler.
    The process gets added to the processes and the ReadyQueue arrays.
     */
    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        processes.add(p);
        ReadyQueue.add(p);
    }

    /*
    Decides which process should run next.
    If there are no processes then it returns null.
    If there is no process running, the quantum timer reaches the quantum value or the current process is terminated,
    then the process that's next in queue starts. If the quantum timer reached its value then the process that stopped
    is added to the ReadyQueue.
    If the process is in a RUNNING state the timer advances.
     */
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
