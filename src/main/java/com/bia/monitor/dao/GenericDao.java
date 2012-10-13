/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bia.monitor.dao;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Creates and return single instance of MongoTemplate
 * 
 * @author Intesar Mohammed
 */
@Repository
public class GenericDao {
    
    protected static Logger logger = Logger.getLogger(GenericDao.class);
    private MongoOperations mongoTemplate;
    
    public GenericDao() {
        try {
            mongoTemplate = new MongoTemplate(new Mongo(), "monitor");
            logger.info("MongoOperations created successfully! " + mongoTemplate);
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
