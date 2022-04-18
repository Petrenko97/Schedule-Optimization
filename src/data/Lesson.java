/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package data;

import GA.GAVariables;
import mainPackage.Main;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

import static data.Lesson.Grade.A;
import static data.Lesson.Grade.B;
import static data.Lesson.Grade.C;

public class Lesson {

    public enum Grade { A, B, C }
    private String code;
    private byte[] codeBinary; //The code in Binary form
    private String name;
    private Grade grade;
    private int hours;
    private List<Teacher> teachers; //Teaching this lesson

    public Lesson(String code, String name, String grade, int hours){
        this.code=code;
        this.name=name;
        this.hours=hours;

        switch (grade) {
            case "Α":
                this.grade = A;
                break;
            case "Β":
                this.grade = B;
                break;
            case "Γ":
                this.grade = C;
                break;
        }

        int codeInt = Integer.parseInt(code);
        if ((codeInt+1) > GAVariables.maxLessonCode) {
            GAVariables.maxLessonCode = codeInt+1; //+1 because for at least one number for "no class taken" slot
            GAVariables.setLessonCodeArraySize(codeInt+1);
        }

        teachers = new ArrayList<>();
    }

    public static byte[] pickRandom(Grade grade) {
        Hashtable<String, Lesson> lessonsForThatGrade = Main.lessons.get(grade);

        //lessonsForThatGrade keys in an array
        String[] keys = lessonsForThatGrade.keySet().toArray(
                new String[lessonsForThatGrade.keySet().size()]
        );

        int random = new Random().nextInt(keys.length);
        return lessonsForThatGrade.get(keys[random]).getCodeBinary();
    }

    public static int getGradeInt(Grade grade) {
        switch (grade) {
            case A:
                return 0;
            case B:
                return 1;
            case C:
                return 2;
        }
        return 0; //unreachable
    }

    //Setters
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    //Getters
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public Grade getGrade() {
        return grade;
    }
    public int getHours() {
        return hours;
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public byte[] getCodeBinary() {
        if (codeBinary != null) { return codeBinary; }

        return Converters.ByteOperations.stringToByteArray(code, GAVariables.lessonCodeArraySize);
    }

    public static String getCodeFromBinary(byte[] code) {
        return new String(code);
    }
}
