package com.company;

/**
 * Created by augustus on 3/22/16.
 */
public class Cache {
    public static int cacheBlocks;
    public static String[] cacheArray;

    public static void createCache(){
       // System.out.println("cache created with " + cacheBlocks + " blocks");
        cacheArray = new String[cacheBlocks];
        addMemToCache("test", 5);
    }

    public static void addMemToCache(String mem, int pos){
       // System.out.println("Address " + mem + " added at position: " + pos);
        cacheArray[pos] = mem;
    }

    public static void clearCacheSpot(int pos){
        cacheArray[pos] = null;
    }

    /**
     * Return true for a cache hit and false for a cache miss.
     * @param pos
     * @return
     */
    public static boolean checkForMissOrHit(int pos){
        if(cacheArray[pos] == null){
            return false;
        }else{
            return true;
        }
    }

}
