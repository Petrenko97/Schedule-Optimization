/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package data;

import GA.GAVariables;
import mainPackage.Main;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import static data.Lesson.Grade.A;
import static data.Lesson.Grade.B;
import static data.Lesson.Grade.C;

public class Teacher {
    private String code;
    private byte[] codeBinary;
    private String name;
    private int maxPerDay;
    private int maxPerWeek;
    private Hashtable<Lesson.Grade, List<Lesson>> subjects = new Hashtable<>();

    public Teacher(String code, String name, List<Lesson> subjectsGradeA,
                   List<Lesson> subjectsGradeB, List<Lesson> subjectsGradeC,
                   int maxPerDay, int maxPerWeek)
    {
        this.code = code;
        this.name = name;
        this.maxPerDay = maxPerDay;
        this.maxPerWeek = maxPerWeek;

        this.subjects.put(A, subjectsGradeA);
        this.subjects.put(B, subjectsGradeB);
        this.subjects.put(C, subjectsGradeC);

        int codeInt = Integer.parseInt(code);
        if ((codeInt+1) > GAVariables.maxTeacherCode) {
            GAVariables.maxTeacherCode = codeInt+1;
            GAVariables.setTeacherCodeArraySize(codeInt+1);
        }
    }

    public static byte[] pickRandom(byte[] lessonArrayCode, Lesson.Grade grade) {
        String lessonCode = Converters.ByteOperations.byteArrayToString(lessonArrayCode);

        Lesson lesson = Main.lessons.get(grade).get(lessonCode);

        //Main.teachers keys in an array
        List<Teacher> availableTeachers = lesson.getTeachers();

        int random = new Random().nextInt(availableTeachers.size());
        return availableTeachers.get(random).getCodeBinary();
    }

    //Getters
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public int getMaxPerDay() {
        return maxPerDay;
    }
    public int getMaxPerWeek() {
        return maxPerWeek;
    }
    public List<Lesson> getSubjects(Lesson.Grade grade) {
        return subjects.get(grade);
    }

    public byte[] getCodeBinary() {
        if (codeBinary != null) { return codeBinary; }

        return Converters.ByteOperations.stringToByteArray(code, GAVariables.teacherCodeArraySize);
    }

    public static String getCodeFromBinary(byte[] code) {
        return new String(code);
    }
}
