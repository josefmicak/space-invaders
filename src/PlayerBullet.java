import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PlayerBullet implements IDrawable, IMovable {
    private double posX;
    private double posY;
    private final double width;
    private final double height;
    private double moveStep;
    private Color color;
    private GraphicsContext gc;

    public PlayerBullet(double posX, double posY, double width, double height, double moveStep, Color color, GraphicsContext gc){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.moveStep = moveStep;
        this.color = color;
        this.gc = gc;
    }

    public void move(int direction)
    {
        switch(direction)
        {
            case 1:
                posX -= moveStep;
                break;
            case 2:
                posX += moveStep;
                break;
            case 3:
                posY -= moveStep;
                break;
            case 4:
                posY += moveStep;
                break;
            default:
                break;
        }
    }

    public double getX()
    {
        return posX;
    }

    public double getY()
    {
        return posY;
    }

    public double getWidth() { return width;}

    public double getHeight() { return height;}

    public void draw() {
        this.gc.setFill(this.color);
        this.gc.fillRect(this.posX, this.posY, this.width, this.height);
    }
}
