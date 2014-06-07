package edu.minggo.chat.ui;

import edu.minggo.chat.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class AutoEmailDialog extends Dialog {

	public AutoEmailDialog(Context context, int theme) {
		super(context, theme);
	}
	public AutoEmailDialog(Context context) {
		super(context);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_menu_layout);
	}
	
}
