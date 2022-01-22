import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess; //PID of the current process.
    private int previousProcess; //PID of the previous process.
    private Process currentProcessObject; //The current process.
    private Process previousProcessObject; //The previous process.

    private ArrayList<Process> ProcessesToBeLoaded; //Processes that are ready to be loaded into the RAM.
    private ArrayList<Process> rejectedProcesses; //Processes that have failed to be loaded into the RAM.

    private int processesCount; //The number of processes that haven't been terminated.

    private boolean processRunning; //If a process is running it's set to true.
    private boolean processTerminated; //If a process just terminated it's set to true.
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
        ProcessesToBeLoaded = new ArrayList<>();
        rejectedProcesses = new ArrayList<>();
        processesCount = processes.length;
        processRunning = false;
        processTerminated = false;
        stateType = 1;
        while (processesCount > 0) {
            tick();
            clock++;
        }
    }

    /*
    The cpu's resources are managed in this function. Every time it's called, it decides what
    the cpu will do.

    !!!!!HOW IT WORKS!!!!!
    Loading a process into RAM - 1 tick
    Getting a process from New to Ready - 1 tick
    Getting a process from Ready to Running - 2 ticks
    Getting a process from Running to Ready - 2 ticks
    Switching two processes (ex: Process 1 - Running -> Ready and Process 2 - Ready -> Running) - 2 ticks
    Running a process - 1 tick (obviously)
    Terminating a process - !!0 ticks!! (when the process runs its last tick the termination will happen on that last tick)
    !!NOTE!!
    Even if a process fails to load into RAM, the failed attempt will still take 1 tick.
     */
    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */

        System.out.print("Tick " + clock + " -- ");

        /*
        Processes that are ready to be loaded get added in the ProcessesToBeLoaded array.
        All rejectedProcesses get added as well when a process gets terminated.
         */
        for (Process process : processes)
            if (process.getArrivalTime() == clock)
                ProcessesToBeLoaded.add(process);
        if (processTerminated) {
            ProcessesToBeLoaded.addAll(rejectedProcesses);
            rejectedProcesses.clear();
            processTerminated = false;
        }

        /*
        stateType = 0: A process got RAM allocated to it so its state gets updated from NEW to READY.
        stateType = 1: Checks if there are processes that require loading into the RAM. If there are and there is a process running,
                        the stateType gets updated to 2 (in order to stop the current process at the next cycle).
                        If there is no process running, RAM tries to get allocated to a process that requires loading using
                        the allocateRAM() function.
                        If there are no new processes, then the cpu continues running the current one (if there is a current one)
                        by running checkCurrentProcess().
        stateType = 2: The current process gets interrupted because there are new processes trying to get loaded and added in the scheduler.
        stateType = 3: The process starts running.
         */
        switch(stateType) {
            case 0:
                System.out.println("Process " + ProcessesToBeLoaded.get(0).getPCB().getPid() + ": New -> Ready");
                ProcessesToBeLoaded.get(0).getPCB().setState(ProcessState.READY, clock);
                scheduler.addProcess(ProcessesToBeLoaded.get(0));
                ProcessesToBeLoaded.remove(0);
                stateType = 1;
                break;
            case 1:
                if (ProcessesToBeLoaded.size() > 0) {
                    if (!processRunning) {
                        if (allocateRAM(ProcessesToBeLoaded.get(0)))
                            stateType = 0;
                        else
                            ProcessesToBeLoaded.remove(0);
                    }
                    else {
                        System.out.println("Waiting (Stopping Process)");
                        stateType = 2;
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
            case 2:
                System.out.println("Process " + currentProcess + ": Running -> Ready");
                currentProcessObject.setClock(clock - 1);
                currentProcessObject.waitInBackground();
                processRunning = false;
                stateType = 1;
                break;
            case 3:
                System.out.println("Process " + currentProcess + ": Ready -> Running");
                currentProcessObject.setClock(clock);
                currentProcessObject.run();
                processRunning = true;
                stateType = 1;
                break;
        }
    }

    /*
    Function that tries to allocate RAM to a process. At first, it tries to allocate RAM to it using the loadProcessIntoRAM
    function. If the allocation wasn't successful, checks if the block sizes are enough to hold the process.
    If they're not, it gets discarded.
    Otherwise, it gets added to the rejectedProcesses in order to get another chance in the future.
    It returns true if the allocation was successful and false if not.
     */
    private boolean allocateRAM(Process process) {
        boolean returnValue = false;
        if (mmu.loadProcessIntoRAM(process)) {
            returnValue = true;
            System.out.println("Process " + process.getPCB().getPid() + " loaded");
        }
        else {
            boolean flag = false;
            int[] blockSizes = mmu.getAvailableBlockSizes();
            for (int blockSize : blockSizes)
                if (blockSize >= process.getMemoryRequirements()) {
                    flag = true;
                    break;
                }
            if (flag) {
                rejectedProcesses.add(process);
                System.out.println("Process " + process.getPCB().getPid() + " could not be loaded");
            }
            else {
                processesCount--;
                System.out.println("Not enough total memory for process " + process.getPCB().getPid());
            }
        }
        return returnValue;
    }

    /*
    Checks what the condition of the current process is and if it should stop, start or continue running.
    First block: the process continues running.
    Second block: the process starts running (stateType = 3).
    Third block: the process that's running must be switched.
     */
    private void checkCurrentProcess() {
        currentProcess = currentProcessObject.getPCB().getPid();
        if (currentProcess == previousProcess && processRunning) {
            System.out.print("Process " + currentProcess + ": Running");
            checkBurstTime();
        }
        else if (previousProcessObject == null || !processRunning) {
            System.out.println("Waiting (Starting process)");
            stateType = 3;
        }
        else {
            System.out.println("Process " + previousProcess + ": Running -> Ready (Switching)");
            previousProcessObject.setClock(clock);
            previousProcessObject.waitInBackground();
            stateType = 3;
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
            for (int i = 0; i < usedMemorySlots.size(); i++)
                if (usedMemorySlots.get(i).getStart() == currentProcessObject.getMemoryLocation()) {
                    usedMemorySlots.remove(i);
                    break;
                }
            System.out.println(" and Terminated");
            currentProcessObject.getPCB().setState(ProcessState.TERMINATED, clock);
            scheduler.removeProcess(currentProcessObject);
            processesCount--;
            currentProcessObject = null;
            currentProcess = 0;
            processRunning = false;
            processTerminated = true;
        }
        else
            System.out.println();
    }
}