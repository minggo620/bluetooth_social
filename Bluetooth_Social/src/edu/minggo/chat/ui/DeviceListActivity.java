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

import java.util.Set;

import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 这个Activity会以一个对话框出现。它会列出匹配的设备和搜索到的设备
 * 当用户选择列表中的设备的时候，该设备的MAC地址就会返回主Activity
 */
public class DeviceListActivity extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // 返回intent中字符串别名
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter mBtAdapter;  //蓝牙适配器
    private ArrayAdapter<String> mPairedDevicesArrayAdapter; //已连接过的蓝牙设备名字适配器
    private ArrayAdapter<String> mNewDevicesArrayAdapter;  //探测到的蓝牙设备名字适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 请求一个窗口
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        //设置返回的事件的结果操作（删除对话框的操作）
        setResult(Activity.RESULT_CANCELED);

        // 初始化按钮和设置探测蓝颜设备的监听器
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        //初始化蓝牙已配对和被探测到的蓝牙设备数组适配器
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // 配对好的蓝牙设备View并设置监听器
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // 探测的蓝牙设备View并设置监听器
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // 当探测蓝牙设备的时候向android系统注册一个广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // 当探测完蓝牙设备的时候向android系统注册一个广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //获取本机的蓝牙设配器
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //获取当前已平配对的蓝牙设配器
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // 如果有已配对的蓝牙设备，那就列表显示
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
        //添加到Activity组件集合中
		BluetoothChatService.allActivity.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // 注销掉蓝牙探测的广播
        this.unregisterReceiver(mReceiver);
    }

    /**
     * 探测蓝牙设备
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // 显示循环进度（android系统自带）
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        
        // 显示其他探测到的蓝牙设备
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // 如果本机蓝牙正在探测那就先停止探测的工作
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        //开始探测
        mBtAdapter.startDiscovery();
    }

    // 蓝牙设备的列表项的监听器
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // 先停止探测，以为只是我们最近开始的探测
            mBtAdapter.cancelDiscovery();

            // 获取设备的名称和设置名称显示的长度
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // 创建一个返回的设备Mac的intent
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
           
            setResult(Activity.RESULT_OK, intent);
            
            //关闭Activity
            finish();
        }
    };

    // 蓝牙探测广播接收器当探测完的时候更新设备列表
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //如果发现设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //从intent中获取一个蓝牙设备的对象
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果配对的话那就忽略它，因为已经在已配对的列表中
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // 当设备探测完成后更新主题
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
