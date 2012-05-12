package com.bia.monitor.service;

import java.util.Date;

/**
 *
 * @author intesar
 */
public class Job implements Comparable {
    // pojo holding url, email, up

    private String id;
    private String url;
    private String email;
    private String status = "NA";
    private boolean lastUp;
    private Date upSince;
    private Date downSince;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public int compareTo(Object t) {
        Job that = (Job) t;
        return this.getId().compareTo(that.getId());
    }

    @Override
    public String toString() {
        return "Job{" + "id=" + id + ", url=" + url + ", email=" + email + ", lastUp=" + lastUp + ", downSince=" + downSince + '}';
    }
    
}
