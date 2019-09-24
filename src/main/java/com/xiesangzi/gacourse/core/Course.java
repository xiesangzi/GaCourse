package com.xiesangzi.gacourse.core;

/**
 * Simple course courseName abstraction, which defines the teachers teaching the courseName.
 * 定义教师授课情况
 */
public class Course {
    private final int courseId;
    private final String courseCode;
    private final String courseName;
    private final int[] teacherIds;
    
    /**
     * Initialize new Module
     * 
     * @param courseId
     * @param courseCode
     * @param courseName
     * @param teacherIds
     */
    public Course(int courseId, String courseCode, String courseName, int[] teacherIds){
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherIds = teacherIds;
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
     * Get courseName code
     * 
     * @return courseCode
     */
    public String getCourseCode(){
        return this.courseCode;
    }
    
    /**
     * Get courseName name
     * 
     * @return moduleName
     */
    public String getCourseName(){
        return this.courseName;
    }
    
    /**
     * Get random teacherId Id
     * 
     * @return teacherId
     */
    public int getRandomTeacherId(){
        int teacherId = teacherIds[(int) (teacherIds.length * Math.random())];
        return teacherId;
    }
}
