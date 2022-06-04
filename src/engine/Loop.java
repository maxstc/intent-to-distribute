package engine;

/**
 * This class contains the game loop that renders and updates the game
 */
public class Loop implements Runnable {
	private Game game;

    private boolean running;
    private final double updateRate = 1.0/30.0;
    private final double frameRate = 1.0/30.0;
    
    private long nextStatTime;		//next time we will measure statistics
    private int fps = 0;			//frames per second
    private int ups = 0;			//updates per second
    private long frameTime = 0;		//time it takes to render in nanoseconds
    private long updateTime = 0;	//time it takes to update in nanoseconds

    public Loop(Game game) {
    	this.game = game;
    }

    /**
     * Starts the game loop
     */
    @Override
    public void run() {
        running = true;
        double updateCounter, renderCounter; //counters that track the amount of time we have spent not updating/rendering to try to match the target rate
        updateCounter = 0;
        renderCounter = 0;
        long currentTime; //the current time in milliseconds
        long lastUpdate = System.currentTimeMillis(); //the time when we last updated
        long lastRender = System.currentTimeMillis(); //the time when we last rendered
        
        nextStatTime = System.currentTimeMillis() + 1000; //the next time we will check statistics
        
        while (running) {
            currentTime = System.currentTimeMillis(); //reset the current time
            double timeTaken = (currentTime - lastUpdate) / 1000d; //check how long it has been since we checked
            
            updateCounter += timeTaken;
            lastUpdate = currentTime;
            
            renderCounter += timeTaken;
            lastRender = currentTime;

            //if we have taken enough time that we can now update (i.e. 1/60th of a second if the target ups is 60), then update
            if (updateCounter >= updateRate) {
            	//and continue to update in case we are behind
                while(updateCounter > updateRate) {
                    update();
                    updateCounter -= updateRate;
                }
            }
            
            //if we have taken enough time that we can now render (i.e. 1/60th of a second if the target fps is 60), then render
            if (renderCounter >= frameRate) {
            	render();
            	renderCounter = 0;
            }
            
            if (System.currentTimeMillis() >= nextStatTime) {
            	game.getMainFrame().getDisplay().giveStats(fps, ups, frameTime, updateTime);
            	fps = 0;
            	ups = 0;
            	nextStatTime = nextStatTime + 1000;
            }
        }
    }
    public void update() {
    	ups++;
    	long start = System.nanoTime();
        game.update();
        updateTime = System.nanoTime() - start;
    }
    public void render() {
    	fps++;
    	long start = System.nanoTime();
        game.getMainFrame().getDisplay().render();
        frameTime = System.nanoTime() - start;
    }
}
