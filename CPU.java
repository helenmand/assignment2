import java.util.ArrayList;

//Edit cause my pc has a different git account for no reason
public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle
    
    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess; //PID of the current process.
    private int previousProcess; //PID of the previous process.
    private Process currentProcessObject; //The current process.
    private Process previousProcessObject; //The previous process.

    private ArrayList<Process> newProcesses; //Processes that have just been loaded in the RAM.
    private ArrayList<Process> rejectedProcesses; //Processes that have failed to be loaded in the RAM.

    private int processesCount; //The number of processes that haven't been terminated.

    private boolean processRunning; //If a process is running it's set to true.
    private int stateType; //Information about the next tick.

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    /*
    This is the program's main function. It initializes all needed variables and calls tick() in a loop
    until all processes have been terminated.
     */
    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */
        currentProcess = 0;
        currentProcessObject = null;
        newProcesses = new ArrayList<>();
        rejectedProcesses = new ArrayList<>();
        processesCount = processes.length;
        processRunning = false;
        stateType = -1;
        while (processesCount > 0) {
            tick();
            clock++;
        }
    }

    /*
    The cpu's resources are managed in this function. Every time it's called, it decides what
    the cpu will do. Past calls might affect feature ones.
     */
    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */

        System.out.print("Tick: " + clock + " -- ");

        //Checks if a process is supposed to arrive now. If it does, it tries to allocate RAM to it.
        for (Process process : processes)
            if (process.getArrivalTime() == clock) {
                 if (mmu.loadProcessIntoRAM(process))
                     newProcesses.add(process);
                 else
                     rejectedProcesses.add(process);
            }

        /*
        stateType = -1: Checks if there are processes in the state NEW. If there are and there is a process running,
                        the stateType gets updated to 0 (in order to stop the current process at the next cycle).
                        If there is no process running, the scheduler gets updated with the new processes.
                        If there are no new processes, then the cpu continues running the current one (if there is a current one)
                        by running checkCurrentProcess().
        stateType = 0: The current process gets interrupted because there are new processes trying to get added in the scheduler.
        stateType = 1: The process starts running.
         */
        switch(stateType) {
            case -1:
                if (newProcesses.size() > 0) {
                    if (!processRunning) {
                        System.out.println("Process " + newProcesses.get(0).getPCB().getPid() + ": New -> Ready");
                        newProcesses.get(0).getPCB().setState(ProcessState.READY, clock);
                        scheduler.addProcess(newProcesses.get(0));
                        newProcesses.remove(0);
                    }
                    else {
                        System.out.println("Waiting (Stopping Process)");
                        stateType = 0;
                    }
                }
                else {
                    previousProcess = currentProcess;
                    previousProcessObject = currentProcessObject;
                    currentProcessObject = scheduler.getNextProcess();
                    if (currentProcessObject != null)
                        checkCurrentProcess();
                    else
                        System.out.println("Waiting");
                }
                break;
            case 0:
                System.out.println("Process " + currentProcess + ": Running -> Ready");
                currentProcessObject.waitInBackground();
                currentProcessObject.getPCB().setState(ProcessState.READY, clock - 1);
                processRunning = false;
                stateType = -1;
                break;
            case 1:
                System.out.println("Process " + currentProcess + ": Ready -> Running");
                currentProcessObject.run();
                currentProcessObject.getPCB().setState(ProcessState.RUNNING, clock);
                processRunning = true;
                stateType = -1;
                break;
        }

        /*At the end of a tick, the array which holds the processes that couldn't be loaded in the RAM gets checked in the event
        that one of these processes can now be loaded (after a process has potentially been terminated).
         */
        for (Process process : rejectedProcesses) {
            if (mmu.loadProcessIntoRAM(process)) {
                newProcesses.add(process);
                rejectedProcesses.remove(process);
            }
        }
    }

    /*
    Calculates the time that the current process has been running. If it's the same as its burstTime then the process
    gets terminated, its allocated RAM is freed and the appropriate variables get updated.
     */
    private void checkBurstTime() {
        int size = currentProcessObject.getPCB().getStartTimes().size();
        int timePassed = 0;
        for (int i = size - 1; i >= 0; i--) {
            if (i == size - 1)
                timePassed += (clock + 1) - (currentProcessObject.getPCB().getStartTimes().get(i) + 1);
            else
                timePassed += currentProcessObject.getPCB().getStopTimes().get(i) - (currentProcessObject.getPCB().getStartTimes().get(i) + 1);
        }
        if (timePassed == currentProcessObject.getBurstTime()) {
            ArrayList<MemorySlot> usedMemorySlots = mmu.getCurrentlyUsMemorySlots();
            for (int i = 0; i < usedMemorySlots.size(); i++) {
                if (usedMemorySlots.get(i).getStart() == currentProcessObject.getMemoryLocation()) {
                    usedMemorySlots.remove(i);
                    for (MemorySlot mem : usedMemorySlots)
                        System.out.println(mem.getStart() + " " + mem.getEnd());
                    break;
                }
            }
            currentProcessObject.getPCB().setState(ProcessState.TERMINATED, clock);
            scheduler.removeProcess(currentProcessObject);
            processesCount--;
            currentProcessObject = null;
            currentProcess = 0;
            processRunning = false;
        }
    }

    /*
    Checks what the condition of the current process is and if it should stop, start or continue running.
    First block: the process continues running.
    Second block: the process starts running (stateType = 1).
    Third block: the process that's running must be switched.
     */
    private void checkCurrentProcess() {
        currentProcess = currentProcessObject.getPCB().getPid();
        if (currentProcess == previousProcess && processRunning) {
            System.out.println("Process " + currentProcess + ": Running");
            checkBurstTime();
        }
        else if (previousProcessObject == null || !processRunning) {
            System.out.println("Waiting (Starting process)");
            stateType = 1;
        }
        else {
            System.out.println("Process " + previousProcess + ": Running -> Ready (Switching)");
            previousProcessObject.waitInBackground();
            previousProcessObject.getPCB().setState(ProcessState.READY, clock);
            stateType = 1;
        }
    }
}
