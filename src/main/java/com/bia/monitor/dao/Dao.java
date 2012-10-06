/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.dao;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mdshannan
 */
@Repository
public class Dao {
    
    protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Dao.class);
    private MongoOperations mongoTemplate;
    
    public Dao() {
        try {
            mongoTemplate = new MongoTemplate(new Mongo(), "monitor");
        } catch (UnknownHostException ex) {
            logger.warn(ex.getMessage(), ex);
        } catch (MongoException ex) {
            logger.warn(ex.getMessage(), ex);
        } 
    }

    public MongoOperations getMongoTemplate() {
        return mongoTemplate;
    }
    
    
}
