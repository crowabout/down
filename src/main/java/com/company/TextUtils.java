package com.company;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pc on 2017/7/6.
 */
public class TextUtils {

    private static Logger log = Logger.getLogger(TextUtils.class.getName());

    private static List<String> suffixs =new ArrayList<String>();
    static {

        suffixs.add(".pdf");
        suffixs.add(".txt");

        suffixs.add(".zip");
        suffixs.add(".rar");
        suffixs.add(".war");
        suffixs.add(".tar");
        suffixs.add(".gz");
        suffixs.add(".7z");
        suffixs.add(".bz2");
        suffixs.add(".xz");
        suffixs.add(".jar");
        suffixs.add(".tgz");

        suffixs.add(".png");
        suffixs.add(".jpeg");
        suffixs.add(".jpg");
        suffixs.add(".mp4");
        suffixs.add(".mp3");

        suffixs.add(".exe");
        suffixs.add(".doc");
        suffixs.add(".docx");
        suffixs.add(".ppt");
        suffixs.add(".xls");

        suffixs.add(".rpm");
        suffixs.add(".deb");
        suffixs.add(".asc");
        suffixs.add(".apk");
        suffixs.add(".sign");

        suffixs.add(".torrent");
        suffixs.add(".iso");

        suffixs.add(".h");
        suffixs.add(".c");
        suffixs.add(".cpp");
        suffixs.add(".cs");

        suffixs.add(".py");
        suffixs.add(".sh");
        suffixs.add(".lzma");
        suffixs.add(".lz");
        suffixs.add(".sql");
        suffixs.add(".pl");

        suffixs.add(".lnk");
        suffixs.add(".svg");
        suffixs.add(".chm");
        suffixs.add(".xml");

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
   public static String convert2Str(InputStream in, Charset charset){
       BufferedReader reader = new BufferedReader(new InputStreamReader(in,charset));
       StringBuilder sb = new StringBuilder();
       String line = null;
       try {
           while ((line = reader.readLine()) != null) {
               sb.append(line+"\r\n");
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               in.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return sb.toString();
   }


    public static Charset toCharSet(String contentType) {
        Pattern p = Pattern.compile(".*charset=.*");
        Matcher matcher=p.matcher(contentType);
        if(matcher.matches()){
            String charsetStr =
                    contentType.substring(contentType.indexOf("=")+1,contentType.length());
//            log.info("charset:"+charsetStr);
            return Charset.forName(charsetStr);
        }
        return null;
    }
}
