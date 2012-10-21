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

package com.bia.monitor.data;

import java.util.Date;

/**
 * Each instance holds information about website down date along with duration
 * @author Intesar Mohammed
 */
public class JobDown implements Comparable<JobDown> {
    // pojo holding url, email, up

    private String id;
    private String job_id;
    private Boolean active;
    private Date downFrom;
    private Date downTill;

    public JobDown() {
    }

    public JobDown(String id, String job_id, Boolean active, Date downFrom, Date downTill) {
        this.id = id;
        this.job_id = job_id;
        this.active = active;
        this.downFrom = downFrom;
        this.downTill = downTill;
    }

    
    public JobDown(String job_id, Boolean active, Date downFrom) {
        this.job_id = job_id;
        this.active = active;
        this.downFrom = downFrom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDownFrom() {
        return downFrom;
    }

    public void setDownFrom(Date downFrom) {
        this.downFrom = downFrom;
    }

    public Date getDownTill() {
        return downTill;
    }

    public void setDownTill(Date downTill) {
        this.downTill = downTill;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JobDown other = (JobDown) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(JobDown that) {
        return this.getId().compareTo(that.getId());
    }

    @Override
    public String toString() {
        return "JobDown{" + "id=" + id + ", job_id=" + job_id + ", active=" + active + ", downFrom=" + downFrom + ", downTill=" + downTill + '}';
    }
}
