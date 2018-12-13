package com.ideas.innovative.zakatapp;

import android.util.Pair;

/**
 * Created by adil6 on 2018-11-21.
 */

public class EditablePair<K, V>  {

    K key;
    V value;

    public EditablePair(K first, V second) {
        key = first;
        value = second;
    }

    public void setKey (K f) {
        this.key = f;
    }

    public void setValue (V f) {
        this.value = f;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
