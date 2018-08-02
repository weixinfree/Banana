package xin.banana.ui.dsl;

import android.view.View;
import android.view.ViewGroup;

import xin.banana.Banana;
import xin.banana.base.Consumer;

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

    public static final int match_parent = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int wrap_content = ViewGroup.LayoutParams.WRAP_CONTENT;

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
