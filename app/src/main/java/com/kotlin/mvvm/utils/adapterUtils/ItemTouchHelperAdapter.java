package com.kotlin.mvvm.utils.adapterUtils;

/**
 * Developed by Waheed on 20,June,2021
 * <p>
 * Helper interface to simulate Swipe, Item position changing
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}