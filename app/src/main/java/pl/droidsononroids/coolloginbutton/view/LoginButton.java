package pl.droidsononroids.coolloginbutton.view;

import pl.droidsononroids.coolloginbutton.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginButton extends FrameLayout
{
    @BindView(R.id.signUpTextView)
    TextView mSignUpTextView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.error_layout)
    View mErrorLayout;
    @BindView(R.id.success_layout)
    View mSuccessLayout;

    @BindView(R.id.error_icon)
    ImageView mErrorIcon;
    @BindView(R.id.success_icon)
    ImageView mSuccessIcon;
    
    private State state;

    public LoginButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        state = State.DEFAULT;

        setUpView(context, attrs);
    }

    private void setUpView(Context context, AttributeSet attrs)
    {
        LayoutInflater.from(context).inflate(R.layout.layout, this, true);
        ButterKnife.bind(this);
        
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoginButton);

        String text = ta.getString(R.styleable.LoginButton_text);
        Drawable defaultColorDrawable = ta.getDrawable(R.styleable.LoginButton_defaultColor);
        int errorColor = ta.getColor(R.styleable.LoginButton_errorColor, getResources().getColor(R.color.colorError));
        int successColor = ta.getColor(R.styleable.LoginButton_successColor, getResources().getColor(R.color.colorError));

        int errorIcon = ta.getResourceId(R.styleable.LoginButton_errorIcon, android.R.drawable.stat_sys_warning);
        int successIcon = ta.getResourceId(R.styleable.LoginButton_successIcon, R.drawable.ic_check_white_24dp);

        ta.recycle();

        if(text != null)
            mSignUpTextView.setText(text);

        setBackground(getResources().getDrawable(R.drawable.ripple_button));
        if(defaultColorDrawable != null)
            setBackground(defaultColorDrawable);

        mErrorLayout.setBackgroundColor(errorColor);
        mSuccessLayout.setBackgroundColor(successColor);

        mErrorIcon.setImageResource(errorIcon);
        mSuccessIcon.setImageResource(successIcon);
    }

    public State getState()
    {
        return state;
    }

    public void reset()
    {
        if (state != State.DEFAULT && state != State.LOADING)
        {
            switch (state)
            {
                case ERROR:
                    hideError();
                    break;
                case SUCCESS:
                    hideSuccess();
            }
            state = State.DEFAULT;
            mSignUpTextView.setVisibility(VISIBLE);
        }
    }

    public void autoReset()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                reset();
            }
        }, 2000);
    }

    public void startLoading()
    {
        mSignUpTextView.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
        state = State.LOADING;
    }

    public void showError()
    {
        showView(mErrorLayout);
        //autoReset(); kłóci się z onKeyUp
        state = State.ERROR;
    }

    public void showSuccess()
    {
        showView(mSuccessLayout);
        //autoReset();
        state = State.SUCCESS;
    }

    private void hideError()
    {
        hideView(mErrorLayout);
    }

    private void hideSuccess()
    {
        hideView(mSuccessLayout);
    }

    private void showView(View v)
    {
        v.setVisibility(View.VISIBLE);
        if (checkLolipop())
        {
            int cx = getMeasuredWidth() / 2;
            int cy = getMeasuredHeight() / 2;

            int finalRadius = (int) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
            anim.start();
        }
    }

    private void hideView(final View v)
    {
        state = State.DEFAULT;
        if (checkLolipop())
        {
            int cx = getMeasuredWidth() / 2;
            int cy = getMeasuredHeight() / 2;

            int initialRadius = (int) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(v, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    hide(v);
                }
            });

            anim.start();
        } else
            hide(v);
    }

    private void hide(View v)
    {
        v.setVisibility(View.GONE);
        if(state == State.DEFAULT)
        {
            mSignUpTextView.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
        }
    }

    private boolean checkLolipop()
    {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    }

    public enum State
    {
        DEFAULT, LOADING, ERROR, SUCCESS
    }
}
