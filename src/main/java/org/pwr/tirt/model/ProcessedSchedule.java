package org.pwr.tirt.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.pwr.tirt.model.dto.Subject;

@Entity
public class ProcessedSchedule implements Serializable {

	private static final long serialVersionUID = 3429008546364776852L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Long indexNo;

	@OneToMany(mappedBy = "processedSchedule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Subject> subject;

	// @Column(length=10000)
	// private String subjectsAsJson;

	public List<Subject> getSubject() {
		return subject;
	}

	public void setSubject(List<Subject> subject) {
		this.subject = subject;
	}

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

	// public String getSubjectsAsJson() {
	// return subjectsAsJson;
	// }

	// public void setSubjectsAsJson(String subjectsAsJson) {
	// this.subjectsAsJson = subjectsAsJson;
	// }

}
