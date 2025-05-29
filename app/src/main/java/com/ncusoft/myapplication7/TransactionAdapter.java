package com.ncusoft.myapplication7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context context;
    private List<Transaction> transactions = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public TransactionAdapter(Context context) {
        this.context = context;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.noteTextView.setText(transaction.getNote());
        holder.dateTextView.setText(dateFormat.format(new Date(transaction.getTimestamp())));

        if (transaction.isIncome()) {
            holder.amountTextView.setTextColor(context.getResources().getColor(R.color.income));
            holder.amountTextView.setText(String.format("+¥%.2f", transaction.getAmount()));
        } else {
            holder.amountTextView.setTextColor(context.getResources().getColor(R.color.expense));
            holder.amountTextView.setText(String.format("-¥%.2f", transaction.getAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView, amountTextView, dateTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.note_text_view);
            amountTextView = itemView.findViewById(R.id.amount_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
        }
    }
}