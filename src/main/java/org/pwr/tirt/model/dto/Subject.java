package org.pwr.tirt.model.dto;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.SubjectDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Subject implements Serializable {

	private static final long serialVersionUID = 5515541047891075287L;

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private String lector;

	private String type;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "scheduleId", nullable = false)
	@JsonBackReference
	@JsonIgnore
	private ProcessedSchedule processedSchedule;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private SubjectDetails details;

	public ProcessedSchedule getProcessedSchedule() {
		return processedSchedule;
	}

	public void setProcessedSchedule(ProcessedSchedule processedSchedule) {
		this.processedSchedule = processedSchedule;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLector() {
		return lector;
	}

	public void setLector(String lector) {
		this.lector = lector;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SubjectDetails getDetails() {
		return details;
	}

	public void setDetails(SubjectDetails details) {
		this.details = details;
	}

	@Override
	public boolean equals(Object obj) {
		Subject subject = (Subject) obj;
		return this.getDetails().getStart()
				.equals(subject.getDetails().getStart())
				&& this.getDetails().getDayOfWeek()
						.equals(subject.getDetails().getDayOfWeek());
	}

}
