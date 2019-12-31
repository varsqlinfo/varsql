package com.varsql.core.test;

import java.sql.Timestamp;

public class CompanyInfo {
	private String company_name = "";
	private String company_eng_name = "";
	
	private int long_non_user_day=0; 
	
	private Timestamp update_dtm; 
	
	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	
	
	@Override
	public String toString() {
		
		return new StringBuffer().append("company_name : ").append(company_name)
				.append("long_non_user_day : ").append(long_non_user_day)
				.append("update_dtm : ").append(update_dtm)
				.append("company_eng_name : ").append(company_eng_name).toString();
	}
	
	
}
