package com.xiesangzi.gacourse.core;

/**
 * A simple class abstraction -- basically a container for class, clazz, module, teacher, timeslot, and room IDs
 */
public class CourseTable {
    private final int tableId;
    private final int clazzId;
    private final int courseId;
    private int teacherId;
    private int timeId;
    private int roomId;
    
    /**
     * Initialize new Class
     * 
     * @param tableId
     * @param clazzId
     * @param courseId
     */
    public CourseTable(int tableId, int clazzId, int courseId){
        this.tableId = tableId;
        this.courseId = courseId;
        this.clazzId = clazzId;
    }
    
    /**
     * Add teacher to class
     * 
     * @param teacherId
     */
    public void addTeacher(int teacherId){
        this.teacherId = teacherId;
    }
    
    /**
     * Add timeslot to class
     * 
     * @param timeId
     */
    public void addTimeId(int timeId){
        this.timeId = timeId;
    }    
    
    /**
     * Add room to class
     * 
     * @param roomId
     */
    public void setRoomId(int roomId){
        this.roomId = roomId;
    }
    
    /**
     * Get tableId
     * 
     * @return tableId
     */
    public int getTableId(){
        return this.tableId;
    }
    
    /**
     * Get clazzId
     * 
     * @return clazzId
     */
    public int getClazzId(){
        return this.clazzId;
    }
    
    /**
     * Get courseId
     * 
     * @return courseId
     */
    public int getCourseId(){
        return this.courseId;
    }
    
    /**
     * Get teacherId
     * 
     * @return teacherId
     */
    public int getTeacherId(){
        return this.teacherId;
    }
    
    /**
     * Get timeId
     * 
     * @return timeId
     */
    public int getTimeId(){
        return this.timeId;
    }
    
    /**
     * Get roomId
     * 
     * @return roomId
     */
    public int getRoomId(){
        return this.roomId;
    }
}

