package com.tgfc.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tgfc.app.SecondActivity.AnalysisTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private List<Page> data = new ArrayList<Page>();
	
	public static final int ANALYSIS_RESPONSE = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANALYSIS_RESPONSE:
				String response = (String) msg.obj;
				//analysisResponse(response);
				AnalysisTask aTask = new AnalysisTask();
				aTask.execute(response);
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Button back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		MainActivity.this.finish();
        	}
        });
        getHttpURLConnection();
    }
    //获取网页源码文本
    private void getHttpURLConnection() {
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			HttpURLConnection connection = null;
    			try {
    				URL url = new URL("http://club.tgfcer.com");
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
    /*
    private void analysisResponse(String response) {
    	
    	Pattern p = Pattern.compile("h2>.*?href=\"(.*?)\">(.*?)<.*?/h2");
    	Matcher m = p.matcher(response);
    	while (m.find()) {
    		data.add(new Page(m.group(2), m.group(1)));
    	}
    	PageAdapter adapter = new PageAdapter(MainActivity.this, R.layout.page_item, data);
    	ListView listView = (ListView) findViewById(R.id.list_view);
    	listView.setAdapter(adapter);
    	
    	//ListView点击事件
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Page page = data.get(position);
    			//Toast.makeText(MainActivity.this, page.getUrl(), Toast.LENGTH_SHORT).show();
    			String post = page.getUrl();
    			Intent intent = new Intent(MainActivity.this, SecondActivity.class);
    			intent.putExtra("post_data", post);
    			startActivity(intent);
    		}
    	});
    }
    */


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
        return super.onOptionsItemSelected(item);
    }
    //后台完成数据筛选
    class AnalysisTask extends AsyncTask<String, Void, Void> {
    	
    	@Override
		protected Void doInBackground(String... params) {
    		try {
    			//正则表达式抓取所需元素：板块名+链接
    	    	Pattern p = Pattern.compile("h2>.*?href=\"(.*?)\">(.*?)<.*?/h2");
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
        	PageAdapter adapter = new PageAdapter(MainActivity.this, R.layout.page_item, data);
        	ListView listView = (ListView) findViewById(R.id.list_view);
        	listView.setAdapter(adapter);
			//Page page = data.get(1);
			//Toast.makeText(SecondActivity.this, result, Toast.LENGTH_LONG).show();
        	
        	//ListView点击事件
        	listView.setOnItemClickListener(new OnItemClickListener() {
        		@Override
        		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        			Page page = data.get(position);
        			//Toast.makeText(MainActivity.this, page.getUrl(), Toast.LENGTH_SHORT).show();
        			String post = page.getUrl();
        			String title = page.getTitle();
        			Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        			intent.putExtra("post_data", post);
        			intent.putExtra("post_title", title);
        			startActivity(intent);
        		}
        	});
    	}

		
    }
}
