package engine;

public class Loop implements Runnable {
	private Game game;

    private boolean running;
    private final double updateRate = 1.0/60.0;
    private final double frameRate = 1.0/60.0;
    
    private long nextStatTime;
    private int fps = 0;
    private int ups = 0;

    public Loop(Game game) {
    	this.game = game;
    }

    @Override
    public void run() {
        running = true;
        double updateCounter, renderCounter;
        updateCounter = 0;
        renderCounter = 0;
        long currentTime;
        long lastUpdate = System.currentTimeMillis();
        long lastRender = System.currentTimeMillis();
        
        nextStatTime = System.currentTimeMillis() + 1000;
        
        while (running) {
            currentTime = System.currentTimeMillis();
            double updateTimeTaken = (currentTime - lastUpdate) / 1000d;
            double renderTimeTaken = (currentTime - lastRender) / 1000d;
            
            updateCounter += updateTimeTaken;
            lastUpdate = currentTime;
            
            renderCounter += renderTimeTaken;
            lastRender = currentTime;

            if (updateCounter >= updateRate) {
                while(updateCounter > updateRate) {
                    update();
                    updateCounter -= updateRate;
                }
            }
            
            if (renderCounter >= frameRate) {
            	render();
            	renderCounter -= frameRate;
            }
            
            if (System.currentTimeMillis() >= nextStatTime) {
            	game.giveStats(fps, ups);
            	fps = 0;
            	ups = 0;
            	nextStatTime = nextStatTime + 1000;
            }
        }
    }
    public void update() {
    	ups++;
        game.update();
    }
    public void render() {
    	fps++;
        game.getMainFrame().getDisplay().render();
    }
}
