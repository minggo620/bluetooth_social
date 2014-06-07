/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.minggo.chat.ui;

import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * 这个Activity会以一个对话框出现。它会列出匹配的设备和搜索到的设备
 * 当用户选择列表中的设备的时候，该设备的MAC地址就会返回主Activity
 */
public class DeviceRequestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 请求一个窗口
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        setContentView(R.layout.requestconect);

        //设置返回的事件的结果操作（删除对话框的操作）
        setResult(Activity.RESULT_CANCELED);

        // 初始化按钮和设置探测蓝颜设备的监听器
        Button scanButton = (Button) findViewById(R.id.button_request);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        //添加到Activity组件集合中
		BluetoothChatService.allActivity.add(this);
    }
}
