import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.lang.Math;
import java.util.Timer;
public class GeneticAlgo {
    public static void main(String[] args) {
        int n = getN();
        while (n != -1) {
            long startTime = System.currentTimeMillis();
            int iteration = 0; // num times through loop
            int bestFitness = n * (n - 1) / 2; // max numbers of pairs for n objects

            Random random = new Random();
            ArrayList<Integer[]> population = new ArrayList<>();
            ArrayList<Integer[]> maxList = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                population.add(new Integer[n]);
                for (int j = 0; j < n; j++) {
                    population.get(i)[j] = random.nextInt(n);
                }
            }

            while (getFitness(getBestFit(population)) < bestFitness) {
                Integer[] x = selection(population);
                Integer[] y = selection(population);
                Integer[] child = reproduce(x, y);
                if (random.nextInt(100) > 95) {
                    mutate(child);
                }
                population.remove(population.indexOf(getWorstFit(population)));
                population.add(child);
                iteration++;
                maxList.add(getBestFit(population));
            }
            Integer[] solution = getBestFit(population);
            System.out.println("found it after " + iteration + " iterations. looks like this: ");
            printResults(solution);
            System.out.println("elapsed time: " + (System.currentTimeMillis() - startTime) + "ms");
            n = getN();
        }

    }
    public static void printResults(Integer[] solution) {
        for (int i : solution) {
            StringBuilder rowBuilder = new StringBuilder();
            for (int j=0; j < i ; j++){
                rowBuilder.append('_');
            }
            rowBuilder.append('Q');
            for (int j=i+1; j < solution.length; j++) {
                rowBuilder.append('_');
            }
            System.out.println(rowBuilder);
        }
    }
    public static int getN(){
        Scanner scan = new Scanner(System.in);
        int n = 0;
        do {
            System.out.println("Enter value for n between 4 and 20: ");
            System.out.println("Enter -1 to quit");
            n = Integer.parseInt(scan.nextLine());
            if (n == -1) {
                return n;
            }
        }while( n<4 || n>20);
        return n;
    }
    public static int getFitness(Integer[] element){
        int fitness = 0;
        for (int i=0; i < (element.length - 1); i++){ // nested loop iterates through all pairs
            for (int j=i+1; j<element.length; j++){
                if (element[i]!=element[j]){ // if pair not on same row
                    if(Math.abs(i-j)!=Math.abs(element[i]-element[j])){ // if pair not on same diagonal
                        fitness++;
                    }
                }
            }
        }
        return fitness;
    }
    public static Integer[] getBestFit(ArrayList<Integer[]> population){
        Integer[] bestFit = population.get(0);
        for(Integer[] element : population){
            if (getFitness(element)>getFitness(bestFit)){
                bestFit = element;
            }
        }
        return bestFit;
    }
    public static Integer[] getWorstFit(ArrayList<Integer[]> population){
        Integer[] worstFit = population.get(0);
        for(Integer[] element : population){
            if (getFitness(element)<getFitness(worstFit)){
                worstFit = element;
            }
        }
        return worstFit;
    }

    /**
     * <p>
     *     Has a % chance of returning any element in population according to its fitness.
     *     This is accomplished by assigning a normalized value to each element based on
     *     fitness such that when a random element is chosen, it is more likely to select
     *     one with a higher fitness.
     * </p>
     * @param population
     * @return element of population
     */
    public static Integer[] selection(ArrayList<Integer[]> population){
        int randomMax = 1000000000;
        Random rand = new Random();
        int[] fitnessArray = new int[population.size()];
        int fitSum = 0;
        for (int i=0; i < population.size(); i++) {
            fitnessArray[i] = getFitness(population.get(i));
            fitSum += getFitness(population.get(i));
        }
        int[] probArray = new int[fitnessArray.length];
        for (int i=0; i<fitnessArray.length; i++) {
            probArray[i] = (fitnessArray[i] * randomMax);
            probArray[i] = probArray[i] / fitSum;
            if (i>0) {
                probArray[i] += probArray[i-1];
            }
        }
        int random = rand.nextInt(randomMax);
        for (int i = 0; i < probArray.length; i++) {
            if (random < probArray[i]){
                return population.get(i);
            }
        }

        //System.out.println("Something went wrong, selecting last element");
        return population.get(population.size()-1);
    }
    public static Integer[] reproduce(Integer[] x, Integer[] y){
        Random rand = new Random();
        int crossover = rand.nextInt(x.length); // index to start crossover from
        Integer[] child = new Integer[x.length];

        for (int i = 0; i < crossover; i++) {
            child[i] = y[i];
        }
        for (int i = crossover; i < x.length; i++) {
            child[i] = x[i];
        }
        return child;
    }
    public static void mutate(Integer[] mutatedChild) {
        Random rand = new Random();
        mutatedChild[rand.nextInt(mutatedChild.length)] = rand.nextInt(mutatedChild.length);
        return;
    }
}
