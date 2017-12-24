package com.saki.aidl

import java.io.Serializable

/**
 * Created by Hoshino Tented on 2017/12/3.
 */
enum class Type : Serializable
{
    MSG, XML, JSON, AT,
    IMAGE
    {
        override fun toString(): String = "img"
    }
}