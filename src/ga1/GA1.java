/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga1;
import java.lang.*;
import java.util.Random;
/**
 *
 * @author Farhan
 */
public class GA1 {
static Random random = new Random();
    static double h(double[] x) {
        return (((Math.cos(x[0])*Math.sin(x[1]))-(x[0]/Math.pow(x[1],2)+1)));
    }

    static int[][] init(int uk_populasi){
        int[][] populasi = new int[uk_populasi][6];
        for (int i = 0; i < populasi.length; i++) {
            int[] init = populasi[i];
            for (int j = 0; j < init.length; j++) {
                init [j] = random.nextInt(2);
            }
        }
        return populasi;
    }
    
    static double[] decode(int[] kromosom){
        double[] x = new double[2];
        x[0] = -1+((2-(-1))/((Math.pow(2,-1)+Math.pow(2,-2)+Math.pow(2,-3))))*(kromosom[0]*Math.pow(2,-1)+kromosom[1]*Math.pow(2,-2)+kromosom[2]*Math.pow(2,-3));
        x[1] = -1+((1-(-1))/((Math.pow(2,-1)+Math.pow(2,-2)+Math.pow(2,-3))))*(kromosom[3]*Math.pow(2,-1)+kromosom[4]*Math.pow(2,-2)+kromosom[5]*Math.pow(2,-3));        
        return x;
    }
    
    static double fitness (double h){
        return -1/h;
    }
    
    static int maxfitness(double[] fitness){    
        int indeks = 0;
        for (int i = 0; i < fitness.length; i++) {
            if(fitness[i] > fitness[indeks]){
                indeks = i;
            }
        }
        return indeks;
    }
    
    static double [] fitness_pop (int[][] populasi){
        double[] fitness = new double[populasi.length];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = fitness(h(decode(populasi[i])));
        }
        return fitness;
    }
    
    static int[][] rwheel(int[][] populasi, double[] fitness){
        double total = 0;
        for (double fit : fitness) {
            total = total + fit;
        }
        double[] prob = new double[fitness.length];
        for (int i = 0; i < prob.length; i++) {
            prob[i] = fitness[i]/total;
        }
        int[][] parent = new int[2][];
        for (int j = 0; j < 2; j++) {
            double r = Math.random();
            double sum = 0;
            int k = 0;
            while (sum <= r){
                sum = sum + prob[k];
                k++;
            }
            parent[j] = populasi[k-1];
        }
        return parent;
    }
    
    static int[][] crossover (int[][] parent, double co){
        double prob = random.nextInt();
        if (prob <= co) {
            int[][] child = parent;
            int  p =  random.nextInt(6);
            for (int i = p; i < 6; i++) {
                child[0][i] = parent[1][i];
                child[1][i] = parent[0][i];
            }
            return child;
        }
        return parent;
    }
    
    static int[] mutasi(int[] kromosom, double mu){
        for (int i = 0; i < kromosom.length; i++) {
            double prob = random.nextDouble();
            if(prob <= mu){
                kromosom[i] = random.nextInt(2);
            }
        }
        return kromosom;
    }
    
    static int[][] regen (int[][] old_p,int[][] new_p, double[] fitness){
        int tmp = maxfitness(fitness);
        new_p[0] = old_p[tmp];
        new_p[1] = old_p[tmp];
        return new_p;
    }
    
    public static void main(String args[]) {
        int uk_populasi = 100;
        int uk_gen = 50;
        double co = 0.65;
        double mu = 0.01;
        int[][] populasi = init(uk_populasi);
        double[] fitness = new double[uk_populasi];
        for (int i = 0; i < uk_gen; i++) {
            int[][] new_p = new int[uk_populasi][];          
            fitness = fitness_pop(populasi);
            int indeks_max = maxfitness(fitness);
            double f = fitness(h(decode(populasi[indeks_max])));      
            int j = 0;
            while (j < uk_populasi) {
                int[][] parent = rwheel(populasi, fitness);
                int[][] child = crossover(parent, co);
                child[0] = mutasi(child[0], mu);
                child[1] = mutasi(child[1], mu);
                new_p[j++] = child[0];
                new_p[j++] = child[1];
            }
            populasi = regen(populasi, new_p, fitness);
        }       
        int best = maxfitness(fitness);
        double x[] = decode(populasi[best]);
        System.out.print("kromosom terbaik : [");
        for (int i = 0; i < 6; i++) {
            if (i == 5){
                System.out.println(populasi[best][i] + "]");
            }else{
                System.out.print(populasi[best][i] + " ");
            }
        }
        System.out.println("nilai x1 : " +x[0]);
        System.out.println("nilai x2 : " +x[1]);
    } 
}
