package ca.ualberta.cs.bholmwooToDo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;


public class TODOListController {
	/*	A controller class for TODOList.
	 * 	Controls the status of ToDos and controls saving and loading.
	 */
	private TODOList List;
	
	public TODOList getTODOList() {
		// If list is not initialised, initialise new list.
		if (List == null) {
			List = new TODOList();
		}
		return List;
	}	

	public boolean getStatus(int i) {
		// Return the current status of ToDo i.
		return List.get(i).getStatus();
	}
	
	public void setStatus(int i, boolean status) {
		// Set the status of ToDo i.
		List.get(i).setStatus(status);
	}

	
	// Adapted from http://stackoverflow.com/questions/12158483/how-to-write-an-arraylist-to-file-and-retrieve-it 2014-09-21
	public void saveInFile(String FILENAME, Context ctx) {
		/*	Save the TODOList in the file FILENAME.
		 */
		
		FileOutputStream fos;
		ObjectOutputStream os;

		try {
		  fos = ctx.openFileOutput(FILENAME, 0);
		  os = new ObjectOutputStream(fos);
		  os.writeObject(List);
		  os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// Adapted from http://stackoverflow.com/questions/12158483/how-to-write-an-arraylist-to-file-and-retrieve-it 2014-09-21
	public void loadFromFile(String FILENAME, Context ctx) throws ClassNotFoundException {
		/*	Load the TODOList from the file FILENAME
		 */
		
		TODOList loadedList = new TODOList();
		
		ObjectInputStream ois = null;
		
		try {
			FileInputStream fis = ctx.openFileInput(FILENAME);
			ois = new ObjectInputStream(fis);
			loadedList = (TODOList) ois.readObject();
		    
		    try {
		        if(ois != null) {
		            ois.close();
		        }
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		} 
		
		catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		List = loadedList;
	}
	
}
