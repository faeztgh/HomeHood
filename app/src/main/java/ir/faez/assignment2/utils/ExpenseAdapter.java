package ir.faez.assignment2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.activities.NewExpenseActivity;
import ir.faez.assignment2.data.model.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    // Instance fields
    private Context context;
    private List<Expense> expenses;
    private LayoutInflater layoutInflater;
    private OnExpenseClickListener onExpenseClickListener;
    private String activityName;


    //Constructor
    public ExpenseAdapter(Context context, List<Expense> expenses,
                          OnExpenseClickListener onExpenseClickListener, String activityName) {
        this.context = context;
        this.expenses = expenses;
        this.layoutInflater = LayoutInflater.from(context);
        this.onExpenseClickListener = onExpenseClickListener;
        this.activityName = activityName;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItemData(position);
    }


    @Override
    public int getItemCount() {
        return expenses.size();
    }


    // this method called from other activities to remove an item from recyclerView
    public void removeItem(int position) {
        expenses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }


    //---------------------------------------------------------------------


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout itemLayout;
        public ImageView expenseIconIv;
        public TextView expenseTv;
        public TextView paymentDateTv;
        public TextView amountTv;
        public String expenseType;
        public ImageView closeIv;
        public ImageView payIv;
        private int position;
        private Expense expense;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseIconIv = itemView.findViewById(R.id.expense_icon_iv);
            expenseTv = itemView.findViewById(R.id.expenses_title_tv);
            paymentDateTv = itemView.findViewById(R.id.expenses_paymentDate_tv);
            amountTv = itemView.findViewById(R.id.expenses_amount_tv);
            expenseType = NewExpenseActivity.getExpenseTypeSp();
            closeIv = itemView.findViewById(R.id.expenses_close_iv);
            payIv = itemView.findViewById(R.id.expenses_pay_iv);
            itemLayout = itemView.findViewById(R.id.item_layout_constraint);

            invokeOnClickListeners();
        }

        // invoke OnClickListeners
        private void invokeOnClickListeners() {
            closeIv.setOnClickListener(this);
            payIv.setOnClickListener(this);
        }

        // set item icons and data
        @SuppressLint("UseCompatLoadingForColorStateLists")
        public void setItemData(final int position) {
            this.position = position;
            Expense expense = expenses.get(position);

            // setting Expense type icon
            switch (expenses.get(position).getExpenseType()) {
                case "Repairs":
                    expenseIconIv.setImageResource(R.drawable.repair_icon);
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.lightOrange));
                    break;
                case "Gas Bill":
                    expenseIconIv.setImageResource(R.drawable.gas_icon);
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow));
                    break;
                case "Water Bill":
                    expenseIconIv.setImageResource(R.drawable.water_icon);
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.skyBlue));
                    break;
                case "Electricity Bill":
                    expenseIconIv.setImageResource(R.drawable.elec_icon);
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.softGreen));
                    break;
            }
            // setting item stringify data
            expenseTv.setText(expenses.get(position).getTitle());
            paymentDateTv.setText(expenses.get(position).getPaymentDate());
            amountTv.setText(expenses.get(position).getAmount());
            closeIv.setImageResource(R.drawable.delete_icon);

            //setting item button icon
            switch (activityName) {
                case Status.payments:
                    payIv.setImageResource(R.drawable.done_icon);
                    break;
                case Status.expenses:
                    payIv.setImageResource(R.drawable.dollar_icon);
                    break;
            }
        }

        //----------------------------- Implementing View.OnclickListener --------------------------
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.expenses_close_iv:
                    onExpenseClickListener.onItemRemoved(expense, position);
                    break;
                case R.id.expenses_pay_iv:
                    onExpenseClickListener.onItemPayed(expense, position);
                    break;
            }
        }
    }
}
