/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package GA;

public class GAVariables {
    //Genetic Algorithm
    public static int POPULATION_MAX = 250;
    public static int MAX_ITERATIONS = 1500;
    public static float MUTATION_CHANCE = 0.002f;
    public static int TOURNAMENT_SIZE = 5;
    //public static float UNIFORM_RATE = 0.01f;
    public static boolean ELITISM = true;

    public static int getElitism() {
        return (ELITISM)?1:0;
    }

    //Schedule
    public static int NUMBER_OF_GROUPS = 3; //Tmhmata se kathe taksh
    public static int MAX_WORK_HOURS_PER_DAY = 7;
    public static int WORK_DAYS_PER_WEEK = 5;

    public static int getNumberOfSlots() {
        return MAX_WORK_HOURS_PER_DAY * WORK_DAYS_PER_WEEK;
    }

    public static int sumHoursToBeTaughtIfOneGroup;

    //Fitness (update if more fitness parameters are added)
    public static int getFitnessMaxPoints (int totalTeachers, int numberOfTeachers, int totalClasses) {
        return  3 * NUMBER_OF_GROUPS * WORK_DAYS_PER_WEEK * 80 +            //1
                3 * NUMBER_OF_GROUPS * 40 +                                 //3
                numberOfTeachers * 5 + numberOfTeachers * 5 +               //2
                totalClasses * 3 +                                          //4
                totalTeachers * 3 +                                         //5
                sumHoursToBeTaughtIfOneGroup * NUMBER_OF_GROUPS * 80        //6
                ;
    }

    //Data
    public static int maxLessonCode;
    public static int maxTeacherCode;
    public static int lessonCodeArraySize;
    public static int teacherCodeArraySize;

    public static int getSlotSize() {
        return lessonCodeArraySize + teacherCodeArraySize;
    }
    public static void setLessonCodeArraySize(int code) {
        lessonCodeArraySize = Integer.toBinaryString(code).length();
    }

    public static void setTeacherCodeArraySize(int code) {
        teacherCodeArraySize = Integer.toBinaryString(code).length();
    }

    //Max Character
    public static int maxLessonNameChars;
    public static int maxTeacherNameChars;

    public static int getMaxChars() {
        return maxLessonNameChars + maxTeacherNameChars + 3; //3 giati 2 parentheseis + 1 keno
    }
}