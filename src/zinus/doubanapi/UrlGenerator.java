package zinus.doubanapi;

public class UrlGenerator {
	public static final String PREFIX = "https://api.douban.com";
	public static final String SEARCH_MOVIE = "/v2/movie/search?";
	public static final String SEARCH_BOOK = "/v2/book/search?";
	public static final String SEARCH_MUSIC = "/v2/music/search?";
	public static final String SEARCHBY_KEYWORD_PREFIX = "q=";
	public static final String MY_API_KEY = "&apikey=036e35401a984e290a4a9fc04fc8ea7b";
	
	public static String searchMovieByKeyword(String key) {
		return PREFIX + SEARCH_MOVIE + SEARCHBY_KEYWORD_PREFIX + key +
				MY_API_KEY;
	}
	
	public static String searchBookByKeyword(String key) {
		return PREFIX + SEARCH_BOOK + SEARCHBY_KEYWORD_PREFIX + key +
				MY_API_KEY;
	}
	
	public static String searchMusicByKeyword(String key) {
		return PREFIX + SEARCH_MUSIC + SEARCHBY_KEYWORD_PREFIX + key +
				MY_API_KEY;
	}
	
	public static String searchwithTypenKey(String type, String key) {
		if(key.length() == 0 || key == null)
			return null;
		key = key.replace(' ', '_');
		if(type.equals("电影"))
			return searchMovieByKeyword(key);
		else if (type.equals("书籍"))
			return searchBookByKeyword(key);
		else if (type.equals("音乐"))
			return searchMusicByKeyword(key);
		else
			return null;
	}
	
	
}
