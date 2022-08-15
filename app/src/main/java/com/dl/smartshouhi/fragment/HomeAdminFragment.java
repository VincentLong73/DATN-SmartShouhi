package com.dl.smartshouhi.fragment;

import static com.dl.smartshouhi.constaint.Constant.EMAIL_KEY;
import static com.dl.smartshouhi.constaint.Constant.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dl.smartshouhi.R;
import com.dl.smartshouhi.adapter.UserAdapter;
import com.dl.smartshouhi.api.AdminDbApi;
import com.dl.smartshouhi.model.UserModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeAdminFragment extends Fragment {
    private TextView tvUserNumber;
    private TextView tvInvoiceNumber;
    private TextView tvItemNumber;
    private RecyclerView rcvUser;
    private List<UserModel> listUser;
    private View mView;
    private String email;
    private SharedPreferences sharedpreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home_admin, container, false);
        initUI();
        return mView;
    }

    private void initUI() {
        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, "");
        tvUserNumber = mView.findViewById(R.id.tv_user_number);
        tvInvoiceNumber = mView.findViewById(R.id.tv_invoice_number);
        tvItemNumber = mView.findViewById(R.id.tv_item_number);
        listUser = new ArrayList<>();
        rcvUser = mView.findViewById(R.id.rcv_user);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvUser.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        rcvUser.addItemDecoration(dividerItemDecoration);

        getCountUserInvoiceItem();
        getUserNotAdmin();
    }

    private void getCountUserInvoiceItem() {
        AdminDbApi.databaseApi.getCountUserInvoiceItem().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    result = result.substring(1, result.length() - 1);
                    String[] strResult = result.split("#");
                    if(strResult[0].equals("200")){
                        String countUser = strResult[1];
                        String countInvoice = strResult[2];
                        String countItem = strResult[3];

                        tvUserNumber.setText(countUser);
                        tvInvoiceNumber.setText(countInvoice);
                        tvItemNumber.setText(countItem);
                    }else{
                        Toast.makeText(getActivity(), strResult[1], Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getUserNotAdmin() {
        AdminDbApi.databaseApi.getUserNotAdmin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                String strResult;

                try {
                    strResult = response.body().string();
                    if(strResult.length() > 1){
                        strResult = strResult.replace("\\", "");
                        strResult = strResult.replace("\"{", "{");
                        strResult = strResult.replace("}\"", "}");
                        strResult = strResult.substring(1, strResult.length() - 1);

                        String[] listResult = strResult.split("#");
                        if(listResult[0].equals("200")) {
                            listUser = Arrays.asList(gson.fromJson(listResult[1], UserModel[].class));
                            if(listUser.size() > 0){
                                initRecycleView();
                            }
                        }else
                            Toast.makeText(getActivity(), listResult[1], Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initRecycleView(){
        UserAdapter userAdapter = new UserAdapter(listUser, new UserAdapter.IClickListener() {
            @Override
            public void onClickBlockUser(UserModel user, int position) {
                blockUser(user, position);
            }

            @Override
            public void onClickUnblock(UserModel user, int position) {
                unblockUser(user, position);
            }
        });
        rcvUser.setAdapter(userAdapter);
    }

    private void unblockUser(UserModel user, int position) {
        AdminDbApi.databaseApi.unblockUser(user.getEmail(), email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    String strResult;
                    try {
                        strResult = response.body().string();
                        String[] listResult = strResult.split("#");
                        if(listResult[0].equals("200")){
                            Toast.makeText(getActivity(), listResult[1], Toast.LENGTH_SHORT).show();
                            getUserNotAdmin();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void blockUser(UserModel user, int position) {
        AdminDbApi.databaseApi.blockUser(user.getEmail(), email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    String strResult;
                    try {
                        strResult = response.body().string();
                        String[] listResult = strResult.split("#");
                        if(listResult[0].equals("200")){
                            Toast.makeText(getActivity(), listResult[1], Toast.LENGTH_SHORT).show();
                            getUserNotAdmin();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
