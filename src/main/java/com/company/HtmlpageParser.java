package com.company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/7/1.
 */
public class HtmlpageParser {

    private Logger log =Logger.getLogger("HtmlpageParse");
    private URL absoluteUrl;
    /**
     * the count of items within current page;
     */
    private int pageNodeCount;

    private BlockingQueue<GraphNode> queue;

    public HtmlpageParser(URL rootUrl, BlockingQueue<GraphNode> queue) {

        this.absoluteUrl =rootUrl;
        this.queue =queue;

    }

    public HtmlpageParser(GraphNode node) {
        this.absoluteUrl =node.relUrl2abs();
    }


    /**
     *
     * @param trTag
     * @return
     */
     private GraphNode convTrTag2GNode(Element trTag){

        Elements tagTd =trTag.select("tr td");

        Elements tagDiv =tagTd.select("td  a");
        //1st <td>
        Element tagA =tagDiv.first();
        String href=tagA.attr("href");
        String text =tagA.text();

        //2ed <td>
        String fileSize =tagTd.get(1).text();
        //3rd <td>
        String  time=tagTd.get(2).text();

        GraphNode node =new GraphNode(text,href,fileSize);

        log.info(text+" "+href +" "+fileSize+" "+time+"   "+node.isLeafNode());

        return node;
    }


    /**
     * extact Tag from HtmlFile which is requested from url;
     * @return
     */
     int exactTrTagFromUrl() throws InterruptedException {
        try {
            Document doc =Jsoup.connect(absoluteUrl.toString()).get();
            Elements eles =doc.select(
                    "tr."+Iidc.TR_TAG_CLASS_FOLDER
                    +",tr."+Iidc.TR_TAG_CLASS_FILE_BG1
                    +",tr."+Iidc.TR_TAG_CLASS_FILE_BG2);

            for (Element ele :eles) {
                if(queue!=null){
                    queue.put(convTrTag2GNode(ele));
                    pageNodeCount++;
                }else{
                    log.info("!!!queue is null!!!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageNodeCount;

    }


    /**
     * exact Tag from html
     * @param htmlPage
     * @return
     */
    private int exactTrTagFromHtml(String htmlPage){
        try {
            Document doc =Jsoup.parse(htmlPage);
            Elements eles =doc.select(
                    "tr."+Iidc.TR_TAG_CLASS_FOLDER
                            +",tr."+Iidc.TR_TAG_CLASS_FILE_BG1
                            +",tr."+Iidc.TR_TAG_CLASS_FILE_BG2);
            for (Element ele :eles) {
                GraphNode node =convTrTag2GNode(ele);
                if(queue!=null){
                    queue.put(node);
                    pageNodeCount++;
                }else{
                    log.info("!!!queue is null!!!");
                    return pageNodeCount;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageNodeCount;

    }




    public static void main(String[] args) {


        String urlStr ="http://linux.linuxidc.com/index.php";
        BlockingQueue<GraphNode> queue =new LinkedBlockingQueue<GraphNode>();
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HtmlpageParser parser =new HtmlpageParser(url,queue);
        try {
            parser.exactTrTagFromUrl();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}

