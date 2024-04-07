/* 
 * Stephen Sallas
 * 04/13/2021
 * COMP-3270
 * Programming Assignment
*/

// Imports
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Random;
import java.lang.System;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.Math;

public class MSCS {
   public static void main(String[] args) throws Exception {
      // Creating new instance of class  
      MSCS mscs = new MSCS();
      
      // Reading in phw_input file and creating array
      ArrayList<Integer> baseArray = new ArrayList<Integer>();
      String file = "phw_input.txt";
      baseArray = mscs.readFile(file, baseArray);
      
      // Printing MSCS values
      mscs.printMSCS(baseArray);
      
      // Creating 19 arrays with random values
      ArrayList<ArrayList<Integer>> randomArrays = new ArrayList<ArrayList<Integer>>(19);  
      for (int i = 1; i < 20; i++) {
         randomArrays.add(new ArrayList<Integer>(i * 5 + 5));
      }
      randomArrays = mscs.fillArray(randomArrays);
      
      // Calculating algorithm run times
      ArrayList<ArrayList<Long>> runTimes = new ArrayList<ArrayList<Long>>(19);
      for (int i = 1; i < 20; i++) {
         runTimes.add(new ArrayList<Long>(i * 5 + 5));
      }
      runTimes = mscs.runAlgorithms(randomArrays, runTimes);
      
      // Calculating and printing theoretic time complexities
      runTimes = mscs.theoreticalComplexities(runTimes);
      mscs.printTimes(runTimes);
       
   }
   
   // Max Function (2 args)
   public int max(int a, int b) {
      if(a >= b) {
         return a;
      }
      return b;
   }
   
   // Max Function (3 args)
   public int max(int a, int b, int c) {
      if (a >= b && a >= c) {
         return a;
      }
      else if (b >= a && b >= c){
         return b;
      }
      return c;
   }
      
   // Algorithm-1
   public int algorithm1(ArrayList<Integer> X) {
      // Variables
      int maxSoFar = 0;  
      // Loops
      for (int L = 0; L < X.size(); L++) {
         for (int U = L; U < X.size(); U++) {
            int sum = 0;
            for (int I = L; I <= U; I++) {
               sum += X.get(I);
            }
            maxSoFar = max(maxSoFar, sum);
         }
      }
      return maxSoFar;
   }
      
   // Algorithm-2
   public int algorithm2(ArrayList<Integer> X) {
      // Variables
      int maxSoFar = 0;
      // Loops
      for (int L = 0; L < X.size(); L++) {
         int sum = 0;
         for (int U = L; U < X.size(); U++) {
            sum += X.get(U);
            maxSoFar = max(maxSoFar, sum);
         }
      }
      return maxSoFar;
   }
   
   // Algorithm-3
   public int maxSum(ArrayList<Integer> X, int L, int U) {
      // Zero-element vector 
      if (L > U) {
         return 0;
      }
      // One-element vector
      if (L == U) {
         return max(0, X.get(L));
      }
      // A is X[L..M], B is X[M+1..U]
      int M = (L + U) / 2;
      // Find max crossing to left
      int sum = 0;
      int maxToLeft = 0;
      for (int I = M; I >= L; I--) {
         sum += X.get(I);
         maxToLeft = max(maxToLeft, sum);
      }
      // Find max crossing to right
      sum = 0;
      int maxToRight = 0;
      for (int I = M + 1; I <= U; I++) {
         sum += X.get(I);
         maxToRight = max(maxToRight, sum);
      }
      // All maxes
      int maxCrossing = maxToLeft + maxToRight;
      int maxInA = maxSum(X, L, M);
      int maxInB = maxSum(X, M + 1, U);
      
      return max(maxCrossing, maxInA, maxInB);
   }
   
   // Algorithm-4
   public int algorithm4(ArrayList<Integer> X) {
      int maxSoFar = 0;
      int maxEndingHere = 0;
      for (int I = 0; I < X.size(); I++) {
         maxEndingHere = max(0, maxEndingHere + X.get(I));
         maxSoFar = max(maxSoFar, maxEndingHere);
      }
      return maxSoFar;
   }
   
