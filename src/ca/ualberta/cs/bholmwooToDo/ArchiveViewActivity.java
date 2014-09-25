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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ArchiveViewActivity extends Activity {

	private static final String TODOFILENAME = "TODOLists.sav";
	private static final String ARCHFILENAME = "ArchLists.sav";
	
	TODOList ActiveList;
	TODOList ArchList;
	
	TODOListController ListController;
	TODOListController ArchController;
	
	ArrayAdapter<TODO> ListViewAdapter;
	ListView ArchListView;
	
	TextView checkedCountText;
	TextView uncheckedCountText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive_view);

	/*
	}
	
	protected void onStart() {
		super.onStart();
		
		*/
		final Context ctx = this;
		
		ArchListView = (ListView) findViewById(R.id.ArchListView);
		
		checkedCountText = (TextView) findViewById(R.id.ArchCheckedCount);
		uncheckedCountText = (TextView) findViewById(R.id.ArchUncheckedCount);
		
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
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, ArchList.getList());
        ArchListView.setAdapter(ListViewAdapter);
        ListViewAdapter.notifyDataSetChanged();
		
		registerForContextMenu(ArchListView);
      
        ArchListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                SparseBooleanArray checkedItemPositions = ArchListView.getCheckedItemPositions();
            	ArchController.updateChecked(checkedItemPositions);
            	ArchController.saveInFile(ARCHFILENAME, ctx);
            	updateStats();
        
            }

        });
        
        setChecked(ArchListView);
        updateStats();
	}
	
	public void updateStats() {
		
        SparseBooleanArray checkedItemPositions = ArchListView.getCheckedItemPositions();
        int itemCount = ArchListView.getCount();

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
	
		for (int i = ( ArchList.size() - 1 ); i >= 0; i--) { 
			TODOListView.setItemChecked(i, (ArchList.get(i).getStatus()) );
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
		
		SparseBooleanArray checkedItemPositions = ArchListView.getCheckedItemPositions();
		
		if (item.getTitle() == "Unarchive") {
			ActiveList.add(ArchList.get(itemIndex));
			ArchList.remove(itemIndex);
			ListController.saveInFile(TODOFILENAME, this);
			ArchController.saveInFile(ARCHFILENAME, this);
			ListViewAdapter.notifyDataSetChanged();;
			checkedItemPositions.clear();
			setChecked(ArchListView);
			updateStats();
		}
		else if (item.getTitle() == "Remove") {
	    	ArchList.remove(itemIndex);
			ListController.saveInFile(ARCHFILENAME, this);
            ListViewAdapter.notifyDataSetChanged();
            checkedItemPositions.clear();
            setChecked(ArchListView);
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
	        Intent returnToList = new Intent(this, MainActivity.class);

	        startActivity(returnToList);
		}
		else if (id == R.id.archClearList) {

			for (int i = ( ArchList.size() - 1 ); i >= 0; i--) {
            	ArchList.remove(i);	
            }
			
			SparseBooleanArray checkedItemPositions = ArchListView.getCheckedItemPositions();
			checkedItemPositions.clear();
			
			ListViewAdapter.notifyDataSetChanged();
    		
			ArchController.saveInFile(ARCHFILENAME, this);
			
            updateStats();
			
		}
		else if (id == R.id.archEmail) {
	        Intent emailTODOs = new Intent(this, EmailActivity.class);
	        
	        emailTODOs.putExtra("saveFileName", ARCHFILENAME);
	        
	        startActivity(emailTODOs);
			
		}
		else if (id == R.id.archEmailAll) {
	        
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