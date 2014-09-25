package ca.ualberta.cs.bholmwooToDo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ArchiveViewActivity extends Activity {

	private static final String TODOFILENAME = "TODOLists.sav";
	private static final String ARCHFILENAME = "ArchLists.sav";
	
	//ArrayList<TODO> TODOList = new ArrayList<TODO>();
	
	ArrayList<TODO> ActiveList;
	ArrayList<TODO> ArchList;
	
	ArrayAdapter<TODO> ListViewAdapter;
	ListView ArchListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive_view);
	
		final Context ctx = this;
		
		/*
		ArchListView = (ListView) findViewById(R.id.ArchListView);
	
		try {
			ActiveList = MainActivity.loadFromFile(TODOFILENAME, this);
			ArchList = MainActivity.loadFromFile(ARCHFILENAME, this);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, ArchList);
        ArchListView.setAdapter(ListViewAdapter);
        ListViewAdapter.notifyDataSetChanged();
		
		registerForContextMenu(ArchListView);
        
        ArchListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	MainActivity.saveInFile(ARCHFILENAME, ArchList, ctx);
            	updateChecked();
        
            }

        });
        
        setChecked(ArchListView);
        updateChecked();
	}
	
	protected void onStart() {
		super.onStart();
		//TextView debugText = (TextView) findViewById(R.id.savedDebug);
		//debugText.setText("onStart() called");
	}
	
	protected void onPause() {
		super.onPause();
		MainActivity.saveInFile(ARCHFILENAME, ArchList, this);
	}
	
	public void updateChecked() {
		TextView checkedCountText = (TextView) findViewById(R.id.ArchCheckedCount);
		TextView uncheckedCountText = (TextView) findViewById(R.id.ArchUncheckedCount);
		
		ListView ArchListView = (ListView) findViewById(R.id.ArchListView); 
		
        SparseBooleanArray checkedItemPositions = ArchListView.getCheckedItemPositions();
        int itemCount = ArchListView.getCount();

        int checkedCount = 0;
        
        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
            	ArchList.get(i).setStatus(true);
                checkedCount++;
            }
            else {
            	ArchList.get(i).setStatus(false);
            }
        }
        
        checkedCountText.setText("Completed: " + checkedCount);
		uncheckedCountText.setText("Uncompleted: " + (itemCount - checkedCount));
		
	}
	
	public void setChecked(ListView ArchListView) {
		 
		
		for (int i = ( ArchList.size() - 1 ); i >= 0; i--) { 
			ArchListView.setItemChecked(i, (ArchList.get(i).getStatus()) );
		}
		
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    String title = ListViewAdapter.getItem(info.position).getText();
	    menu.setHeaderTitle(title);

	    menu.add("Unarchive");
	    menu.add("Remove");
	}

	public boolean onContextItemSelected(MenuItem item) {

	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    int itemIndex = info.position;
		
		//TextView debugText = (TextView) findViewById(R.id.savedDebug);
	    SparseBooleanArray checkedItemPositions = ArchListView.getCheckedItemPositions();
		
		if (item.getTitle() == "Unarchive") {
			//debugText.setText("Archiving item " + itemIndex);
			TODOList.add(ArchList.get(itemIndex));
			ArchList.remove(itemIndex);
			MainActivity.saveInFile(TODOFILENAME, TODOList, this);
			MainActivity.saveInFile(ARCHFILENAME, ArchList, this);
			ListViewAdapter.notifyDataSetChanged();
			//updateChecked();
			checkedItemPositions.clear();
			setChecked(ArchListView);
			updateChecked();
		}
		else if (item.getTitle() == "Remove") {
	    	//debugText.setText("Removing item " + itemIndex);
            ArchList.remove(itemIndex);
            MainActivity.saveInFile(ARCHFILENAME, ArchList, this);
            ListViewAdapter.notifyDataSetChanged();
            //updateChecked();
            checkedItemPositions.clear();
            setChecked(ArchListView);
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
		getMenuInflater().inflate(R.menu.archive_view, menu);
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
		if (id == R.id.returnToList) {
	        Intent mainActivity = new Intent(this, MainActivity.class);

	        startActivity(mainActivity);
		}
		else if (id == R.id.archClearList) {
			//TODOList = new ArrayList<TODO>();
            
			for (int i = ( ArchList.size() - 1 ); i >= 0; i--) { 
    			ArchListView.setItemChecked(i, false );
    		}
			for (int i = ( ArchList.size() - 1 ); i >= 0; i--) {
            	ArchList.remove(i);	
            }
            
			ListViewAdapter.notifyDataSetChanged();
    		
            updateChecked();
			
		}
		else if (id == R.id.archEmail) {
	        Intent emailTODOs = new Intent(this, EmailActivity.class);
	        
	        emailTODOs.putExtra("saveFileName", ARCHFILENAME);
	        
	        startActivity(emailTODOs);
			
		}
		else if (id == R.id.archEmailAll) {
    		String emailBody = "My ToDos: \n\n Active ToDos:\n ----------------------\n\n";
        	
    	    int TODOItemCount = TODOList.size();
    	    int ArchItemCount = ArchList.size();
    	    
    	    for(int i = 0; i < TODOItemCount; i++) {
    	    	
    	    	emailBody += "[";
    	    	if (TODOList.get(i).getStatus()) {
    	    		emailBody += "X]  ";
    		    }
    	    	else {
    	    		emailBody += "   ]  ";
    		    }
    	    	emailBody += TODOList.get(i).getText() + "\n\n"; 
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
    		//try {
    		startActivity(Intent.createChooser(email, "Send as email using..."));
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	*/

	}

}