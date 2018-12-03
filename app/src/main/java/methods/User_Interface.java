package methods;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import tech.kuweka.kuweka.R;

public class User_Interface {

    // Disables the keys
    public static void setViewAndChildrenEnabled(View view, final boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    public static void freeze(Activity myActivity){

        myActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void unfreeze(Activity myActivity){

        myActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void exitToRightAndEnterFromLeft(Context myContext, final View[] exitingViews, final View[] enteringViews){

        final Animation exitAnim = AnimationUtils.loadAnimation(myContext, R.anim.exits_to_right);
        final Animation enterAnim = AnimationUtils.loadAnimation(myContext, R.anim.enters_from_left);

        for(View v : exitingViews){
            v.startAnimation(exitAnim);
        }

        exitingViews[0].postDelayed(new Runnable() {
            @Override
            public void run() {

                for(View v : exitingViews){

                    v.setVisibility(View.GONE);
                }

                for(View v : enteringViews){

                    v.setVisibility(View.VISIBLE);

                    v.startAnimation(enterAnim);
                }
            }
        },500);

    }

    public static void exitToLeftAndEnterFromRight(Context myContext, final View[] exitingViews, final View[] enteringViews){

        final Animation exitAnim = AnimationUtils.loadAnimation(myContext, R.anim.exits_to_left);
        final Animation enterAnim = AnimationUtils.loadAnimation(myContext, R.anim.enters_from_right);

        for(View v : exitingViews){
            v.startAnimation(exitAnim);
        }

        exitingViews[0].postDelayed(new Runnable() {
            @Override
            public void run() {

                for(View v : exitingViews){

                    v.setVisibility(View.GONE);
                }

                for(View v : enteringViews){

                    v.setVisibility(View.VISIBLE);

                    v.startAnimation(enterAnim);
                }
            }
        },500);

    }

    public static void scaleUp(Context myContext, final View myView){

        Animation anim = AnimationUtils.loadAnimation(myContext,R.anim.scale_up);

        myView.startAnimation(anim);

        myView.setVisibility(View.VISIBLE);
    }

    public static void scaleDown(Context myContext, View myView){

        Animation anim = AnimationUtils.loadAnimation(myContext,R.anim.scale_down);

        myView.startAnimation(anim);

        myView.setVisibility(View.INVISIBLE);
    }
}
