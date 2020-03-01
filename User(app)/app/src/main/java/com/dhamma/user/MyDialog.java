package com.dhamma.user;

import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MyDialog {
	
	static AlertDialog box;
	
	public static void showTopupInfoDialog(final Activity act) {
		
		View root = act.getLayoutInflater().inflate(R.layout.topup_info_dialog,null);
		Button cancel = (Button)root.findViewById(R.id.topup_cancel_btn);
		Button call = (Button)root.findViewById(R.id.topup_call_btn);

		cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					box.dismiss();
					box = null;
				}
			});
		call.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i = new Intent();
					i.setAction(Intent.ACTION_DIAL);
					i.setData(Uri.parse("tel:09969984495"));
					act.startActivity(i);
				}
			});
       	box = new AlertDialog.Builder(act)
			.setView(root)
			.create();

		box.show();
    }
}
