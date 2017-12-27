package com.example.bankapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.modle.Chat;

import java.util.Iterator;
import java.util.List;

/**
 * 对话 Adapter
 *@author Guanluocang
 *created at 2017/12/25 、
*/

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int MSG_PERSONAL = 1;//人发送消息
    public static final int MSG_ROBOT = 0;//机器人消息

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Chat> mChatList;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.mContext = context;
        this.mChatList = chatList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MSG_PERSONAL://用户发送消息
                view = mLayoutInflater.inflate(R.layout.layout_chat_personal, parent, false);
                holder = new PersonalChatViewHolder(view);
                break;
            case MSG_ROBOT://机器人发送消息
                view = mLayoutInflater.inflate(R.layout.layout_chat_robot, parent, false);
                holder = new RobotChatViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat = mChatList.get(position);

        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case MSG_PERSONAL:
                personalMsgLayout((PersonalChatViewHolder) holder, chat, position);
                break;
            case MSG_ROBOT:
                robotMsgLayout((RobotChatViewHolder) holder, chat, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mChatList != null ? mChatList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mChatList.get(position).getType();
    }

    @Override
    public void onClick(View view) {

    }

    class PersonalChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChatPersonal;

        public PersonalChatViewHolder(View view) {
            super(view);
            tvChatPersonal = (TextView) view.findViewById(R.id.tv_chatPersonal);
        }
    }

    class RobotChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChatRobot;

        public RobotChatViewHolder(View view) {
            super(view);
            tvChatRobot = (TextView) view.findViewById(R.id.tv_chatRobot);
        }
    }

    private void personalMsgLayout(final PersonalChatViewHolder holder, Chat chat, int position) {
//        if (position != 0) {
        holder.tvChatPersonal.setText(chat.getMassage());
        holder.itemView.setTag(position);
//        }
    }

    private void robotMsgLayout(RobotChatViewHolder holder, Chat chat, int position) {
//        if (position != 0) {
//            }
        holder.tvChatRobot.setText(chat.getMassage());
        holder.itemView.setTag(position);
    }

    /**
     * 添加一项
     *
     * @param chat
     */
    public void addItem(Chat chat) {
        mChatList.add(chat);
        notifyDataSetChanged();
    }

    /**
     * 清除所有
     */
    public void clear() {
        if (mChatList == null || mChatList.size() <= 0)
            return;
        for (Iterator it = mChatList.iterator(); it.hasNext(); ) {

            Chat chat = (Chat) it.next();
            int position = mChatList.indexOf(chat);
            it.remove();
            notifyItemRemoved(position);
        }
    }


}
