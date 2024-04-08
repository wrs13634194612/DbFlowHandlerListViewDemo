package com.example.mepositry;



        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;


        import java.util.ArrayList;
        import java.util.List;

public class LanguageRecycleAdapter extends RecyclerView.Adapter<LanguageViewHolder> {

    private List<Loc> languageList = new ArrayList<>();


    public LanguageRecycleAdapter(List<Loc> languageList) {
        this.languageList = languageList;

        Log.e("TAG", "LanguageRecycleAdapter " + languageList.size());
    }

    public void setData(List<Loc> languageList2) {
        this.languageList = languageList2;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LanguageViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        holder = new LanguageViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, final int position) {
        holder.orderNumber.setText(languageList.get(position).getLocId());
        holder.language.setText(languageList.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }


}
