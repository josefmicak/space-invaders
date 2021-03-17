import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player implements IDrawable, IMovable, IHealable {
    private double posX;
    private double posY;
    private final double width;
    private final double height;
    private GraphicsContext gc;
    private double moveStep;
    private int healthAmount;
    private Image image;

    public Player(double posX, double posY, double width, double height, double moveStep, int healthAmount, GraphicsContext gc, Image image){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.moveStep = moveStep;
        this.healthAmount = healthAmount;
        this.gc = gc;
        this.image = image;
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

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public int getHealthAmount()
    {
        return healthAmount;
    }

    public void lowerHealthAmount()
    {
        this.healthAmount -= 1;
    }

    public void draw() {
        this.gc.drawImage(image, this.posX, this.posY, this.width, this.height);

        for(int i = 0; i < this.healthAmount; i++)
        {
            int posX = (int)(gc.getCanvas().getWidth() - (this.width + 5) - (i * 1.5 * this.width + 10));
            int posY = 15;
            this.gc.drawImage(image, posX, posY, this.width, this.height);
        }
    }
}