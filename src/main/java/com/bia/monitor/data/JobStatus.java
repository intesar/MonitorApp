/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.data;

/**
 *
 * @author mdshannan
 */
public enum JobStatus {
    
    UP("up"), DOWN("down");
    
    private String val;
    
    private JobStatus(String val) {
        this.val = val;
    }
    
    @Override
    public String toString() {
        return val;
    }
}
