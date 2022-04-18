/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package GA;


import java.util.Random;

import static GA.GAVariables.getNumberOfSlots;
import static GA.GAVariables.getSlotSize;

public class Chromosome {
    private byte[] chromosomeArray = new byte[
                (getSlotSize()) * 3 * GAVariables.NUMBER_OF_GROUPS * getNumberOfSlots()
            ];
    private float fitness = 0;

    public static Chromosome generateRandom() {
        Chromosome chromosome = new Chromosome();

        Random random = new Random();

        for (int i=0; i <chromosome.chromosomeArray.length; ++i) {
            if (random.nextDouble() < 0.5) {
                chromosome.chromosomeArray[i] = (byte) 1;
            }
            else {
                chromosome.chromosomeArray[i] = (byte) 0;
            }
        }

        return chromosome;
    }

    public void mutate() {
        Random r = new Random();


        for (int i=0; i < chromosomeArray.length; ++i) {
            float chance = r.nextFloat();

            if (chance <= GAVariables.MUTATION_CHANCE) {
                chromosomeArray[i] = (chromosomeArray[i]==1)?(byte)0:(byte)1;
            }
        }
    }

    public static byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    //Setters
    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
    public void setArray(byte[] chromosomeArray) {
        this.chromosomeArray = chromosomeArray;
    }

    //Getters
    public byte[] getArray() {
        return chromosomeArray;
    }
    public float getFitness() {
        return fitness;
    }
}