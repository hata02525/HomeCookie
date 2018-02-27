package com.app.homecookie.Network;

/**
 * Created by fluper on 27/3/17.
 */
public interface OnNetworkCallBack<E> {


    public void onSuccess(int requestType, boolean isSuccess, String msg, E data);

    public void onError(String msg);
}
