package ca.ualberta.cs.bholmwooToDo;

import java.io.Serializable;

public class TODO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2995738622752577566L;
	private String text;
	private Boolean status;
	
	
	//Constructor
	//Set TODO text to input text and set default status to false (not done)
	public TODO(String text) {
		//super();
		this.text = text;
		this.status = false;
	}

	public String toString() {
		return text;
	}
	
	public String getText() {
		return text;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
