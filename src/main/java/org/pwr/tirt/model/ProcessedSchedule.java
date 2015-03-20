package org.pwr.tirt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class ProcessedSchedule implements Serializable{

    private static final long serialVersionUID = 3429008546364776852L;

    @Id
    @GeneratedValue
    private long id;

    private Long indexNo;

    @Column(length=10000)
    private String subjectsAsJson;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public Long getIndexNo() {
        return indexNo;
    }


    public void setIndexNo(Long indexNo) {
        this.indexNo = indexNo;
    }


    public String getSubjectsAsJson() {
        return subjectsAsJson;
    }


    public void setSubjectsAsJson(String subjectsAsJson) {
        this.subjectsAsJson = subjectsAsJson;
    }

}
