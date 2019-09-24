package com.xiesangzi.gacourse.ga;


import com.xiesangzi.gacourse.base.Clazz;
import com.xiesangzi.gacourse.base.Course;
import com.xiesangzi.gacourse.base.CourseTable;
import com.xiesangzi.gacourse.base.Room;
import com.xiesangzi.gacourse.base.Teacher;
import com.xiesangzi.gacourse.base.TimesLot;

import java.util.HashMap;

/**
 * Timetable is the main evaluation class for the class scheduler GA.
 *
 * A timetable represents a potential solution in human-readable form, unlike an
 * Individual or a chromosome. This timetable class, then, can read a chromosome
 * and develop a timetable from it, and ultimately can evaluate the timetable
 * for its fitness and number of scheduling clashes.
 *
 * The most important methods in this class are createClazzes and calcClashes.
 *
 * The createClazzes method accepts an Individual (really, a chromosome),
 * unpacks its chromosome, and creates Class objects from the genetic
 * information. Class objects are lightweight; they're just containers for
 * information with getters and setters, but it's more convenient to work with
 * them than with the chromosome directly.
 *
 * The calcClashes method is used by GeneticAlgorithm.calcFitness, and requires
 * that createClazzes has been run first. calcClashes looks at the Class objects
 * created by createClazzes, and figures out how many hard constraints have been
 * violated.
 *
 */
public class Timetable {
    private final HashMap<Integer, Room> rooms;
    private final HashMap<Integer, Teacher> teachers;
    private final HashMap<Integer, Course> courses;
    private final HashMap<Integer, Clazz> groups;
    private final HashMap<Integer, TimesLot> times;
    private CourseTable[] courseTables;

    private int numClazzes = 0;

    /**
     * Initialize new Timetable
     */
    public Timetable() {
        this.rooms = new HashMap<Integer, Room>();
        this.teachers = new HashMap<Integer, Teacher>();
        this.courses = new HashMap<Integer, Course>();
        this.groups = new HashMap<Integer, Clazz>();
        this.times = new HashMap<Integer, TimesLot>();
    }

