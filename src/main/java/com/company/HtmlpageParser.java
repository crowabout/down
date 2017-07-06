package com.company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

    public HtmlpageParser(URL rootUrl) {
        this.absoluteUrl =rootUrl;
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
        Element tagA =tagDiv.first();
        String href=tagA.attr("href");
        String text =tagA.text();

        String fileSize =tagTd.get(1).text();
        String  time=tagTd.get(2).text();

        GraphNode node =new GraphNode(text,href,fileSize);

        log.info(text+" "+href +" "+fileSize+" "+time+"   "+node.isLeafNode());

        return node;
    }


    private int exactTrTagFromHtml(){
        int count=0;
        try {
            Document doc =Jsoup.connect(absoluteUrl.toString()).get();
            Elements eles =doc.select(
                    "tr."+Iidc.TR_TAG_CLASS_FOLDER
                    +",tr."+Iidc.TR_TAG_CLASS_FILE_BG1
                    +",tr."+Iidc.TR_TAG_CLASS_FILE_BG2);
            for (Element ele :eles) {
                convTrTag2GNode(ele);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;

    }





    public static void main(String[] args) {


        String urlStr ="http://linux.linuxidc.com/index.php";
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HtmlpageParser parser =new HtmlpageParser(url);
        parser.exactTrTagFromHtml();


    }


}

