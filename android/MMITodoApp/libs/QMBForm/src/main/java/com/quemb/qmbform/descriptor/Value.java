package com.quemb.qmbform.descriptor;

/**
 * Created by tonimoeckel on 14.07.14.
 */
public class Value<T> {
    private T mValue;
    private OnValueChangeListener mOnValueChangeListener;

    public Value(T value) {
        mValue = value;
    }

    public T getValue() {
        return mValue;
    }


    public void setValue(T value) {
        mValue = value;
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onChange(value);
        }
    }


    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mOnValueChangeListener = listener;
    }
	
    public OnValueChangeListener getOnValueChangeListener() {
        return this.mOnValueChangeListener;
    }	
}