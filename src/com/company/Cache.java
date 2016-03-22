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

    public static String getCacheAddrAtPos(int pos){
        if((cacheArray[pos] == null)){
            return "";
        }else{
            return cacheArray[pos];
        }

    }

    /**
     * Return true for a cache hit and false for a cache miss.
     * Not exactly a valid bit but null can act like one.  If
     * null, there is no data so return a miss.  If not then check
     * the tag to see if they match.
     * @param pos
     * @return
     */
    public static boolean checkForMissOrHit(String addr, int pos){
        if(cacheArray[pos] == null){
            return false;
        }else{
            if(cacheArray[pos].equals(addr)){
                return true;
            }
            return false;
        }
    }

}
