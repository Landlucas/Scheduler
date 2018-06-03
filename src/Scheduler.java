
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabriel
 */
public class Scheduler extends Thread{
	PriorityQueue<Process> processes = new PriorityQueue<Process>();
	PriorityQueue<Process> expiredProcesses = new PriorityQueue<Process>();
    private Boolean running = true;
    private Boolean usingQuantum = false;
    private Integer quantum = 0;
    private Integer remainingQtime = 0;
    private Process runningProcess;
    private Integer nextPid = 0;
    private Integer currentTime = 0;
    
    public void addProcess(Process p){
        processes.add(p);
        updateCounter();
        System.out.println("Added new process with PID: " + p.getPid() + " time: " + p.getTotalTime() + " priority: " + p.getPriority());
    }
    
    public Integer getCurrentTime(){
        return currentTime;
    }
    
    private void updateCounter(){
        SISOPInterface.labelProcessCount.setText("Processes Count: " + processes.size());
        SISOPInterface.labelCurrentTime.setText("Current Time: " + currentTime);
    }
    
    public void setQuantum(Integer quantum) {
        this.quantum = quantum;
    }        
    
    public void stopSchedler(){
        running = false;
    }
    
    public Integer nextPid(){
        return nextPid++;
    }
    
    @Override
    public void run() {
        while(running){
            try {
                if(runningProcess == null){
                    for(Process p:processes){
                        if(!p.isFinished()){
                            runningProcess = p;
                        	System.out.println("Running proccess PID = " + runningProcess.getPid() + " with remainingTime = " + runningProcess.getRemainingTime());
                            if (quantum > 0 ) {
                            	usingQuantum = true;
                                remainingQtime = quantum;
                            } else {
                            	usingQuantum = false;
                            }
                            break;
                        }
                    }
                }
                
                if(runningProcess == null){
                    if (usingQuantum && !expiredProcesses.isEmpty()) {
                    	System.out.println("Finished all active processes. Retrieving expired processes.");
                    	processes.addAll(expiredProcesses);
                    	expiredProcesses.removeAll(expiredProcesses);
                    } else {
                        SISOPInterface.outputTextArea.setText("IDLE!");
                    }
                } else {
                    SISOPInterface.outputTextArea
                            .setText("RUNNING PROCESS PID = " 
                            + runningProcess.getPid());
                    
                    SISOPInterface.outputTextArea.append("\n");
                    
                    SISOPInterface.outputTextArea.append("INSERTION TIME = " 
                            + runningProcess.getInsertionTime());
                    
                    SISOPInterface.outputTextArea.append("\n");
                    
                    SISOPInterface.outputTextArea.append("REMAINING TIME = " 
                            + runningProcess.getRemainingTime());
                    runningProcess.runProcess();
                    if (usingQuantum) {
                        remainingQtime--;
                        if(remainingQtime == 0 || runningProcess.isFinished()){
                            if (!runningProcess.isFinished()) expiredProcesses.add(runningProcess);
                            processes.remove(runningProcess);
                            runningProcess = null;                        
                        }
                    	
                    } else {
                        if(runningProcess.isFinished()){
                            processes.remove(runningProcess);
                            runningProcess = null;                        
                        }
                    }
                }
                
                currentTime++;
                updateCounter();
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
