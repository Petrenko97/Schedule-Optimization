/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package data;

import GA.GAVariables;
import mainPackage.Main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FitnessCalculatorData { //an instance of this class holds all the data of an FitnessCalculator fitness computation
    public int fAcc = 0;

    public int totalClasses = 0;
    public int teacherHappenedNumber = 0;

    //1 if lesson happened, 0 if not
    public boolean[][][][] lessonHappened = new boolean
            [3]
            [GAVariables.NUMBER_OF_GROUPS]
            [GAVariables.WORK_DAYS_PER_WEEK]
            [GAVariables.MAX_WORK_HOURS_PER_DAY]; //so to reward balance,
                                                  // see hours with no classes tought etc

    //Wres pou xrhsimopoihthhke enas kathhghths
    public Hashtable<String, Integer> teacherHours = new Hashtable<>();

    //Periexei lista me tis wres pou didakse
    //p.x. [2,5,9] an didakse thn deytera tis wres 3,6 kai thn trith thn wra 5
    //epishsan yparxoun entries typou [3,3] paei na pei oti einai se 2 thn idia wra
    public Hashtable<String, List<Integer>> teacherHappened = new Hashtable<>();

    //Apo to parakatw vlepw epishs kai an einai konta sto max hours tou mathhmatos
    public Hashtable<Lesson.Grade, List<Hashtable<String,List<Integer>>>> lessonHoursByDay
            = new Hashtable<>();

    //Use lesson's teacher like:
    //lessonTeacher.get(grade).get(groupNumber).get(lessonCode)
    public Hashtable<Lesson.Grade, List<Hashtable<String,List<Teacher>>>> lessonTeacher = new Hashtable<>();

    public FitnessCalculatorData() {
        lessonTeacher.put(Lesson.Grade.A, new ArrayList<>());
        lessonTeacher.put(Lesson.Grade.B, new ArrayList<>());
        lessonTeacher.put(Lesson.Grade.C, new ArrayList<>());

        lessonHoursByDay.put(Lesson.Grade.A, new ArrayList<>());
        lessonHoursByDay.put(Lesson.Grade.B, new ArrayList<>());
        lessonHoursByDay.put(Lesson.Grade.C, new ArrayList<>());

        for (int i=0; i<GAVariables.NUMBER_OF_GROUPS; ++i) {
            lessonTeacher.get(Lesson.Grade.A).add(new Hashtable<>());
            lessonTeacher.get(Lesson.Grade.B).add(new Hashtable<>());
            lessonTeacher.get(Lesson.Grade.C).add(new Hashtable<>());

            lessonHoursByDay.get(Lesson.Grade.A).add(new Hashtable<>());
            lessonHoursByDay.get(Lesson.Grade.B).add(new Hashtable<>());
            lessonHoursByDay.get(Lesson.Grade.C).add(new Hashtable<>());
        }
    }

    public float getFitness() {
        return (float)fAcc / GAVariables.getFitnessMaxPoints(Main.teachers.size(), teacherHappenedNumber, totalClasses);
    }
}
