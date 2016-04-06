package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    public static ArrayList<memoryObj> memoryObjs = new ArrayList<>();
    private static int n;
    private static int m, p, associativity;
    private static int cacheSize;
    private static int blockSize;
    private static int numOfBlocks;
    private static boolean traceFlag;
    private static File filePath;
    private static String queType;
    public static int index;
    public static int offset;
    public static int tagSize;
    public static int cacheHits = 0;
    public static int cacheMisses = 0;
    public static int accessesSoFar = 0;
    public static int memAddrLength = 32;
    public static int numOfSets = 1;
    public static ArrayList<CacheWithSets> setList = new ArrayList<>();



    public static void main(String[] args) {
        //int testing = 4294967296;
        //Integer testing = 4294967296;
        /**
         * quick check for correct number of parameters
         */
        if(args.length != 6){
            System.out.println("Incorrect number of parameters.");
            System.exit(0);
        }

        /**
         * assign variables
         */
        n = Integer.parseInt(args[0]);
        m = Integer.parseInt(args[1]);
        p = Integer.parseInt(args[2]);
        cacheSize = (int) Math.pow(2, (double)n);
        blockSize = (int) Math.pow(2, (double)m);
        traceFlag = false; //true == on, false == off
        filePath = new File(args[5]); //check for valid filepath later
        queType = args[3].toLowerCase(); // fifo/lru

        /**
         * Quick check for fifo or lru ques
         */
        if(!(queType.equals("fifo")) && !(queType.equals("lru"))){
            System.out.println("Incorrect que type[+"+queType+"].  Accepted values: fifo/lru");
        }

        /**
         * Quick check for the variable p for fully associative
         */
        if(p<0 || p > (n-m)){
            p = n-m;
        }

        associativity = (int)Math.pow(2, (double)p); // After check, assign the associativity

        /**
         * Get the on or off flag.  If neither, exit.
         */
        if(args[4].toLowerCase().equals("on")){
            traceFlag = true;
        }
        else if(args[4].toLowerCase().equals("off")){
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
            System.out.println("File: " + args[5] + " does not exist.");
            System.exit(0);
        }

        if(!areParamsValid(n, m, cacheSize, blockSize)){ //if parameters are not valid, say so then exit.
            System.out.println("Invalid Cache size / Block size.");
            System.exit(0);
        }

        //Calc the rest of the info about the cache structure with given information
        numOfBlocks = cacheSize / blockSize;
        index = (int)(Math.log((double)numOfBlocks) / (Math.log(2)));
        offset = (int)(Math.log((double)blockSize) / (Math.log(2)));

        numOfSets = numOfBlocks / associativity; // ex) 8192 blocks / 16(p^4) associativity = 512 sets

        //create numOfSets of caches...these represent your cache sets
        for(int x = 0; x < numOfSets; x++){
            setList.add(new CacheWithSets(associativity));
        }

        tagSize = memAddrLength - index - offset;

        //System.out.println("numBlocks: " + numOfBlocks);
        //System.out.println("index; "+index);
        //System.out.println("offset: "+offset);
        //System.out.println("tagSize: "+tagSize);

        //Create cache since number of blocks is known now
        Cache.cacheBlocks = numOfBlocks;
        Cache.createCache();


        getMemoryAddresses(filePath);

        String sep = "-----------------------------------------------------------------------";
        //System.out.println(sep.length());

        if(traceFlag){
            System.out.printf("%10s|%7s|%7s|%7s|%5s|%5s|%5s|%7s|%9s\n", "Addr", "Tag", "Block#", "C Tag",
                    "H/M", "Hits", "Misses", "MemAcc", "Miss %");
            //System.out.println(test);
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
     * then create memory objects based if they are hex or
     * decimal and then add to arraylist.
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
                        if(line.substring(0, 2).toLowerCase().equals("0x")){ //hex address
                            memoryObj memObj = new memoryObj(line.substring(2).toLowerCase(), true);
                            memoryObjs.add(memObj);
                        }else{ //decimal address
                            memoryObj memObj = new memoryObj(line, false);
                            memoryObjs.add(memObj);
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Unable to open file: " + file.toString());
        }
    }

    /**
     * Calculate all the information needed for the memory address
     * then print it out. Tracing is on.
     *
     * I could do it with objects but I'm just going to do it with
     * static strings
     */
    public static void runWithTracingOn(){

        //Run through the objects
        for(memoryObj obj : memoryObjs){
            accessesSoFar++;
            obj.calcTag(); // calc all the necessary info about the memory address
            //System.out.println("SET NUM: " + obj.setNum + "     INDEX: " + obj.index);
            //sets are from 0 to max
            setList.get(obj.setNum).checkForMissOrHit(obj, obj.blockNum, queType, true); //true = print info
        }
    }

    /**
     * Calculate all the information needed for the memory address
     * and do not print it out, just update the counters since
     * tracing is off.
     */
    public static void runWithTracingOff(){
        //Run through the objects
        for(memoryObj obj : memoryObjs){
            accessesSoFar++;
            obj.calcTag(); // calc all the necessary info about the memory address
            setList.get(obj.setNum).checkForMissOrHit(obj, obj.blockNum, queType, false); // false = don't print
        }
    }

    public static void printLastSixLines(String[] args){
        System.out.println("Augustus Scott Rutkoski");

        for (String str: args) {
            System.out.print(str + " ");
        }
        System.out.println();

        System.out.println("memory accesses: " + memoryObjs.size());

        System.out.println("hits: " + cacheHits);

        System.out.println("misses: " + cacheMisses);

        System.out.printf("miss ratio: %.08f\n",  ((double)cacheMisses / (double)accessesSoFar));
    }
}
