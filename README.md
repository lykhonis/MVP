# Movel-View-Presenter for Android

#### Presenter
Presenter is base class to extend from to make presenters per view. Calling `create` to create new presenter instace. In the example caching technique is used to cache presenters. Presenters will survive configuration changes and will be removed only in a case of finishing activity.

E.g. In a case of Fragment being View (MVP)

```java
@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
  super.onViewCreated(view, savedInstanceState);
  mPresenter.attachView(this);
}

@Override
public void onDestroyView() {
  super.onDestroyView();
  mPresenter.detachView(!getActivity().isFinishing());
}
```

There is still space for saving/restoring state through Bundle though. But even in this case, if we lose process (heap) state, we will consider it as fresh start and reload data. This is extreme case and may be ignored to keep code clean and maintainable.
