package com.example.karokojnr.nadab_customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.R;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Hotel;

import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;


public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> implements Filterable{

    private ArrayList<Hotel> mDataList;
    private ArrayList<Hotel> mFilteredList;
    private Context context;

    public HotelAdapter(ArrayList<Hotel> mDataList, Context context) {
        this.mDataList = mDataList;
        mFilteredList = mDataList;
        this.context = context;
    }

   private  OnItemlickListener mListener;
    public interface OnItemlickListener{
        void onItemClick(int position);
    }
    public  void setOnItemClickListener(OnItemlickListener listener){
        mListener = listener;
    }

    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //correct
        LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_hotels, parent, false);

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_hotels, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        holder.txtHotelName.setText(mDataList.get(position).getBusinessName ());
        holder.txtAddress.setText(mDataList.get(position).getAddress ());
        holder.txtPayBillNo.setText(mDataList.get(position).getPayBillNo ());
        Glide.with(context)
                .load(RetrofitInstance.BASE_URL+"images/uploads/hotels/thumb_"+mDataList.get(position).getProfile())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    //Search bar filter


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mDataList;
                } else {

                    ArrayList<Hotel> filteredList = new ArrayList<>();

                    for (Hotel androidVersion : mDataList) {

                        if (androidVersion.getBusinessName ().toLowerCase().contains(charString) || androidVersion.getAddress ().toLowerCase().contains(charString) || androidVersion.getPayBillNo ().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Hotel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //End of Search filter


    class HotelViewHolder extends RecyclerView.ViewHolder {

        TextView txtHotelName, txtAddress, txtPayBillNo;
        ImageView imageView;

        HotelViewHolder(View itemView) {
            super(itemView);
            txtHotelName = (TextView) itemView.findViewById(R.id.txt_hotel_name);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtPayBillNo = (TextView) itemView.findViewById(R.id.pay_bill_no);
            imageView = (ImageView) itemView.findViewById(R.id.ivHotel);



            itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition ();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick ( position );
                        }
                    }
                }
            } );
        }
    }
}
