package mseffner.twitchnotifier.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mseffner.twitchnotifier.R;


public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    private final List<Channel> channelList;

    public ChannelAdapter(List<Channel> channelList) {
        this.channelList = channelList;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.follower_list_item, parent, false);

        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {

        private ImageView channelLogo;
        private TextView channelName;
        private TextView currentGame;
        private TextView offlineText;
        private TextView streamTitle;
        private LinearLayout streamInfo;

        ChannelViewHolder(View itemView) {
            super(itemView);
            channelLogo = itemView.findViewById(R.id.channel_logo);
            channelName = itemView.findViewById(R.id.channel_name);
            currentGame = itemView.findViewById(R.id.current_game);
            offlineText = itemView.findViewById(R.id.offline_text);
            streamTitle = itemView.findViewById(R.id.stream_title);
            streamInfo = itemView.findViewById(R.id.live_stream_info);
        }

        void bind(int index) {
            Channel channel = channelList.get(index);

            channelName.setText(channel.getDisplayName());
        }
    }
}