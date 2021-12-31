public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess;
    private int process_timer;
    
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
        for (int i = processes.size() - 2; i >= 0; i--)
            processes.set(i + 1, processes.get(i));
        processes.set(0, p);
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if (processes.isEmpty())
            return null;
        else if (currentProcess == null) {
            currentProcess = processes.get(processes.size() - 1);
            process_timer = 1;
        }
        else if (process_timer == quantum || currentProcess.getPCB().getState() == ProcessState.TERMINATED) {
            int pos = processes.indexOf(currentProcess);
            if (pos - 1 >= 0)
                currentProcess = processes.get(pos - 1);
            else
                currentProcess = processes.get(processes.size() - 1);
            process_timer = 1;
        }
        else if (currentProcess.getPCB().getState() == ProcessState.RUNNING)
            process_timer++;
        return currentProcess;
    }
}
