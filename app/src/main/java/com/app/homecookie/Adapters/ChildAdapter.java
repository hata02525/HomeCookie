package com.app.homecookie.Adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fluper on 2/5/17.
 */
public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    Activity activity;
    ArrayList<String> list;

    public ChildAdapter(Activity activity, ArrayList<String> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.sub.setText(list.get(position));
        holder.tv_step.setText(String.valueOf(position+1)+".");
//
//        holder.checkbox_choose_sub_category.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    subCategoriesList.get(catId).get(position).setSubCategoryChoosed(true);
//                } else {
//                    subCategoriesList.get(catId).get(position).setSubCategoryChoosed(false);
//                }
//            }
//        });
//        holder.checkbox_choose_sub_category.setChecked(subCategoriesList.get(catId).get(position).isSubCategoryChoosed());


//        holder.ratingBar.setActivated(false);
//        holder.ratingBar.setRating(4.0f);
//        holder.tv_recipe_name.setText(recipesBean.getRecipesBean().get(position).getTitle());
//        if(recipesBean.getRecipesBean().get(position).getLike()==1){
//            holder.radio_fav.setChecked(true);
//        }else{
//            holder.radio_fav.setChecked(false);
//        }
//        holder.radio_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    if(Network.isConnected(activity)){
//                        callToApi(position);
//                    }else{
//                        Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });

//        holder.rl_recipe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.startActivity(new Intent(activity, RecipeViewActivity.class));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sub;
        TextView tv_step;
        CheckBox checkbox_choose_sub_category;

        public ViewHolder(View v) {
            super(v);
            sub = (TextView) v.findViewById(R.id.sub);
            tv_step = (TextView) v.findViewById(R.id.tv_step);
//            tv_sub_category = (TextView) v.findViewById(R.id.tv_sub_category);
////            tv_sub_category.setTypeface(null, Typeface.BOLD);
//            rl_recipe = (RelativeLayout) v.findViewById(R.id.rl_recipe);
//            iv_recipe = (ImageView) v.findViewById(R.id.iv_recipe);
//            radio_fav = (RadioButton) v.findViewById(R.id.radio_fav);
//            tv_recipe_name = (TextView) v.findViewById(R.id.tv_recipe_name);
//            tv_poster_name = (TextView) v.findViewById(R.id.tv_poster_name);
//            tv_poster_dsc = (TextView) v.findViewById(R.id.tv_poster_dsc);
//            ratingBar = (RatingBar) v.findViewById(R.id.rating_bar_recipe);
//            iv_poster = (ImageView) v.findViewById(R.id.iv_poster);

        }
    }

}
