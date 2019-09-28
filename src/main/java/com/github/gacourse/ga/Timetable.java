package com.github.gacourse.ga;


import com.github.gacourse.core.Clazz;
import com.github.gacourse.core.Course;
import com.github.gacourse.core.CourseTable;
import com.github.gacourse.core.Room;
import com.github.gacourse.core.Teacher;
import com.github.gacourse.core.TimesLot;

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
    private final HashMap<Integer, Clazz> clazzes;
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
        this.clazzes = new HashMap<Integer, Clazz>();
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
        this.clazzes = cloneable.getClazzes();
        this.times = cloneable.getTimes();
    }

    private HashMap<Integer, Clazz> getClazzes() {
        return this.clazzes;
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
     * Add new teacher
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
     * Add new Clazz
     *
     * @param clazzId
     * @param clazzName
     * @param numSize
     * @param courseIds
     */
    public void addClazz(int clazzId, String clazzName, int numSize, int[] courseIds) {
        this.clazzes.put(clazzId, new Clazz(clazzId, clazzName, numSize, courseIds));
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
        CourseTable[] tables = new CourseTable[this.getNumClazzes()];

        // Get individual's chromosome
        int[] chromosome = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;

        for (Clazz clazz : this.getClazzesAsArray()) {
            int[] courseIds = clazz.getCourseIds();
            for (int courseId : courseIds) {
                tables[classIndex] = new CourseTable(classIndex, clazz.getClazzId(), courseId);

                // Add timeslot
                tables[classIndex].addTimeId(chromosome[chromosomePos]);
                chromosomePos++;

                // Add room
                tables[classIndex].setRoomId(chromosome[chromosomePos]);
                chromosomePos++;

                // Add teacher
                tables[classIndex].addTeacher(chromosome[chromosomePos]);
                chromosomePos++;

                classIndex++;
            }
        }

        this.courseTables = tables;
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
     * Get teacher from teacherId
     *
     * @param teacherId
     * @return teacher
     */
    public Teacher getTeacher(int teacherId) {
        return (Teacher) this.teachers.get(teacherId);
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
     * Get moduleIds of student clazz
     *
     * @param clazzId
     * @return courseId array
     */
    public int[] getClazzCourses(int clazzId) {
        Clazz clazz = (Clazz) this.clazzes.get(clazzId);
        return clazz.getCourseIds();
    }

    /**
     * Get clazz from clazzId
     *
     * @param clazzId
     * @return clazz
     */
    public Clazz getClazz(int clazzId) {
        return (Clazz) this.clazzes.get(clazzId);
    }

    /**
     * Get all student clazzes
     *
     * @return array of clazzes
     */
    public Clazz[] getClazzesAsArray() {
        return (Clazz[]) this.clazzes.values().toArray(new Clazz[this.clazzes.size()]);
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
        Clazz[] clazzes = (Clazz[]) this.clazzes.values().toArray(new Clazz[this.clazzes.size()]);
        for (Clazz clazz : clazzes) {
            numClasses += clazz.getCourseIds().length;
        }
        this.numClazzes = numClasses;

        return this.numClazzes;
    }

    /**
     * Calculate the number of clashes between Classes generated by a
     * chromosome.计算冲突
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
            int clazzSize = this.getClazz(classA.getClazzId()).getNumSize();

            if (roomCapacity < clazzSize) {
                clashes++;
            }

            // Check if room is taken
            for (CourseTable item : this.courseTables) {
                if (classA.getRoomId() == item.getRoomId() && classA.getTimeId() == item.getTimeId()
                        && classA.getTableId() != item.getTableId()) {
                    clashes++;
                    break;
                }
            }

            // Check if teacher is available
            for (CourseTable item : this.courseTables) {
                if (classA.getTeacherId() == item.getTeacherId() && classA.getTimeId() == item.getTimeId()
                        && classA.getTableId() != item.getTableId()) {
                    clashes++;
                    break;
                }
            }
        }

        return clashes;
    }
}