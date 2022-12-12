package com.harshul.notify.presenter;

public interface LoginActivityContract {

   interface View{
       void onShowProgressBar();
       void onError(Exception e);
       void startActivity();
   }

   interface Presenter{
       void doLogin(String email, String password);
       void forgotPassword(String email);
   }

}
