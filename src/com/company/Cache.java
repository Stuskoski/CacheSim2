package com.company;

/**
 * Created by augustus on 3/22/16.
 */
public class Cache {
    public static int cacheBlocks;
    public static String[] cacheArray;

    public static void createCache(){
        System.out.println("cache created with " + cacheBlocks + " blocks");
        cacheArray = new String[cacheBlocks];
        addMemToCache("test", 5);
    }

    public static void addMemToCache(String mem, int pos){
        System.out.println("Address " + mem + " added at position: " + pos);
        cacheArray[pos] = mem;
    }

    public static void clearCacheSpot(int pos){
        cacheArray[pos] = null;
    }

    /**
     * Return true if the position in the cache is
     * clear.  Return false otherwise.
     * @param pos
     * @return
     */
    public static boolean checkCachePos(int pos){
        if(cacheArray[pos] == null){
            return true;
        }else{
            return false;
        }
    }

}
