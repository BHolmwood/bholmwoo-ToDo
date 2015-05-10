package ca.ualberta.cs.bholmwooToDo;

import java.io.Serializable;

public class TODO implements Serializable {
	/*
	 * 	A simple implementation of a single ToDo item.
	 *	Contatins a string for storing the ToDo text and a boolean to store
	 *	the ToDo's current status (done/checked or not done/unchecked).
	 */
	
	// Auto generated serial version UID, used for serialization
	private static final long serialVersionUID = -2995738622752577566L;
	private String text;
	private Boolean status;
	
	
	public TODO(String text) {
		/*	Constructor
		*	Set ToDo text to input text and set default status to false (not done)		
		*/
		this.text = text;
		this.status = false;
	}

	public String toString() {
		/*	Required for serialization
		 */
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
