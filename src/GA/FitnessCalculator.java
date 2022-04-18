/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package GA;

import data.FitnessCalculatorData;
import data.Lesson;
import mainPackage.Main;

import java.util.ArrayList;
import java.util.Set;

import static GA.GAVariables.*;

public class FitnessCalculator {

    public static void assess(Population population) {
        float sum = 0; //for average

        for (int i=0; i < population.getPopulation().size(); ++i) {
            float f = assess(population.getPopulation().get(i));
            sum += f;

            if (f > population.getFittestFitness()) {
                population.setFittestFitness(f);
                population.setFittest(population.getPopulation().get(i));
            }

            if (f < population.getWorstFitness()) {
                population.setWorstFitness(f);
            }
        }
        population.setAverageFitness(sum / population.getPopulation().size());
    }

    private static float assess(Chromosome chromosome) {
        FitnessCalculatorData fcData = new FitnessCalculatorData();


        for (int i=0; i < getNumberOfSlots(); ++i) {
            for (Lesson.Grade grade : Lesson.Grade.values()) {
                for (int j = 0; j < GAVariables.NUMBER_OF_GROUPS; ++j) {
                    byte[] allele = new byte[getSlotSize()];
                    int startPos =
                            i * getSlotSize() * 3 * GAVariables.NUMBER_OF_GROUPS
                            + Lesson.getGradeInt(grade) * getSlotSize() * GAVariables.NUMBER_OF_GROUPS
                            + j * getSlotSize();

                    System.arraycopy(
                            chromosome.getArray(), startPos,
                            allele, 0,
                            getSlotSize()
                    );

                    byte[] lCodeArray = new byte[GAVariables.lessonCodeArraySize];
                    byte[] tCodeArray = new byte[GAVariables.teacherCodeArraySize];

                    System.arraycopy(
                            allele, 0,
                            lCodeArray, 0,
                            GAVariables.lessonCodeArraySize
                    );
                    System.arraycopy(
                            allele, GAVariables.lessonCodeArraySize,
                            tCodeArray, 0,
                            GAVariables.teacherCodeArraySize
                    );

                    String lessonCode = Converters.ByteOperations.byteArrayToString(lCodeArray);
                    String teacherCode = Converters.ByteOperations.byteArrayToString(tCodeArray);

                    processClass(lessonCode, teacherCode, i, grade, j, fcData);
                }
            }
        }

        //Analyze the whole schedule
        processSchedule(fcData);

        //Finishing
        chromosome.setFitness(fcData.getFitness());
        return fcData.getFitness();
    }

    private static void processClass(String lessonCode, String teacherCode,
                                int i, Lesson.Grade grade, int j,
                                FitnessCalculatorData fcData) {
        if (Main.lessons.get(grade).containsKey(lessonCode)
                && Main.teachers.containsKey(teacherCode))
        {
            //boolean array tou an ginontai mathhmata
            fcData.lessonHappened
                    [Lesson.getGradeInt(grade)]
                    [j]
                    [i/GAVariables.MAX_WORK_HOURS_PER_DAY]
                    [i%GAVariables.MAX_WORK_HOURS_PER_DAY] = true;

            //Poses wres didaskei enas kathhghths
            if (fcData.teacherHours.containsKey(teacherCode)) {
                fcData.teacherHours.put(
                        teacherCode,
                        fcData.teacherHours.get(teacherCode)+1
                );
            }
            else {
                fcData.teacherHours.put(teacherCode,1);
            }

            //Wres pou didaskei enas kathhghths
            if (!fcData.teacherHappened.containsKey(teacherCode)) {
                fcData.teacherHappened.put(teacherCode, new ArrayList<>());
            }
            fcData.teacherHappened.get(teacherCode).add(i);

            //Gia omoiomorfes wres enos mathhmatos
            if (!fcData.lessonHoursByDay.get(grade).get(j).containsKey(lessonCode)){
                fcData.lessonHoursByDay.get(grade).get(j)
                        .put(lessonCode, new ArrayList<>());

                for (int k = 0; k < GAVariables.WORK_DAYS_PER_WEEK; ++k) {
                    fcData.lessonHoursByDay.get(grade).get(j).get(lessonCode).add(0);
                }
            }

            fcData.lessonHoursByDay
                    .get(grade)
                    .get(j)
                    .get(lessonCode)
                    .set(
                            i/GAVariables.MAX_WORK_HOURS_PER_DAY,
                            1 + fcData.lessonHoursByDay
                                    .get(grade)
                                    .get(j)
                                    .get(lessonCode)
                                    .get(i/GAVariables.MAX_WORK_HOURS_PER_DAY)
                    );

            if (!fcData.lessonTeacher.get(grade).get(j).containsKey(lessonCode)){
                fcData.lessonTeacher.get(grade).get(j)
                        .put(lessonCode, new ArrayList<>());

            }

            fcData.lessonTeacher
                    .get(grade)
                    .get(j)
                    .get(lessonCode)
                    .add(Main.teachers.get(teacherCode));

        }
        else {
            fcData.lessonHappened
                    [Lesson.getGradeInt(grade)]
                    [j]
                    [i/GAVariables.MAX_WORK_HOURS_PER_DAY]
                    [i%GAVariables.MAX_WORK_HOURS_PER_DAY] = false;
        }
    }

