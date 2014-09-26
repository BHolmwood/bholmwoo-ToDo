package ca.ualberta.cs.bholmwooToDo;

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

public class ArchiveViewActivity extends TODOListActivity {
	/*	The archive. Shows a ToDo list with check boxes.
	 * 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive_view);

		// Set context for saving to file
		final Context ctx = this;
		
		
		// Set up buttons, views, and controllers
		ListView = (ListView) findViewById(R.id.ArchListView);
		
		checkedCountText = (TextView) findViewById(R.id.ArchCheckedCount);
		uncheckedCountText = (TextView) findViewById(R.id.ArchUncheckedCount);
		
		ListController = new TODOListController();
		ArchController = new TODOListController();
		
		// Load both the active ToDo list and the archive list from file.
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
        ListView.setAdapter(ListViewAdapter);
        ListViewAdapter.notifyDataSetChanged();
		
		registerForContextMenu(ListView);
      
		
		// Adapted from //http://wptrafficanalyzer.in/blog/deleting-selected-items-from-listview-in-android/ 2014-09-21
		ListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	ListViewAdapter.notifyDataSetChanged();
                SparseBooleanArray checkedItemPositions = ListView.getCheckedItemPositions();
            	ArchController.updateChecked(checkedItemPositions);
            	ArchController.saveInFile(ARCHFILENAME, ctx);
            	updateStats();
        
            }

        });
        
        setChecked(ListView);
        updateStats();
	}
	
	public void updateStats() {
		/* Updates the checked and unchecked counts and displays them on screen.
		 */
		
        SparseBooleanArray checkedItemPositions = ListView.getCheckedItemPositions();
        int itemCount = ListView.getCount();

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
		/*	Updates the check boxes to reflect the current status of each ToDo.
		 */
		
		for (int i = ( ArchList.size() - 1 ); i >= 0; i--) { 
			TODOListView.setItemChecked(i, (ArchList.get(i).getStatus()) );
		}
		
	}
	
	// Adapted from http://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/ 2014-09-21
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
		
		SparseBooleanArray checkedItemPositions = ListView.getCheckedItemPositions();
		
		if (item.getTitle() == "Unarchive") {
			ActiveList.add(ArchList.get(itemIndex));
			ArchList.remove(itemIndex);
			ListController.saveInFile(TODOFILENAME, this);
			ArchController.saveInFile(ARCHFILENAME, this);
			ListViewAdapter.notifyDataSetChanged();;
			checkedItemPositions.clear();
			setChecked(ListView);
			updateStats();
		}
		else if (item.getTitle() == "Remove") {
	    	ArchList.remove(itemIndex);
			ArchController.saveInFile(ARCHFILENAME, this);
            ListViewAdapter.notifyDataSetChanged();
            checkedItemPositions.clear();
            setChecked(ListView);
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
			
			SparseBooleanArray checkedItemPositions = ListView.getCheckedItemPositions();
			checkedItemPositions.clear();
			
			ArchController.saveInFile(ARCHFILENAME, this);
			
			ListViewAdapter.notifyDataSetChanged();
			
            updateStats();
			
		}
		else if (id == R.id.archEmail) {
	        Intent emailTODOs = new Intent(this, EmailActivity.class);
	        
	        emailTODOs.putExtra("saveFileName", ARCHFILENAME);
	        
	        startActivity(emailTODOs);
			
		}
		else if (id == R.id.archEmailAll) {
	        
			emailAllTODOs();
		}
		return super.onOptionsItemSelected(item);
	}

}