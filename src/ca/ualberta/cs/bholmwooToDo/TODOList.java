package ca.ualberta.cs.bholmwooToDo;

import java.io.Serializable;
import java.util.ArrayList;

public class TODOList implements Serializable {
	/*	A list of ToDos.
	 * 	Stores ToDos in an arraylist.
	 */
	
	// Auto generated serial version UID, used for serialization
	private static final long serialVersionUID = 2695985525090052035L;
	private ArrayList<TODO> TODOList;

	public TODOList() {
		TODOList = new ArrayList<TODO>();
	}
	
	public ArrayList<TODO> getList(){
		return TODOList;
	}
	
	public void add(TODO newTODO) {
		TODOList.add(newTODO);
	}
	
	public void remove(int i) {
		TODOList.remove(i);
	}
	
	public TODO get(int i) {
		return TODOList.get(i);
	}
	
	public int size() {
		return TODOList.size();
	}
	
	public String toString() {
		return TODOList.toString();
	}
	
}