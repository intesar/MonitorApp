package com.bia.monitor.data;

import java.util.Date;

/**
 *
 * @author intesar
 */
public class JobDown implements Comparable {
    // pojo holding url, email, up

    private String id;
    private String job_id;
    private Boolean active;
    private Date downFrom;
    private Date downTill;

    public JobDown() {
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
    public int compareTo(Object t) {
        JobDown that = (JobDown) t;
        return this.getId().compareTo(that.getId());
    }

    @Override
    public String toString() {
        return "JobDown{" + "id=" + id + ", job_id=" + job_id + ", active=" + active + ", downFrom=" + downFrom + ", downTill=" + downTill + '}';
    }
}
