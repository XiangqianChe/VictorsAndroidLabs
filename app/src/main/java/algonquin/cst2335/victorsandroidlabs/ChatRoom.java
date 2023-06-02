package algonquin.cst2335.victorsandroidlabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt = new MyChatAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.chatlayout );
        chatList = findViewById(R.id.myrecycler);
        chatList.setAdapter(adt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        chatList.setLayoutManager(layoutManager);

        Button sendBtn = findViewById(R.id.send_button);
        Button receiveBtn = findViewById(R.id.receive_button);
        EditText editMessage = findViewById(R.id.edit_message);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

        MyOpenHelper opener = new MyOpenHelper( this );

        sendBtn.setOnClickListener(clk -> {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage thisMessage = new ChatMessage(editMessage.getText().toString(),1,currentDateandTime);
            messages.add( thisMessage );
            editMessage.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        });
        receiveBtn.setOnClickListener(clk -> {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage thisMessage = new ChatMessage(editMessage.getText().toString(),2,currentDateandTime);
            messages.add( thisMessage );
            editMessage.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        });

    }

    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        int position = -1;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question:")
                        .setNegativeButton("No",(dialog, clkN) -> {})
                        .setPositiveButton("Yes", (dialog, clkY) -> {
                            position = getAbsoluteAdapterPosition();
                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            adt.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_SHORT)
                                    .setAction("Undo", clkU -> {
                                        messages.add(position, removedMessage);
                                        adt.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show();
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

    private class ChatMessage {
        String message;
        int sendOrReceive;
        String timeSent;

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
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
