/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
 **************************************/

package GA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Population {
    private List<Chromosome> population = new ArrayList<>();
    private float fittestFitness = 0;
    private float averageFitness = 0;
    private float worstFitness = 1;
    private Chromosome fittest;

    public void addChromosome(Chromosome chromosome) {
        population.add(chromosome);
    }

    public static Population generateRandom() {
        Population population = new Population();

        for (int i=0; i < GAVariables.POPULATION_MAX; ++i) {
            Chromosome chromosome = Chromosome.generateRandom();
            population.addChromosome(chromosome);
        }
        return population;
    }

    public Population evolve() {
        Population nextGen = new Population();
        if (GAVariables.ELITISM) {
            nextGen.addChromosome(fittest);
        }

        for (int i = GAVariables.getElitism(); i < GAVariables.POPULATION_MAX; ++i) {
            Chromosome c1 = tournamentSelection(this);
            Chromosome c2 = tournamentSelection(this);
            Chromosome cc = crossover(c1,c2);
            nextGen.addChromosome(cc);
        }

        return nextGen;
    }

    public Population mutate() {
        for (int i = 1; i < GAVariables.POPULATION_MAX; ++i) {
            population.get(i).mutate();
        }
        return this;
    }

    public static Chromosome tournamentSelection(Population population) {
        Chromosome fittest = null;
        float fittestFitness = 0;
        List<Integer> alreadyIn = new ArrayList<>();

        for (int i=0; i<GAVariables.TOURNAMENT_SIZE; ++i) {
            boolean found = false;
            while (!found) {
                int random = (int) (Math.random() * population.getPopulation().size());
                if (!alreadyIn.contains(random)) {
                    if (population.getPopulation().get(random).getFitness() > fittestFitness) {
                        fittest = population.getPopulation().get(random);
                        fittestFitness = population.getPopulation().get(random).getFitness();
                    }

                    alreadyIn.add(random);
                    found = true;
                }
            }
        }

        return fittest;
    }

    public static Chromosome crossover(Chromosome c1, Chromosome c2) {
        Chromosome offspring = new Chromosome();
        int cut = 0;

        while ((cut==0) || (cut==c1.getArray().length-2)) {
            cut = (int) (Math.random() * c1.getArray().length);
        }

        offspring.setArray(Chromosome.concat(
                Arrays.copyOfRange(c1.getArray(), 0, cut),
                Arrays.copyOfRange(c2.getArray(), cut, c2.getArray().length)
        ));

        return offspring;
    }

    //Setters
    public void setFittestFitness(float fittestFitness) {
        this.fittestFitness = fittestFitness;
    }
    public void setAverageFitness(float averageFitness) {
        this.averageFitness = averageFitness;
    }
    public void setWorstFitness(float worstFitness) {
        this.worstFitness = worstFitness;
    }

    public void setFittest(Chromosome fittest) {
        this.fittest = fittest;
    }

    //Getters
    public List<Chromosome> getPopulation() {
        return population;
    }
    public float getFittestFitness() {
        return fittestFitness;
    }
    public float getAverageFitness() {
        return averageFitness;
    }
    public float getWorstFitness() {
        return worstFitness;
    }

    public Chromosome getFittest() {
        return fittest;
    }
}
