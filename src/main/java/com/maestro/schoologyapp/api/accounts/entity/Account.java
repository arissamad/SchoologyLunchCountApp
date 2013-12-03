package com.maestro.schoologyapp.api.accounts.entity;

import java.util.*;

import javax.persistence.*;

import com.sirra.appcore.accounts.*;

@Entity
@Table(name = "accounts")
public class Account extends BaseAccount {
	
	protected String schoologyAccountId;
	
	protected Date approvalDate;
	
	public Account() {
		
	}
	
	public String getSchoologyAccountId() {
		return schoologyAccountId;
	}

	public void setSchoologyAccountId(String schoologyAccountId) {
		this.schoologyAccountId = schoologyAccountId;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
}