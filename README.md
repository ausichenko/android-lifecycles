# android-lifecycles
Android lifecycle-aware components codelab (for kotlin)

## Introduction
### Components
[Architecture components](https://developer.android.com/topic/libraries/architecture/index.html) are a set of Android libraries that help you structure your app in a way that is robust, testable, and maintainable.

This codelab introduces you to the following lifecycle-aware architecture components for building Android apps:

* **ViewModel** - provides a way to create and retrieve objects that are bound to a specific lifecycle. A [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) typically stores the state of a view's data and communicates with other components, such as data repositories or the domain layer which handles business logic. To read an introductory guide to this topic, see [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html).
* **LifecycleOwner/LifecycleRegistryOwner** - both [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html) and [`LifecycleRegistryOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html) are interfaces that are implemented in the [`AppCompatActivity`](https://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html) and [`Support Fragment`](https://developer.android.com/reference/android/support/v4/app/Fragment.html) classes. You can subscribe other components to owner objects which implement these interfaces, to observe changes to the lifecycle of the owner. To read an introductory guide to this topic, see [Handling Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle.html).
* **LiveData** - allows you to observe changes to data across multiple components of your app without creating explicit, rigid dependency paths between them. [`LiveData`](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html) respects the complex lifecycles of your app components, including activities, fragments, services, or any [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html) defined in your app. [`LiveData`](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html) manages observer subscriptions by pausing subscriptions to stopped [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html) objects, and cancelling subscriptions to [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html) objects that are finished. To read an introductory guide to this topic, see [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html).

### What you'll build
In this codelab, you implement examples of each of the components described above. You begin with a sample app and add code through a series of steps, integrating the various architecture components as you progress.

### What you'll need
Android Studio 2.3 or greater.
To be familiar with the Android [activity lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle.html).

## Step 1 - Setup Your Environment
In this step, you download the code for the entire codelab and then run a simple example app. Clone or download this repository for this codelab.

Run the Step 1 run configuration on a device or emulator:

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/b0c91bf76f7b9a13.png "Android lifecycle-aware components")

The app runs and displays a screen similar to the following screenshot:

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/d928fd8587074072.png "Android lifecycle-aware components")

Rotate the screen and notice that the timer resets!

You now need to update the app to persist state across screen rotations. You can use a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) because instances of this class survive configuration changes, such as screen rotation.

## Step 2 - Add a ViewModel
In this step, you use a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) to persist state across screen rotations and address the behaviour you observed in the previous step. In the previous step, you ran an activity that displays a timer. This timer is reset when a configuration change, such as screen rotation, destroys an activity.

You can use a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) to retain data across the entire lifecycle of an activity or a fragment. As the previous step demonstrates, an activity is a poor choice to manage app data. Activities and fragments are short-lived objects which are created and destroyed frequently as a user interacts with an app. A [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) is also better suited to managing tasks related to network communication, as well as data manipulation and persistence.

### Persisting the state of the Chronometer using a ViewModel
Open `ChronoActivity2` and examine how the class retrieves and uses a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html):
```
ChronometerViewModel chronometerViewModel = ViewModelProviders.of(this).get(ChronometerViewModel.class);
```

`this` refers to an instance of [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html). The framework keeps the [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) alive as long as the scope of the [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html) is alive. A [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) is not destroyed if its owner is destroyed for a configuration change, such as screen rotation. The new instance of the owner re-connects to the existing [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html), as illustrated by the following diagram:

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/1d42e8efcb42ff58.png "Android lifecycle-aware components")
```
Caution: The scope of an activity or fragment goes from created to finished (or terminated), which you must not confuse with destroyed. Remember that when a device is rotated, the activity is destroyed but any instances of ViewModel associated with it are not.
```

### Try it out
Run the app (choose **Step 2** in the Run Configurations dropdown) and confirm the timer doesn't reset when you perform either of the following actions:

1. Rotate the screen.
2. Navigate to another app and then return.

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/5fcb2c457ab6ae30.png "Android lifecycle-aware components")
However, if you or the system exit the app, then the timer resets.

```
Caution: The system persists instances of ViewModel in memory throughout the life of a lifecycle owner, such as a fragment or activity. The system doesn't persist instances of ViewModel to long-term storage.
```

## Step 3 - Wrap Data Using LiveData
In this step, you replace the chronometer used in previous steps with a custom one which uses a [`Timer`](https://developer.android.com/reference/java/util/Timer.html), and updates the UI every second. A [`Timer`](https://developer.android.com/reference/java/util/Timer.html) is a `java.util` class that you can use to recurrently schedule tasks in the future. You add this logic to the `LiveDataTimerViewModel` class, and leave the activity to focus on managing the interaction between the user and the UI.

The activity updates the UI when the timer notifies it to. To help avoid memory leaks, the [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) doesn't include references to the activity. For example, a configuration change, such as a screen rotation, might result in references in a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) to an activity that should be garbage collected. The system retains instances of [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) until the corresponding activity or lifecycle owner no longer exists.

```
Caution: Storing a reference to a Context or View in a ViewModel can result in memory leaks. Avoid fields that reference instances of the Context or View classes. The onCleared() method is useful to unsubscribe or clear references to other objects with a longer lifecycle, but not for clearing references to Context or View objects.
```

Instead of modifying views directly from the [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html), you configure an activity or fragment to observe a data source, receiving the data when it changes. This arrangement is called the observer pattern.

```
Note: To expose data as an observable, wrap the type in a LiveData class.
```

You may be familiar with the observer pattern if you've used the [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/index.html), or other reactive libraries like RxJava. [`LiveData`](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html) is a special observable class which is lifecycle-aware, and only notifies active observers.

### LifecycleOwner
`ChronoActivity3` is an instance of [`LifecycleActivity`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleActivity.html), which can provide the state of a lifecycle. This is the class declaration:

```
public class LifecycleActivity extends FragmentActivity implements LifecycleRegistryOwner {...}
```
The [`LifecycleRegistryOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html) is used to bind the lifecycle of instances of [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) and [`LiveData`](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html) to the activity or fragment. The equivalent class for fragments is [`LifecycleFragment`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleFragment.html).

#### Update ChronoActivity
1. Add the following code to the ChronoActivity3 class, in the subscribe() method, to create the subscription:

```
mLiveDataTimerViewModel.getElapsedTime().observe(this, elapsedTimeObserver);
```
2. Next, set the new elapsed time value in the LiveDataTimerViewModel class. Find the following comment:
```
//TODO set the new value
```
Replace the comment with the following statement:
```
mElapsedTime.postValue(newValue);
```
3. Run the app and open the **Android Monitor** in Android Studio. Notice that the log updates every second unless you navigate to another app. If your device supports multi-window mode, you may like to try using it. Rotating the screen does not affect how the app behaves.

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/9913571bad30fed9.png "Android lifecycle-aware components")

