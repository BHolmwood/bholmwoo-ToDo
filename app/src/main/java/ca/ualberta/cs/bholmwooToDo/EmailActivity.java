package ca.ualberta.cs.bholmwooToDo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EmailActivity extends Activity {
	/*	The email activity. Shows a ToDo list with check boxes that allows the user to select
	 * 	which ToDos they would like to email. User may select all, or clear all selections.
	 */

	String TODOFILENAME;
	
	TODOList TODOList;
	
	TODOListController ListController;
	
	ArrayAdapter<TODO> ListViewAdapter;
	ListView ListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Context ctx = this;
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    TODOFILENAME = extras.getString("saveFileName");
		}
		
		
		setContentView(R.layout.activity_email);
	
		
		Button emailButton = (Button) findViewById(R.id.emailButton);
		Button selectAllButton = (Button) findViewById(R.id.selectAllButton);
		Button deselectAllButton = (Button) findViewById(R.id.deselectAllButton);
		
		ListView = (ListView) findViewById(R.id.EmailListView);
	
		ListController = new TODOListController();
		
		try {
			ListController.loadFromFile(TODOFILENAME, ctx);
		} catch (ClassNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		TODOList = ListController.getTODOList();
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, TODOList.getList());
        ListView.setAdapter(ListViewAdapter);
        ListViewAdapter.notifyDataSetChanged();
		
		registerForContextMenu(ListView);
		
		
        OnClickListener emailListener = new OnClickListener() {
            public void onClick(View v) {
            
        		String emailBody = "A selection of my ToDos: \n\n";
        	
        		SparseBooleanArray checkedItemPositions = ListView.getCheckedItemPositions();
        	    int itemCount = ListView.getCount();
        	    
        	    for(int i = 0; i < itemCount; i++) {
        	    	
        	        if(checkedItemPositions.get(i)) {
        		    	emailBody += "[";
        		    	if (TODOList.get(i).getStatus()) {
        		    		emailBody += "X]  ";
        		    	}
        		    	else {
        		    		emailBody += "   ]  ";
        		    	}
        		    	emailBody += TODOList.get(i).getText() + "\n\n"; 
        	        }
        	    }
        	    
        	    emailBody += "Sent from bholmwoo-ToDo, a simple ToDo list app for Android. \n";
        	    
        	    // Adapted from http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application 2014-09-21
        	    
        		Intent email = new Intent(Intent.ACTION_SEND);
        		email.setType("message/rfc822");
        		email.putExtra(Intent.EXTRA_SUBJECT, "My ToDos");
        		email.putExtra(Intent.EXTRA_TEXT, emailBody);
        		try {
        		startActivity(Intent.createChooser(email, "Send as email using..."));
        		} catch (android.content.ActivityNotFoundException ex) {
        		    Toast.makeText(ctx, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        		}
            
            }
        };
        
        // Click listener for "Select All" button
        OnClickListener selectAllListener = new OnClickListener() {
            public void onClick(View v) {
        		for (int i = ( TODOList.size() - 1 ); i >= 0; i--) { 
        			ListView.setItemChecked(i, true);
        		}
            }
        };

        // Click listener for "Deselect All" button
        OnClickListener deselectAllListener = new OnClickListener() {
            public void onClick(View v) {
        		for (int i = ( TODOList.size() - 1 ); i >= 0; i--) { 
        			ListView.setItemChecked(i, false);
        		}
            }
        };
        
        emailButton.setOnClickListener(emailListener);
        selectAllButton.setOnClickListener(selectAllListener);
        deselectAllButton.setOnClickListener(deselectAllListener);
	}
	
}
