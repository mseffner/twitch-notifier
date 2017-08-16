package mseffner.twitchnotifier.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import mseffner.twitchnotifier.R;


public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    private final List<Channel> channelList;

    public ChannelAdapter(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public void clear() {
        channelList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Channel> list) {
        channelList.addAll(list);
        notifyDataSetChanged();
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

        private View itemView;

        private ImageView channelLogo;
        private TextView channelName;
        private TextView currentGame;
        private TextView offlineText;
        private TextView streamTitle;
        private LinearLayout streamInfo;
        private TextView viewerCount;
        private TextView uptime;
        private TextView vodcastTag;
        private ImageView pinIcon;

        ChannelViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            channelLogo = itemView.findViewById(R.id.channel_logo);
            channelName = itemView.findViewById(R.id.channel_name);
            currentGame = itemView.findViewById(R.id.current_game);
            offlineText = itemView.findViewById(R.id.offline_text);
            streamTitle = itemView.findViewById(R.id.stream_title);
            streamInfo = itemView.findViewById(R.id.live_stream_info);
            vodcastTag = itemView.findViewById(R.id.vodcast_tag);
            viewerCount = itemView.findViewById(R.id.viewer_count);
            uptime = itemView.findViewById(R.id.uptime);
            pinIcon = itemView.findViewById(R.id.channel_options_icon);
        }

        void bind(int index) {

            final Channel channel = channelList.get(index);

            channelName.setText(channel.getDisplayName());
            if (channel.getPinned() == ChannelContract.ChannelEntry.IS_PINNED) {
                pinIcon.setVisibility(View.VISIBLE);
            } else {
                pinIcon.setVisibility(View.INVISIBLE);
            }

            Picasso.with(itemView.getContext())
                    .load(channel.getLogoUrl())
                    .placeholder(R.drawable.default_logo_300x300)
                    .into(channelLogo);

            if (channel.getStream() == null) {
                bindOfflineStream();
            } else {
                bindOnlineStream(channel);
            }

            // LongClickListener to toggle pin
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new ChannelDb(view.getContext()).toggleChannelPin(channel);
                    channel.togglePinned();
                    updatePinIcon(channel, pinIcon);
                    return true;
                }
            });
        }

        private void bindOfflineStream() {

            offlineText.setVisibility(View.VISIBLE);

            currentGame.setVisibility(View.INVISIBLE);
            streamInfo.setVisibility(View.INVISIBLE);
            streamTitle.setVisibility(View.INVISIBLE);
            vodcastTag.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(null);
        }

        private void bindOnlineStream(final Channel channel) {
            Stream stream = channel.getStream();

            // Click listener to open the stream
            /* TODO check if Twitch app is installed and open stream there, otherwise
            check if a browser is installed and open it there, otherwise do nothing? */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri channelPage = Uri.parse(channel.getStreamUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, channelPage);
                    view.getContext().startActivity(intent);
                }
            });

            offlineText.setVisibility(View.INVISIBLE);
            currentGame.setVisibility(View.VISIBLE);
            streamTitle.setVisibility(View.VISIBLE);
            streamInfo.setVisibility(View.VISIBLE);

            currentGame.setText(stream.getCurrentGame());
            streamTitle.setText(stream.getStatus());
            viewerCount.setText(Integer.toString(stream.getCurrentViewers()));

            uptime.setText(getUptime(stream.getCreatedAt()));

            if (stream.getStreamType() == ChannelContract.ChannelEntry.STREAM_TYPE_VODCAST) {
                vodcastTag.setVisibility(View.VISIBLE);
            } else {
                vodcastTag.setVisibility(View.INVISIBLE);
            }
        }

        private void updatePinIcon(Channel channel, View pinIconView) {
            if (channel.getPinned() == ChannelContract.ChannelEntry.IS_PINNED) {
                pinIconView.setVisibility(View.VISIBLE);
            } else {
                pinIcon.setVisibility(View.INVISIBLE);
            }
        }

        private String getUptime(long createdAt) {
            long currentTime = System.currentTimeMillis() / 1000;
            int uptimeInSeconds = (int) (currentTime - createdAt);

            int hours = uptimeInSeconds / 3600;
            uptimeInSeconds %= 3600;
            int minutes = uptimeInSeconds / 60;

            return String.format("%d:%02d", hours, minutes);

        }
    }
}