```
Note: LiveData objects only send updates when the activity, or the LifecycleOwner is active. If you navigate to a different app, the log messages pause until you return. LiveData objects only consider subscriptions as active when their respective lifecycle owner is either STARTED or RESUMED.
```

## Step 4 - Subscribe to Lifecycle Events
Many Android components and libraries require you to:
1. Subscribe, or initialize the component or library.
2. Unsubscribe, or stop the component or library.

Failing to complete the steps above can lead to memory leaks and subtle bugs.

A lifecycle owner object can be passed to new instances of lifecycle-aware components, to ensure they're aware of the current state of a lifecycle.

You can query the current state of a lifecycle using the following statement:

```
lifecycleOwner.getLifecycle().getCurrentState()
```
The statement above returns a state, such as [`Lifecycle.State.RESUMED`](https://developer.android.com/reference/android/arch/lifecycle/Lifecycle.State.html), or [`Lifecycle.State.DESTROYED`](https://developer.android.com/reference/android/arch/lifecycle/Lifecycle.State.html).

A lifecycle-aware object that implements [`LifecycleObserver`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleObserver.html) can also observe changes in the state of a lifecycle owner:

```
lifecycleOwner.getLifecycle().addObserver(this);
```
You can annotate the object to instruct it to call the appropriate methods when required:

```
@OnLifecycleEvent(Lifecycle.EVENT.ON_RESUME)
void addLocationListener() { ... }
```

### Create a lifecycle-aware component
In this step, you create a component that reacts to an activity lifecycle owner. Similar principles and steps apply when using a fragment as the lifecycle owner.

You use the Android framework's [`LocationManager`](https://developer.android.com/reference/android/location/LocationManager.html) to get the current latitude and longitude and display them to the user. This addition allows you to:

* Subscribe to changes and automatically update the UI using [`LiveData`](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html).
* Create a wrapper of the [`LocationManager`](https://developer.android.com/reference/android/location/LocationManager.html) that registers and unregisters, based on changes to the status of the activity.
You would typically subscribe a [`LocationManager`](https://developer.android.com/reference/android/location/LocationManager.html) to changes in either the `onStart()` or `onResume()` methods of an activity, and remove the listener in the `onStop()` or `onPause()` methods:

```
// Typical use, within an activity.

@Override
protected void onResume() {
    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
}

@Override
protected void onPause() {
    mLocationManager.removeUpdates(mListener);
}
```
In this step, you use an implementation of [`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner.html) called [`LifecycleRegistryOwner`](https://developer.android.com//reference/android/arch/lifecycle/LifecycleRegistryOwner.html) in a class called `BoundLocationManager`. The name of the `BoundLocationManager` class refers to the fact that instances of the class bind to the activity's lifecycle.

For the class to observe the activity's lifecycle, you must add it as an observer. To accomplish this, instruct the `BoundLocationManager` object to observe the lifecycle by adding the following code to its constructor:

```
lifecycleOwner.getLifecycle().addObserver(this);
```
To call a method when a lifecycle change occurs, you can use the `@OnLifecycleEvent` annotation. Update the `addLocationListener()` and `removeLocationListener()` methods with the following annotations in the `BoundLocationListener` class:

```
@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
void addLocationListener() {
    ...
}
@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
void removeLocationListener() {
    ...
}
```

```
Note: The observer is brought to the current state of the provider, so there's no need to call addLocationListener() from the constructor. It's called for you when the observer is added to the lifecycle owner.
```

Run the app and verify that the **Log Monitor** displays the following actions, when you rotate the device:

```
D/BoundLocationMgr: Listener added
D/BoundLocationMgr: Listener removed
D/BoundLocationMgr: Listener added
D/BoundLocationMgr: Listener removed
```
Use the Android Emulator to simulate changing the location of the device (click the three dots to show the extended controls). The [`TextView`](https://developer.android.com/reference/android/widget/TextView.html) is updated when it changes:


![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/62bc0f2aa883739c.png "Android lifecycle-aware components")

## Step 5 - Share a ViewModel between Fragments
#### Share a ViewModel between fragments
Complete the following additional steps using a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) to enable communication between fragments and the following:
* A single activity.
* Two instances of a fragment, each one with a [`SeekBar`](https://developer.android.com/reference/android/widget/SeekBar.html).
* A single [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) with a [`LiveData`](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html) field.
Run this step and notice two instances of [`SeekBar`](https://developer.android.com/reference/android/widget/SeekBar.html) which are independent of each other:

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/3779b3df20c610d4.png "Android lifecycle-aware components")

Connect the fragments with a [`ViewModel`](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html) so that when one [`SeekBar`](https://developer.android.com/reference/android/widget/SeekBar.html) is changed, the other [`SeekBar`](https://developer.android.com/reference/android/widget/SeekBar.html) is updated:

![](https://codelabs.developers.google.com/codelabs/android-lifecycles/img/a6c30f446ff0869a.png "Android lifecycle-aware components")

```
Note: You should use the activity as the lifecycle owner, as the lifecycle of each fragment is independent.
```

There's no step-by-step manual for this exercise but you can find a solution in the `step5.solution` package.
