package com.example.testtask.swipe;

import android.view.animation.Animation;

import com.example.testtask.ContactsFragment;

/**
 * Created by Алексей on 04.02.2018.
 */


public class MyAnimationListener implements Animation.AnimationListener
{
    private int position;
    private ContactsFragment contactsFragment;

    public MyAnimationListener(int position, ContactsFragment contactsFragment)
    {
        this.contactsFragment = contactsFragment;
        this.position = position;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        contactsFragment.callPhone(position);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}