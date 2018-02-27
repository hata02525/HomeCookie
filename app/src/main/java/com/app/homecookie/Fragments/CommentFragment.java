package com.app.homecookie.Fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.CommentBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.ImagePicker;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {
    View view;
    private Button btn_post;
    private ImageView iv_person;
    private ImageView iv_pic;
    private ImageView iv_back;
    private EditText et_comment;
    private RecyclerView recyclerView;
    private RelativeLayout rl_no_internet;
    private TextView tv_no_data;
    private String comment = "";
    SharedPreference sharedPreference;
    String userId;
    Activity activity;
    PopupWindow pw;
    Progress progress;
    String id;
    String from;
    private ArrayList<CommentBean> commentList = new ArrayList<>();

    public static CommentFragment newInstance(String id, String from) {
        Bundle args = new Bundle();
        args.putString(Constants.COMMENTIG_FROM, from);
        args.putSerializable(Constants.MEAL_ID, id);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString(Constants.COMMENTIG_FROM);
            id = getArguments().getString(Constants.MEAL_ID);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);

        return view;
    }

    private void initView(ArrayList<CommentBean> commentList, boolean hasData) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (hasData) {
            rl_no_internet.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            CommentAdapter adapter = new CommentAdapter(commentList, activity);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            rl_no_internet.setVisibility(View.VISIBLE);
            tv_no_data.setText("Be the First to Comment");
        }

        iv_person = (ImageView) view.findViewById(R.id.iv_person);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        btn_post = (Button) view.findViewById(R.id.btn_post);
        btn_post.setOnClickListener(this);


        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
            Helper.setProfilePic(activity, userPhoto, iv_pic);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
            Helper.setProfilePic(activity, userPhotoo, iv_pic);
        }


        rl_no_internet.setOnClickListener(this);


    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_COMMENT) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            ((HomeActivity) activity).replaceCommentFragment(id, from);
        }
        if (requestType == Network.REQUEST_TYPE_VIEW_COMMENT) {
            progress.dismiss();
            if (isSuccess) {
                commentList = (ArrayList<CommentBean>) data;
                initView(commentList, true);
            } else {
                commentList = (ArrayList<CommentBean>) data;
                initView(commentList, false);
            }
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                Helper.hideSoftKeyBoard(activity);
                if (isValidComment()) {
                    if (Network.isConnected(activity)) {
                        progress.show();
                        JsonObject object = new JsonObject();
                        object.addProperty("userId", Integer.parseInt(userId));
                        object.addProperty("typeId", Integer.parseInt(id));
                        object.addProperty("type", Integer.parseInt(from));
                        object.addProperty("commentText", comment);
                        Network.requestForComment(activity, object, this);
                    } else {
                        Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.rl_no_internet:
                if (Network.isConnected(activity))
                    ((HomeActivity) activity).replaceCommentFragment(id, from);
                else
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

        ArrayList<CommentBean> commentList = new ArrayList<>();
        Activity activity;

        public CommentAdapter(ArrayList<CommentBean> commentList, Activity activity) {
            this.activity = activity;
            this.commentList = commentList;
        }


        @Override
        public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.recipe_comment_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CommentAdapter.ViewHolder holder, int position) {
            final String userId = commentList.get(position).getUserId();

            holder.iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initiatePopupWindow(holder.iv_options, userId);
                }
            });
            String userPhoto = commentList.get(position).getPhoto();
            String commentText = commentList.get(position).getCommentText();
            String date = commentList.get(position).getDateTime();

            holder.tv_comment.setText(commentText);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
            try {
                Date date1 = sdf.parse(date);
                String formattedDate = format.format(date1);
                holder.tv_date_time.setText(formattedDate);
            } catch (ParseException e) {
                holder.tv_date_time.setText(date);
                e.printStackTrace();
            }
            Helper.setProfilePic(activity, userPhoto, holder.iv_pic);

        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_options;
            ImageView iv_pic;
            TextView tv_comment;
            TextView tv_date_time;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_options = (ImageView) itemView.findViewById(R.id.iv_options);
                iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
                tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
                tv_date_time = (TextView) itemView.findViewById(R.id.tv_date_time);
            }
        }
    }

    private void initiatePopupWindow(ImageView iv_options, String commneterId) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.comment_option_layout, null, false);
            //initializing view's components here
            LinearLayout ll_report = (LinearLayout) layout.findViewById(R.id.ll_report);
            LinearLayout ll_edit = (LinearLayout) layout.findViewById(R.id.ll_edit);
            LinearLayout ll_delete = (LinearLayout) layout.findViewById(R.id.ll_delete);
            ll_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });
            ll_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });
            ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // display popup
            pw.showAsDropDown(iv_options, -240, -120, Gravity.BOTTOM);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidComment() {
        comment = et_comment.getText().toString().trim();
        if (comment.isEmpty()) {
            Toast.makeText(activity, "Please Enter a Comment First", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
