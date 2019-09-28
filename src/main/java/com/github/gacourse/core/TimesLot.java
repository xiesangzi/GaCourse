package com.github.gacourse.core;
/**
 * Simple timeName abstraction -- just represents a timeName (like "Wed 9:00am-11:00am").
 *  时段表示班级上课在周几的什么时间
 * @author bkanber
 *
 */
public class TimesLot {
    private final int timeId;
    private final String timeName;

    /**
     * Initalize new Timeslot
     * 
     * @param timeId The ID for this timeName
     * @param timeName The timeName being initalized
     */
    public TimesLot(int timeId, String timeName){
        this.timeId = timeId;
        this.timeName = timeName;
    }
    
    /**
     * Returns the timeId
     * 
     * @return timeId
     */
    public int getTimeId(){
        return this.timeId;
    }
    
    /**
     * Returns the timeName
     * 
     * @return timeName
     */
    public String getTimeName(){
        return this.timeName;
    }
}
