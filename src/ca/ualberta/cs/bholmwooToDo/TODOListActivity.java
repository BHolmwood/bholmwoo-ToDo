package ca.ualberta.cs.bholmwooToDo;


import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


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
}
