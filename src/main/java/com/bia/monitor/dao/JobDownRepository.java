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

import com.bia.monitor.data.JobDown;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * {@See http://www.scribd.com/doc/103202332/44/Querying-documents-in-a-collection}
 * @author mdshannan
 */
@Repository
public interface JobDownRepository extends PagingAndSortingRepository<JobDown, String> {
    @Query("{ 'job_id' : { $regex : ?0 }, 'downFrom' : { $gte : ?1 }}")
    List<JobDown> findByJobIdAndDownFromGreaterThan(String job_id, Date downFrom);
    
    @Query("{ 'job_id' : { $regex : ?0 }, 'active' : { $regex : ?1 }}")
    JobDown findByJobIdAndActive(String jobId, Boolean active);
}
