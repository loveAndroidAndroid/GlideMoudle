package com.pa.glide.pahglideapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.pa.glide.pahglidemodule.util.GlideUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.reactivex.functions.Consumer;

/**
 * Created by zhangxiaowen on 2018/7/17.
 */

public class PermissionUtils {

    private volatile static PermissionUtils inst;

    private PermissionUtils() {

    }

    public static PermissionUtils getInstance() {
        if (inst == null) {
            synchronized (GlideUtils.class) {
                if (inst == null) {
                    inst = new PermissionUtils();
                }
            }
        }
        return inst;
    }

    /**
     * todo 建议sp保存多状态  或者数据库存储
     *
     * @param activity
     * @param permission
     */
    public void getSinglePermission(Activity activity, String permission) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(permission)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            toast(activity, "用户已经同意该权限");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            toast(activity, "用户拒绝了该权限");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            toast(activity, "权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用");
                            SharedPreUtils.putInt(activity, MainActivity.TEST, 1);
                        }
                    }
                });
    }

    public void getMultiplePermission(Activity activity, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions)//多个权限用","隔开
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //当所有权限都允许之后，返回true
                            toast(activity, "用户已经同意该权限");
                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            toast(activity, "用户拒绝了该权限");
                        }
                    }
                });
    }

    public void getMultiplePermissionForSingleResult(Activity activity, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions//分别申请多个权限时，使用requestEach
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (permission.granted) {
                                // 用户已经同意该权限
                                toast(activity, "用户已经同意该权限");
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                toast(activity, "用户拒绝了该权限");
                            } else {
                                // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                                toast(activity, "权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用");
                            }
                        }
                        if (permission.name.equals(Manifest.permission.RECORD_AUDIO)) {
                            if (permission.granted) {
                                // 用户已经同意该权限
                                toast(activity, "用户已经同意该权限");
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                toast(activity, "用户拒绝了该权限");
                            } else {
                                // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                                toast(activity, "权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用");
                            }
                        }
                    }
                });
    }

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public boolean lacksPermissions(Context mContexts, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     */
    public boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 跳转: 「权限设置」界面
     */
    @Deprecated
    public boolean gotoPermissionSetting(Activity activity) {
        boolean success = true;

        String name = Build.MANUFACTURER;

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String packageName = activity.getPackageName();

        switch (name) {
            case "HUAWEI": // 华为 （honor 9）
//                gotoAppDetailSetting(activity, packageName); 应用详情页
                intent.putExtra("packageName", packageName); //所有应用权限列表
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
                break;
            case "Xiaomi": // 小米  （mix 2）
                String rom = getMiuiVersion();
                if ("V6".equals(rom) || "V7".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else if ("V8".equals(rom) || "V9".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else {
                    intent = getAppDetailsSettingsIntent(packageName);
                }
                break;
            case "Meizu": // 魅族 （魅蓝max）
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", packageName);
                break;
            case "samsung": // 三星
            case "smartisan": // 锤子 （M1L）
            case "vivo":// vivo (x20)
                gotoAppDetailSetting(activity, packageName);
                break;
            case "OPPO": // OPPO
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionManagerActivity"));
                break;
            case "Sony": // 索尼
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity"));
                break;
            case "LG": // LG
                intent.setAction("android.intent.action.MAIN");
                intent.putExtra("packageName", packageName);
                ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                intent.setComponent(comp);
                break;
            default:
                intent.setAction(Settings.ACTION_SETTINGS);
                Log.i(PermissionUtils.class.getSimpleName(), "没有适配该机型, 跳转普通设置界面");
                success = false;
                break;
        }
        try {
            startActivity(activity, intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 跳转失败, 前往普通设置界面
            gotoSetting(activity);
            success = false;
            Log.i(PermissionUtils.class.getSimpleName(), "无法跳转权限界面, 开始跳转普通设置界面");
        }
        return success;
    }

    /**
     * 跳转:「设置」界面
     */
    public void gotoSetting(Activity activity) {
        activity.startActivity(new Intent(Settings.ACTION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 跳转:「应用详情」界面
     *
     * @param packageName 应用包名
     */
    public void gotoAppDetailSetting(Activity activity, String packageName) {
        activity.startActivity(getAppDetailsSettingsIntent(packageName));
    }

    /**
     * 获取跳转「应用详情」的意图
     *
     * @param packageName 应用包名
     * @return 意图
     */
    public Intent getAppDetailsSettingsIntent(String packageName) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + packageName))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 跳转界面
     */
    private void startActivity(Activity activity, Intent intent) {
        if (intent != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 获取 MIUI 版本号
     */
    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(PermissionUtils.class.getSimpleName(), "MiuiVersion = " + line);
        return line;
    }

    /**
     * 跳转到权限设置界面
     */


    public void toast(Activity activity, String messsage) {
        Toast.makeText(activity, messsage, Toast.LENGTH_SHORT).show();
    }


}
