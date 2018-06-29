package udacity.george.journalapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import udacity.george.journalapp.R;
import udacity.george.journalapp.utilities.entity.JournalEntries;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder>  {
   private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;
    private List<JournalEntries> mJournalEntries;
    private final Context mContext;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public JournalAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_journal to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_journal, parent, false);

        return new JournalViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntries journalEntries = mJournalEntries.get(position);
        String description = journalEntries.getTitle();
        String updatedAt = dateFormat.format(journalEntries.getUpdatedAt());
        holder.taskDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);
    }


    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public List<JournalEntries> getJournalEntries() {
        return mJournalEntries;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setJournalEntries(List<JournalEntries> taskEntries) {
        mJournalEntries = taskEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the task description and priority TextViews
        final TextView taskDescriptionView;
        final TextView updatedAtView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public JournalViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mJournalEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }

}
