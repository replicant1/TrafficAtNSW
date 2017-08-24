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

### IView and IPresenter interfaces

![IView and IPresenter Class Diagram](/doc/uml/uml_view_presenter_interfaces.jpg)

The two fundamental interfaces in the app are `IView` and `IPresenter`, which represent the
View and Presenter parties in the classic `MVP` pattern. The `IView` is just a tag interface
implemented by Fragments and Activities that are views. The `IPresenter` is generally injected
into the `IView` implementation. It attaches itself to the `IPresenter` and detaches itself
from the `IPresenter` at the appropriate times in its Android lifecycle. Typically the `onResume`
and `onPause` lifecycle methods are the appropriate places to attach and detach the View
from the Presenter.

### Hazards List screen

![Hazards View and Presenter Class Diagram](/doc/uml/uml_hazards_vp.jpg)

The following shows `IView` and `IPresenter` implementations for the Hazards screens. Both the
"Sydney Incidents" and  "Regional Incidents" screens use the same Presenter and View classes, just
with different `IHazardFilter` implementations applied.

### IDataService

![IDataService](/doc/uml/uml_data_service.jpg)

All domain data is retrieved via the `IDataService` interface. In production, the `RemoteDataService`
implementation is used, but for testing the `TestDataService` implementation is provided by Dagger
instead. This is achieved by having different Dagger modules defined for each implementation.

### Refresh Hazards

![Refresh Hazards Collaboration Diagram](/doc/uml/uml_collab_hazard_refresh.jpg)

0) `HazardsListFragment` tells its Presenter to load Hazards from the Data Service
1) `HazardsOverviewPresenter` constructs a `DownloadHazardsCommand` to perform the download asynchronously
2) `HazardsOverviewPresenter` constructs a `DownloadSuccessHandler` to receive the hazards when they are later returned
3) `HazardsOverviewPresenter` passes the `DownloadHazardsCommand` and the `SuccesHandler` to the `CommandEngine` for 
asynchronous execution.
4)  The `CommandEngine` executes the `DownloadHazardsCommand` asynchronously
5) The `RemoteDataService` gets the `incident.json` file from the remote web server and...
6) ... returns them to the `DownloadSuccessHandler` for parsing into data objects.
7) The resulting List of Hazards is stored away in the `HazardCacheSingleton` for future reference
8) The `DownloadSuccessHandler`, via the `HazardOverviewPresenter`, tells the view (`HazardListFragment`) to
refresh its display of Hazards because new hazard data is now available in the `HazardCacheSingleton`
9) The `HazardListFragment` constructs a new `HazardListAdapter` which interrogates the `HazardCacheSingleton` for
its new data which is then used to populate the Hazards List on the screen.

## Screenshots

### Navigation

![Navigation Drawer](/doc/screenshot/screenshot_navigation.png)

The Navigation drawer slides out from left of top level screens.
  
### Incidents List

![Hazard List](/doc/screenshot/screenshot_hazard_list.png)  

A list of Hazards (Incidents) in Regional NSW, grouped by RMS Region.

### Hazard Details

![Hazard Details](/doc/screenshot/screenshot_hazard_details.png)

Details screen for an individual Incident.

### Hazard Map

![Map](/doc/screenshot/screenshot_map.png)

Clicking the Location icon on the Hazards Detail screen shows the GPS
location of the Incident.
  
### Traffic Cameras

![Camera List](/doc/screenshot/screenshot_cameras.png) 

List of traffic cameras in the Sydney area, grouped by RMS Region.

### Traffic Camera Image

![Camera Image](/doc/screenshot/screenshot_camera_image.png)

Image from an individual traffic camera. Images update on the server
every minute. The star action toggles the inclusion of this camera
in the "Favourite Cameras" set.
 
### Travel Times 

![Travel Times](/doc/screenshot/screenshot_travel_times.png) 

Travel Times on the M1 Motorway. Touching individual rows toggles the rows'
travel time in and out of the total travel time for a given direction of travel
on the Motorway.
