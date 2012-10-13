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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Job holds each url information along with emails
 * @author Intesar Mohammed
 */
public class Job implements Comparable<Job> {
    // pojo holding url, email, up

    private String id;
    private String url;
    private Set<String> email = new LinkedHashSet<String>();
    private String status = "NA";
    private boolean lastUp = true;
    private boolean notified = false;
    private Date upSince;
    private Date downSince;
    
    public Job() {}

    public Set<String> getEmail() {
        return email;
    }

    public void setEmail(Set<String> email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLastUp() {
        return lastUp;
    }

    public void setLastUp(boolean lastUp) {
        this.lastUp = lastUp;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpSince() {
        return upSince;
    }

    public void setUpSince(Date upSince) {
        this.upSince = upSince;
    }
    
    public Date getDownSince() {
        return downSince;
    }

    public void setDownSince(Date downSince) {
        this.downSince = downSince;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
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
    public int compareTo(Job that) {
        return this.getId().compareTo(that.getId());
    }

    @Override
    public String toString() {
        return "Job{" + "id=" + id + ", url=" + url + ", email=" + email + ", lastUp=" + lastUp + ", downSince=" + downSince + '}';
    }

}
