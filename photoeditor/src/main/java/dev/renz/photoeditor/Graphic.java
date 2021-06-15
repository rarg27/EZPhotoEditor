package dev.renz.photoeditor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https://github.com/burhanrashid52>
 */
public abstract class Graphic {

    private final View mRootView;

    private final GraphicManager mGraphicManager;

    abstract ViewType getViewType();

    abstract int getLayoutId();

    abstract void setupView(View rootView);

    void updateView(View view) {
        //Optional for subclass to override
    }

    Graphic(Context context, GraphicManager graphicManager) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getLayoutId() == 0) {
            throw new UnsupportedOperationException("Layout id cannot be zero. Please define a layout");
        }
        mRootView = layoutInflater.inflate(getLayoutId(), null);
        mGraphicManager = graphicManager;
        setupView(mRootView);
    }

    Graphic(View rootView, GraphicManager graphicManager) {
        mRootView = rootView;
        mGraphicManager = graphicManager;
        setupView(mRootView);
    }

    protected MultiTouchListener.OnGestureControl buildGestureController(final ViewGroup viewGroup, final PhotoEditorViewState viewState) {
        return new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                // Change the in-focus view
                viewState.setCurrentSelectedView(mRootView);
            }

            @Override
            public void onLongClick() {
                updateView(mRootView);
            }
        };
    }

    public View getRootView() {
        return mRootView;
    }
}
