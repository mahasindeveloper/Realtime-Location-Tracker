package com.mahasin.inforootdelivery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


// ... (imports and package declaration)

public class HistoryFragment extends Fragment {

    /*


    EditText ed_name, ed_gmail, ed_phone;
    Button button;
    ListView listView;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    MyAdapter myAdapter = new MyAdapter();


     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

           /*

        ed_name = view.findViewById(R.id.ed_name);
        ed_gmail = view.findViewById(R.id.ed_gmail);
        ed_phone = view.findViewById(R.id.ed_phone);
        button = view.findViewById(R.id.button);
        listView = view.findViewById(R.id.listView);



        loadData();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String name = ed_name.getText().toString();
                String gmail = ed_gmail.getText().toString();
                String phone = ed_phone.getText().toString();

                String url = "https://mahasin786.000webhostapp.com/App/insert.php?n=" + name + "&g=" + gmail + "&m=" + phone;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                loadData();
                                // Handle the response if needed
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors here
                        progressDialog.dismiss();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        });

















        return view;
    }
//============================================
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.item_lay, null);

            TextView tv_id = view.findViewById(R.id.tv_id);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_gmail = view.findViewById(R.id.tv_gmail);
            TextView tv_mobile = view.findViewById(R.id.tv_mobile);
            Button update=view.findViewById(R.id.update);
            Button deleth=view.findViewById(R.id.deleth);


            hashMap = arrayList.get(position);
            String id = hashMap.get("id");
            String name = hashMap.get("name");
            String gmail = hashMap.get("gmail");
            String number = hashMap.get("number");

            tv_id.setText(id);
            tv_name.setText(name);
            tv_gmail.setText(gmail);
            tv_mobile.setText(number);



            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    String name = ed_name.getText().toString();
                    String gmail = ed_gmail.getText().toString();
                    String phone = ed_phone.getText().toString();

                    String url = "https://mahasin786.000webhostapp.com/App/update.php?name=" + name + "&gmail=" + gmail + "&mobile=" + phone+"&id="+id;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    loadData();
                                    // Handle the response if needed
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle network errors here
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);


                }
            });


            deleth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();



                    String url = "https://mahasin786.000webhostapp.com/App/update.php?id="+id;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    loadData();
                                    // Handle the response if needed
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle network errors here
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);


                }
            });


            return view;
        }
    }
    //===========================================

   public void loadData(){
         arrayList=new ArrayList<>();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://mahasin786.000webhostapp.com/App/view.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String gmail = jsonObject.getString("gmail");
                        String number = jsonObject.getString("number");

                        hashMap = new HashMap<>();
                        hashMap.put("id", id);
                        hashMap.put("name", name);
                        hashMap.put("gmail", gmail);
                        hashMap.put("number", number);
                        arrayList.add(hashMap);
                    }


                    listView.setAdapter(myAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle network errors here
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);


         */




        return view;
    }




}
