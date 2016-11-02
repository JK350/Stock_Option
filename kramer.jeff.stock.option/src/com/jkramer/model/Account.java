package com.jkramer.model;

import java.util.Date;

public class Account {
	private int accountID;
	private String number;
	private String owner;
	private int status;
	private Date dateOpened;
	private String nickname;
	private String accountType;
	
	//Constructors used when pulling account data out of the database
	public Account(int accountID, String number, String owner, int status, Date opened, String nickname, String type){
		this.accountID = accountID;
		this.number = number;
		this.owner = owner;
		this.status = status;
		this.dateOpened = opened;
		this.nickname = nickname;
		this.accountType = type;
	}
	
	//Constructor used for new accounts creating in the application
	public Account(String number, String owner, int status, Date opened, String nickname, String type){
		this.number = number;
		this.owner = owner;
		this.status = status;
		this.dateOpened = opened;
		this.nickname = nickname;
		this.accountType = type;
	}

	/**
	 * @return the accountID
	 */
	public int getAccountID() {
		return accountID;
	}

	/**
	 * @param accountID the accountID to set
	 */
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the dateOpened
	 */
	public Date getDateOpened() {
		return dateOpened;
	}

	/**
	 * @param dateOpened the dateOpened to set
	 */
	public void setDateOpened(Date dateOpened) {
		this.dateOpened = dateOpened;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}	
}
