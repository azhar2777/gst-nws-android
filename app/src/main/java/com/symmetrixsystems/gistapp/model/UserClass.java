/*
 *
 *
 *
 */

package com.symmetrixsystems.gistapp.model;

import java.io.Serializable;

public class UserClass implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private String userFullName;
	private String UserId;
	private boolean isLoggedIn;
	private boolean hasSelection;
	String userCity;
	String userState;

	public boolean hasSelection() {
		return hasSelection;
	}

	public void setHasSelection(boolean hasSelection) {
		this.hasSelection = hasSelection;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	private String userEmail;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	private String journeyId;



	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}
