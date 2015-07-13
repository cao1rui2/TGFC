package com.tgfc.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {
	

	
	private List<Page> data = new ArrayList<Page>();
	
	
	public static final int ANALYSIS_RESPONSE = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANALYSIS_RESPONSE:
				String response = (String) msg.obj;
				//analysisResponse(response);
				//String test = "<span id=\"thread_7108976\"><a href=\"thread-7108976-1-1.html\">\r\n完了    今天泳池玩大了   手机疑似进水？？？    sim 卡  tf  卡都没了</a></span>";


				AnalysisTask aTask = new AnalysisTask();
				aTask.execute(response);
			}
		}
	};	
	
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
    	String before = "http://club.tgfcer.com/";
        Intent intent = getIntent();
        String post = intent.getStringExtra("post_data");
        String urlString = before + post;
        
        URL perUrl = null;
		try {
			perUrl = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        getHttpURLConnection(perUrl);
    }
	
    private void getHttpURLConnection(final URL url) {
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			HttpURLConnection connection = null;
    			try {
    				//URL url = new URL("http://club.tgfcer.com/forum-10-1.html");
    				connection = (HttpURLConnection) url.openConnection();
    				connection.setRequestMethod("GET");
    				connection.setConnectTimeout(8000);
    				connection.setReadTimeout(8000);
    				InputStream in = connection.getInputStream();
    				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gbk"));
    				StringBuilder response = new StringBuilder();
    				String line;
    				while((line = reader.readLine()) != null) {
    					response.append(line);
    				}
    				Message message = new Message();
    				message.what = ANALYSIS_RESPONSE;
    				message.obj = response.toString();
    				handler.sendMessage(message);
    			} catch (Exception e) {
    				e.printStackTrace();
    			} finally {
    				if (connection != null) {
    					connection.disconnect();
    				}
    			}
    		}
    	}).start();
    }
    
    private List<Page> analysisResponse(String response) {
    	
    	Pattern p = Pattern.compile("<a href=\"(.*?)\">\r\n(.*?)</a></span>");
    	Matcher m = p.matcher(response);
    	while (m.find()) {
    		data.add(new Page(m.group(2), m.group(1)));
    	}
    	//PageAdapter2 adapter = new PageAdapter2(SecondActivity.this, R.layout.page_item2, data);
    	//ListView listView = (ListView) findViewById(R.id.list_view2);
    	//listView.setAdapter(adapter);
    	return data;
		//Page page = data.get(1);
		//Toast.makeText(SecondActivity.this, page.getTitle(), Toast.LENGTH_SHORT).show();
    	
    	//ListView点击事件
    	/*
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Page page = data.get(position);
    			//Toast.makeText(MainActivity.this, page.getTitle(), Toast.LENGTH_SHORT).show();
    			String post = page.getUrl();
    			Intent intent = new Intent(SecondActivity.this, SecondActivity.class);
    			intent.putExtra("psot_data", post);
    			startActivity(intent);
    		}
    	});
    	*/	
    }
    
    class AnalysisTask extends AsyncTask<String, Void, Void> {
    	
    	@Override
		protected Void doInBackground(String... params) {
    		try {
    			
    	    	Pattern p = Pattern.compile("<span id=\".*?\"><a href=\"(.*?)\">\r\n(.*?)</a></span>");
    	    	Matcher m = p.matcher(params[0]);
    	    	while (m.find()) {
    	    		data.add(new Page(m.group(2), m.group(1)));
    	    		
    	    	}
    	    	
    	    	
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
			return null;
		}
    	
    	@Override
    	protected void onPostExecute(Void result) {
        	PageAdapter2 adapter = new PageAdapter2(SecondActivity.this, R.layout.page_item2, data);
        	ListView listView = (ListView) findViewById(R.id.list_view2);
        	listView.setAdapter(adapter);
			//Page page = data.get(1);
			//Toast.makeText(SecondActivity.this, result, Toast.LENGTH_LONG).show();
    	}

		
    }
    

}
