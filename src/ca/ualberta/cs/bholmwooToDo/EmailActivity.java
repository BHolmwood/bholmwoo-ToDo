package ca.ualberta.cs.bholmwooToDo;

import java.io.FileInputStream;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class EmailActivity extends Activity {

	private static final String TODOFILENAME = "TODOLists.sav";
	
	ArrayList<TODO> TODOList;
	
	ArrayAdapter<TODO> ListViewAdapter;
	ListView TODOListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
	
		
		Button emailButton = (Button) findViewById(R.id.emailButton);
		Button selectAllButton = (Button) findViewById(R.id.selectAllButton);
		
		TODOListView = (ListView) findViewById(R.id.EmailListView);
	
		try {
			TODOList = MainActivity.loadFromFile(TODOFILENAME, this);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, TODOList);
        TODOListView.setAdapter(ListViewAdapter);
        ListViewAdapter.notifyDataSetChanged();
		
		registerForContextMenu(TODOListView);
		
		//TextView emailDebugText = (TextView) findViewById(R.id.emailDebug);
		
        OnClickListener emailListener = new OnClickListener() {
            public void onClick(View v) {
            
        		String emailBody = "A selection of my ToDos: \n\n";
        	
        		SparseBooleanArray checkedItemPositions = TODOListView.getCheckedItemPositions();
        	    int itemCount = TODOListView.getCount();
        	    
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
        	    
        		Intent email = new Intent(Intent.ACTION_SEND);
        		email.setType("message/rfc822");
        		email.putExtra(Intent.EXTRA_SUBJECT, "My ToDos");
        		email.putExtra(Intent.EXTRA_TEXT, emailBody);
        		//try {
        		startActivity(Intent.createChooser(email, "Send mail..."));
        		//} catch (android.content.ActivityNotFoundException ex) {
        		    //Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        		//}
            
            }
        };
        
        OnClickListener selectAllListener = new OnClickListener() {
            public void onClick(View v) {
        		for (int i = ( TODOList.size() - 1 ); i >= 0; i--) { 
        			TODOListView.setItemChecked(i, true);
        		}
            }
        };
        
        emailButton.setOnClickListener(emailListener);
        selectAllButton.setOnClickListener(selectAllListener);
	}
	
	/*
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
		
	}
	
	public void setChecked(ListView TODOListView) {
		 
		
		for (int i = ( TODOList.size() - 1 ); i >= 0; i--) { 
			TODOListView.setItemChecked(i, (TODOList.get(i).getStatus()) );
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
		
		if (item.getTitle() == "Archive") {
			debugText.setText("Archiving item " + itemIndex);
			ArchList.add(TODOList.get(itemIndex));
			saveInFile(ARCHFILENAME, ArchList);
			TODOList.remove(itemIndex);
			ListViewAdapter.notifyDataSetChanged();
		}
		else if (item.getTitle() == "Remove") {
	    	debugText.setText("Removing item " + itemIndex);
            TODOList.remove(itemIndex);
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
		else if (id == R.id.clearList) {
			//TODOList = new ArrayList<TODO>();
            for (int i = ( TODOList.size() - 1 ); i >= 0; i--) {
            	TODOList.remove(i);	
            }
            ListViewAdapter.notifyDataSetChanged();
            updateChecked();
			
		}
		else if (id == R.id.emailTODOs) {
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void saveInFile(String FILENAME, ArrayList<TODO> TODOList) {
		
		TextView savedDebugText = (TextView) findViewById(R.id.savedDebug);
		
		savedDebugText.setText("saveInFile() called");
		
		FileOutputStream fos;
		ObjectOutputStream os;

		try {
		  fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		  os = new ObjectOutputStream(fos);
		  os.writeObject(TODOList);
		  //savedDebugText.setText("Saved to file");
		  os.close();
		} catch (Exception e) {
			savedDebugText.setText("exception thrown: " + e);
			e.printStackTrace();
		}
		
	}


	public static ArrayList<TODO> loadFromFile(String FILENAME, Context ctx) throws ClassNotFoundException {
		ArrayList<TODO> loadedList = new ArrayList<TODO>();
		
		ObjectInputStream ois = null;
		
		try {
			FileInputStream fis = ctx.openFileInput(FILENAME);
			ois = new ObjectInputStream(fis);
			loadedList = (ArrayList<TODO>) ois.readObject();
		    
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
		
		return loadedList;
	}
	 */	
	
	/*
	
	public void emailSelected() {
		String emailBody = "";
	
		ListView TODOListView = (ListView) findViewById(R.id.TodoListView); 
	
		SparseBooleanArray checkedItemPositions = TODOListView.getCheckedItemPositions();
	    int itemCount = TODOListView.getCount();
	    
	    for(int i=itemCount-1; i >= 0; i--){
	    	
	        if(checkedItemPositions.get(i)) {
		    	emailBody += TODOList.get(i).getText() + "[ ";
		    	if (TODOList.get(i).getStatus()) {
		    		emailBody += "X";
		    	}
		    	else {
		    		emailBody += " ";
		    	}
		    	emailBody += "]\n"; 
	        }
	    }
		Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("message/rfc822");
		email.putExtra(Intent.EXTRA_SUBJECT, "My ToDos");
		email.putExtra(Intent.EXTRA_TEXT, emailBody);
		try {
		    startActivity(Intent.createChooser(email, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	
	*/
	
}
