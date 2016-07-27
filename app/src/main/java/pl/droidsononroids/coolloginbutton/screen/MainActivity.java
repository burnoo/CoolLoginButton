package pl.droidsononroids.coolloginbutton.screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsononroids.coolloginbutton.R;
import pl.droidsononroids.coolloginbutton.api.LoginManager;
import pl.droidsononroids.coolloginbutton.view.LoginButton;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.email_edit_text) EditText mEmailEditText;
    @BindView(R.id.login_button) LoginButton mLoginButton;
    private LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginManager = new LoginManager();
        setContentView(R.layout.activity_main);

        setUpViews();

        mLoginManager.setLoginListener(new LoginManager.LoginListener() {
            @Override
            public void loginSuccess() {
                mLoginButton.showSuccess();
            }

            @Override
            public void loginFailure() {
                mLoginButton.showError();
            }
        });
    }

    private void setUpViews()
    {
        ButterKnife.bind(this);
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mLoginButton.startLoading();
                mLoginManager.performLogin(mEmailEditText.getText().toString());
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
    }
}