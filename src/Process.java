/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabriel
 */
public class Process implements Comparable<Process> {
    private Integer pid;
    private Integer priority;
    private Integer totalTime;
    private Integer remainingTime;
    private Integer insertionTime;
    private Integer quantumSlices;
    private Boolean finished;
	private double avgTime;

    public Process(Integer pid, Integer priority, Integer totalTime, Integer currentTime) {
        this.pid = pid;
        this.priority = priority;
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        this.finished = false;
        this.insertionTime = currentTime;
        this.quantumSlices = 0;
        this.avgTime = 0;
    }

    /**
     * @return the pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @return the totalTime
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     * @return the remainingTime
     */
    public Integer getRemainingTime() {
        return remainingTime;
    }

    /**
     * @return the insertionTime
     */
    public Integer getInsertionTime(){
        return insertionTime;
    }
    
    public void setInsertionTime(Integer insertionTime) {
		this.insertionTime = insertionTime;
	}
    
    public double getAvgTime() {
    	return avgTime;
    }

	public void runProcess(){
    	
        if(remainingTime > 0){
            remainingTime--;
        }
        
        if (remainingTime == 0) {
        	finished = true;
        }
    }

	public Boolean isFinished(){
        return finished;
    }
    
    public void recalcAvgWait(Integer currentTime) {
    	this.quantumSlices++;
    	this.avgTime = (avgTime + (currentTime - insertionTime)) / quantumSlices;
    }

	@Override
	public int compareTo(Process o) {
		
        //System.out.println("Comparing PID " + this.getPid() + " with PID " + o.getPid());

        if (this.priority > o.getPriority()) {
            return 1;
        } else if (this.priority < o.getPriority()) {
            return -1;
        }

        return 0;
	}
}
