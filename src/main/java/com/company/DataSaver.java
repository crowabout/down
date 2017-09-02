package com.company;

import javafx.scene.chart.PieChart;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ConsolePU", configurationOverrides);
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

}
