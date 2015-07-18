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

import com.tgfc.app.SecondActivity.AnalysisTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ThirdActivity extends Activity {
	
	private List<Neirong> data = new ArrayList<Neirong>();
	private ProgressBar progressBar;
	private ListView listView;
	private View loadMoreView;
	private NeirongAdapter adapter;
	private int visibleLastIndex = 0;   //最后的可视项索引 
    private int itemCount;
    private int i = 2;
    private TextView loadView;
	
	
	public static final int ANALYSIS_RESPONSE = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANALYSIS_RESPONSE:
				String response = (String) msg.obj;
				analysis(response);
				adapter.notifyDataSetChanged();
				progressBar.setVisibility(View.GONE);
				//setListAdapter();
				//listView.setSelection(15 * (i - 2));
				//AnalysisTask aTask = new AnalysisTask();
				//aTask.execute(response);
				//TextView responseText = (TextView) findViewById(R.id.response);
				//responseText.setText(response);
			}
		}
	};	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thi);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Button back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		ThirdActivity.this.finish();
        	}
        });
        
        
        loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
        listView = (ListView) findViewById(R.id.list_view3);
        listView.addFooterView(loadMoreView);
        loadView = (TextView) findViewById(R.id.loading);
        adapter = new NeirongAdapter(ThirdActivity.this, R.layout.neirong_item, data);
        listView.setAdapter(adapter);
        
    	String before = "http://club.tgfcer.com/";
        Intent intent = getIntent();
        String post = intent.getStringExtra("post_data");
        String title = intent.getStringExtra("post_title");
        String strnum = intent.getStringExtra("post_pagenum");
        final int pagenum = Integer.parseInt(strnum);
        final String urlString = before + post;
        
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        
        URL perUrl = null;
		try {
			perUrl = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final URL link = perUrl;
		
		
		getHttpURLConnection(perUrl);
		
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				//itemCount = visibleItemCount;
				visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
			}
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int itemLastIndex = adapter.getCount() - 1;
				int lastIndex = itemLastIndex + 1;
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
					
					if (i <= pagenum) {
			        	String num = i + "";
			        	String everystr = urlString.replaceFirst("-1-1.html", "-" + num + "-1.html");
			        	Log.d("test", everystr);
			        	URL everyUrl = null;
			        	try {
							everyUrl = new URL(everystr);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        	final URL finUrl = everyUrl;
			        	getHttpURLConnection(finUrl);
			        	i++;
					} else {
						loadView.setText("已无更多");
					}
				}
			}

		});
		/*
        int i;
        for (i=1; i<=pagenum; i++) {
        	String num = i + "";
        	String everystr = urlString.replaceFirst("-1-1.html", "-" + num + "-1.html");
        	Log.d("test", everystr);
        	URL everyUrl = null;
        	try {
				everyUrl = new URL(everystr);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	final URL finUrl = everyUrl;
        	getHttpURLConnection(finUrl);
        }
        //setListAdapter();
        */
        Button refresh = (Button) findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		data.removeAll(data);
        		i = 2;
        		getHttpURLConnection(link);
        		loadView.setText("正在加载更多。。。");
        	}
        });
        
        

    }
	
    private synchronized void getHttpURLConnection(final URL url) {
    	
    	progressBar.setVisibility(View.VISIBLE);
    	
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
    /*
    class AnalysisTask extends AsyncTask<String, Void, Void> {
    	
    	@Override
		protected Void doInBackground(String... params) {
    		try {
    			//此处正则表达式极不严谨，待优化。
    	    	Pattern p = Pattern.compile("id\\)\">(.{1,20})</a></cite>[\\s\\S]*?id=\"postmessage_\\d{1,9}\"[\\s\\S]*?>([\\s\\S]*?)</div>\\W");
    	    	Matcher m = p.matcher(params[0]);
    	    	while (m.find()) {
    	    		String neirong = decode(m.group(2));
    	    		data.add(new Neirong(m.group(1), neirong));
    	    		
    	    	}
    	    	
    	    	
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
			return null;
		}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		
        	NeirongAdapter adapter = new NeirongAdapter(ThirdActivity.this, R.layout.neirong_item, data);
        	ListView listView = (ListView) findViewById(R.id.list_view3);
        	listView.setAdapter(adapter);
			//Page page = data.get(1);
			//Toast.makeText(SecondActivity.this, result, Toast.LENGTH_LONG).show();
        	
        	listView.setOnItemClickListener(new OnItemClickListener() {
        		@Override
        		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        			
        		}
        	});
        	
        	progressBar.setVisibility(View.GONE);
        	
    	}

		
    }
    */
    
    public void analysis(String response) {
    	Pattern p = Pattern.compile("id\\)\">(.{1,20})</a></cite>[\\s\\S]*?id=\"postmessage_\\d{1,9}\"[\\s\\S]*?>([\\s\\S]*?)</div>[^\\w<]");
    	Matcher m = p.matcher(response);
    	while (m.find()) {
    		String neirong = decode(m.group(2));
    		data.add(new Neirong(m.group(1), neirong));
    	}
    }
    /*
    public void setListAdapter() {
    	adapter = new NeirongAdapter(ThirdActivity.this, R.layout.neirong_item, data);
    	//listView = (ListView) findViewById(R.id.list_view3);
    	listView.setAdapter(adapter);
    	
    	progressBar.setVisibility(View.GONE);
    }
    */
    
    //之前不知道有fromHtml方法，自己竟然手动替换。。。
    public String decode(String str) {
    	str = str.replace("\"DarkRed\"", "#8B0000");
    	/*
    	str = str.replace("<br />", "\n");
    	str = str.replace("<br>", "\n");
    	str = str.replace("&gt", ">");
    	str = str.replace("&lt", "<");
    	str = str.replace("&nbsp;", " ");
    	str = str.replace("&quot;", "\"");
    	str = str.replace("''", "'");
    	str = str.replace("&amp", "&");
    	str = str.replaceAll("<font color[\\s\\S]*?</font></font>", "<posted by wap>\n");
    	str = str.replaceAll("<div class=\"quote[\\s\\S]*?<blockquote>", "引用:");
    	str = str.replaceAll("</blockquote></div>", "\n\n回复:\n"); */
    	return str;
    }
    

}
