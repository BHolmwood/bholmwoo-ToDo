package ca.ualberta.cs.bholmwooToDo;

import java.util.ArrayList;

import android.app.Activity;
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

public class ArchiveViewActivity extends Activity {

	private static final String TODOFILENAME = "TODOLists.sav";
	private static final String ARCHFILENAME = "ArchLists.sav";
	
	ArrayList<TODO> ArchList = new ArrayList<TODO>();
	ArrayAdapter<TODO> ListViewAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive_view);
		
		ListView TODOListView = (ListView) findViewById(R.id.ArchTodoListView);
		
		registerForContextMenu(TODOListView);
		
		/*
		try {
			ArchList = MainActivity.loadFromFile(TODOFILENAME);
		} catch (ClassNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		ListViewAdapter = new ArrayAdapter<TODO>(this, android.R.layout.simple_list_item_multiple_choice, ArchList);

        
        TODOListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	updateChecked();
        
            }

        });
        
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
            ArchList.remove(item.getItemId());
            ListViewAdapter.notifyDataSetChanged();
            updateChecked();
	    } 
	    /*
	      else if (...) {
	        // code
	    } */
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
}
