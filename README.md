# MMobile for Android 

Mobile Car Configurator for Android, to be used in project SysPlace demo case. Implements the look & feel of a corresponding iOS app with which it can interact. Unless otherwise stated, all communication is performed via RESTful JSON operations using a central server (which can be configured at runtime).

Features
-----
- Receives Swipe events from iOS instances of the MMobile App and changes the color accordingly
  - polls server for key `cas_mmobile_swipe_data` every 150ms
  - if swipe data was returned, the key is cleared on the server by setting it to `""` (so it processes the swipe only once)
- Allows for changing the color
  - updates key `config-cas` with the current color
- Performs contious background scan for eddystone beacons and notices entry and exit of a configured beacon range
  - On entry: 
	1. CAVE is notified of entry, a car is shown with the corresponding color (UDP packet containing json `{'color':'<color>', 'inside':true}` is sent to CAVE server)
	2. Key `personal_profile` is updated with the current Personal Profile (can be configured inside the app and consists of *name*, *age* and *gender*)
  - On exit: CAVE is notified of exit, the car will be removed (UDP packet containing json `{'color':'<color>', 'inside':true}` is sent to CAVE server)

Configuration
-----
Compile time:

- `private final double THRESHOLD`: Defines the threshold distance to the configured eddystone beacon. If the current distance drops below `THRESHOLD`, an entry event is generated, otherwise an exit event. The distance is measured every 500ms, every 5 measurements an arithmetic mean is calculated and compared to `THRESHOLD`, so it takes at least 2.5s to recognize entry into or exit from the beacon range / CAVE
- `private final double DEBUG`: Set to true to display Toasts with fired events for on-screen debugging

Run time:

- At run time, the last screen ("Einstellungen") allows configuration of the `IP` and `Port` of the RESTful JSON server