   // Reading file and creating array
   public ArrayList<Integer> readFile(String file, ArrayList<Integer> list) throws Exception {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = br.readLine();
      String[] nums = line.split(",");
      for (String num : nums) {
         list.add(Integer.parseInt(num));
      }
      br.close();
      return list;
   }
   
   // Printing mscs results from all 4 algorithms
   public void printMSCS(ArrayList<Integer> list) {
      System.out.println("algorithm-1: " + algorithm1(list));
      System.out.println("algorithm-2: " + algorithm2(list));
      System.out.println("algorithm-3: " + maxSum(list, 0, 9));
      System.out.println("algorithm-4: " + algorithm4(list)); 
      System.out.println("Where " + algorithm4(list) + " is the MSCS as determined by each of the algorithms.");
   }
   
   // Filling arrays with random values
   public ArrayList<ArrayList<Integer>> fillArray(ArrayList<ArrayList<Integer>> list) {
      Random rand =  new Random();
      int size = 10;
      for (ArrayList<Integer> array : list) {
         for (int i= 0; i < size; i++) {
            array.add(rand.nextInt());
         }
         size += 5;
      }
      return list;
   }
   
   // Running algorithms with the random arrays
   public ArrayList<ArrayList<Long>> runAlgorithms(ArrayList<ArrayList<Integer>> list, ArrayList<ArrayList<Long>> timeList) {
      // Variables
      long t1 = 0;
      long t2 = 0;
      long t3 = 0;
      long t4 = 0;
      int N = 1000;
      long start;
      long end;
      int timeListNum = 0;
      
      // Calculating execution time
      // Outer loop through random arrays
      // Inner loop for N iterations
      for (ArrayList<Integer> array : list) {
         for (int i = 0; i < N; i++) {
            start = System.nanoTime();
            algorithm1(array);
            end = System.nanoTime();
            t1 += (end - start);
            
            start = System.nanoTime();
            algorithm2(array);
            end = System.nanoTime();
            t2 += (end - start);
            
            start = System.nanoTime();
            maxSum(array, 0, array.size() - 1);
            end = System.nanoTime();
            t3 += (end - start);
            
            start = System.nanoTime();
            algorithm4(array);
            end = System.nanoTime();
            t4 += (end - start);
         }
         
         // Finding average times in nanoseconds and adding them to time matrix
         t1 /= N;
         timeList.get(timeListNum).add(t1);
         t2 /= N;
         timeList.get(timeListNum).add(t2);
         t3 /= N;
         timeList.get(timeListNum).add(t3);
         t4 /= N;
         timeList.get(timeListNum).add(t4);
         timeListNum++;
      }
      return timeList;   
   }
   
   // Calculating theoretical time complexities
   public ArrayList<ArrayList<Long>> theoreticalComplexities(ArrayList<ArrayList<Long>> timeList) {
      double t;
      long n = 10;
      for (ArrayList<Long> array : timeList) {
         for (int i = 1; i < 5; i++) {
            switch(i){
               case 1:
                  t = ((7*n*n*n)/6) + (7*n*n) + ((41*n)/6) + 6;
                  array.add((long)t);
                  break;
               case 2:
                  t = (6*n*n) + (8*n) + 5;
                  array.add((long)t);
                  break;
               case 3:
                  t = 2 * (n+4.5) * (2*n) + 9;
                  array.add((long)t);
                  break;
               case 4:
                  t = (14*n) + 5;
                  array.add((long)t);
                  break;   
               default:
            }
         }
         n += 5;
      }
      return timeList;
   }
   
   // Printing outfile
   public void printTimes(ArrayList<ArrayList<Long>> list) throws Exception {
      BufferedWriter br = new BufferedWriter(new FileWriter("stephensallas_phw_output.txt"));
      br.write("algorithm-1,algorithm-2,algorithm-3,algorithm-4,T1(n),T2(n),T3(n),T4(n)");  
      br.newLine();
      for (ArrayList<Long> array : list) {
         for (int i = 0; i < array.size(); i++) {
            br.write(array.get(i) + ",");
         }
         br.newLine();
      }
      br.close();
   }  
}