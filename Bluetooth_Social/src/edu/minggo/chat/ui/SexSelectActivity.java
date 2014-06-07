package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.SexSelectListAdapter;
import edu.minggo.chat.control.BluetoothChatService;


public class SexSelectActivity extends Activity {
	private Button confirm;
	private ListView sexList;
	private SexSelectListAdapter listAdapter;
	private ImageView sexSelect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置窗口tittle个性化
		setContentView(R.layout.sex_list);
		
		WindowManager.LayoutParams lp=this.getWindow().getAttributes();   
        lp.dimAmount=0.7f;   
        this.getWindow().setAttributes(lp);   
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  

		confirm = (Button)findViewById(R.id.sex_confirm);
		sexList = (ListView)findViewById(R.id.sex_list_selection);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(BluetoothChatService.nowuser.getSex()==null){
					Intent intent = new Intent();
					intent.putExtra("sexSelect", "男");
					setResult(Activity.RESULT_OK, intent);
				}
				finish();
			}
		});
		sexList.setOnItemClickListener(new SexSelectListener());
		
		listAdapter = new SexSelectListAdapter(getApplicationContext(), getIntent().getStringExtra("sex"));
		
		sexList.setAdapter(listAdapter);
		
	}
	public class SexSelectListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			sexSelect = (ImageView)view.findViewById(R.id.sex_select);
			Intent intent = new Intent();
			if(position==0){
				sexSelect.setImageResource(R.drawable.radio_checked);
				intent.putExtra("sexSelect", "男");
			}else if(position==1){
				sexSelect.setImageResource(R.drawable.radio_checked);
				intent.putExtra("sexSelect", "女");
			}
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		
	}
	
}
