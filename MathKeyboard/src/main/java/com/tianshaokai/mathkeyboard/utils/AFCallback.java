package com.tianshaokai.mathkeyboard.utils;

/**
 * 回调基类
 */
public interface AFCallback<T> extends CallbackBase<T> {

    void onStart(String uuid);

    void onProcess(int process);
}
