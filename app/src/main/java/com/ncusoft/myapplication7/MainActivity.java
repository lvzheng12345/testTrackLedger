package com.ncusoft.myapplication7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText amountEditText, noteEditText;
    private TextView incomeTextView, expenseTextView, balanceTextView;
    private RecyclerView transactionsRecyclerView;
    private TransactionAdapter transactionAdapter;
    private double income = 0.0, expense = 0.0, balance = 0.0;
    private int userId = 1; // 假设用户 ID 为 1，实际应从登录信息中获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 设置导航栏标题
        setTitle(getString(R.string.toolbar_title));

        // 初始化视图组件
        initViews();

        // 加载交易记录
        loadTransactions();

        // 更新财务统计
        updateFinancialStats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单资源
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            finishAffinity(); // 关闭所有 Activity
            System.exit(0);   // 退出应用
            return true;
        } else if (id == R.id.action_switch_account) {
            // 切换账号，返回登录页面
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_statistics) {
            // 跳转到统计页面
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initViews() {
        // 初始化文本视图
        incomeTextView = findViewById(R.id.income_text_view);
        expenseTextView = findViewById(R.id.expense_text_view);
        balanceTextView = findViewById(R.id.balance_text_view);

        // 初始化输入框
        amountEditText = findViewById(R.id.amount_edit_text);
        noteEditText = findViewById(R.id.note_edit_text);

        // 初始化RecyclerView
        transactionsRecyclerView = findViewById(R.id.transactions_recycler_view);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionAdapter = new TransactionAdapter(this);
        transactionsRecyclerView.setAdapter(transactionAdapter);

        // 设置收入按钮
        Button incomeButton = findViewById(R.id.income_button);
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction(true);
            }
        });

        // 设置支出按钮
        Button expenseButton = findViewById(R.id.expense_button);
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction(false);
            }
        });

        // 设置筛选按钮
        ImageButton filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示筛选对话框
                showFilterDialog();
            }
        });

        // 设置搜索按钮
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示搜索对话框
                showSearchDialog();
            }
        });
    }

    private void loadTransactions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = HttpUtils.sendGetRequest("/transactions/" + userId);
                    if (response != null) {
                        JSONArray jsonArray = new JSONArray(response);
                        List<Transaction> transactions = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            int type = jsonObject.getInt("type");
                            double amount = Double.parseDouble(jsonObject.getString("amount"));
                            String note = jsonObject.getString("note");
                            String timestampStr = jsonObject.getString("timestamp");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            long timestamp = sdf.parse(timestampStr).getTime();

                            Transaction transaction = new Transaction(type, amount, note, timestamp);
                            transaction.setId(id);
                            transactions.add(transaction);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transactionAdapter.setTransactions(transactions);
                                updateFinancialStats();
                            }
                        });
                    }
                } catch (JSONException | IOException | java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateFinancialStats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = HttpUtils.sendGetRequest("/transactions/" + userId);
                    if (response != null) {
                        JSONArray jsonArray = new JSONArray(response);
                        income = 0.0;
                        expense = 0.0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int type = jsonObject.getInt("type");
                            double amount = Double.parseDouble(jsonObject.getString("amount"));
                            if (type == Transaction.TYPE_INCOME) {
                                income += amount;
                            } else {
                                expense += amount;
                            }
                        }
                        balance = income - expense;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                incomeTextView.setText(String.format("+¥%.2f", income));
                                expenseTextView.setText(String.format("-¥%.2f", expense));
                                balanceTextView.setText(String.format("¥%.2f", balance));
                            }
                        });
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addTransaction(final boolean isIncome) {
        String amountText = amountEditText.getText().toString().trim();
        String noteText = noteEditText.getText().toString().trim();

        if (amountText.isEmpty()) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                Toast.makeText(this, "金额必须大于0", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("userId", userId);
                        jsonBody.put("type", isIncome ? Transaction.TYPE_INCOME : Transaction.TYPE_EXPENSE);
                        jsonBody.put("amount", String.format("%.2f", amount));
                        jsonBody.put("note", noteText.isEmpty() ? (isIncome ? "收入" : "支出") : noteText);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        jsonBody.put("timestamp", sdf.format(new Date()));

                        HttpUtils.sendPostRequest("/transactions", jsonBody.toString());
                        loadTransactions(); // 重新加载列表（可优化为局部刷新）
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "添加交易记录失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();

            // 清空输入框
            amountEditText.setText("");
            noteEditText.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilterDialog() {
        // 实现筛选对话框逻辑
        Toast.makeText(this, "筛选功能即将推出", Toast.LENGTH_SHORT).show();
    }

    private void showSearchDialog() {
        // 实现搜索对话框逻辑
        Toast.makeText(this, "搜索功能即将推出", Toast.LENGTH_SHORT).show();
    }
}