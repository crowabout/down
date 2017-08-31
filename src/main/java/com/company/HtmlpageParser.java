package com.company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/7/1.
 */
public class HtmlpageParser {

    private Logger log =Logger.getLogger("HtmlpageParser");
    private URL absoluteUrl;
    /**
     * the count of items within current page;
     */
    private int pageNodeCount;

    private Queue<GraphNode> queue;

    public HtmlpageParser(Queue<GraphNode> queue) {
//        this.absoluteUrl =rootUrl;
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
        node.setUploadTime(time);
        node.setLeafNode(node.isLeafNode());

//        log.info(text+" "+href +" "+fileSize+" "+time+"   "+node.isLeafNode());

        return node;
    }


    /**
     * extact Tag from url
     * @return
     */
     public int exactTrTagFromUrl(URL url) throws InterruptedException {
         Document doc = null;
         try {
             doc = Jsoup.connect(url.toString()).get();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return exactTrTagFromDoc(doc);
    }

    /**
     * extact Tag From html file;
     * @param html
     * @return
     */
    public int exactTrTagFromHtml(String html){

         if(html==null||html.equalsIgnoreCase("")){
             throw  new NullPointerException("html is null!");
         }
         Document doc =Jsoup.parse(html);
         return exactTrTagFromDoc(doc);
    }



    /**
     * exact Tag from url
     * @param doc
     * @return
     */
    private int exactTrTagFromDoc(Document doc){
        try {

            String index =exactIndexFromDoc(doc);

            Elements eles =doc.select(
                    "tr."+Iidc.TR_TAG_CLASS_FOLDER
                            +",tr."+Iidc.TR_TAG_CLASS_FILE_BG1
                            +",tr."+Iidc.TR_TAG_CLASS_FILE_BG2);
            for (Element ele :eles) {
                GraphNode node =convTrTag2GNode(ele);
                node.setCurPageIndex(index);
                    if(queue!=null){
                        queue.offer(node);
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


    /**
     * 从文档中抽取目录索引
     * @return
     */
    private String exactIndexFromDoc(Document doc){

        Element tdEle=doc.select("td[valign='top'] table:eq(0) tr").get(0);
        Elements aEles =tdEle.select("tr a");
        int size =aEles.size();
        StringBuilder builder =new StringBuilder();
        for(int i=0;i<size;i++){
           String dir = aEles.get(i).text();
           builder.append(dir+"/");
        }
        return builder.toString();
    }


    public static void main(String[] args) {


//        Map<String, String> configurationOverrides = new HashMap<String, String>();
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("htmlParser", configurationOverrides);
//        EntityManager entityManager = emf.createEntityManager();

//        String urlStr ="http://linux.linuxidc.com/index.php";
//        BlockingQueue<GraphNode> queue =new LinkedBlockingQueue<GraphNode>();
//        URL url = null;
//        try {
//            url = new URL(urlStr);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        HtmlpageParser parser =new HtmlpageParser(queue);
//        try {
//            parser.exactTrTagFromUrl(url);
//
//            Iterator it =queue.iterator();
//            while(it.hasNext()){
//                GraphNode node = (GraphNode) it.next();
//                System.out.println(node.toString());
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//

    }


}

