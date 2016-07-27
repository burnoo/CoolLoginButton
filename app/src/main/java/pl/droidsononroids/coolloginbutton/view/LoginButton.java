package pl.droidsononroids.coolloginbutton.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsononroids.coolloginbutton.R;

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

    public enum State {
        DEFAULT, LOADING, ERROR, SUCCESS
    }

    private State state;

    public LoginButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout, this, true);
        ButterKnife.bind(this);
        state = State.DEFAULT;
    }

    public State getState()
    {
        return state;
    }

    public void reset()
    {
        if(state != State.DEFAULT)
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
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

        int finalRadius = (int) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);

        v.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void hideView(final View v)
    {
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

        int initialRadius = (int) Math.hypot(cx, cy);

        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
                mSignUpTextView.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
            }
        });

        anim.start();
    }
}
