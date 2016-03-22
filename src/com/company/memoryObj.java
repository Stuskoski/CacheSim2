package com.company;

import java.math.BigInteger;

/**
 * Created by augustus on 3/21/16.
 */
public class memoryObj {
    public String address;
    public String hexAddress;
    public String tag;
    public String index;
    public String offset;
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


    public String getTag() {return tag;}
    public void setTag(String tag) {this.tag = tag;}


    public int getBlockNum() {return blockNum;}
    public void setBlockNum(int blockNum) {this.blockNum = blockNum;}


    public int getCacheEntryTag() {return cacheEntryTag;}
    public void setCacheEntryTag(int cacheEntryTag) {this.cacheEntryTag = cacheEntryTag;}


    public String getHitOrMiss() {return hitOrMiss;}
    public void setHitOrMiss(String hitOrMiss) {this.hitOrMiss = hitOrMiss;}

    public boolean isHex() {return isHex;}
    public void setIsHex(boolean isHex) {this.isHex = isHex;}

    public String calcTag(){
        int dec;
        int addZeros = 0;
        String hex;
        String bin;
        String tagLoc = "";
        String indexLoc = "";
        String offsetLoc =  "";

        if(isHex){ //convert dec to hex if address is not in hex already
            hex = address;
            hexAddress = hex;
            dec = Integer.parseInt(hex, 16);
            bin = Integer.toBinaryString(dec);
        }else{
            dec = Integer.parseInt(address);
            hex = Integer.toHexString(dec);
            hexAddress = hex;
            bin = Integer.toBinaryString(dec);
        }

        if(bin.length() < Main.memAddrLength){
            addZeros = Main.memAddrLength - bin.length();
        }

        String oneZero = "0";
        String addingZeros = "";
        for(int i = 0; i<addZeros; i++){
            addingZeros += oneZero;
        }

        bin = addingZeros + bin;

        tagLoc = bin.substring(0, Main.tagSize-1);
        indexLoc = bin.substring(Main.tagSize, Main.tagSize+Main.index-1);
        offsetLoc = bin.substring(Main.tagSize+Main.index, bin.length()-1);


        //System.out.println(tagLoc + "\t" + indexLoc + "\t" + offsetLoc);
        //System.out.println(Integer.toHexString(Integer.parseInt(tagLoc,2)) + "\t\t\t\t\t" +
        //        Integer.toHexString(Integer.parseInt(indexLoc,2)) + "\t" +
        //        Integer.toHexString(Integer.parseInt(offsetLoc,2)));
        //System.out.println();

        tag = Integer.toHexString(Integer.parseInt(tagLoc,2));
        index = Integer.toHexString(Integer.parseInt(indexLoc,2));
        offset = Integer.toHexString(Integer.parseInt(offsetLoc,2));

        blockNum = Integer.parseInt(index, 16);

        //Note that the Block Address is the address with the offset bits removed.
        // Note that the block position is just the index.


        return hex;
    }



}
