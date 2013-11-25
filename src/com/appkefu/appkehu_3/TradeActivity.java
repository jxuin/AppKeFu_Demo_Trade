package com.appkefu.appkehu_3;

import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFSLog;

import android.os.Bundle;
import android.os.IBinder;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class TradeActivity extends TabActivity {
	
	/*
	 ����ʹ�ð����μ���http://appkefu.com/AppKeFu/tutorial-android.html
	 
	 ��Ҫʹ��˵����
	 ��1������http://appkefu.com/AppKeFu/admin/��ע��/����Ӧ��/����ͷ���������ȡ��appkey����AnroidManifest.xml
	 		�е�com.appkefu.lib.appkey
	 ��2��������ʵ�Ŀͷ�����ʼ��mKefuUsername
	 ��3�������� KFInterfaces.visitorLogin(this); ������¼
	 ��4��������chatWithKeFu(mKefuUsername);��ͷ��Ự������mKefuUsername��Ҫ�滻Ϊ��ʵ�ͷ���
	 ��5����(��ѡ)
	 	//���ͷ�����״̬ (�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
    	KFInterfaces.checkKeFuIsOnline(mKefuUsername, this);
      		
    	//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
    	KFInterfaces.setVisitorNickname("�ÿ�1", this);
	 */
	
	TabHost tabHost; 
	private RadioButton main_tab_home, main_tab_catagory, main_tab_car,
			main_tab_buy, main_tab_more;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTab();
        init();
        ExitManager.getInstance().addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFSLog.d("onStart");
		
		IntentFilter intentFilter = new IntentFilter();
		//�����������ӱ仯���
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //������Ϣ
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);
        //�����ͷ�����״̬֪ͨ
        intentFilter.addAction(KFMainService.ACTION_KEFU_ONLINE_CHECK_RESULT);
        
        registerReceiver(mXmppreceiver, intentFilter); 
        
        Intent intent = new Intent(KFMainService.ACTION_CONNECT);
        bindService(intent, mMainServiceConnection, Context.BIND_AUTO_CREATE);
        
	}


	@Override
	protected void onStop() {
		super.onStop();
		KFSLog.d("onStop");
		
		unbindService(mMainServiceConnection);
        unregisterReceiver(mXmppreceiver);
	}
	
	//����������״̬����ʱͨѶ��Ϣ���ͷ�����״̬
		private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
		{
	        public void onReceive(Context context, Intent intent) 
	        {
	            String action = intent.getAction();
	            //����������״̬
	            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//��������״̬
	            {
	                updateStatus(intent.getIntExtra("new_state", 0));        
	            }
	            //��������ʱͨѶ��Ϣ
	            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//������Ϣ
	            {
	            	String body = intent.getStringExtra("body");
	            	String from = StringUtils.parseName(intent.getStringExtra("from"));
	            	
	            	KFSLog.d("body:"+body+" from:"+from);
	            }
	            //�������ͷ�����״̬
	            else if(action.equals(KFMainService.ACTION_KEFU_ONLINE_CHECK_RESULT))
	            {            	
	            	boolean isonline = intent.getBooleanExtra("isonline", false);
	            	if(isonline)
	            	{
	            		//mChatBtn.setText("��ѯ�ͷ�(����)");
	            	}
	            	else
	            	{
	            		//mChatBtn.setText("��ѯ�ͷ�(����)");
	            	}
	            }
	        }
	    };
	    
	    //
	    private ServiceConnection mMainServiceConnection = new ServiceConnection() {
	        public void onServiceConnected(ComponentName className, IBinder service) {

	        	com.appkefu.lib.service.KFMainService$LocalBinder binder = 
	        			(com.appkefu.lib.service.KFMainService$LocalBinder) service;
	        	KFMainService mainService = binder.getService();
	            updateStatus(mainService.getConnectionStatus());
	            
	        }

	        public void onServiceDisconnected(ComponentName className) {

	        }
	    };
		

	  //���ݼ����������ӱ仯������½�����ʾ
	    private void updateStatus(int status) {

	    	switch (status) {
	            case KFXmppManager.CONNECTED:
	            	KFSLog.d("connected");
	            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)");
	            	
	            	//���ͷ�����״̬ (�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
	        		//KFInterfaces.checkKeFuIsOnline(mKefuUsername, this);
	        		
	        		//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
	        		//KFInterfaces.setVisitorNickname("�ÿ�1", this);

	                break;
	            case KFXmppManager.DISCONNECTED:
	            	KFSLog.d("disconnected");
	            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(δ����)");
	                break;
	            case KFXmppManager.CONNECTING:
	            	KFSLog.d("connecting");
	            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(��¼��...)");
	            	break;
	            case KFXmppManager.DISCONNECTING:
	            	KFSLog.d("connecting");
	            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ǳ���...)");
	                break;
	            case KFXmppManager.WAITING_TO_CONNECT:
	            case KFXmppManager.WAITING_FOR_NETWORK:
	            	KFSLog.d("waiting to connect");
	            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ȴ���)");
	                break;
	            default:
	                throw new IllegalStateException();
	        }
	    }
	    
	public void init(){
    	main_tab_home=(RadioButton)findViewById(R.id.main_tab_home);
    	main_tab_catagory = (RadioButton) findViewById(R.id.main_tab_catagory);
		main_tab_car = (RadioButton) findViewById(R.id.main_tab_car);
		main_tab_buy = (RadioButton) findViewById(R.id.main_tab_buy);
		main_tab_more = (RadioButton) findViewById(R.id.main_tab_more);
		main_tab_home.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("home");

			}
		});

		main_tab_catagory.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("catagory");

			}
		});
		main_tab_car.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("car");

			}
		});
		main_tab_buy.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("buy");

			}
		});
		main_tab_more.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("more");

			}
		});
    }
    
    public void initTab(){
    	tabHost=getTabHost();
    	tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(new Intent(this, HomeActivity.class)));
    	tabHost.addTab(tabHost.newTabSpec("catagory").setIndicator("catagory")
				.setContent(new Intent(this, CategoryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("car").setIndicator("car")
				.setContent(new Intent(this, CarActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("buy").setIndicator("buy")
				.setContent(new Intent(this, BuyActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("more")
				.setContent(new Intent(this, MoreActivity.class)));
    }
    
    public boolean dispatchKeyEvent( KeyEvent event) {
		int keyCode=event.getKeyCode();
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getRepeatCount() == 0) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						TradeActivity.this);
				alertDialog.setTitle(TradeActivity.this
						.getString(R.string.app_close));
				alertDialog.setPositiveButton(TradeActivity.this
						.getString(R.string.btn_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ExitManager.getInstance().exit();
							}
						});
				alertDialog.setNegativeButton(TradeActivity.this
						.getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialog.show();
			}
		}
		return super.dispatchKeyEvent(event);
	}


}






















