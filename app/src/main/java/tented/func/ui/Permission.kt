package tented.func.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Permission
 * @author Hoshino Tented
 * @date 2018/1/17 3:12
 */

private val permissions : Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                 )

/**
 * 返回未获取到的权限
 */
fun noPermission( context : Context) : List<String>
{
    return if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
    {
        permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
    }
    else arrayListOf()
}

/**
 * 申请权限
 */
fun requestPermission( context : Activity )
{
    val permissions = noPermission(context)

    if( permissions.isNotEmpty() ) ActivityCompat.requestPermissions(context, permissions.toTypedArray(), 9)
}

fun requestAgain(context : Activity, permission : String)
{
    val alert = AlertDialog.Builder(context).create()

    alert.setTitle("缺少权限...需要到权限管理器手动授权")
    alert.setMessage(permission + "\n如果您已授权, 则请重启本插件")

    alert.setButton(
            AlertDialog.BUTTON_POSITIVE,
            "确定",
            {
                _, _ ->

                System.exit(0)
            }
                   )

    alert.setCancelable(false)      //防止异常退出

    alert.show()
}