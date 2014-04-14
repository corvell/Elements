/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.project.elements.model;

import java.io.Serializable;

/**
 *
 * @author Suvorkov Vladimir
 */
public class Element implements Serializable{
    
    private Integer id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(Integer id) {
        this.id = id;
    }

        private Integer groupId;

    /**
     * Get the value of groupId
     *
     * @return the value of groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Set the value of groupId
     *
     * @param groupId new value of groupId
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
