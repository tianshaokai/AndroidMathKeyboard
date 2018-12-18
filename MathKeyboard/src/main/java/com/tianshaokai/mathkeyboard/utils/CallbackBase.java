package com.tianshaokai.mathkeyboard.utils;

public interface CallbackBase<T> {

    void onSuccess(T entity);

    void onFailed(String errMsg);
}
