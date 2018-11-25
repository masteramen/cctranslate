package cc.translate;

public class Particle {
    private float x, y;
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public void setX(float x) {
    	System.out.println(x);
        this.x = x;
    }

    public void setY(float y) {
    	System.out.println(y);
        this.y = y;
    }
}