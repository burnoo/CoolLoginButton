package pl.droidsononroids.coolloginbutton.screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsononroids.coolloginbutton.R;
import pl.droidsononroids.coolloginbutton.api.LoginManager;
import pl.droidsononroids.coolloginbutton.view.LoginButton;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;
    @BindView(R.id.login_button)
    LoginButton mLoginButton;
    @BindView(R.id.content_main)
    ViewGroup mContentMain;
    private LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLoginManager = new LoginManager();
        setContentView(R.layout.activity_main);

        setUpViews();

        mLoginManager.setLoginListener(new LoginManager.LoginListener()
        {
            @Override
            public void loginSuccess()
            {
                mLoginButton.showSuccess();
                mEmailEditText.setFocusableInTouchMode(true);
            }

            @Override
            public void loginFailure()
            {
                mLoginButton.showError();
                mEmailEditText.setFocusableInTouchMode(true);
            }
        });
    }

    private void setUpViews()
    {
        ButterKnife.bind(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 1);
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onLoginClick();
            }
        });
        setSupportActionBar(mToolbar);
        mEmailEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                mLoginButton.reset();
            }
        });
        mEmailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                onLoginClick();
                return true;
            }
        });
        mContentMain.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    private void onLoginClick()
    {
        if (mLoginButton.getState() == LoginButton.State.DEFAULT)
        {
            mLoginButton.startLoading();
            mLoginManager.performLogin(mEmailEditText.getText().toString());
            mEmailEditText.setFocusable(false);
        }
    }
}