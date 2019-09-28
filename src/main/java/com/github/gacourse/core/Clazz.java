package com.github.gacourse.core;

/**
 * A simple "clazz-of-students" abstraction. Defines the modules that the clazz is enrolled in.
 * 定义学生分组信息
 */
public class Clazz {
    private final int clazzId;
    private final String clazzName;
    private final int numSize;
    private final int[] courseIds;

    /**
     * Initialize Clazz
     *
     * @param clazzId
     * @param clazzName
     * @param numSize
     * @param courseIds
     */
    public Clazz(int clazzId, String clazzName, int numSize, int[] courseIds) {
        this.clazzId = clazzId;
        this.clazzName = clazzName;
        this.numSize = numSize;
        this.courseIds = courseIds;
    }

    /**
     * Get clazzId
     *
     * @return clazzId
     */
    public int getClazzId() {
        return this.clazzId;
    }


    public String getClazzName() {
        return clazzName;
    }

    /**
     * Get numSize
     *
     * @return numSize
     */
    public int getNumSize() {
        return this.numSize;
    }

    /**
     * Get array of clazz's courseIds
     *
     * @return courseIds
     */
    public int[] getCourseIds() {
        return this.courseIds;
    }
}
