package com.company;

/**
 * Created by augustus on 4/5/16.
 */
public class CacheWithSets {
    public int cacheBlocks;
    //public String[] cacheArray;
    public memoryObj[] cacheArray;

    public CacheWithSets(int cacheBlocks){
        this.cacheBlocks = cacheBlocks;
        cacheArray = new memoryObj[cacheBlocks];
        for(int i = 0; i<cacheBlocks; i++){
            cacheArray[i] = null;
        }
    }

    public void checkForMissOrHit(memoryObj obj, int pos, String queType, Boolean printFlag){
        boolean hitFlag = false;
        int position = 0;

        for(int i = 0; i<cacheBlocks; i++){
            if(cacheArray[i]!=null){
                if(cacheArray[i].tag.equals(obj.tag)) {//Cycle through the array and check if the tag matches any other tags
                    hitFlag = true;
                    position = i;
                }
            }
        }

        if(hitFlag) { //there was a hit
            Main.cacheHits++;

            if(printFlag) {
                System.out.printf("%10s|%7s|%7d|%7s|%5s|%5d|%6d|%7d|%9.08f\n", obj.hexAddress.toLowerCase(),
                        obj.tag.toLowerCase(), obj.setNum,
                        Cache.getCacheAddrAtPos(obj.blockNum).toLowerCase(),
                        "hit", Main.cacheHits, Main.cacheMisses, Main.accessesSoFar,
                        ((double) Main.cacheMisses / (double) Main.accessesSoFar));
            }

            if(queType.equals("lru")){ //Only move the position if LRU, FIFO it would stay in place
                addMemory(obj, queType, "hit", position);
            }
        }else{ //there was a miss
            Main.cacheMisses++;

            if(printFlag) {
                System.out.printf("%10s|%7s|%7d|%7s|%5s|%5d|%6d|%7d|%9.08f\n", obj.hexAddress.toLowerCase(),
                        obj.tag.toLowerCase(), obj.setNum,
                        Cache.getCacheAddrAtPos(obj.blockNum).toLowerCase(),
                        "miss", Main.cacheHits, Main.cacheMisses, Main.accessesSoFar,
                        ((double) Main.cacheMisses / (double) Main.accessesSoFar));
            }

            addMemory(obj, queType, "miss", position);
        }
    }

    public void addMemory(memoryObj obj, String queType, String hitOrMiss, int position){
        switch (queType){
            case "lru":{
                int counter = 0;
                for(int i=0; i<cacheBlocks; i++){
                    if(cacheArray[i] == null){ //Go thru the array till non null, save that position in counter
                        break;
                    }
                    counter++;
                }
                if(hitOrMiss.equals("hit")) {
                    if (counter == cacheBlocks) {//set is full
                        if (position + 1 != cacheBlocks) {//block is not at the end of the que so need to move it there
                           // memoryObj temp = cacheArray[position];
                            for(int i=position; i<cacheBlocks-1; i++){
                                cacheArray[i] = cacheArray[i+1];
                            }
                            cacheArray[cacheBlocks-1] = obj;
                        }
                    } else {//set is not full
                        if (position + 1 != counter) {//block is not at the end of the que so need to move it there
                            for(int i=position; i<counter; i++){
                                cacheArray[i] = cacheArray[i+1];
                            }
                            cacheArray[counter-1] = obj;
                        }
                    }
                }
                if(hitOrMiss.equals("miss")) {
                    if (counter == cacheBlocks) {//set is full
                        if (position + 1 != cacheBlocks) {//block is not at the end of the que so need to move it there
                            for(int i = 0; i< cacheBlocks-1; i++){ //Shift everyone down one
                                cacheArray[i] = cacheArray[i+1];
                            }
                            cacheArray[cacheBlocks-1] = obj; //add new obj onto the fifo "que"
                        }
                    } else {//set is not full
                        if (position + 1 != cacheBlocks) {//block is not at the end of the que so need to move it there
                            cacheArray[counter] = obj;//add to the last position of the que
                        }
                    }
                }

                break;
            }
            case "fifo":{
                int counter = 0;
                for(int i=0; i<cacheBlocks; i++){
                    if(cacheArray[i] == null){ //Go thru the array till non null, save that position in counter
                        break;
                    }
                    counter++;
                }

                if(counter == cacheBlocks){//set is full
                    for(int i = 0; i< cacheBlocks-1; i++){ //Shift everyone down one
                        cacheArray[i] = cacheArray[i+1];
                    }
                    cacheArray[cacheBlocks-1] = obj; //add new obj onto the fifo "que"

                }else{//set is not full
                    cacheArray[counter] = obj; //just add to the end of the "que"
                }
                break;
            }
        }
    }

}
