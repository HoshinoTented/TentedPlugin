package com.saki.aidl;
import com.saki.aidl.PluginMsg;
interface AppServiceInterface
{
	PluginMsg handlerMessage(in PluginMsg msg);
}