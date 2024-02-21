package com.hq.tool.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hq.tool.R;


public class TitleEditText extends LinearLayout {

    private RelativeLayout rl_bg;
    private TextView tv_title;
    private TextView tv_title_top;
    private EditText et_content;
    private ImageView iv_arrow;
    private LinearLayout ll_captcha;
    private ImageView iv_captcha;

    private TextWatcher mTextWatcher;

    public TitleEditText(Context context) {
        this(context,null);
    }

    public TitleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.title_edit_layout, this);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title_top = (TextView) findViewById(R.id.tv_title_top);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        ll_captcha = (LinearLayout) findViewById(R.id.ll_captcha);
        iv_captcha = (ImageView) findViewById(R.id.iv_captcha);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleEditText);
        tv_title.setText(typedArray.getString(R.styleable.TitleEditText_title));
        tv_title.setText(typedArray.getString(R.styleable.TitleEditText_title));
        et_content.setHint(typedArray.getString(R.styleable.TitleEditText_hint));
        int maxLength = typedArray.getInteger(R.styleable.TitleEditText_maxLength, 0);
        if(maxLength > 0){
            et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        String inputType = typedArray.getString(R.styleable.TitleEditText_inputType);
        if("number".equals(inputType)){
            et_content.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if("password".equals(inputType)){
            et_content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        String digits = typedArray.getString(R.styleable.TitleEditText_digits);
        if(!TextUtils.isEmpty(digits)){
            et_content.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
        boolean arrowVisible = typedArray.getBoolean(R.styleable.TitleEditText_arrowVisible, true);
        iv_arrow.setVisibility(arrowVisible ? View.VISIBLE : View.GONE);
        et_content.setCursorVisible(!arrowVisible);
        et_content.setFocusable(!arrowVisible);
        et_content.setFocusableInTouchMode(!arrowVisible);
        String titleAlign = typedArray.getString(R.styleable.TitleEditText_titleAlign);
        if("top".equals(titleAlign)){
            tv_title.setVisibility(View.GONE);
            tv_title_top.setVisibility(View.VISIBLE);
        }else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title_top.setVisibility(View.GONE);
        }
        String textAlign = typedArray.getString(R.styleable.TitleEditText_textAlign);
        int gravity = Gravity.RIGHT;
        if("left".equals(textAlign)){
            gravity = Gravity.LEFT;
        }else if("right".equals(gravity)){
            gravity = Gravity.RIGHT;
        }else if("top".equals(gravity)){
            gravity = Gravity.TOP;
        }else if("bottom".equals(gravity)){
            gravity = Gravity.BOTTOM;
        }
        et_content.setGravity(Gravity.CENTER_VERTICAL|gravity);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rl_bg.setBackground(getResources().getDrawable(R.drawable.edit_bg_normal));
                if(mTextWatcher != null)mTextWatcher.beforeTextChanged(charSequence, i, i1, i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mTextWatcher != null)mTextWatcher.onTextChanged(charSequence, i, i1, i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(mTextWatcher != null)mTextWatcher.afterTextChanged(editable);
            }
        });
        et_content.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    rl_bg.setBackground(getResources().getDrawable(R.drawable.edit_bg_focused));
                }else{
                    rl_bg.setBackground(getResources().getDrawable(R.drawable.edit_bg_normal));
                }
            }
        });
        int paddingRight = arrowVisible ? et_content.getPaddingRight() : 0;
        et_content.setPadding(et_content.getPaddingLeft(),et_content.getPaddingTop(), paddingRight,et_content.getPaddingBottom());
    }

    public void setOnClickListener(OnClickListener onClickListener){
        et_content.setOnClickListener(onClickListener);
    }

    public void addTextChangedListener(TextWatcher textWatcher){
        this.mTextWatcher = textWatcher;
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public String getTitle(){
        return tv_title.getText().toString();
    }

    public void setText(String text){
        et_content.setText(text);
    }

    public String getText(){
        return et_content.getText().toString().trim();
    }

    public void setEnable(boolean enable){
        et_content.setEnabled(enable);
        et_content.setSingleLine(enable);
    }

    public boolean isEmpty(){
        boolean isEmpty = TextUtils.isEmpty(et_content.getText().toString().trim());
        if(isEmpty) {
            rl_bg.setBackground(getResources().getDrawable(R.drawable.edit_bg_red));
//            ToastUtils.getInstance().showToast(String.format(getContext().getString(R.string.can_not_empty), getTitle()));
            Toast.makeText(getContext(), String.format(getContext().getString(R.string.can_not_empty), getTitle()), Toast.LENGTH_SHORT).show();
        }
        return isEmpty;
    }

    public void editError(String errorToast){
        rl_bg.setBackground(getResources().getDrawable(R.drawable.edit_bg_red));
       // ToastUtils.getInstance().showToast(errorToast);
    }

    public void setCaptchaVisible(boolean visible){
        ll_captcha.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setCaptchaImg(Bitmap bitmap){
        iv_captcha.setImageBitmap(bitmap);
    }

    public void setCaptchaClickListener(OnClickListener onClickListener){
        iv_captcha.setOnClickListener(onClickListener);
    }
}
