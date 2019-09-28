package com.github.gacourse.core;
/**
 * Simple teacher abstraction.
 * 教师信息
 */
public class Teacher {
    private final int teacherId;
    private final String teacherName;

    /**
     * Initalize new teacher
     * 
     * @param teacherId The ID for this teacher
     * @param teacherName The name of this teacher
     */
    public Teacher(int teacherId, String teacherName){
        this.teacherId = teacherId;
        this.teacherName = teacherName;
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
     * Get teacher's name
     * 
     * @return teacherName
     */
    public String getTeacherName(){
        return this.teacherName;
    }
}
