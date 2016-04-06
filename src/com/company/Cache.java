package com.company;

/**
 * Created by augustus on 3/22/16.
 * This is the cache structure which
 * is just a simple array of strings
 * that gets manipulated with properties
 * from the memoryObj
 */
public class Cache {
    public static int cacheBlocks;
    public static String[] cacheArray;

    public static void createCache(){
       // System.out.println("cache created with " + cacheBlocks + " blocks");
        cacheArray = new String[cacheBlocks];
        for(int i = 0; i<cacheArray.length; i++){
            cacheArray[i] = "";
        }

    }

    public static void addMemToCache(String mem, int pos, String queType, memoryObj obj, int accessesSoFar){
        switch (queType){
            case "fifo":{ // put the time in the object for fifo
                obj.time = accessesSoFar;
                int lower = 0;
                int index = 0;
                lower = Main.memoryObjs.get(0).time;
                for(memoryObj test : Main.memoryObjs){ //get the lowest time and index of that object
                    if(test.time < lower){
                        lower = test.time;
                        index = findCacheTagInIndex(mem);
                       // index = Main.memoryObjs.indexOf(test);// need index of cache no memoryobjs
                    }
                }
                cacheArray[index] = mem; //replace the object with the lowest time
                break;
            }
            case "lru":{
                obj.lastAccessed = accessesSoFar;

                int lower = 0;
                int max = 0;
                int index = 0;

                index = findCacheTagInIndex(mem);

                lower = Main.memoryObjs.get(0).time;
                for(memoryObj test : Main.memoryObjs){ //get the lowest time and index of that object
                    if(test.lastAccessed < lower){
                        lower = test.lastAccessed;
                        index = findCacheTagInIndex(mem);
                        //index = Main.memoryObjs.indexOf(test); // need index of cache no memoryobjs
                    }
                }

                for(memoryObj test : Main.memoryObjs){ //get the highest number in lastAccessed in the obj's
                    if(test.lastAccessed > max){
                        max = test.lastAccessed;
                    }
                }

                for(memoryObj test : Main.memoryObjs){ //decrement all the lastAccessed to make room for new obj
                    test.lastAccessed = test.lastAccessed-1;
                }

                cacheArray[index] = mem;



                //obj.lastAccessed = accessesSoFar;
                cacheArray[pos] = mem;
                break;
            }
        }
       // System.out.println("Address " + mem + " added at position: " + pos);

    }

    private static int findCacheTagInIndex(String mem) {
        int index = cacheArray.length-1;

        for(int i = 0; i<cacheArray.length; i++){
            if(cacheArray[i].equals(mem)){
                return i;
            }
        }

        return index;
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
