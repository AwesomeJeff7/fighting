public class Fighter {
    private int x, y;
    private int width = 50;
    private int height = 100;
    private int health = 100;
    private boolean isBlocking = false;
    private boolean isFacingRight = true;
    private String name;
    
    public Fighter(String name, int startX, int startY, boolean facingRight) {
        this.name = name;
        this.x = startX;
        this.y = startY;
        this.isFacingRight = facingRight;
    }
    
    public void move(int dx) {
        if (!isBlocking) {
            x += dx;
            isFacingRight = dx > 0;
        }
    }
    
    public void attack(Fighter opponent) {
        if (!isBlocking && Math.abs(this.x - opponent.getX()) < 60) {
            if (opponent.isBlocking()) {
                opponent.takeDamage(5); // Reduced damage when blocking
            } else {
                opponent.takeDamage(20);
            }
        }
    }
    
    public void block() {
        isBlocking = true;
    }
    
    public void stopBlocking() {
        isBlocking = false;
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getHealth() { return health; }
    public boolean isBlocking() { return isBlocking; }
    public boolean isFacingRight() { return isFacingRight; }
    public String getName() { return name; }
}
