package com.saki.aidl;
import com.saki.aidl.PluginMsg;
interface AppInterface
{
	String author();
	String info();
	byte[] icon();
	boolean jump();
	void onMessageHandler(in PluginMsg msg);
}