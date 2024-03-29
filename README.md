# Group 3 Elevator project 

## 👥 AUTHORS 
Mohammed Alkhaledi, 101162465

Sara Shikhhassan, 101142208

James Anderson, 101147068

Marina Latif, 101149148


### 🚀 LAUNCHING THE APPLICATION
To set up and launch the applicaton please follow these steps:
- At the top right on the Github page, click the green button "code"
- Choose "Download ZIP"
- Open the ZIP, and a folder that has been downloaded will open
- Open your favourite IDE (set up instructions are targetted towards Eclipse)
- In eclipse select File-> Open Projects from File System 
- Select the Directory of where the project folder is located
- Click Open then Finish

**To run the application**

- Navigate to the "src" folder, click on "Scheduler"
- Copy the path of the "requests.txt" file and add it to the Floor.java class in "path" on line 45 as a String
- Run the schedular process first, the floor process second, then the elevator process
- Click "Run"
The application should now execute

**To run the test cases**

- Navigate to the "tests" folder
- Click on one of the files you want to test (i.e SchedulerTest.java)
- Click "Run"

### 📄 GENERAL USAGE NOTES
**requests.txt**
- Takes a list of values of user inputs which is sent as requests to the elevator. 
- Follows the format: time, currentFloor, floorDirection, floorStop
(where time is the time the request is being sent in the format "13:34:03.23", currentFloor is the current floor of the user,
floorDirection is the direction of the floor as "True" if moving upwards, "False" if moving downwards, floorStop is the final destination of the passenger.)

Example:

13:12:04, 4, false, 2 ->
The elevator starts at floor 4 at 1:12pm, travels downwards to floor 2.

17:39:34, 5, true, 6 ->
The elevator starts at floor 5 at 5:39pm, travels upwards to floor 6.

**Floor.java and Elevator.java**
- These classes are not testable as their methods are already being tested in the Scheduler.java class


### 🔨 BREAKDOWN OF RESPONSIBILITIES
###### M5
Mohammed: GUI

Sara: GUI, README.txt file

James: Measured values, timing diagram

Marina: State machine diagrams, UML diagrams

###### M4
Mohammed: State machine diagrams, UML diagrams, utilities class

Sara: elevator.java class, README.txt file

James: test classes, timing diagram

Marina: userRequest.java class

###### M3
Mohammed: ElevatorServiceThread.java, ElevatorServer.java

Sara: State machine diagrams, UML diagrams, README.txt file

James: FloorServer.java, FloorServiceThread.java

Marina: test classes

###### M2
Mohammed: Schedular.java class, ElevatorState.java class, Floor.java class

Sara: ElevatorControlPanel.java class, ElevatorLight.java class, UML class diagram, README.txt file, test classes

James: Elevator.java class, Schedular.java class, ElevatorState.java class

Marina: Sequence diagram, State machine diagram, Elevator.java class

###### M1
Mohammed: Elevator.java class, Schedular.java class, test classes

Sara: RequestEvent.java class, UserRequest.java class, Schedular.java class, README.txt file

James: Floor.java class, Schedular.java class

Marina: UML class diagram, sequence diagram, test classes

