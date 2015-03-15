package zinus.netutil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetUtil {
	public static String getJsonFrom(String url) {
		HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        StringBuilder entityStringBuilder = new StringBuilder();
        // 加用户代理, 否则返回500错误.
        get.addHeader("User-Agent", "527ee844");
        try {
        	// Http GET
            HttpResponse res = client.execute(get);
            
            if (res.getStatusLine().getStatusCode() == 200) {  
                HttpEntity entity = res.getEntity();
                if(entity != null) {
                	
                	BufferedReader bufferedReader = new BufferedReader(
                			new InputStreamReader(entity.getContent(), HTTP.UTF_8),
                			8 * 1024);
                	String line = null;
                	
                	while((line = bufferedReader.readLine()) != null)
                		entityStringBuilder.append(line + "\n");
                }
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
        } finally{  
            //关闭连接 ,释放资源  
            client.getConnectionManager().shutdown();  
        }
        
        return entityStringBuilder.toString();
	}
	
	public static Bitmap getBmpFrom(String urlInString) {
		try {
		URL url = new URL(urlInString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		conn.connect();
		
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while((length = in.read(buffer)) != -1)
			bos.write(buffer, 0, length);
		byte[] imgData = bos.toByteArray();
		bos.close();
		in.close();
		return BitmapFactory.decodeByteArray(
				imgData, 0, imgData.length);
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
