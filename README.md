# Traffic@NSW

Monday 31 Jul 7 2017

An Android application that captures a subset of the information presented on the 
https://livetraffic.com web site but in a form more easily digested on a mobile device.

This is the very first Android app I ever wrote, which I have converted from Java to Kotlin 
as a learning exercise.

## Libraries
The app makes use of these libraries:

* *Android Annotations* - I mainly use these annotations:
  * `@EActivity`
  * `@EFragment` 
  * `@ViewById` 
  * `@AfterViews` 
  * `@FragmentArg`
  * `@Extra` 
  * `@OptionsMenu`
  * `@OptionsItem`
* *Dagger* - Dependency injection, switching between test and real implementations of `IDataService`
* *GSON* - For parsing of JSON data files
* *RxJava* - For asynchronous loading of data
* *Timber* - For logging

## Design

The two fundamental interfaces in the app are `IView` and `IPresenter`, which represent the
View and Presenter parties in the classic `MVP` pattern. The `IView` is just a tag interface
implemented by Fragments and Activities that are views. The `IPresenter` is generally injected
into the `IView` implementation. It attaches itself to the `IPresenter` and detaches itself
from the `IPresenter` at the appropriate times in its Android lifecycle. Typically the `onResume`
and `onPause` lifecycle methods are the appropriate places to attach and detach the View
from the Presenter.

![IView and IPresenter](/doc/uml/uml_view_presenter_interface.pdf)

The following shows `IView` and `IPresenter` implementations for the Hazards screens. Both the
"Sydney Incidents" and  "Regional Incidents" screens use the same Presenter and View classes, just
with different `IHazardFilter` implementations applied.

![Hazards View and Presenter](/doc/uml/uml_hazards_vp.pdf)

## Screenshots

### Navigation

![Navigation Drawer](/doc/screenshot/screenshot_navigation.png)
  
### Incidents

![Hazard List](/doc/screenshot/screenshot_hazard_list.png)  
![Hazard Details](/doc/screenshot/screenshot_hazard_details.png)
![Map](/doc/screenshot/screenshot_map.png)
  
### Traffic Cameras

![Camera List](/doc/screenshot/screenshot_cameras.png) 
![Camera Image](/doc/screenshot/screenshot_camera_image.png)
 
### Travel Times 

![Travel Times](/doc/screenshot/screenshot_travel_times.png) 
