package com.company;

/**
 * Created by augustus on 3/21/16.
 * This is a memory object that will
 * contain all the variables for the
 * objects to be used in various functions
 */
public class memoryObj {
    public String address;
    public String hexAddress;
    public String tag;
    public String index;
    public String offset;
    public int blockNum;
    public String blockAddr;
    public boolean isHex;

    //Constructor
    public memoryObj(String addr, boolean isHexBool){
        address = addr;
        isHex = isHexBool;
    }

    public String calcTag(){
        long dec;
        //int dec;
        int addZeros = 0;
        String hex;
        String bin;
        String tagLoc;
        String indexLoc;
        String offsetLoc;

        if(isHex){ //convert dec to hex if address is not in hex already
            hex = address;
            hexAddress = hex;
            //dec = Integer.parseInt(hex, 16);
            //bin = Integer.toBinaryString(dec);
            dec = Long.parseLong(hex, 16);
            bin = Long.toBinaryString(dec);
        }else{
            //dec = Integer.parseInt(address);
            //hex = Integer.toHexString(dec);
            dec = Long.parseLong(address);
            hex = Long.toHexString(dec);
            hexAddress = hex;
            bin = Long.toBinaryString(dec);
        }

        if(bin.length() < Main.memAddrLength){
            addZeros = Main.memAddrLength - bin.length();
        }

        //Add appropriate # of zeros to make the string 32 bits
        String oneZero = "0";
        String addingZeros = "";
        for(int i = 0; i<addZeros; i++){
            addingZeros += oneZero;
        }
        bin = addingZeros + bin;
        //System.out.println(bin);

        tagLoc = bin.substring(0, Main.tagSize-1);
        if(Main.index == 0){
            indexLoc = "";
        }else{
            indexLoc = bin.substring(Main.tagSize, Main.tagSize+Main.index-1);
        }
        offsetLoc = bin.substring(Main.tagSize+Main.index, bin.length()-1);

        String temp;
        temp = tagLoc+indexLoc;

        if(temp.equals("")){
            blockAddr = Long.toHexString(0);
        }else{
            blockAddr = Long.toHexString(Long.parseLong(temp,2));
        }

        if(tagLoc.equals("")){
            tag = Long.toHexString(0);
        }else{
            tag = Long.toHexString(Long.parseLong(tagLoc,2));
        }

        if(indexLoc.equals("")){
            index = Long.toHexString(0);
        }else{
            index = Long.toHexString(Long.parseLong(indexLoc,2));
        }

        if(offsetLoc.equals("")){
            offset = Long.toHexString(0);
        }else{
            offset = Long.toHexString(Long.parseLong(offsetLoc,2));
        }




        blockNum = Integer.parseInt(index, 16);

        return hex;
    }



}
