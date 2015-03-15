package zinus.presenter;

import java.util.List;
import java.util.Map;

import zinus.imodel.ISearchModel;
import zinus.model.SearchModel;
import zinus.view.ISearchActivity;

public class SearchPresenter {
	ISearchActivity view;
	ISearchModel model;
	
	public SearchPresenter(ISearchActivity _view) {
		view = _view;
		model = new SearchModel(this);
	}
	
	public void search(String type, String key) {
		model.search(type, key);
	}
	
	public void loadNextPage() {
		
	}
	
	public void haveResult(List<Map<String, Object>> result) {
		view.showResult(result);
	}
	
	public void haveError(String msg) {
		view.makeToast(msg);
	}
}