    //Analyse data further
    private static void processSchedule(FitnessCalculatorData fcData) {
        //1. no spaces
        //3. no day variance
        for (Lesson.Grade grade : Lesson.Grade.values()) {
            for (int j = 0; j < GAVariables.NUMBER_OF_GROUPS; ++j)
            {
                int[] daySum = new int[GAVariables.WORK_DAYS_PER_WEEK];

                for (int day = 0; day < GAVariables.WORK_DAYS_PER_WEEK; ++day)
                {
                    boolean spaceHappened=false;
                    boolean classesResumedAfterSpace=false;
                    for (int hour = 0; hour < GAVariables.MAX_WORK_HOURS_PER_DAY; ++hour)
                    {
                        if (!fcData.lessonHappened
                                [Lesson.getGradeInt(grade)]
                                [j]
                                [day]
                                [hour]
                        ) {
                            spaceHappened = true;
                        }
                        else {
                            daySum[day]++;
                            if (spaceHappened) {
                                classesResumedAfterSpace = true;
                            }
                        }
                    }
                    if (!classesResumedAfterSpace){
                        fcData.fAcc += 1 * 80;
                    }
                }

                int maxHours=0;
                int minHours=GAVariables.MAX_WORK_HOURS_PER_DAY+1;

                for (int dd=0; dd<GAVariables.WORK_DAYS_PER_WEEK; ++dd) {
                    if (daySum[dd] > maxHours) {
                        maxHours = daySum[dd];
                    }
                    if (daySum[dd] < minHours) {
                        minHours = daySum[dd];
                    }
                }
                if (!(maxHours-minHours > 1)) {
                    fcData.fAcc += 1 * 40;
                }
            }
        }

        //2. no more than two consecutive + no multiple at the same time
        Set<String> teacherHKeys = fcData.teacherHappened.keySet();

        fcData.teacherHappenedNumber = teacherHKeys.size();

        for (String key : teacherHKeys) {
            boolean samePlaceSameTime=false;
            boolean noBrake=false;
            int prev = -1;
            int prevprev = -1;


            for (int i : fcData.teacherHappened.get(key)) {
                if (i == prev) {
                    samePlaceSameTime=true;
                }
                int day = i/GAVariables.MAX_WORK_HOURS_PER_DAY;
                int hour = i%GAVariables.MAX_WORK_HOURS_PER_DAY;
                int dayPrev = prev/GAVariables.MAX_WORK_HOURS_PER_DAY;
                int hourPrev = prev%GAVariables.MAX_WORK_HOURS_PER_DAY;
                int dayPrevPrev = prevprev/GAVariables.MAX_WORK_HOURS_PER_DAY;
                int hourPrevPrev = prevprev%GAVariables.MAX_WORK_HOURS_PER_DAY;

                if ((day==dayPrev)
                        && (day == dayPrevPrev)
                        && (hour == hourPrev+1)
                        && (hour == hourPrevPrev+2)
                ) {
                    noBrake = true;
                }

                prevprev = prev;
                prev = i;
            }

            if (!samePlaceSameTime) {
                fcData.fAcc += 5 * 1;
            }
            if (!noBrake) {
                fcData.fAcc += 5 * 1;
            }
        }

        //4. no lesson variance in a group
        int totalClasses = 0;

        for (Lesson.Grade grade : Lesson.Grade.values()) {
            for (int j = 0; j < GAVariables.NUMBER_OF_GROUPS; ++j) {
                Set<String> keys = fcData.lessonHoursByDay.get(grade).get(j).keySet();
                totalClasses += keys.size();
                for (String lesson : keys) {
                    int minHours = GAVariables.MAX_WORK_HOURS_PER_DAY;
                    int maxHours = 0;
                    for (int day = 0; day < GAVariables.WORK_DAYS_PER_WEEK; ++day) {
                        int h = fcData.lessonHoursByDay.get(grade).get(j).get(lesson).get(day);
                        if (h < minHours) { minHours = h; }
                        if (h > maxHours) { maxHours = h; }
                    }
                    if (!(maxHours-minHours>1)) {
                        fcData.fAcc += 3 * 1;
                    }
                }
            }
        }
        fcData.totalClasses = totalClasses;


        //5. no teacher hours variance
        int teachersHoursSum = 0;
        for (String teacherCode : Main.teachers.keySet()) {
            if (fcData.teacherHappened.containsKey(teacherCode)) {
                for (int i : fcData.teacherHappened.get(teacherCode)) {
                    teachersHoursSum++;
                }
            }
        }
        int teacherAverageTeachingTime = teachersHoursSum / Main.teachers.keySet().size();

        for (String teacherCode : Main.teachers.keySet()) {
            int t = 0;
            if (fcData.teacherHappened.containsKey(teacherCode)) {
                for (int i : fcData.teacherHappened.get(teacherCode)) {
                    t++;
                }
            }
            if ((t - teachersHoursSum < 6) && (t - teachersHoursSum > -6 )) {
                fcData.fAcc += 3 * 1;
            }
        }

        //6. no lesson left behind
        //Additional to the required
        for (Lesson.Grade grade : Lesson.Grade.values()) {
            for (String lessonCode : Main.lessons.get(grade).keySet()) {
                int hours = Main.lessons.get(grade).get(lessonCode).getHours();
                for (int j=0; j < GAVariables.NUMBER_OF_GROUPS; ++j) {
                    if (fcData.lessonHoursByDay.get(grade).get(j).containsKey(lessonCode)) {
                        int sum = 0;
                        for (int k = 0; k < GAVariables.WORK_DAYS_PER_WEEK; ++k) {
                            sum += fcData.lessonHoursByDay.get(grade).get(j).get(lessonCode).get(k);
                        }
                        int relevant;
                        if (sum < hours) {
                            relevant =  sum;
                        }
                        else {
                            relevant = hours;
                        }
                        fcData.fAcc += relevant * 80;
                    }
                }
            }
        }
        //Add more here, process them at processClass(), hold their data at FitnessCalculatorData, and update
        //GAVariables.getFitnessMaxPoints(...)
    }

}