package com.company;

/**
 * Created by augustus on 3/21/16.
 */
public class memoryObj {
    public String address;
    public int tag;
    public int blockNum;
    public int cacheEntryTag;
    public String hitOrMiss;
    public boolean isHex;

    //Constructor
    public memoryObj(String addr, boolean isHexBool){
        address = addr;
        isHex = isHexBool;
    }

    //Getters and setters
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}


    public int getTag() {return tag;}
    public void setTag(int tag) {this.tag = tag;}


    public int getBlockNum() {return blockNum;}
    public void setBlockNum(int blockNum) {this.blockNum = blockNum;}


    public int getCacheEntryTag() {return cacheEntryTag;}
    public void setCacheEntryTag(int cacheEntryTag) {this.cacheEntryTag = cacheEntryTag;}


    public String getHitOrMiss() {return hitOrMiss;}
    public void setHitOrMiss(String hitOrMiss) {this.hitOrMiss = hitOrMiss;}

    public boolean isHex() {return isHex;}
    public void setIsHex(boolean isHex) {this.isHex = isHex;}

    public String calcTag(){
        int dec = 0;
        String hex = address;
        String bin = "";
        String tempAddr = "";

        if(!isHex){ //convert dec to hex if address is not in hex already
            dec = Integer.parseInt(address);
            hex = Integer.toHexString(dec);
            bin = Integer.toBinaryString(dec);
        }

        String oct = "0000";
        for(int i = 0;  i<hex.length(); i++){

        }

        //System.out.println(hex.length());
        System.out.println(bin);


        return hex;
    }



}
