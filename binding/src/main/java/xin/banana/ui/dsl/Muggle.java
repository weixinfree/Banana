package xin.banana.ui.dsl;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import xin.banana.Banana;
import xin.banana.base.Consumer;
import xin.banana.binding.Binding;

import static xin.banana.base.Objects.requireNonNull;

/**
 * 视图构建dsl
 * Created by wangwei on 2018/08/01.
 */
@SuppressWarnings("unused")
public class Muggle {

    private Muggle() {
        //no instance
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    public static final int match_parent = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int wrap_content = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static final int center = Gravity.CENTER;
    public static final int center_horizontal = Gravity.CENTER_HORIZONTAL;
    public static final int center_vertical = Gravity.CENTER_VERTICAL;

    public static final Class<FrameLayout.LayoutParams> frameLayoutParams = FrameLayout.LayoutParams.class;
    public static final Class<LinearLayout.LayoutParams> linearLayoutParams = LinearLayout.LayoutParams.class;
    public static final Class<RelativeLayout.LayoutParams> relativeLayoutParams = RelativeLayout.LayoutParams.class;

    public static final int visible = View.VISIBLE;
    public static final int invisible = View.INVISIBLE;
    public static final int gone = View.GONE;

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    private static volatile float _density = -1;

    private static float getDensity() {
        if (_density <= 0) {
            _density = Banana.getApplication().getResources().getDisplayMetrics().density;
        }
        return _density;
    }

    private static volatile float _scaledDensity = -1;

    private static float getScaledDensity() {
        if (_scaledDensity <= 0) {
            _scaledDensity = Banana.getApplication().getResources().getDisplayMetrics().scaledDensity;
        }
        return _scaledDensity;
    }

    public static int $dp(double dp) {
        return (int) (dp * getDensity() + 0.5f);
    }

    public static int $sp(double sp) {
        return (int) (sp * getScaledDensity() + 0.5f);
    }

    @SuppressWarnings("WeakerAccess")
    public static <Layout extends ViewGroup.LayoutParams> Layout defaultLayout(Class<Layout> clazz) {
        requireNonNull(clazz);

        try {
            return clazz.getConstructor(int.class, int.class).newInstance(
                    wrap_content, wrap_content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    public static <V extends View, Layout extends ViewGroup.LayoutParams> Leaf<V, Layout> leaf(V leaf, Class<Layout> layout) {
        return new Leaf<>(leaf, defaultLayout(layout));
    }

    public static <V extends View, Layout extends ViewGroup.LayoutParams> Leaf<V, Layout> leaf(V leaf, Layout layout) {
        return new Leaf<>(leaf, layout);
    }

    public static <V extends ViewGroup, Layout extends ViewGroup.LayoutParams> Tree<V, Layout> tree(V leaf, Class<Layout> layout) {
        return new Tree<>(leaf, defaultLayout(layout));
    }

    public static <V extends ViewGroup, Layout extends ViewGroup.LayoutParams> Tree<V, Layout> tree(V leaf, Layout layout) {
        return new Tree<>(leaf, layout);
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    public static class Leaf<V extends View, Layout extends ViewGroup.LayoutParams> {
        final V view;
        final Layout layout;

        Leaf(V view, Layout layout) {
            this.view = requireNonNull(view);
            this.layout = requireNonNull(layout);
            view.setLayoutParams(layout);
        }

        public Leaf<V, Layout> layout(Consumer<? super Layout> layoutSetter) {
            requireNonNull(layoutSetter).accept(layout);
            return this;
        }

        public Leaf<V, Layout> attrs(Consumer<? super V> attrSetter) {
            requireNonNull(attrSetter).accept(view);
            return this;
        }

        public Leaf<V, Layout> binding(Consumer<Binding.Binder<V>> binderConsumer) {
            requireNonNull(binderConsumer).accept(Binding.with(view.getContext()).on(view));
            return this;
        }

        public View getView() {
            return this.view;
        }
    }

    public static class Tree<V extends ViewGroup, Layout extends ViewGroup.LayoutParams> extends Leaf<V, Layout> {

        Tree(V view, Layout layout) {
            super(view, layout);
        }

        public Tree<V, Layout> child(Leaf leaf) {
            this.view.addView(requireNonNull(leaf).view);
            return this;
        }

        @Override
        public Tree<V, Layout> layout(Consumer<? super Layout> layoutSetter) {
            super.layout(layoutSetter);
            return this;
        }

        @Override
        public Tree<V, Layout> attrs(Consumer<? super V> attrSetter) {
            super.attrs(attrSetter);
            return this;
        }
    }
}
