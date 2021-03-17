import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Wall implements IDrawable, IHealable {
    private double posX;
    private double posY;
    private final double width;
    private final double height;
    private GraphicsContext gc;
    private int healthAmount;
    private Image imageWall1 = new Image("file:Images/Wall1.png");
    private Image imageWall2 = new Image("file:Images/Wall2.png");
    private Image imageWall3 = new Image("file:Images/Wall3.png");
    private Image imageWall4 = new Image("file:Images/Wall4.png");

    public Wall(double posX, double posY, double width, double height, int healthAmount, GraphicsContext gc){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.healthAmount = healthAmount;
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

    public double getWidth() { return width;}

    public double getHeight() { return height;}

    public int getHealthAmount()
    {
        return healthAmount;
    }

    public void lowerHealthAmount()
    {
        this.healthAmount -= 1;
    }

    public void draw() {
        Image image;
        switch (healthAmount)
        {
            case 1:
                image = imageWall4;
                break;
            case 2:
                image = imageWall3;
                break;
            case 3:
                image = imageWall2;
                break;
            case 4:
                image = imageWall1;
                break;
            default:
                image = null;
                break;
        }
        this.gc.drawImage(image, this.posX, this.posY, this.width, this.height);
    }
}
