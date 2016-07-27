package pl.droidsononroids.coolloginbutton.api;

import android.os.Handler;

public class LoginManager
{
    private static final String CORRECT_LOGIN = "correct";
    private LoginListener mLoginListener;

    public void setLoginListener(final LoginListener loginListener)
    {
        mLoginListener = loginListener;
    }

    public void performLogin(final String email)
    {
        if (email.equals(CORRECT_LOGIN))
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mLoginListener.loginSuccess();
                }
            }, 750);
        } else
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mLoginListener.loginFailure();
                }
            }, 800);
        }
    }

    public interface LoginListener
    {
        void loginSuccess();

        void loginFailure();
    }

}
