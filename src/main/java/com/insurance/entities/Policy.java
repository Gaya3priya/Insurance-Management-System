package com.insurance.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="POLICY")
public class Policy {
	 
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private int pId;
	private String name;
	
	@Pattern(regexp="(^$|[0-9]{9})")
	@Column(name="policyID")
	private String policyID;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="DOB")
	private Date DOB;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="DOC")
	private Date DOC;
	@Column(name="Term")
	private Integer term;
	@Column(name="Premium")
	private Float premium;
	@Column(name="SumAssured")
	private Float sumAssured;
	@Column(name="Mode")
	private String mode;
	
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="FUP")
	private Date FUP;
	
	@Pattern(regexp="(^$|[0-9]{10})")
	@Column(name="mobileNumber")
	private String mobileNumber;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPolicyID() {
		return policyID;
	}
	public void setPolicyID(String policyID) {
		this.policyID = policyID;
	}
	
	public Date getDOB() {
		return DOB;
	}
	public void setDOB(Date dOB) {
		DOB = dOB;
	}
	public Date getDOC() {
		return DOC;
	}
	public void setDOC(Date dOC) {
		DOC = dOC;
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	public Float getPremium() {
		return premium;
	}
	public void setPremium(Float premium) {
		this.premium = premium;
	}
	public Float getSumAssured() {
		return sumAssured;
	}
	public void setSumAssured(Float sumAssured) {
		this.sumAssured = sumAssured;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	public Date getFUP() {
		return FUP;
	}
	public void setFUP(Date FUP) {
		this.FUP = FUP;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public boolean equals(Object obj) {
		return this.pId==((Policy)obj).getpId();
	}
	

}
