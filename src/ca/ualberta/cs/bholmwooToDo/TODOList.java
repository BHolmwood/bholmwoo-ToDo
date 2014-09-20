package ca.ualberta.cs.bholmwooToDo;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class TODOList {
	private ArrayList<TODO> TODOList;
	private ArrayAdapter<TODO> ListViewAdapter;
	
	public TODOList(ArrayList<TODO> TODOList, ArrayAdapter<TODO> ListViewAdapter) {
		this.TODOList = TODOList;
		this.ListViewAdapter = ListViewAdapter;
	}
	
	public ArrayList<TODO> getTODOList(){
		return TODOList;
	}
	
	public void setTODOList(ArrayList<TODO> newTODOList) {
		this.TODOList = newTODOList;
	}
	
	public ArrayAdapter<TODO> getListViewAdapter(){
		return ListViewAdapter;
	}
	
	public void setListViewAdapter(ArrayAdapter<TODO> newListViewAdapter) {
		this.ListViewAdapter = newListViewAdapter;
	}
	
}