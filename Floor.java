import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import requestEvent.RequestEvent;
import scheduler.Scheduler;

/**
 * Implements the Floor class/thread
 * @author James Anderson, 101147068
*/

public class Floor extends Thread {

    private Scheduler scheduler;
    private ArrayList<RequestEvent> details;
    private ArrayList<RequestEvent> currentEvents;

    /**
     * Floor constructor
    */

    public Floor(Scheduler sch) {
        this.details = new ArrayList<RequestEvent>();
        this.currentEvents = new ArrayList<RequestEvent>();
        this.scheduler = sch;
    }

    /**
     * Reads events from given text file
     * @return the parsed event info from the text file derived from path
    */

    public static ArrayList<InputEvent> getEvents(String path) {
		ArrayList<InputEvent> evs = new ArrayList<InputEvent>();

		try {
		    BufferedReader reader = new BufferedReader(new FileReader(path));
			String l;
			l = reader.readLine();
			while (l != null) {
                evs.add(event);
				l = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return evs;
	}

    /**
     * Registers given event to ongoing events
     *
    */

    public void register(RequestEvent x) {
        currentEvents.add(x);
    }

    /**
     * @return the events scheduled for this floor
    */

    public ArrayList<RequestEvent> getDetails(){
        return this.details;
    }

    /**
     * method for executing a floor thread
    */
    
    public synchronized void run() {
        details = getEvents("src/test.txt"); // replace this obv

        for (RequestEvent x : details) {
            scheduler.sendRequest(x);

            try {
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.register(scheduler.getFloorRequest());
        }
    }
}