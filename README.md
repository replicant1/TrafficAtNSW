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

## Screenshots

### Navigation

 ![Navigation Drawer](/doc/navigation.png)
  
### Incidents

  ![Hazard List](/doc/hazard_list.png)  ![Hazard Details](/doc/hazard_details.png)
  ![Map](/doc/map.png)
  
### Traffic Cameras

  ![Camera List](/doc/cameras.png) ![Camera Image](/doc/camera_image.png)
 
### Travel Times 

  ![Travel Times](/doc/travel_times.png) 
