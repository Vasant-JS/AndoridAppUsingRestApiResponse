package com.hobby_projects.retrofit_rest_api.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hobby_projects.retrofit_rest_api.Adapters.ItemsAdapter;
import com.hobby_projects.retrofit_rest_api.Model.API_Response;
import com.hobby_projects.retrofit_rest_api.Model.AllItems;
import com.hobby_projects.retrofit_rest_api.Model.Items;
import com.hobby_projects.retrofit_rest_api.R;
import com.hobby_projects.retrofit_rest_api.API.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ItemsAdapter.itemsCallback {
    ItemsAdapter adapter;
    RecyclerView items;
    NestedScrollView nestedSV;
    ArrayList<Items> ItemsList;
    AlertDialog alertDialog;
    ProgressBar loadingPB;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedSV = findViewById(R.id.nestedSV);
        loadingPB = findViewById(R.id.loadingPB);
        items = findViewById(R.id.items);
//        buildRecyclerView();
        ItemsList = new ArrayList<>();
        getSetOfItemsFromApi();

        // on below line we are setting layout manger to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        items.setLayoutManager(manager);
        // adding on scroll change listener method for our nested scroll view.
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    loadingPB.setVisibility(View.VISIBLE);
                    getSetOfItemsFromApi();
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    /*count++;
                    // on below line we are making our progress bar visible.
                    loadingPB.setVisibility(View.VISIBLE);
                    if (count < 20) {
                        // on below line we are again calling
                        // a method to load data in our array list.
                        getSetOfItemsFromApi();
                    }*/
                }
            }
        });
    }

    @Override
    public void infoItem(Context context, Items item, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Information")
                .setMessage(
                        "Id: "+item.getId()+"\n" +
                        "Name: "+item.getName()+"\n" +
                        "Description: "+item.getDescription()+"\n" +
                        "Price: "+item.getPrice()+"\n" +
                        "Category_id: "+item.getCategory_id()+"\n" +
                        "Created: "+item.getCreated()+"\n" +
                        "Modified: "+item.getModified()+"\n"
                )

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    public void editItem(Context context, Items i, int position) {
        if(alertDialog == null || !alertDialog.isShowing())
            showAddEditDialog(1, context, i, position);
    }

    @Override
    public void deleteItem(Context context, Items item, int position) {
        Call<API_Response> call = RetrofitClient.getInstance().getMyApi().deleteItem(item.getId()+"");
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, Response<API_Response> response) {
                API_Response resp = response.body();
                switch (resp.getStatus()){
                    case "200":
                        Toast.makeText(MainActivity.this, "Item was deleted.", Toast.LENGTH_SHORT).show();
                        ItemsList.remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                    case "503":
                        Toast.makeText(MainActivity.this, "Unable to delete item.", Toast.LENGTH_SHORT).show();
                        break;
                    case "400":
                        Toast.makeText(MainActivity.this, "Unable to delete items. Data is incomplete.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.addItem:
                addItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buildRecyclerView() {
        adapter = new ItemsAdapter(ItemsList, MainActivity.this);

        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        items.setHasFixedSize(true);
        items.setLayoutManager(manager);
        items.setAdapter(adapter);
        adapter.setCallBack(MainActivity.this);
    }

    private void getItemsFromApi() {
        Call<AllItems> call = RetrofitClient.getInstance().getMyApi().getItems();
        call.enqueue(new Callback<AllItems>() {
            @Override
            public void onResponse(Call<AllItems> call, Response<AllItems> response) {
                AllItems respAll = response.body();
                List<Items> resp = respAll.getItems();

                for(int i=0; i<resp.size(); i++){
                    Log.d("RESPONSE", "onResponse: "+resp.get(i).getName());
                }
                ItemsList.addAll(resp);
                buildRecyclerView();
            }

            @Override
            public void onFailure(Call<AllItems> call, Throwable t) {

            }
        });
//        return ItemsList;
    }

    private void getSetOfItemsFromApi() {
        Call<AllItems> call = RetrofitClient.getInstance().getMyApi().getSetOfItems(count+"");
        call.enqueue(new Callback<AllItems>() {
            @Override
            public void onResponse(Call<AllItems> call, Response<AllItems> response) {
                AllItems respAll = response.body();
                if(respAll != null){
                    List<Items> resp = respAll.getItems();

                    for(int i=0; i<resp.size(); i++){
                        Log.d("RESPONSE", "onResponse: "+resp.get(i).getName());
                    }
                    ItemsList.addAll(resp);
                    buildRecyclerView();

                    count++;
                }else{
                    nestedSV.stopNestedScroll();
                    loadingPB.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<AllItems> call, Throwable t) {

            }
        });
//        return ItemsList;
    }

    private ArrayList<Items> getItemFromApi(String id) {
        Call<AllItems> call = RetrofitClient.getInstance().getMyApi().getItem(id);
        call.enqueue(new Callback<AllItems>() {
            @Override
            public void onResponse(Call<AllItems> call, Response<AllItems> response) {
                AllItems respAll = response.body();
                List<Items> resp = respAll.getItems();
                ItemsList.addAll(resp);
            }

            @Override
            public void onFailure(Call<AllItems> call, Throwable t) {

            }
        });
        return ItemsList;
    }

    private void addItem() {
        if(alertDialog == null || !alertDialog.isShowing())
            showAddEditDialog(0, this, new Items(), -1);
    }

    private void createItem(Items item) {
        Call<API_Response> call = RetrofitClient.getInstance().getMyApi().createItem(item);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, Response<API_Response> response) {
                API_Response resp = response.body();
                if(resp != null){
                    Toast.makeText(MainActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                    if (resp.getStatus().equals("201")) {
                        ItemsList.add(item);
                        adapter.notifyItemInserted(ItemsList.size() - 1);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

            }
        });
    }

    public void updateItem(Items item, int position) {
        Call<API_Response> call = RetrofitClient.getInstance().getMyApi().updateItem(item);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, Response<API_Response> response) {
                API_Response resp = response.body();
                switch (resp.getStatus()){
                    case "200":
                        Toast.makeText(MainActivity.this, "Item was updated", Toast.LENGTH_SHORT).show();
                        ItemsList.set(position, item);
                        adapter.notifyItemChanged(position);
                        break;
                    case "404":
                        Toast.makeText(MainActivity.this, "No Item found to update.", Toast.LENGTH_SHORT).show();
                        break;
                    case "201":
                        Toast.makeText(MainActivity.this, "Item was created.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

            }
        });
    }

    public void showAddEditDialog(int action, Context context, Items item, int position) {
        /*action: 0: add , 1: edit */
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_item_edit, null);

        TextView title = promptsView.findViewById(R.id.title);
        TextView itemId = promptsView.findViewById(R.id.itemId);
        TextInputLayout itemName = promptsView.findViewById(R.id.itemName);
        TextInputLayout itemDescription = promptsView.findViewById(R.id.itemDescription);
        TextInputLayout itemPrice = promptsView.findViewById(R.id.itemPrice);
        TextInputLayout itemCategory_id = promptsView.findViewById(R.id.itemCategory_id);

        MaterialButton btnCancle = promptsView.findViewById(R.id.btnCancle);
        MaterialButton btnUpdate = promptsView.findViewById(R.id.btnUpdate);

        if(action == 1){
            itemId.setText("ID: "+item.getId()+"");
            title.setText("Update Item");
            btnUpdate.setCompoundDrawables(getDrawable(R.drawable.ic_check), null, null, null);
            btnUpdate.setText("Update");
            itemName.getEditText().setText(item.getName());
            itemDescription.getEditText().setText(item.getDescription());
            itemPrice.getEditText().setText(item.getPrice()+"");
            itemCategory_id.getEditText().setText(item.getCategory_id()+"");
        }else{
            title.setText("Create Item");
            btnUpdate.setCompoundDrawables(getDrawable(R.drawable.ic_add), null, null, null);
            btnUpdate.setText("Create");
        }

        btnCancle.setOnClickListener(v -> {
            if(alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
        });
        btnUpdate.setOnClickListener(v -> {
            item.setName(itemName.getEditText().getText().toString().trim());
            item.setDescription(itemDescription.getEditText().getText().toString().trim());
            item.setPrice(Integer.parseInt(itemPrice.getEditText().getText().toString().trim()));
            item.setCategory_id(Integer.parseInt(itemCategory_id.getEditText().getText().toString().trim()));

            if(alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();

            if(action == 0)
                createItem(item);
            else
                updateItem(item, position);
        });

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.show();
    }
}