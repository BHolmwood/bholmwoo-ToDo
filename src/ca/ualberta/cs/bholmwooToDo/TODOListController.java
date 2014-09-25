package ca.ualberta.cs.bholmwooToDo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.SparseBooleanArray;


public class TODOListController {
	private TODOList List;
	
	public TODOList getTODOList() {
		if (List == null) {
			List = new TODOList();
		}
		return List;
	}
	
	
	//Taken from http://wptrafficanalyzer.in/blog/deleting-selected-items-from-listview-in-android/ 2014-09-21
	public void updateChecked(SparseBooleanArray checkedItemPositions) {

        int itemCount = checkedItemPositions.size();
        
        for(int i = itemCount - 1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
            	setStatus(i, true);
            }
            else {
            	setStatus(i, false);
            }
        }
	}

	public void setStatus(int i, boolean status) {
		List.get(i).setStatus(status);
	}
	
	public void saveInFile(String FILENAME, Context ctx) {
		
		//TextView savedDebugText = (TextView) findViewById(R.id.savedDebug);
		
		//savedDebugText.setText("saveInFile() called");
		
		FileOutputStream fos;
		ObjectOutputStream os;

		try {
		  fos = ctx.openFileOutput(FILENAME, 0);
		  os = new ObjectOutputStream(fos);
		  os.writeObject(List);
		  //savedDebugText.setText("Saved to file");
		  os.close();
		} catch (Exception e) {
			//savedDebugText.setText("exception thrown: " + e);
			e.printStackTrace();
		}
		
	}
	
	public void loadFromFile(String FILENAME, Context ctx) throws ClassNotFoundException {
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
