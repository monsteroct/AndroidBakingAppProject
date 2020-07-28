package com.salab.project.projectbakingrecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.salab.project.projectbakingrecipe.Model.RecipeStep;

import java.util.List;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.RecipeStepListViewHolder> {

    private List<RecipeStep> mRecipeStepList;
    // context required to get string resources
    private Context mContext;

    public RecipeStepListAdapter(Context context, List<RecipeStep> RecipeStepList) {
        mContext = context;
        mRecipeStepList = RecipeStepList;
    }

    @NonNull
    @Override
    public RecipeStepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepListViewHolder holder, int position) {
        int numStep = mRecipeStepList.get(position).getId();
        String shortDesc = mRecipeStepList.get(position).getShortDescription();
        String step_text = mContext.getString(R.string.msg_step_desc, numStep, shortDesc); //ex: Step 1: short desc

        holder.mRecipeStepDescTextView.setText(step_text);
    }

    @Override
    public int getItemCount() {
        if (mRecipeStepList == null){
            return 0;
        } else {
            return mRecipeStepList.size();
        }
    }

    class RecipeStepListViewHolder extends RecyclerView.ViewHolder{

        TextView mRecipeStepDescTextView;

        public RecipeStepListViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeStepDescTextView = itemView.findViewById(R.id.tv_step_desc);
        }
    }



}
