package tdtu.finalproject.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private GridLayoutManager gridLayoutManager;

    public PaginationScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = gridLayoutManager.getChildCount();
        int totalItemCount = gridLayoutManager.getItemCount();
        int firstVisibleItemPosition =gridLayoutManager.findFirstVisibleItemPosition();
        if(isLoading() || isLastPage()){
            return;
        }
        if(firstVisibleItemPosition >= 0 && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)){
            loadMoreItem();
        }
    }

    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();
}
