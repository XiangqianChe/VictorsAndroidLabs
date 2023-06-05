package algonquin.cst2335.victorsandroidlabs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment {

    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt;
    MyOpenHelper opener;
    SQLiteDatabase db;

    Button sendBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);

        sendBtn = chatLayout.findViewById(R.id.send_button);
        Button receiveBtn = chatLayout.findViewById(R.id.receive_button);
        EditText editMessage = chatLayout.findViewById(R.id.edit_message);

        opener = new MyOpenHelper( getContext() );
        db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("SELECT * FROM " + MyOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);
        while (results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int sendOrReceive = results.getInt(sendCol);
            messages.add(new ChatMessage(message,sendOrReceive,time,id));
        }

        chatList = chatLayout.findViewById(R.id.myrecycler);
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        chatList.setLayoutManager(layoutManager);

        sendBtn.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            ChatMessage thisMessage = new ChatMessage(editMessage.getText().toString(),1,currentDateandTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            messages.add( thisMessage );
            editMessage.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        });
        receiveBtn.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            ChatMessage thisMessage = new ChatMessage(editMessage.getText().toString(),2,currentDateandTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            messages.add( thisMessage );
            editMessage.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        });

        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setMessage("Do you want to delete the message: " + sendBtn.getText())
                .setTitle("Question:")
                .setNegativeButton("No",(dialog, clkN) -> {})
                .setPositiveButton("Yes", (dialog, clkY) -> {
                    ChatMessage removedMessage = messages.get(chosenPosition);
                    messages.remove(chosenPosition);
                    adt.notifyItemRemoved(chosenPosition);
                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] {String.valueOf(removedMessage.getId())});

                    Snackbar.make(sendBtn, "You deleted message #" + chosenPosition, Snackbar.LENGTH_SHORT)
                            .setAction("Undo", clkU -> {
                                messages.add(chosenPosition, removedMessage);
                                adt.notifyItemInserted(chosenPosition);
                                db.execSQL("INSERT INTO " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                        "','" + removedMessage.getMessage() +
                                        "','" + removedMessage.getSendOrReceive() +
                                        "','" + removedMessage.getTimeSent() + "');");
                            })
                            .show();
                })
                .create().show();
    }


    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        int position = -1;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
//                builder.setMessage("Do you want to delete the message: " + messageText.getText())
//                        .setTitle("Question:")
//                        .setNegativeButton("No",(dialog, clkN) -> {})
//                        .setPositiveButton("Yes", (dialog, clkY) -> {
//                            position = getAbsoluteAdapterPosition();
//                            ChatMessage removedMessage = messages.get(position);
//                            messages.remove(position);
//                            adt.notifyItemRemoved(position);
//                            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] {String.valueOf(removedMessage.getId())});
//
//                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_SHORT)
//                                    .setAction("Undo", clkU -> {
//                                        messages.add(position, removedMessage);
//                                        adt.notifyItemInserted(position);
//                                        db.execSQL("INSERT INTO " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
//                                                "','" + removedMessage.getMessage() +
//                                                "','" + removedMessage.getSendOrReceive() +
//                                                "','" + removedMessage.getTimeSent() + "');");
//                                    })
//                                    .show();
//                        })
//                        .create().show();
                ChatRoom parentActivity = (ChatRoom) getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage( messages.get(position), position);
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {
        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            int layoutId;
            int imageId;
            if (viewType == 1) {
                layoutId = R.layout.sent_message;
                imageId = R.id.send_image;
            } else {
                layoutId = R.layout.receive_message;
                imageId = R.id.recieve_image;
            }
            View loadedRow = inflater.inflate(layoutId, parent, false);

            // 换自定义头像
            //Bitmap theImage = BitmapFactory.decodeFile("/data/user/0/algonquin.cst2335.victorsandroidlabs/files/Picture.png");
            //ImageView imgView = loadedRow.findViewById(imageId);
            //imgView.setImageBitmap(theImage);

            MyRowViews initRow = new MyRowViews(loadedRow);
            return initRow;
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText( messages.get(position).getMessage() );
            holder.timeText.setText( messages.get(position).getTimeSent() );
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            ChatMessage thisRow = messages.get(position);
            return thisRow.getSendOrReceive();
        }
    }

    class ChatMessage {
        String message;
        int sendOrReceive;
        String timeSent;
        long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }
}
