package edu.minggo.chat.util;

import edu.minggo.chat.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
/**
 * 通讯录的右边导拉菜单
 * @author minggo
 * @created 2013-1-30下午07:30:34
 */
public class SideBar extends View {  
		private char[] l;  
	    private SectionIndexer sectionIndexter = null;  
	    private ListView list;  
	    private TextView mDialogText;
	    private final int m_nItemHeight = 20; 
	    public SideBar(Context context) {  
	        super(context);  
	        init();  
	    }  
	    public SideBar(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        init();  
	    }  
	    private void init() {  
	        l = new char[] {'搜', '$','!','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',  
	                'T', 'U', 'V', 'W', 'X', 'Y', 'Z','#' };  
	    }  
	    public SideBar(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle); 
	        init();  
	    }  
	    public void setListView(ListView _list) {
	        list = _list;  
	        sectionIndexter = (SectionIndexer) ((HeaderViewListAdapter)_list.getAdapter()).getWrappedAdapter();  
	    }  
	    public void setTextView(TextView mDialogText) {  
	    	this.mDialogText = mDialogText;  
	    }  
	    
	    public boolean onTouchEvent(MotionEvent event) {  
	        super.onTouchEvent(event);  
	       
	        int i = (int) event.getY();  
	        int idx = i / m_nItemHeight;  
	        if (idx >= l.length) {  
	            idx = l.length - 1;  
	        } else if (idx < 0) {  
	            idx = 0;  
	        }  
	        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {  
	        	this.setBackgroundResource(R.drawable.mm_text_bg_trans);
	        	mDialogText.setVisibility(View.VISIBLE);
	        	mDialogText.setTextColor(Color.WHITE);
	        	mDialogText.setText(""+l[idx]);
	            if (sectionIndexter == null) {  
	                sectionIndexter = (SectionIndexer) ((HeaderViewListAdapter)list.getAdapter()).getWrappedAdapter();  
	            }  
	            int position = sectionIndexter.getPositionForSection(l[idx]);  
	            if (position == -1) {  //没有该字母开头的
	            	mDialogText.setText(""+l[idx]);
	            	mDialogText.setTextColor(Color.GRAY);
	            	if(idx==0){
	            		list.setSelection(position+1);  
	            	}
	            	return true;
	            }  
	            list.setSelection(position+1);  
	        }else{
	        	mDialogText.setVisibility(View.INVISIBLE);
	        	this.setBackgroundResource(R.drawable.mm_text_bg_trans_normal);
	        }  
	        return true;  
	    }  
	    protected void onDraw(Canvas canvas) {
	    	
	        Paint paint = new Paint();  
	        paint.setColor(0xff595c61);  
	        paint.setTextSize(16);  
	        paint.setTextAlign(Paint.Align.CENTER);  
	        float widthCenter = getMeasuredWidth() / 2;  
	        for (int i = 0; i < l.length; i++) {  
	        	canvas.drawText(String.valueOf(l[i]), widthCenter, widthCenter + (i * m_nItemHeight), paint);  
	        }  
	        super.onDraw(canvas);  
	    }  
	    
}
