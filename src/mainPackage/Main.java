/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package mainPackage;


import GA.GAVariables;
import GA.GeneticAlgorithm;
import data.Lesson;
import data.Teacher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static data.Lesson.Grade.A;
import static data.Lesson.Grade.B;
import static data.Lesson.Grade.C;

public class Main {

    public static Hashtable<String, Teacher> teachers = new Hashtable<>();
    public static Hashtable<Lesson.Grade, Hashtable<String,Lesson>> lessons = new Hashtable<>();

    public static void main(String[] args)
    {
        final String inputLessons="./src/inputFiles/lessons.txt";
        final String inputTeachers="./src/inputFiles/teachers.txt";

        readLessons(inputLessons);
        readTeachers(inputTeachers);
        findMaxLessonsAndTeacherCharacters();
        findTotalHoursToBeTaughtIfOneGroup();

        new GeneticAlgorithm().run();
    }

    private static void readLessons(String inputFile) {
        //Initializations
        lessons.put(A, new Hashtable<>());
        lessons.put(B, new Hashtable<>());
        lessons.put(C, new Hashtable<>());

        GAVariables.maxLessonCode = 0;
        GAVariables.maxTeacherCode = 0;
        GAVariables.setLessonCodeArraySize(0);
        GAVariables.setTeacherCodeArraySize(0);

        File file = new File(inputFile);
        Scanner scanner;

        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                String[] lineArray = line.split("_");

                Lesson newLessonTemp = new Lesson(
                                lineArray[0],
                                lineArray[1],
                                lineArray[2],
                                Integer.parseInt(lineArray[3])
                );
                lessons.get(newLessonTemp.getGrade()).put(
                        newLessonTemp.getCode(),
                        newLessonTemp
                );
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void readTeachers(String inputFile) {
        File file = new File(inputFile);
        Scanner scanner;

        try {
            scanner = new Scanner(file);
            while (scanner.hasNext())
            {
                List<Lesson> subjectsGradeAtemp = new ArrayList<>();
                List<Lesson> subjectsGradeBtemp = new ArrayList<>();
                List<Lesson> subjectsGradeCtemp = new ArrayList<>();

                String line = scanner.nextLine();
                String[] lineArray = line.split(" ");
                String[] subjectsList = lineArray[3].split(",");

                for (String s : subjectsList)
                {
                    if (lessons.get(A).containsKey(s)) {
                        subjectsGradeAtemp.add(lessons.get(A).get(s));
                    }
                    else if (lessons.get(B).containsKey(s)) {
                        subjectsGradeBtemp.add(lessons.get(B).get(s));
                    }
                    else {
                        subjectsGradeCtemp.add(lessons.get(C).get(s));
                    }
                }

                Teacher newTeacher = new Teacher (
                        lineArray[0],
                        (lineArray[1]+" "+lineArray[2]),
                        subjectsGradeAtemp,
                        subjectsGradeBtemp,
                        subjectsGradeCtemp,
                        Integer.parseInt(lineArray[4]),
                        Integer.parseInt(lineArray[5])
                );

                teachers.put(lineArray[0], newTeacher);

                //Adding the teacher to the lessons
                for (Lesson lesson : subjectsGradeAtemp) {
                    lesson.addTeacher(newTeacher);
                }
                for (Lesson lesson : subjectsGradeBtemp) {
                    lesson.addTeacher(newTeacher);
                }
                for (Lesson lesson : subjectsGradeCtemp) {
                    lesson.addTeacher(newTeacher);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void findMaxLessonsAndTeacherCharacters() {
        int lessonMax = 0;
        int teacherMax = 0;
        for (Lesson.Grade grade : Lesson.Grade.values()) {
            for (String lessonCode : lessons.get(grade).keySet()) {
                String lessonName = lessons.get(grade).get(lessonCode).getName();
                if (lessonName.length() > lessonMax) { lessonMax = lessonName.length(); }
            }
        }
        for (String teacherCode : teachers.keySet()) {
            String teacherName = teachers.get(teacherCode).getName();
            if (teacherName.length() > teacherMax) { teacherMax = teacherName.length(); }
        }

        GAVariables.maxLessonNameChars = lessonMax;
        GAVariables.maxTeacherNameChars = teacherMax;
    }

    private static void findTotalHoursToBeTaughtIfOneGroup() {
        int sum = 0;
        for (Lesson.Grade grade : Lesson.Grade.values()) {
            for (String lessonCode : lessons.get(grade).keySet()) {
                sum += lessons.get(grade).get(lessonCode).getHours();
            }
        }
        GAVariables.sumHoursToBeTaughtIfOneGroup = sum;
    }
}
