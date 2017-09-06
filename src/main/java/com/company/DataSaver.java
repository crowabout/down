package com.company;

import javafx.scene.chart.PieChart;
import sun.reflect.generics.tree.FieldTypeSignature;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/9/2.
 */
public class DataSaver {

    private EntityManager manager;
    private Logger log = Logger.getLogger("DataSaver");
    public DataSaver() {
        manager =setup();
    }

    private EntityManager setup(){
        Map<String, String> configurationOverrides = new HashMap<String, String>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Downer", configurationOverrides);
        EntityManager entityManager = emf.createEntityManager();
        return entityManager;
    }

    public void save(GraphNode node){
       synchronized (manager){
           log.info(String.format("save > %s",node.toString()));
          manager.getTransaction().begin();
          manager.persist(node);
          manager.getTransaction().commit();
       }
    }

    /**
     * query GraphNode by FileType . 100s per page
     * @param type {@see FileType}
     * @param pageIndex  base 1. 1 represent 1th page
     * @return
     */
    public List<GraphNode> queryGraphNodeByFileType(String type,int pageIndex){
        int count=100;
        int begin=1+((pageIndex-1)*count);
        int end=begin+count;
        String sql =String.format("select g from GraphNode g where g.nodeName like \'%%.%s\' order by g.nodeName",type,begin,end);
        List<GraphNode> nodes =manager.createQuery(sql.toString()).setFirstResult(begin).setMaxResults(end)
                .getResultList();
        return nodes;
    }

    public List<GraphNode> queryAllGraphNodeByFileTypeAndKey(String key, String type){

        String sql =String.format("select g from GraphNode g where g.nodeName like \'%%%s%%.%s\'",key,type);
        List<GraphNode> nodes =manager.createQuery(sql.toString()).getResultList();
        return nodes;

    }

    public long countByFileType(String type){
        return queryAllGraphNodeByFileTypeAndKey("", type).size();
    }


    public static void main(String[] args) {
        DataSaver saver =new DataSaver();
//        List<GraphNode> nodes =saver.queryAllGraphNodeByFileTypeAndKey("android", DefaultTask.FileType.PDF);
//        System.out.println(nodes.size());
//        List<GraphNode> nodes2 =saver.queryGraphNodeByFileType(DefaultTask.FileType.PDF,1);
//        System.out.println(saver.countByFileType(DefaultTask.FileType.PDF));
    }

}
