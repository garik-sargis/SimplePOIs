package com.example.simplepois;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simplepois.data.model.PoiInfo;
import com.example.simplepois.data.model.WithId;

import java.util.Collections;
import java.util.List;

import static butterknife.ButterKnife.findById;

// TODO: Externalize some responsibilities
public class PoiTitlesAdapter extends RecyclerView.Adapter<PoiTitlesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(WithId<PoiInfo> data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView vTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            vTitle = findById(itemView, R.id.text_title);
        }

        public void bind(PoiInfo poiInfo) {
            vTitle.setText(poiInfo.title);
        }
    }

    private List<WithId<PoiInfo>> poiInfoList;

    /**
     * May be null
     */
    private OnItemClickListener itemClickListener;

    public PoiTitlesAdapter(List<WithId<PoiInfo>> poiInfoList) {
        this.poiInfoList = poiInfoList;
        setHasStableIds(true);
    }

    public PoiTitlesAdapter() {
        this(Collections.<WithId<PoiInfo>>emptyList());
    }

    public void setClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    /**
     * Notify the listener if one exists
     */
    private void notifyClick(WithId<PoiInfo> data) {
        if(itemClickListener != null) {
            itemClickListener.onItemClicked(data);
        }
    }

    public void swapList(List<WithId<PoiInfo>> list) {
        poiInfoList = list;
        notifyDataSetChanged();
    }

    /**
     * @param viewType is always 0
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_poi_titles, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WithId<PoiInfo> poiInfoWithId = poiInfoList.get(position);
        holder.bind(poiInfoWithId.data);
        holder.vTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyClick(poiInfoWithId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poiInfoList.size();
    }

    @Override
    public long getItemId(int position) {
        return poiInfoList.get(position).id;
    }
}
