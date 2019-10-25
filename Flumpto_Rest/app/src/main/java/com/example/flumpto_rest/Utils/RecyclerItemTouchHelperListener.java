package com.example.flumpto_rest.Utils;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction, int position);
}