    /**
     * "Clone" a timetable. We use this before evaluating a timetable so we have
     * a unique container for each set of courseTables created by "createClazzes".
     * Truthfully, that's not entirely necessary (no big deal if we wipe out and
     * reuse the .courseTables property here), but Chapter 6 discusses
     * multi-threading for fitness calculations, and in order to do that we need
     * separate objects so that one thread doesn't step on another thread's
     * toes. So this constructor isn't _entirely_ necessary for Chapter 5, but
     * you'll see it in action in Chapter 6.
     *
     * @param cloneable
     */
    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.teachers = cloneable.getTeachers();
        this.courses = cloneable.getCourses();
        this.groups = cloneable.getGroups();
        this.times = cloneable.getTimes();
    }

    private HashMap<Integer, Clazz> getGroups() {
        return this.groups;
    }

    private HashMap<Integer, TimesLot> getTimes() {
        return this.times;
    }

    private HashMap<Integer, Course> getCourses() {
        return this.courses;
    }

    private HashMap<Integer, Teacher> getTeachers() {
        return this.teachers;
    }

    /**
     * Add new room
     *
     * @param roomId
     * @param roomName
     * @param capacity
     */
    public void addRoom(int roomId, String roomName, int capacity) {
        this.rooms.put(roomId, new Room(roomId, roomName, capacity));
    }

    /**
     * Add new professor
     *
     * @param teacherId
     * @param teacherName
     */
    public void addTeacher(int teacherId, String teacherName) {
        this.teachers.put(teacherId, new Teacher(teacherId, teacherName));
    }

    /**
     * Add new module
     *
     * @param courseId
     * @param courseCode
     * @param courseName
     * @param teacherIds
     */
    public void addCourse(int courseId, String courseCode, String courseName, int[] teacherIds) {
        this.courses.put(courseId, new Course(courseId, courseCode, courseName, teacherIds));
    }

    /**
     * Add new group
     *
     * @param clazzId
     * @param clazzName
     * @param numSize
     * @param courseIds
     */
    public void addClazz(int clazzId, String clazzName, int numSize, int[] courseIds) {
        this.groups.put(clazzId, new Clazz(clazzId, clazzName, numSize, courseIds));
        this.numClazzes = 0;
    }

    /**
     * Add new timeslot
     *
     * @param timeId
     * @param timeName
     */
    public void addTimes(int timeId, String timeName) {
        this.times.put(timeId, new TimesLot(timeId, timeName));
    }

    /**
     * Create courseTables using individual's chromosome
     *
     * One of the two important methods in this class; given a chromosome,
     * unpack it and turn it into an array of Class (with a capital C) objects.
     * These Class objects will later be evaluated by the calcClashes method,
     * which will loop through the Classes and calculate the number of
     * conflicting times, rooms, teachers, etc.
     *
     * While this method is important, it's not really difficult or confusing.
     * Just loop through the chromosome and create Class objects and store them.
     *
     * @param individual
     */
    public void createClazzes(Individual individual) {
        // Init courseTables
        CourseTable[] classes = new CourseTable[this.getNumClazzes()];

        // Get individual's chromosome
        int[] chromosome = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;

        for (Clazz clazz : this.getGroupsAsArray()) {
            int[] moduleIds = clazz.getCourseIds();
            for (int moduleId : moduleIds) {
                classes[classIndex] = new CourseTable(classIndex, clazz.getClazzId(), moduleId);

                // Add timeslot
                classes[classIndex].addTimeId(chromosome[chromosomePos]);
                chromosomePos++;

                // Add room
                classes[classIndex].setRoomId(chromosome[chromosomePos]);
                chromosomePos++;

                // Add professor
                classes[classIndex].addTeacher(chromosome[chromosomePos]);
                chromosomePos++;

                classIndex++;
            }
        }

        this.courseTables = classes;
    }

    /**
     * Get room from roomId
     *
     * @param roomId
     * @return room
     */
    public Room getRoom(int roomId) {
        if (!this.rooms.containsKey(roomId)) {
            System.out.println("Rooms doesn't contain key " + roomId);
        }
        return (Room) this.rooms.get(roomId);
    }

    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    /**
     * Get random room
     *
     * @return room
     */
    public Room getRandomRoom() {
        Object[] roomsArray = this.rooms.values().toArray();
        Room room = (Room) roomsArray[(int) (roomsArray.length * Math.random())];
        return room;
    }

    /**
     * Get professor from professorId
     *
     * @param professorId
     * @return teacher
     */
    public Teacher getProfessor(int professorId) {
        return (Teacher) this.teachers.get(professorId);
    }

    /**
     * Get module from moduleId
     *
     * @param courseId
     * @return course
     */
    public Course getCourse(int courseId) {
        return (Course) this.courses.get(courseId);
    }

    /**
     * Get moduleIds of student group
     *
     * @param groupId
     * @return courseId array
     */
    public int[] getGroupCourses(int groupId) {
        Clazz clazz = (Clazz) this.groups.get(groupId);
        return clazz.getCourseIds();
    }

    /**
     * Get group from groupId
     *
     * @param groupId
     * @return group
     */
    public Clazz getName(int groupId) {
        return (Clazz) this.groups.get(groupId);
    }

    /**
     * Get all student groups
     *
     * @return array of groups
     */
    public Clazz[] getGroupsAsArray() {
        return (Clazz[]) this.groups.values().toArray(new Clazz[this.groups.size()]);
    }

    /**
     * Get timeslot by timeslotId
     *
     * @param timeslotId
     * @return timeslot
     */
    public TimesLot getTimesLot(int timeslotId) {
        return (TimesLot) this.times.get(timeslotId);
    }

    /**
     * Get random timeslotId
     *
     * @return timeslot
     */
    public TimesLot getRandomTimesLot() {
        Object[] timeslotArray = this.times.values().toArray();
        TimesLot timeslot = (TimesLot) timeslotArray[(int) (timeslotArray.length * Math.random())];
        return timeslot;
    }

    /**
     * Get courseTables
     *
     * @return courseTables
     */
    public CourseTable[] getCourseTables() {
        return this.courseTables;
    }

    /**
     * Get number of courseTables that need scheduling
     *
     * @return numClazzes
     */
    public int getNumClazzes() {
        if (this.numClazzes > 0) {
            return this.numClazzes;
        }

        int numClasses = 0;
        Clazz[] clazzes = (Clazz[]) this.groups.values().toArray(new Clazz[this.groups.size()]);
        for (Clazz clazz : clazzes) {
            numClasses += clazz.getCourseIds().length;
        }
        this.numClazzes = numClasses;

        return this.numClazzes;
    }

    /**
     * Calculate the number of clashes between Classes generated by a
     * chromosome.
     *
     * The most important method in this class; look at a candidate timetable
     * and figure out how many constraints are violated.
     *
     * Running this method requires that createClazzes has been run first (in
     * order to populate this.courseTables). The return value of this method is
     * simply the number of constraint violations (conflicting teachers,
     * times, or rooms), and that return value is used by the
     * GeneticAlgorithm.calcFitness method.
     *
     * There's nothing too difficult here either -- loop through this.courseTables,
     * and check constraints against the rest of the this.courseTables.
     *
     * The two inner `for` loops can be combined here as an optimization, but
     * kept separate for clarity. For small values of this.courseTables.length it
     * doesn't make a difference, but for larger values it certainly does.
     *
     * @return numClashes
     */
    public int calcClashes() {
        int clashes = 0;

        for (CourseTable classA : this.courseTables) {
            // Check room capacity
            int roomCapacity = this.getRoom(classA.getRoomId()).getRoomCapacity();
            int groupSize = this.getName(classA.getClazzId()).getNumSize();

            if (roomCapacity < groupSize) {
                clashes++;
            }

            // Check if room is taken
            for (CourseTable classB : this.courseTables) {
                if (classA.getRoomId() == classB.getRoomId() && classA.getTimeId() == classB.getTimeId()
                        && classA.getTableId() != classB.getTableId()) {
                    clashes++;
                    break;
                }
            }

            // Check if professor is available
            for (CourseTable classB : this.courseTables) {
                if (classA.getTeacherId() == classB.getTeacherId() && classA.getTimeId() == classB.getTimeId()
                        && classA.getTableId() != classB.getTableId()) {
                    clashes++;
                    break;
                }
            }
        }

        return clashes;
    }
}