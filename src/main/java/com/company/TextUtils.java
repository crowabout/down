package com.company;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/7/6.
 */
public class TextUtils {


    private static List<String> suffixs =new ArrayList<String>();
    static {
        suffixs.add(".pdf");
        suffixs.add(".zip");
        suffixs.add(".rar");
        suffixs.add(".gz");
        suffixs.add(".txt");
        suffixs.add(".7z");
        suffixs.add(".xz");
        suffixs.add(".bz2");
        suffixs.add(".jar");
        suffixs.add(".war");
        suffixs.add(".png");
        suffixs.add(".jpeg");
        suffixs.add(".jpg");
    }


    /**
     * check if the suffix of filename contain the pre-setting  suffixes;
     * @param fileName the file Name will extract suffix from
     *
     * @return  true    yes
     *          false not
     */
   public static boolean suffixIn(String fileName){
        boolean flag=false;
       if(!fileName.contains(".")){
          return flag;
       }

       int begin =fileName.lastIndexOf(".");
       String suffix =fileName.substring(begin);
       for (String s :
               suffixs) {
           boolean in =suffix.equalsIgnoreCase(s);
           if(in){
               flag=true;
               break;
           }
       }
       return flag;
   }


   /*
    public static void main(String[] args) {
       boolean b =TextUtils.suffixIn("我阿尼你的我我的.xm");
        System.out.println(b);
    }
    */

}
