import java.util.ArrayList;

//Edit cause my pc has a different git account for no reason
public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle
    
    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;
    private int previousProcess;
    private Process currentProcessObject;
    private Process previousProcessObject;

    private ArrayList<Process> newProcesseses;
    private ArrayList<Process> rejectedProcesses;

    private int processesCount;

    private boolean processRunning;
    private int stateType;

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }
    
    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */
        currentProcess = 0;
        currentProcessObject = null;
        newProcesseses = new ArrayList<>();
        rejectedProcesses = new ArrayList<>();
        processesCount = processes.length;
        processRunning = false;
        stateType = -1;
        while (processesCount > 0) {
            tick();
            clock++;
        }
    }
    
    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */
        System.out.print("Tick: " + clock + " -- ");
        for (Process process : processes)
            if (process.getArrivalTime() == clock) {
                 scheduler.addProcess(process);
                 if (mmu.loadProcessIntoRAM(process))
                    newProcesseses.add(process);
                 else
                    rejectedProcesses.add(process);
            }
        switch(stateType) {
            case -1:
                if (newProcesseses.size() > 0) {
                    if (!processRunning) {
                        System.out.println("Process " + newProcesseses.get(0).getPCB().getPid() + ": New -> Ready");
                        newProcesseses.get(0).getPCB().setState(ProcessState.READY, clock);
                        newProcesseses.remove(0);
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
        for (Process process : rejectedProcesses) {
            if (mmu.loadProcessIntoRAM(process)) {
                newProcesseses.add(process);
                rejectedProcesses.remove(process);
            }
        }
    }

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
            System.out.println("Process " + currentProcess + ": Running -> Terminated");
            ArrayList<MemorySlot> usedMemorySlots = mmu.getCurrentlyUsMemorySlots();
            for (int i = 0; i < usedMemorySlots.size(); i++)
                if (usedMemorySlots.get(i).getStart() == currentProcessObject.getMemoryLocation()) {
                    usedMemorySlots.remove(i);
                    break;
                }
            currentProcessObject.getPCB().setState(ProcessState.TERMINATED, clock);
            scheduler.removeProcess(currentProcessObject);
            processesCount--;
            currentProcessObject = null;
            currentProcess = 0;
            processRunning = false;
        }
    }

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
            System.out.println("Process " + previousProcess + ": Running -> Ready");
            previousProcessObject.waitInBackground();
            previousProcessObject.getPCB().setState(ProcessState.READY, clock);
            stateType = 1;
        }
    }
}
