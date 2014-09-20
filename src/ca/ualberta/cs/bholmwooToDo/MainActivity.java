/*  
 *  bholmwoo-ToDo: A simple ToDo list app.  
 *  
 *  Copyright (C) 2014 Benjamin Holmwood bholmwood@ualberta.ca
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 

//http://wptrafficanalyzer.in/blog/deleting-selected-items-from-listview-in-android/

package ca.ualberta.cs.bholmwooToDo;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

	
	private static final String TODOFILENAME = "TODOLists.sav";
	private static final String ARCHFILENAME = "ArchLists.sav";
	
	ArrayList<TODO> TODOList = new ArrayList<TODO>();
	ArrayAdapter<TODO> ListViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button addButton = (Button) findViewById(R.id.addButton);
		
		ListView TODOListView = (ListView) findViewById(R.id.TodoListView);
		
		registerForContextMenu(TODOListView);
		
		File file = new File(TODOFILENAME);
		if(file.exists()) {
			try {	
				TODOList = loadFromFile(TODOFILENAME);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, TODOList);
		
        OnClickListener addTODOListener = new OnClickListener() {
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.addTODOField);
                TODO newTODO = new TODO(edit.getText().toString());
                TODOList.add(newTODO);
                edit.setText("");
                ListViewAdapter.notifyDataSetChanged();
                updateChecked();
            }
        };
        
        TODOListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	updateChecked();
        
            }

        });
		
        addButton.setOnClickListener(addTODOListener);
        
        TODOListView.setAdapter(ListViewAdapter);
        
	}
	
	public void updateChecked() {
		TextView checkedCountText = (TextView) findViewById(R.id.checkedCount);
		TextView uncheckedCountText = (TextView) findViewById(R.id.uncheckedCount);
		
		ListView TODOListView = (ListView) findViewById(R.id.TodoListView); 
		
        SparseBooleanArray checkedItemPositions = TODOListView.getCheckedItemPositions();
        int itemCount = TODOListView.getCount();

        int checkedCount = 0;
        
        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
            	TODOList.get(i).setStatus(true);
                checkedCount++;
            }
            else {
            	TODOList.get(i).setStatus(false);
            }
        }
        
        checkedCountText.setText("Completed: " + checkedCount);
		uncheckedCountText.setText("Uncompleted: " + (itemCount - checkedCount));
		
		saveInFile(TODOFILENAME, TODOList);
		
	}
	
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    String title = ListViewAdapter.getItem(info.position).getText();
	    menu.setHeaderTitle(title);

	    menu.add("Archive");
	    menu.add("Remove");
	}

	public boolean onContextItemSelected(MenuItem item) {

	    if (item.getTitle() == "Remove") {
            TODOList.remove(item.getItemId());
            ListViewAdapter.notifyDataSetChanged();
            updateChecked();
	    } 
		else {
	        return false;
	    }
	    return true;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.viewArchive) {
	        Intent viewArchive = new Intent(this, ArchiveViewActivity.class);

	        startActivity(viewArchive);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveInFile(String FILENAME, ArrayList<TODO> TODOList) {
		try {
			//FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			
			FileOutputStream fos = new FileOutputStream(FILENAME);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(TODOList);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<TODO> loadFromFile(String FILENAME) throws ClassNotFoundException{
		ArrayList<TODO> loadedList = new ArrayList<TODO>();
		
		//FileInputStream fis;
		try {
			FileInputStream fis = new FileInputStream(FILENAME);
		    //fis = openFileInput(FILENAME);
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    
		    
		    //Object obj = ois.readObject();
		    
		    //ArrayList loadedList = (ArrayList) obj;
		    //loadedTODO = (ArrayList<TODO>) loadedList;
		    
		    //loadedTODO = (ArrayList<TODO>) obj;
		    
		    loadedList = (ArrayList<TODO>) ois.readObject();
		    
		    ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return loadedList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loadedList;
	}
	
	
	
	
}
