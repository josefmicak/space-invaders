import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy implements IDrawable, IMovable {
    private double posX;
    private double posY;
    private final double width;
    private final double height;
    private double moveStep;
    private Image image;
    private GraphicsContext gc;

    public Enemy(double posX, double posY, double width, double height, double moveStep, Image image, GraphicsContext gc){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.moveStep = moveStep;
        this.image = image;
        this.gc = gc;
    }

    public double getX()
    {
        return posX;
    }

    public double getY()
    {
        return posY;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public void setMoveStep(double value) {this.moveStep += value;}

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

    public void setNewLocation()
    {
        posY += 20;
    }

    public void draw() {
        this.gc.drawImage(image, this.posX, this.posY, this.width, this.height);
        double posX2 = gc.getCanvas().getWidth() * 0.65;
        double posY2 = 20;
        gc.drawImage(image, posX2, posY2, this.width, this.height );
    }
}