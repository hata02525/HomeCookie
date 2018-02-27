package com.app.homecookie.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Beans.CommentBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity implements OnNetworkCallBack, View.OnClickListener {
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
    Intent intent;
    Bundle bundle;
    Typeface face;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        activity = CommentActivity.this;
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getString(Constants.MEAL_ID);
        from = bundle.getString(Constants.TYPE_ID);

        sharedPreference = new SharedPreference(activity);
        sharedPreference.putString(Constants.TYPE_ID, from);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        rl_no_internet = (RelativeLayout) findViewById(R.id.rl_no_internet);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
        if (Network.isConnected(activity)) {
            progress.show();
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            obj.addProperty("type", from);
            obj.addProperty("typeId", id);
            Network.requestForViewComment(activity, obj, this);
        } else {
            rl_no_internet.setVisibility(View.VISIBLE);
        }
    }


    private void initView(ArrayList<CommentBean> commentList, boolean hasData) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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

        iv_person = (ImageView) findViewById(R.id.iv_person);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        et_comment.setTypeface(face);
        btn_post.setTypeface(face);
        ((TextView) findViewById(R.id.tv_header)).setTypeface(face);

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
            restartActivity();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
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


        if (requestType == Network.REQUEST_TYPE_DELETE_COMMENT) {
            progress.dismiss();
            restartActivity();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

        }

        if (requestType == Network.REQUEST_TYPE_REPORT_COMMENT) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }

        if (requestType == Network.REQUEST_TYPE_REPORT_COMMENT) {
            progress.dismiss();
            restartActivity();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
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
            case R.id.iv_back:
                setResult();
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
            final String commentText = commentList.get(position).getCommentText();
            final String commentId = commentList.get(position).getCommentId();
            holder.iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initiatePopupWindow(holder.iv_options, userId, commentId,commentText);
                }
            });
            String userPhoto = commentList.get(position).getPhoto();
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
                tv_comment.setTypeface(face);
                tv_date_time.setTypeface(face);
            }
        }
    }

    private void initiatePopupWindow(ImageView iv_options, String commneterId, final String commentId,final String commentText) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.comment_option_layout, null, false);
            //initializing view's components here
            LinearLayout ll_report = (LinearLayout) layout.findViewById(R.id.ll_report);
            LinearLayout ll_edit = (LinearLayout) layout.findViewById(R.id.ll_edit);
            LinearLayout ll_delete = (LinearLayout) layout.findViewById(R.id.ll_delete);
            ((TextView) layout.findViewById(R.id.tv_report)).setTypeface(face);
            ((TextView) layout.findViewById(R.id.tv_edit)).setTypeface(face);
            ((TextView) layout.findViewById(R.id.tv_delete)).setTypeface(face);

            if (userId.equalsIgnoreCase(commneterId)) {
                ll_report.setVisibility(View.GONE);
            } else {
                ll_delete.setVisibility(View.GONE);
                ll_edit.setVisibility(View.GONE);
            }
            final String userid = sharedPreference.getString(Constants.USER_USR_ID, "0");
            ll_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsonObject object = new JsonObject();
                    object.addProperty("userId",userid);
                    object.addProperty("commentId",Integer.parseInt(commentId));
                    object.addProperty("reportText","fdsa");
                    hitReportCommentApi(object);
                    pw.dismiss();
                }
            });
            ll_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // progress.show();
                    showEditCommentPopup(commentId,commentText);
                    pw.dismiss();
                }
            });
            ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.show();
                    JsonObject object = new JsonObject();
                    object.addProperty("commentId", commentId);
                    hitDeleteCommentApi(object);
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


    private void showEditCommentPopup(final String commentId, final String commentText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_comment_dialog, null);
        Button btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final EditText et_comment = (EditText) dialogView.findViewById(R.id.et_comment);
        et_comment.setText(commentText);
        et_comment.setSelection(et_comment.getText().length());
        btn_confirm.setTypeface(face);
        btn_cancel.setTypeface(face);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String updatedComment = et_comment.getText().toString();
                if (Network.isConnected(activity)) {
                    //commentText
                    Helper.hideSoftKeyBoard(activity);
                    if (!updatedComment.isEmpty()) {
                        if (Network.isConnected(activity)) {
                            alertDialog.dismiss();
                            progress.show();
                            JsonObject object = new JsonObject();
                            object.addProperty("userId", userId);
                            object.addProperty("typeId", id);
                            object.addProperty("type", from);
                            object.addProperty("commentText", updatedComment);
                            object.addProperty("commentId", commentId);
                            requestForUpdateCommentApi(object);
                        } else {
                            Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Please Enter a Comment First", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidComment() {
        comment = et_comment.getText().toString().trim();
        if (comment.isEmpty()) {
            Toast.makeText(activity, "Please Enter a Comment First", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        setResult();
    }

    private void hitReportCommentApi(JsonObject object) {
        Network.requestForReportComment(activity, object, this);
    }

    private void hitDeleteCommentApi(JsonObject object) {
        Network.requestForDeleteComment(activity, object, this);
    }

    private void requestForUpdateCommentApi(JsonObject object) {
        Network.requestForComment(activity, object, this);
    }


    private void restartActivity(){
        Intent intent = new Intent(activity, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MEAL_ID, id);
        bundle.putString(Constants.TYPE_ID, from);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private void setResult(){
        int commentCount = commentList.size();
        sharedPreference.putInt(Constants.COMMENT_COUNT,commentCount);
        Intent intent=new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.COMMENT_COUNT, String.valueOf(id));
        intent.putExtras(bundle);
        setResult(Constants.COMMENT_COUNT_CODE,intent);
        finish();
    }


}
