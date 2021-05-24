package com.example.tasksolution;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AllRegisteredEmployeesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    final int EMPTY_VIEW = 77777;
    private final Activity mActivity;
    private ArrayList<UserClass> mList;
    private List<UserClass> mListFiltered;


    private final OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onEdit(UserClass personModel,int position);

    }


    public AllRegisteredEmployeesAdapter(Activity activity,
                                         ArrayList<UserClass> list,
                                         OnItemClickListener listener) {
        mActivity = activity;
        mList = list;
        mListFiltered = mList;
        mListener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(layoutInflater.inflate(R.layout.nothing_yet, parent, false));
        } else {
            return new MyViewHolder(layoutInflater.inflate(R.layout.row_employee_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) != EMPTY_VIEW) {
            MyViewHolder itemview = (MyViewHolder) holder;
            UserClass personModel = mListFiltered.get(position);

            String yearString = String.valueOf((personModel.getName()));
            String yearString1 = personModel.getAge();

                itemview.tvPersonName1.setText("Age : " + yearString1);


            itemview.tvPersonName.setText("Name " + yearString);



            itemview.btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mActivity, itemview.btnOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_user_profile);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_tasks:
                                    mListener.onEdit(mListFiltered.get(itemview.getAdapterPosition()), itemview.getAdapterPosition());
                                    break;

                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        } else {
            final EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.tvAlertMessage.setText("No Forms Found.");
        }
    }

    @Override
    public int getItemCount() {
        return mListFiltered.size() > 0 ? mListFiltered.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListFiltered.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListFiltered = mList;
                } else {
                    List<UserClass> filteredList = new ArrayList<>();
                    for (UserClass row : mList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                            filteredList.add(row);

                    }

                    mListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListFiltered = (ArrayList<UserClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlertMessage;

        EmptyViewHolder(View view) {
            super(view);
            tvAlertMessage = view.findViewById(R.id.tvAlertMessage);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPersonName;
        TextView tvPersonName1;
        Button btnOptions;

        public MyViewHolder(View view) {
            super(view);

            tvPersonName =  view.findViewById(R.id.tv_person_name);
            tvPersonName1 =  view.findViewById(R.id.tv_person_name1);
            btnOptions =  view.findViewById(R.id.btn_options);
        }
    }

    public void removeItem(int position) {
        UserClass tempLeaveModel = mListFiltered.get(position);
        //Don't switch position of below 2 lines
        mListFiltered.remove(position);
        mList.remove(tempLeaveModel);
        notifyItemRemoved(position);
    }

    public void clearList() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addList(ArrayList<UserClass> personModelArrayList) {
        if(mList!=null)
            mList.clear();
        else
            mList=new ArrayList<>();

        mList.addAll(personModelArrayList);
        notifyDataSetChanged();
    }
}

