package org.pwr.tirt.model;

import org.springframework.stereotype.Component;

@Component
public class ScheduleDto {

    private long id;
    
    private long indexNo;
    
    private String semester;
    
    private String html;

    public long getIndexNo() {
        return indexNo;
    }

    
    public void setIndexNo(long indexNo) {
        this.indexNo = indexNo;
    }

    
    public String getSemester() {
        return semester;
    }

    
    public void setSemester(String semester) {
        this.semester = semester;
    }

    
    public String getHtml() {
        return html;
    }

    
    public void setHtml(String html) {
        this.html = html;
    }

    
    public long getId() {
        return id;
    }

    
    public void setId(long id) {
        this.id = id;
    }
    
}
