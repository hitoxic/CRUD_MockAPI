package com.example.haiapi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Game> gameArrayList;
    private String url="https://5fd263bc8cee610016adf537.mockapi.io/api/haiapi/gamebyhai/";

    public GameAdapter(Context context, ArrayList<Game> gameArrayList) {
        this.context = context;
        this.gameArrayList = gameArrayList;
    }

    @NonNull
    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view=layoutInflater.inflate(R.layout.listgame_item, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.MyViewHolder holder, int position) {
        holder.id.setText("#"+ String.valueOf(position+1));
        holder.name.setText(gameArrayList.get(position).getName());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= gameArrayList.get(position).getId();
                String value= gameArrayList.get(position).getName();
                editGame(id, value);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= gameArrayList.get(position).getId();
                delete(id);
            }
        });
    }

    private void delete(final int id) {
        TextView close;
        EditText edt_name;
        Button btnSubmit;
        final Dialog dialog;

        dialog= new Dialog(context);
        dialog.setContentView(R.layout.activity_notify_delete);

        close= dialog.findViewById(R.id.txt_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit= dialog.findViewById(R.id.submit);
        btnSubmit.setText("delete");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit("DELETE","",dialog, id);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void editGame(final int id, String value) {
        TextView close;
        EditText edt_name;
        Button btnSubmit;
        final Dialog dialog= new Dialog(context);

        dialog.setContentView(R.layout.activity_modcat);

        close= dialog.findViewById(R.id.txt_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        edt_name= dialog.findViewById(R.id.name);
        btnSubmit= dialog.findViewById(R.id.submit);
        btnSubmit.setText("update");

        edt_name.setText(value);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data= edt_name.getText().toString();
                Submit("PUT",data, dialog,id);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Submit(String method, final String data, final Dialog dialog, final int id) {
        if(method == "PUT") {
            StringRequest request = new StringRequest(Request.Method.PUT, url+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));
                    params.put("name", data);
                    return params;
                }
            };
            Volley.newRequestQueue(context).add(request);

        }else if(method == "DELETE"){
            StringRequest request = new StringRequest(Request.Method.DELETE, url+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(context).add(request);
        }
    }

    @Override
    public int getItemCount() {
        return gameArrayList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        private TextView id, name;
        private ImageView edit, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            delete= itemView.findViewById(R.id.delete);
            edit= itemView.findViewById(R.id.edit);
            id= itemView.findViewById(R.id.tv_id);
            name= itemView.findViewById(R.id.tv_game);
        }
    }
}