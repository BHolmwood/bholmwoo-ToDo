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
//http://stackoverflow.com/questions/8785955/serialization-arraylist-java
//http://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/
//http://stackoverflow.com/questions/12158483/how-to-write-an-arraylist-to-file-and-retrieve-it
//http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application

package ca.ualberta.cs.bholmwooToDo;


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
import android.widget.Toast;


public class MainActivity extends Activity {

	
	private static final String TODOFILENAME = "TODOLists.sav";
	private static final String ARCHFILENAME = "ArchLists.sav";
	
	TODOList ActiveList;
	TODOList ArchList;
	
	TODOListController ListController;
	TODOListController ArchController;
	
	ArrayAdapter<TODO> ListViewAdapter;
	ListView ActiveListView;
	
	TextView checkedCountText;
	TextView uncheckedCountText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	protected void onStart() {
		super.onStart();
		
		TextView debugText = (TextView) findViewById(R.id.savedDebug);
		debugText.setText("onStart() called");

		final Context ctx = this;
		
		Button addButton = (Button) findViewById(R.id.addButton);
		
		ActiveListView = (ListView) findViewById(R.id.TodoListView);
		
		checkedCountText = (TextView) findViewById(R.id.checkedCount);
		uncheckedCountText = (TextView) findViewById(R.id.uncheckedCount);
		
		ListController = new TODOListController();
		ArchController = new TODOListController();
		
		try {
			ListController.loadFromFile(TODOFILENAME, ctx);
			ArchController.loadFromFile(ARCHFILENAME, ctx);
		} catch (ClassNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		ActiveList = ListController.getTODOList();
		ArchList = ArchController.getTODOList();
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, ActiveList.getList());
        ActiveListView.setAdapter(ListViewAdapter);
        ListViewAdapter.notifyDataSetChanged();
		
		registerForContextMenu(ActiveListView);
		
        OnClickListener addTODOListener = new OnClickListener() {
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.addTODOField);
                TODO newTODO = new TODO(edit.getText().toString());
                ActiveList.add(newTODO);
                ListController.saveInFile(TODOFILENAME, ctx);
                edit.setText("");
                ListViewAdapter.notifyDataSetChanged();
                updateStats();
            }
        };
        
        addButton.setOnClickListener(addTODOListener);
        
        ActiveListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                SparseBooleanArray checkedItemPositions = ActiveListView.getCheckedItemPositions();
            	ListController.updateChecked(checkedItemPositions);
            	ListController.saveInFile(TODOFILENAME, ctx);
            	updateStats();
        
            }

        });
        
        setChecked(ActiveListView);
        updateStats();
	}
	
	public void updateStats() {
		
        SparseBooleanArray checkedItemPositions = ActiveListView.getCheckedItemPositions();
        int itemCount = ActiveListView.getCount();

        int checkedCount = 0;
        
        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                checkedCount++;
            }
        }
        
        checkedCountText.setText("Completed: " + checkedCount);
		uncheckedCountText.setText("Uncompleted: " + (itemCount - checkedCount));
		
	}
	
	public void setChecked(ListView TODOListView) {
	
		for (int i = ( ActiveList.size() - 1 ); i >= 0; i--) { 
			TODOListView.setItemChecked(i, (ActiveList.get(i).getStatus()) );
		}
		
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

	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    int itemIndex = info.position;
		
		TextView debugText = (TextView) findViewById(R.id.savedDebug);
		
		SparseBooleanArray checkedItemPositions = ActiveListView.getCheckedItemPositions();
		
		if (item.getTitle() == "Archive") {
			debugText.setText("Archiving item " + itemIndex);
			ArchList.add(ActiveList.get(itemIndex));
			ActiveList.remove(itemIndex);
			ListController.saveInFile(TODOFILENAME, this);
			ArchController.saveInFile(ARCHFILENAME, this);
			ListViewAdapter.notifyDataSetChanged();
			checkedItemPositions.clear();
			setChecked(ActiveListView);
			updateStats();
		}
		else if (item.getTitle() == "Remove") {
	    	debugText.setText("Removing item " + itemIndex);
            ActiveList.remove(itemIndex);
			ListController.saveInFile(TODOFILENAME, this);
            ListViewAdapter.notifyDataSetChanged();
            checkedItemPositions.clear();
            setChecked(ActiveListView);
            updateStats();
            
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
		else if (id == R.id.clearList) {

			for (int i = ( ActiveList.size() - 1 ); i >= 0; i--) {
            	ActiveList.remove(i);	
            }
			
			SparseBooleanArray checkedItemPositions = ActiveListView.getCheckedItemPositions();
			checkedItemPositions.clear();
			
			ListViewAdapter.notifyDataSetChanged();
    		
            updateStats();
			
		}
		else if (id == R.id.emailTODOs) {
	        Intent emailTODOs = new Intent(this, EmailActivity.class);
	        
	        emailTODOs.putExtra("saveFileName", TODOFILENAME);
	        
	        startActivity(emailTODOs);
			
		}
		else if (id == R.id.emailAllTODOs) {
	        
    		String emailBody = "My ToDos: \n\n Active ToDos:\n ----------------------\n\n";
        	
    	    int TODOItemCount = ActiveList.size();
    	    int ArchItemCount = ArchList.size();
    	    
    	    for(int i = 0; i < TODOItemCount; i++) {
    	    	
    	    	emailBody += "[";
    	    	if (ActiveList.get(i).getStatus()) {
    	    		emailBody += "X]  ";
    		    }
    	    	else {
    	    		emailBody += "   ]  ";
    		    }
    	    	emailBody += ActiveList.get(i).getText() + "\n\n"; 
    		}		

    	    emailBody += " Archived ToDos:\n --------------------------\n\n";
    	    
    	    for(int i = 0; i < ArchItemCount; i++) {  	
    	        
    	    	emailBody += "[";
    	    	if (ArchList.get(i).getStatus()) {
    	    		emailBody += "X]  ";
    		    }
    	    	else {
    	    		emailBody += "   ]  ";
    		    }
    	    	emailBody += ArchList.get(i).getText() + "\n\n"; 
    	    }
    	    
    	    emailBody += "Sent from bholmwoo-ToDo, a simple ToDo list app for Android. \n";
    	    
    		Intent email = new Intent(Intent.ACTION_SEND);
    		email.setType("message/rfc822");
    		email.putExtra(Intent.EXTRA_SUBJECT, "My ToDos");
    		email.putExtra(Intent.EXTRA_TEXT, emailBody);
    		try {
    			startActivity(Intent.createChooser(email, "Send as email using..."));
    		} catch (android.content.ActivityNotFoundException ex) {
    			Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    		}		
		}
		return super.onOptionsItemSelected(item);
	}
	
}
