package algonquin.cst2335.victorsandroidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageDetailsFragment extends Fragment {

    MessageListFragment.ChatMessage chosenMessage;
    int chosenPosition;

    public MessageDetailsFragment(MessageListFragment.ChatMessage message, int position) {
        this.chosenMessage = message;
        this.chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.details_layout, container, false);

        TextView messageView = detailsView.findViewById(R.id.message_details);
        TextView sendView = detailsView.findViewById(R.id.send_or_receive_details);
        TextView timeView = detailsView.findViewById(R.id.time_details);
        TextView idView = detailsView.findViewById(R.id.id_details);

        messageView.setText("Message is: " + chosenMessage.getMessage());
        sendView.setText("Send Or Receive?" + chosenMessage.getSendOrReceive());
        timeView.setText("Time send: " + chosenMessage.getTimeSent());
        idView.setText("Database id is:  " + chosenMessage.getId());

        Button closeButton = detailsView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener( closeClicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        Button deleteButton = detailsView.findViewById(R.id.delete_btn);
        deleteButton.setOnClickListener( deleteClicked -> {
            ChatRoom parentActivity = (ChatRoom) getContext();
            parentActivity.notifyMessageDeleted(chosenMessage, chosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return detailsView;
    }
}
