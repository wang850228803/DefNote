package zinus.note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zinus.presenter.SearchPresenter;
import zinus.view.ISearchActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends Activity implements ISearchActivity, OnScrollListener{
	
	private Spinner spinner;
    private EditText editText;
    private Button searchBtn;
    private ListView listView;
    
    private SimpleAdapter resultAdapter;
    private List<Map<String, Object>> resultData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search);
		
		// get views
		spinner = (Spinner) findViewById(R.id.searchTypeSpinr);
		editText = (EditText) findViewById(R.id.searchKeyword);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		listView = (ListView) findViewById(R.id.searchResultList);
		
		// setup views
		viewSetup();
        
	}

	private void viewSetup() {
		// initialize spinner for search type: books, movies, music
		ArrayAdapter<CharSequence> spinrAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_types, android.R.layout.simple_spinner_item);
		spinrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(spinrAdapter);
        
        // have search edit text listens to enter key, for searching
        editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent Event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					presenter.search(
							spinner.getSelectedItem().toString(),
							editText.getText().toString());
                }
                return false;
			}
        });
        
        // have search button listens, for searching
        searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				presenter.search(
						spinner.getSelectedItem().toString(),
						editText.getText().toString());
			}
        });
        
        // initialize the list view for search results
        resultData = new ArrayList<Map<String, Object>>();
        resultAdapter = new SimpleAdapter(this, resultData,
        		R.layout.search_result_list_item,
        		new String[] {"preview", "title", "year", "people"},
        		new int[] {R.id.previewImgView, R.id.titleTxtView, R.id.YearTxtView
        		,R.id.peopleTxtView});
        resultAdapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if( (view instanceof ImageView) & (data instanceof Bitmap) ) {  
		                ImageView iv = (ImageView) view;  
		                Bitmap bm = (Bitmap) data;  
		                iv.setImageBitmap(bm);
		                return true;
	                } 
				return false;
			}
        });
        listView.setAdapter(resultAdapter);
        listView.setOnScrollListener(this);
        
        //listView.setOnItemLongClickListener(listener);
	}
	
	public void makeToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showResult(List<Map<String, Object>> _resultData) {
		resultData.clear();
		for(Map<String, Object> map: _resultData)
			resultData.add(map);
		resultAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void updateResult(List<Map<String, Object>> _resultData) {
		resultAdapter.notifyDataSetChanged();
	}
	
	private int currentIndex;
	private int lastVisibleItem;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,  
            int visibleItemCount, int totalItemCount) {
		lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (lastVisibleItem == resultAdapter.getCount()  
                && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
			presenter.loadNextPage();
	}
	
	private SearchPresenter presenter = new SearchPresenter(this);

	
}
