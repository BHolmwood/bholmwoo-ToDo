package ca.ualberta.cs.bholmwooToDo;


import android.app.Activity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TODOListActivity extends Activity {

	
	protected static final String TODOFILENAME = "TODOLists.sav";
	protected static final String ARCHFILENAME = "ArchLists.sav";
	
	TODOList ActiveList;
	TODOList ArchList;
	
	TODOListController ListController;
	TODOListController ArchController;
	
	ArrayAdapter<TODO> ListViewAdapter;
	ListView ListView;
	
	TextView checkedCountText;
	TextView uncheckedCountText;
	
	public void emailAllTODOs() {
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
}
