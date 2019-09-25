package com.xiesangzi.gacourse.ga;


import com.xiesangzi.gacourse.core.CourseTable;

/**
 * Don't be daunted by the number of classes in this chapter -- most of them are
 * just simple containers for information, and only have a handful of properties
 * with setters and getters.
 *
 * The real stuff happens in the GeneticAlgorithm class and the Timetable class.
 *
 * The Timetable class is what the genetic algorithm is expected to create a
 * valid version of -- meaning, after all is said and done, a chromosome is read
 * into a Timetable class, and the Timetable class creates a nicer, neater
 * representation of the chromosome by turning it into a proper list of Classes
 * with rooms and teachers and whatnot.
 *
 * The Timetable class also understands the problem's Hard Constraints (ie, a
 * teacher can't be in two places simultaneously, or a room can't be used by
 * two classes simultaneously), and so is used by the GeneticAlgorithm's
 * calcFitness class as well.
 *
 * Finally, we overload the Timetable class by entrusting it with the
 * "database information" generated here in initializeTimetable. Normally, that
 * information about what teachers are employed and which classrooms the
 * university has would come from a database, but this isn't a book about
 * databases so we hardcode it.
 *
 * @author bkanber
 *
 */
public class TimetableGA {

    public static void main(String[] args) {
        // Get a Timetable object with all the available information.
        Timetable timetable = initializeTimetable();

        // Initialize GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.9, 2, 5);

        // Initialize population
        Population population = ga.initPopulation(timetable);

        // Evaluate population
        ga.evalPopulation(population, timetable);

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop，
        // 如果有冲突进行交叉、变异
        // 逐代进化
        while (!ga.isTerminationConditionMet(generation, 1000) && !ga.isTerminationConditionMet(population)) {

            // Apply crossover 交叉
            population = ga.crossoverPopulation(population);

            // Apply mutation 变异
            population = ga.mutatePopulation(population, timetable);

            // Evaluate population 种群进化
            ga.evalPopulation(population, timetable);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        timetable.createClazzes(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Clashes: " + timetable.calcClashes());

        // Print classes
        System.out.println();
        CourseTable[] classes = timetable.getCourseTables();
        int classIndex = 1;
        for (CourseTable bestClass : classes) {
            System.out.println("index： " + classIndex + ":");
            System.out.println("课程: " +
                    timetable.getCourse(bestClass.getCourseId()).getCourseName());
            System.out.println("班级: " +
                    timetable.getClazz(bestClass.getClazzId()).getClazzName());
            System.out.println("教室: " +
                    timetable.getRoom(bestClass.getRoomId()).getRoomNumber());
            System.out.println("教师: " +
                    timetable.getTeacher(bestClass.getTeacherId()).getTeacherName());
            System.out.println("时段: " +
                    timetable.getTimesLot(bestClass.getTimeId()).getTimeName());
            System.out.println("-----");
            classIndex++;
        }
    }

    /**
     * Creates a Timetable with all the necessary course information.
     *
     * Normally you'd get this info from a database.
     *
     * @return
     */
    private static Timetable initializeTimetable() {
        // Create timetable
        Timetable timetable = new Timetable();

        // Set up rooms
        //初始化教室
        timetable.addRoom(1, "笃行楼A1", 15);
        timetable.addRoom(2, "笃行楼B1", 30);
        timetable.addRoom(4, "博学楼D1", 20);
        timetable.addRoom(5, "综合楼F1", 25);

        // Set up timeslots
        //初始化时段
        timetable.addTimes(1, "周一 9:00 - 11:00");
        timetable.addTimes(2, "周一 11:00 - 13:00");
        timetable.addTimes(3, "周一 13:00 - 15:00");

        timetable.addTimes(4, "周二 9:00 - 11:00");
        timetable.addTimes(5, "周二 11:00 - 13:00");
        timetable.addTimes(6, "周二 13:00 - 15:00");

        timetable.addTimes(7, "周三 9:00 - 11:00");
        timetable.addTimes(8, "周三 11:00 - 13:00");
        timetable.addTimes(9, "周三 13:00 - 15:00");

        timetable.addTimes(10, "周四 9:00 - 11:00");
        timetable.addTimes(11, "周四 11:00 - 13:00");
        timetable.addTimes(12, "周四 13:00 - 15:00");

        timetable.addTimes(13, "周五 9:00 - 11:00");
        timetable.addTimes(14, "周五 11:00 - 13:00");
        timetable.addTimes(15, "周五 13:00 - 15:00");

        // Set up teachers
        //初始化教师
        timetable.addTeacher(1, "李老师");
        timetable.addTeacher(2, "张老师");
        timetable.addTeacher(3, "谢老师");
        timetable.addTeacher(4, "程老师");

        // Set up courses and define the teachers that teach them
        timetable.addCourse(1, "cs1", "计算机于科学", new int[]{1, 2});
        timetable.addCourse(2, "en1", "英语", new int[]{1, 3});
        timetable.addCourse(3, "ma1", "数学", new int[]{1, 2});
        timetable.addCourse(4, "ph1", "物理", new int[]{3, 4});
        timetable.addCourse(5, "hi1", "历史", new int[]{4});
        timetable.addCourse(6, "dr1", "绘画", new int[]{1, 4});

        // Set up student clazzes and the courses they take.
        timetable.addClazz(1, "18级软件工程", 10, new int[]{1, 3, 4});
        timetable.addClazz(2, "18级电子商务", 30, new int[]{2, 3, 5, 6});
        timetable.addClazz(3, "18级物流管理", 18, new int[]{3, 4, 5});
        timetable.addClazz(4, "18级计算机与科学", 25, new int[]{1, 4});
        return timetable;
    }
}
