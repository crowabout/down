package com.company;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pc on 2017/7/5.
 */
public class GraphNode {
    /**
     * node Name
     */
    private String nodeName;
    /**
     * url
     */
    private String relUrl;
    /**
     * level
     */
    private int level;

    /**
     * branch node or leaf node?
     */
    private boolean isLeafNode;

    private String fileSize;


    public GraphNode(String nodeName, String relUrl) {
        this.nodeName = nodeName;
        this.relUrl = relUrl;
    }

    public GraphNode(String nodeName, String relUrl,String fileSize) {
        this(nodeName,relUrl);
        this.fileSize=fileSize;
    }
    /**
     * convert relative url to absolute url;
     * @return
     */
    public URL relUrl2abs(){
        if(relUrl==null || relUrl.equals("")){
            throw new IllegalArgumentException("!!!relUrl is null or empty!!!");
        }
        URL url=null;
        try {
             url=new URL(Iidc.BASE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * leafNode or branchNode
     * @return
     */
    public boolean isLeafNode(){


        //suffix end of the fileName
        boolean bNodeName =TextUtils.suffixIn(nodeName);
        //and the attribute of size
        Pattern p =Pattern.compile("\\d*.{0,1}\\d*\\s*[GgMmKkTt]{0,1}[Bb]");
        Matcher m =p.matcher(fileSize);
        boolean bfileSize =m.matches();

        return bfileSize&&bNodeName;

    }


//    public static void main(String[] args) {
//      GraphNode node =new GraphNode("www.linux.idc.png","http://www.com", "10.1 KB ") ;
//
//      String str=node.fileSize.trim();
//      Pattern p =Pattern.compile("\\d*.{0,1}\\d*\\s*[GgMmKkTt]{0,1}[Bb]");
//      Matcher match =p.matcher(str);
//        System.out.println(match.matches());
//        System.out.println(p.matcher("10.1KKB").matches());
//
//
//    }


}
