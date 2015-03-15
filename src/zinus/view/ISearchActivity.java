package zinus.view;

import java.util.List;
import java.util.Map;

public interface ISearchActivity {
	void showResult(List<Map<String, Object>> _resultData);
	void updateResult(List<Map<String, Object>> _resultData);
	void makeToast(String msg);
}
