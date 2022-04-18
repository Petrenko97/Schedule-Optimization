/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package GA;

import data.Lesson;
import mainPackage.Main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static GA.GAVariables.getNumberOfSlots;
import static GA.GAVariables.getSlotSize;

public class GeneticAlgorithm {
    private int iterations = 0;
    final String output="./src/outputFiles/schedule.txt";
    PrintWriter writer;

    public GeneticAlgorithm() {
        try {
            writer = new PrintWriter(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Population population = Population.generateRandom();
        iterate(population);
        writer.close();
    }

    private void iterate(Population population) {

        FitnessCalculator.assess(population);
        if (iterations%50 == 0) { printPopulationData(population); }

        if ((population.getFittestFitness() == 1) || (iterations == GAVariables.MAX_ITERATIONS)) {
            printPopulation(population);
            return; //Finished
        }

        iterations++;
        iterate(population.evolve().mutate());
    }

    private void printPopulationData(Population population) {
        System.out.println("Generation " + iterations + ": "
                + "Best " + population.getFittestFitness()
                + " (Average " + population.getAverageFitness() + ")"
                + " (Worst " + population.getWorstFitness() + ")");
    }

    private void printPopulation(Population population) {

        writer.println("[Best of generation " + iterations + ": fitness="
                + population.getFittestFitness() + "]\n");

        Chromosome chromosome = population.getFittest();

        //grade->group->day (lessonCode)
        List<List<List<List<String>>>> lessonsPrint = new ArrayList<>();
        //grade->group->day->teacherCode
        List<List<List<List<String>>>> teachersPrint = new ArrayList<>();

        for (Lesson.Grade grade : Lesson.Grade.values()) {
            lessonsPrint.add(Lesson.getGradeInt(grade), new ArrayList<>());
            teachersPrint.add(Lesson.getGradeInt(grade), new ArrayList<>());

            for (int j=0; j < GAVariables.NUMBER_OF_GROUPS; ++j) {
                lessonsPrint.get(Lesson.getGradeInt(grade)).add(j, new ArrayList<>());
                teachersPrint.get(Lesson.getGradeInt(grade)).add(j, new ArrayList<>());

                for (int day=0; day < GAVariables.WORK_DAYS_PER_WEEK; ++day) {
                    lessonsPrint.get(Lesson.getGradeInt(grade)).get(j).add(day, new ArrayList<>());
                    teachersPrint.get(Lesson.getGradeInt(grade)).get(j).add(day, new ArrayList<>());
                }
            }
        }

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
                    int day  = i/GAVariables.MAX_WORK_HOURS_PER_DAY;
                    //int hour = i%GAVariables.MAX_WORK_HOURS_PER_DAY;

                    if (
                            (Main.lessons.get(grade).containsKey(lessonCode))
                            && Main.teachers.containsKey(teacherCode)
                    ) {
                        lessonsPrint.get(Lesson.getGradeInt(grade)).get(j).get(day).add(Main.lessons.get(grade).get(lessonCode).getName());
                        teachersPrint.get(Lesson.getGradeInt(grade)).get(j).get(day).add(Main.teachers.get(teacherCode).getName());
                    }
                    else {
                        lessonsPrint.get(Lesson.getGradeInt(grade)).get(j).get(day).add("-");
                        teachersPrint.get(Lesson.getGradeInt(grade)).get(j).get(day).add("-");
                    }

                }
            }
        }

        final String line1 = "|"
                     + getWithRestBlank("Δευτέρα") + "|"
                     + getWithRestBlank("Τρίτη") + "|"
                     + getWithRestBlank("Τετάρτη") + "|"
                     + getWithRestBlank("Πέμπτη") + "|"
                     + getWithRestBlank("Παρασκευή");

        final String line2 = "|"
                           + getSeperator() + "|"
                           + getSeperator() + "|"
                           + getSeperator() + "|"
                           + getSeperator() + "|"
                           + getSeperator();

        for (Lesson.Grade grade : Lesson.Grade.values()) {
            for (int j = 0; j < GAVariables.NUMBER_OF_GROUPS; ++j) {
                writer.println(grade.toString() + (j+1) + ":");
                writer.println(line1);
                writer.println(line2);

                for (int hours = 0; hours < GAVariables.MAX_WORK_HOURS_PER_DAY; ++hours) {
                    StringBuilder sb = new StringBuilder("");
                    for (int day = 0; day < GAVariables.WORK_DAYS_PER_WEEK; ++day) {
                        String lesson = lessonsPrint.get(Lesson.getGradeInt(grade)).get(j).get(day).get(hours);
                        String teacher = teachersPrint.get(Lesson.getGradeInt(grade)).get(j).get(day).get(hours);

                        String both = "|" + getWithRestBlank(lesson + " (" + teacher + ")");
                        sb.append(both);
                    }
                    writer.println(sb.toString());
                }
                writer.println();
            }
        }
    }

    private static String getWithRestBlank(String word) {
        int b = GAVariables.getMaxChars();
        StringBuilder sb = new StringBuilder(word);
        for (int i = word.length(); i < b; ++i) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private static String getSeperator() {
        int b = GAVariables.getMaxChars();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b; ++i) {
            sb.append("-");
        }
        return sb.toString();
    }
}
