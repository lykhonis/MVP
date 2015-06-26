package com.vl.rx.example.presenter;

import android.support.v4.util.SimpleArrayMap;

/**
 * Example of MVP Presenter which can survive configuration changes aka screen orientation
 * 
 * Created by vladlichonos on 5/20/15.
 */
public abstract class Presenter<V> {

    static final Cache sCache = new Cache();

    V mView;

    public final void attachView(V view) {
        onViewAttached(mView = view);
    }

    // retainInstanceState = !getActivity().isFinishing().
    // UPD: We might need to have a flag from onSaveInstateState instead
    public final void detachView(boolean retainInstanceState) {
        onViewDetached();
        mView = null;
        if (!retainInstanceState) {
            sCache.remove(this);
        }
    }

    protected final boolean isViewAttached() {
        return mView != null;
    }

    protected final V getView() {
        return mView;
    }

    protected void onViewAttached(V view) {
    }

    protected void onViewDetached() {
    }

    // Call this to create/inject presenter
    public static <T extends Presenter> T create(Class<T> cls, Object... args) {
        T presenter = sCache.get(cls);
        if (presenter == null) {
            try {
                presenter = cls.getDeclaredConstructor(toClasses(args)).newInstance(args);
            } catch (Exception e) {
                // catch Exception because of compilation error, it is okay in this case
                throw new IllegalArgumentException("Presenter " + cls.getName() + " has to have public constructor w/o arguments",
                                                   e);
            }
            sCache.put(cls, presenter);
        }
        return presenter;
    }

    static Class<?>[] toClasses(Object... args) {
        Class<?>[] classes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            classes[i] = args[i].getClass();
        }
        return classes;
    }

    static class Cache {

        SimpleArrayMap<Class<? extends Presenter>, Presenter> mPresenters = new SimpleArrayMap<>();

        public <T extends Presenter> T get(Class<T> cls) {
            return cls.cast(mPresenters.get(cls));
        }

        public <T extends Presenter> T put(Class<T> cls, T presenter) {
            return cls.cast(mPresenters.put(cls, presenter));
        }

        @SuppressWarnings("unchecked")
        public <T extends Presenter> T remove(T presenter) {
            Class<T> cls = (Class<T>) presenter.getClass();
            return cls.cast(mPresenters.remove(cls));
        }
    }
}
