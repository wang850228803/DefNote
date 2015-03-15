package zinus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zinus.doubanapi.SearchJsonParser;
import zinus.doubanapi.UrlGenerator;
import zinus.imodel.ISearchModel;
import zinus.netutil.NetUtil;
import zinus.presenter.SearchPresenter;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class SearchModel implements ISearchModel{
	
	SearchPresenter presenter;
	SearchJsonParser jsonParser = null;
	
	private String type = null;
	private String url = null;
	private String json = null;
	private List<Map<String, Object>> result = null;
	private ArrayList<String> imgUrlCache = null;
	
	public SearchModel(SearchPresenter _presenter) {
		presenter = _presenter;
	}
	
	@Override
	public void search(String type, String key) {
		this.type = type;
		// get url
		url = UrlGenerator.searchwithTypenKey(type, key);
		if(url == null) {
			presenter.haveError("Can't make good url with type: " + type +
					" and key: " + key + "!!");
			return;
		}
		
		// get json from url
		new Thread(search).start();
	}
	
	Runnable search = new Runnable() {
		
		@Override
		public void run() {
			// get json from url
			json = NetUtil.getJsonFrom(url);
			if((json = NetUtil.getJsonFrom(url)).length() == 0) {
				errorHandler.sendMessage(
						wrapMsg("error", "Getting no stuff from url: " + url));
				return;
			}
			
			Message msg = new Message();
			// get data from json
			try {
				if(jsonParser == null)
					jsonParser = new SearchJsonParser(getPosterSize());
				Bundle resultInBundle = jsonParser.parseJson(json);
				msg.setData(resultInBundle);
				
				resultHandler.sendMessage(msg);
				return;
				
			} catch (Exception e) {
				e.printStackTrace();
				errorHandler.sendMessage(
						wrapMsg("error", e.toString()));
				return;
			}
		}
	};
	
	@SuppressLint("HandlerLeak")
	Handler resultHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 0:
				Bundle resultInBundle = msg.getData();
				result = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = null;
				ArrayList<String> titles = (ArrayList<String>) resultInBundle.get("title");
				ArrayList<String> years = (ArrayList<String>) resultInBundle.get("year");
				ArrayList<String> people = (ArrayList<String>) resultInBundle.get("people");
				imgUrlCache = (ArrayList<String>) resultInBundle.get("imgUrl");
				for(int i = 0; i < titles.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("title", titles.get(i));
					map.put("year", years.get(i));
					map.put("people", people.get(i));
					result.add(map);
				}
				presenter.haveResult(result);
				if(wantPoster())
					new Thread(getPoster).start();
				break;
			case 1:
				presenter.haveResult(result);
			}
		}
	};
	
	Runnable getPoster = new Runnable() {

		@Override
		public void run() {
			int i = 0;
			for(Map<String, Object> map: result) {
				Bitmap bmp = NetUtil.getBmpFrom(imgUrlCache.get(i));
				if(bmp == null) {
					errorHandler.sendMessage(
							wrapMsg("error", "getting null from the imgUrl: " +
									imgUrlCache.get(i)));
				}
				map.put("preview", bmp);
				i++;
			}
			Message msg = new Message();
			msg.what = 1;
			resultHandler.sendMessage(msg);
		}
		
	};
	
	// error handler
	@SuppressLint("HandlerLeak")
	Handler errorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String errorMsg = null;
			if((errorMsg = msg.getData().getString("error")).length() != 0) {
				presenter.haveError(errorMsg);
				return;
			}
			else
				return;
		}
	};
	
	public boolean wantPoster() {
		return true;
	}
	
	public String getPosterSize() {
		return "large";
	}
	
	public Message wrapMsg(String key, String value) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString(key, value);
		msg.setData(data);
		return msg;
	}
}
