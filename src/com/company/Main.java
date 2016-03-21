package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    private static ArrayList<String> tokenList = new ArrayList<>();
    private static ArrayList<String> hexTokenList = new ArrayList<>();
    private static ArrayList<String> decTokenList = new ArrayList<>();
    private static int cacheHits = 0;
    private static int cacheMisses = 0;
    private static double missRatio = 0.0;
    private static int accessesSoFar = 0;
   // private static int cacheSize;
   // private static int blockSize;

    public static void main(String[] args) {
        /**
         * quick check for correct number of parameters
         */
        if(args.length != 4){
            System.out.println("Incorrect number of parameters.");
            System.exit(0);
        }

        /**
         * assign variables
         */
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        int cacheSize = (int) Math.pow(2, (double)n);
        int blockSize = (int) Math.pow(2, (double)m);
        boolean traceFlag = false; //true == on, false == off
        File filePath = new File(args[3]); //check for valid filepath later


        /**
         * Get the on or off flag.  If neither, exit.
         */
        if(args[2].toLowerCase().equals("on")){
            traceFlag = true;
        }
        else if(args[2].toLowerCase().equals("off")){
            traceFlag = false;
        }
        else{
            System.out.println("Incorrect parameter 3.  Must be \"on\" or \"off\".");
            System.exit(0);
        }

        /**
         * quick check to see if file exists. Else exit.
         */
        if(!filePath.exists()){
            System.out.println("File: " + args[3] + " does not exist.");
            System.exit(0);
        }

        if(!areParamsValid(n, m, cacheSize, blockSize)){ //if parameters are not valid, say so then exit.
            System.out.println("Invalid Cache size / Block size.");
            System.exit(0);
        }

        getMemoryAddresses(filePath);

        String test = "Address|  Tag|  BlockNum|  Cache Entry Tag|  Hit/Miss|  Hits|  Misses|  Accesses|  Miss Ratio";
        String sep = "---------------------------------------------------------------------------------------------";
        //System.out.println(test.length());

        if(traceFlag){
            System.out.println(test);
            System.out.println(sep);
            runWithTracingOn();
        }else{
            runWithTracingOff();
        }

        printLastSixLines(args);
    }

    /**
     * function that takes all of parameters passed from the command
     * line and runs a few checks on them to make sure all of them
     * are valid for the program to run on.
     * @param n
     * @param m
     * @param cacheSize
     * @param blockSize
     * @return
     */
    public static boolean areParamsValid(int n, int m, int cacheSize, int blockSize){
        if(n<=0 || m<=0){
            return false;
        }
        if(m > n){
            return false;
        }

        return true;
    }

    /**
     * Reads the memory addresses line by line from the file
     * and strips them of all white space.  The function will
     * then put all the strings into an ArrayList for use later.
     * @param file
     */
    public static void getMemoryAddresses(File file){
        String line;

        try {
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null){
                if(!line.equals("\n")) {
                    line = line.replaceAll("\\s+|\\n+|\\r+", "");
                    if(!line.equals("")){
                        tokenList.add(line);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Unable to open file: " + file.toString());
        }
    }
    String test = "Address|  Tag|  BlockNum|  Cache Entry Tag|  Hit/Miss|  Hits|  Misses|  Accesses|  Miss Ratio";

    /**
     * Calculate all the information needed for the memory address
     * then print it out. Tracing is on.
     *
     * I could do it with objects but I'm just going to do it with
     * static strings
     */
    public static void runWithTracingOn(){
        for (String str: tokenList) {
           if(str.substring(0, 2).toLowerCase().equals("0x")){ //toLowerCase might be redundant since 0x = hex
               accessesSoFar++;
               System.out.println(str.substring(2) + "|\t" + "tag|\t" + "blockNum|\t" + "EntryTag|\t" + "miss|\t" +
                       cacheHits + "|\t" + cacheMisses + "|\t" + accessesSoFar + "|\t" + missRatio);
               hexTokenList.add(str.substring(2));
           }else{
               decTokenList.add(str);
           }
        }
    }

    /**
     * Calculate all the information needed for the memory address
     * and do not print it out, just update the counters since
     * tracing is off.
     */
    public static void runWithTracingOff(){
        for (String str: tokenList) {
            if(str.substring(0, 2).toLowerCase().equals("0x")){ //toLowerCase might be redundant since 0x = hex
                hexTokenList.add(str);
            }else{
                decTokenList.add(str);
            }
        }
    }

    public static void printLastSixLines(String[] args){
        System.out.println("Augustus Scott Rutkoski");

        for (String str: args) {
            System.out.print(str + " ");
        }
        System.out.println();

        System.out.println("memory accesses: " + tokenList.size());

        System.out.println("hits: " + cacheHits);

        System.out.println("misses: " + cacheMisses);

        System.out.println("miss ratio: " + missRatio);
    }
}
