import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.util.Duration;
import java.util.Random;
import java.util.ArrayList;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;


public class CanvasManager {

    private final Pane root = new Pane();
    private Canvas canvas;
    private GraphicsContext gc;
    private final int width;
    private final int height;
    public final String title;
    private int divisionX;
    private int divisionY;
    private int divisionHeight;

    private IDrawable player;
    private ArrayList<IDrawable> playerBullets;
    private ArrayList<IDrawable> enemies;
    private ArrayList<IDrawable> enemyBullets;
    private ArrayList<IDrawable> walls;

    private Boolean changeDirection = false;
    private Boolean gameStarted = false;
    private Boolean enemyDirection = false;
    private Boolean firstGame = true;
    private Boolean gameWon = false;

    private Random rand = new Random();
    private int level;
    private int freq = 0;

    private Score score;
    private String filename = "score.txt";
    private int savedScore;

    public final Timeline t1 = new Timeline(new KeyFrame(Duration.millis(5), event -> {
        freq++;
        collisions();
        drawCanvas();
        if(freq % 200 == 0 && gameStarted && enemies.size() > 0)
        {
            shootEnemyBullet();
        }
    }));

    public CanvasManager(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;

        this.t1.setCycleCount(Timeline.INDEFINITE);
        this.t1.play();
    }

    public void keyPress(KeyEvent e){
        KeyCode code = e.getCode();

        if(code == KeyCode.S && !gameStarted)
        {
            if(firstGame)
            {
                firstGame();
            }
            if(gameWon)
            {
                savedScore = score.getScore();
            }
            else
            {
                level = 1;
            }
            destroyAllEnemies();
            createEnemies();

            if(playerBullets.size() > 0)
            {
                destroyAllPlayerBullets();
            }

            destroyAllEnemyBullets();

            destroyPlayer();
            createPlayer();
            createWalls();

            score.setMaxScore(score.getMaxScoreFromFile(filename));
            gameStarted = true;
            gameWon = false;
        }

        if(gameStarted) {
            if (code == KeyCode.LEFT && player.getX() > 10) {
                ((IMovable)player).move(1);
            }
            else if (code == KeyCode.RIGHT && (player.getX() + player.getWidth()) < this.width - 10) {
                ((IMovable)player).move(2);
            }
            else if (code == KeyCode.UP && playerBullets.size() == 0) {
                shootPlayerBullet();
            }
        }
    }


    public Parent createElements(){
        // Create root
        root.setPrefSize(width, height);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));

        // Canvas
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        return root;
    }

    private void firstGame()
    {
        level++;
        playerBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();

        divisionX = 0;
        divisionY = 60;
        divisionHeight = 10;

        createEnemies();

        firstGame = false;
    }

    private void drawCanvas() {
        if(gameStarted)
        {
            gc.setFill(Color.WHITE);
            gc.fillRect(divisionX, divisionY, this.width, divisionHeight);

            player.draw();

            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).draw();
            }

            for (int i = 0; i < playerBullets.size(); i++) {
                playerBullets.get(i).draw();
            }

            for (int i = 0; i < enemyBullets.size(); i++) {
                enemyBullets.get(i).draw();
            }

            for(int i = 0; i < walls.size(); i++){
                walls.get(i).draw();
            }
        }
    }

    private void resetCanvas(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void collisions(){
        resetCanvas();
        if (!gameStarted) {
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(35));
            gc.fillText("press S to start new game", width * 0.3, height * 0.5);

            if(firstGame)
            {
                gc.setFill(Color.LIGHTBLUE);
                gc.setFont(Font.font(45));
                gc.fillText("WELCOME TO SPACE INVADERS", width * 0.18, height * 0.37);
            }
            else
            {
                if(gameWon)
                {
                    gc.setFill(Color.GREENYELLOW);
                    gc.setFont(Font.font(45));
                    gc.fillText("YOU WIN", width * 0.4, height * 0.37);
                }
                else
                {
                    gc.setFill(Color.RED);
                    gc.setFont(Font.font(45));
                    gc.fillText("GAME OVER", width * 0.38, height * 0.28);
                }
            }
        } else {
            if (((IHealable)player).getHealthAmount() < 1) {
                gameStarted = false;
                firstGame = false;
                gameWon = false;
                score.writeScoreToFile(this.filename, score.getScore());
            }

            if ((playerBullets.size() > 0) && (enemies.size() > 0)) {
                for (int i = 0; i < playerBullets.size(); i++) {
                    PlayerBullet playerBullet = (PlayerBullet)playerBullets.get(i);
                    playerBullet.draw();
                    playerBullet.move(3);

                    for (int j = 0; j < enemies.size(); j++) {
                        Enemy enemy = (Enemy)enemies.get(j);
                        if (playerBullet.getY() < enemy.getY() + enemy.getHeight() && playerBullet.getX() < enemy.getX() + (enemy.getWidth() - 1) && playerBullet.getX() > enemy.getX() - (playerBullet.getWidth() - 1) && enemy.getY() - playerBullet.getY() < (enemy.getHeight() - 1)) {
                            destroyEnemy(enemy);
                            destroyPlayerBullet(playerBullet);
                            score.addScore();
                        }
                    }

                    if (playerBullet.getY() < (divisionY + divisionHeight)) {
                        destroyPlayerBullet(playerBullet);
                    }
                }
            }

            if(enemies.size() > 0)
            {
                int x = 0;
                while (x < enemies.size()) {
                    if (!enemyDirection) {
                        Enemy enemy = (Enemy)enemies.get(x);
                        enemy.move(2);
                        x++;
                        if (enemy.getX() > this.width - (enemy.getWidth() + 1))
                        {
                            enemyDirection = true;
                            changeDirection = true;
                            break;
                        }
                    } else if (enemyDirection) {
                        Enemy enemy = (Enemy)enemies.get(x);
                        enemy.move(1);
                        x++;
                        if (enemy.getX() < enemy.getWidth() + 1) {
                            enemyDirection = false;
                            changeDirection = true;
                            break;
                        }
                    }
                }
            }

            if (changeDirection) {
                changeEnemiesDirection();
            }

            for (int i = 0; i < enemyBullets.size(); i++) {
                EnemyBullet enemyBullet = (EnemyBullet)enemyBullets.get(i);
                enemyBullet.draw();
                enemyBullet.move(4);

                if(enemyBullet.getY() + (enemyBullet.getHeight() - 1) > player.getY() && enemyBullet.getX() + enemyBullet.getWidth() - 1 > player.getX() && enemyBullet.getX() - 1 < player.getX() + player.getWidth()){
                    destroyEnemyBullet(enemyBullet);
                    ((IHealable)player).lowerHealthAmount();
                }

                if (enemyBullet.getY() > height) {
                    destroyEnemyBullet(enemyBullet);
                }
            }

            if (playerBullets.size() > 0) {
                for (int i = 0; i < playerBullets.size(); i++) {
                    PlayerBullet playerBullet = (PlayerBullet)playerBullets.get(i);

                    for (int j = 0; j < walls.size(); j++) {
                        Wall wall = (Wall)walls.get(j);

                        if (playerBullet.getY() < wall.getY() + wall.getHeight() && playerBullet.getX() - 1 < wall.getX() + wall.getWidth() && playerBullet.getX() + (playerBullet.getWidth() - 1) > wall.getX()) {
                            destroyPlayerBullet(playerBullet);
                            wall.lowerHealthAmount();
                            if (wall.getHealthAmount() == 0) {
                                destroyWall(wall);
                                break;
                            }
                        }
                    }
                }
            }

            if (enemyBullets.size() > 0) {
                for (int i = 0; i < enemyBullets.size(); i++) {
                    EnemyBullet enemyBullet = (EnemyBullet)enemyBullets.get(i);

                    for (int j = 0; j < walls.size(); j++) {
                        Wall wall = (Wall)walls.get(j);
                        if (enemyBullet.getY() + enemyBullet.getHeight() - 1 > wall.getY() && enemyBullet.getX() < wall.getX() + (wall.getWidth() - 1) && enemyBullet.getX() + enemyBullet.getWidth() - 1 > wall.getX()) {
                            destroyEnemyBullet(enemyBullet);
                            wall.lowerHealthAmount();
                            if (wall.getHealthAmount() == 0) {
                                destroyWall(wall);
                                break;
                            }
                        }
                    }
                }
            }

            if(enemies.size() < 1)
            {
                gameWon = true;
                gameStarted = false;
            }
            else
            {
                for (int i = 0; i < enemies.size(); i++) {
                    if(enemies.get(i).getY() > (height * 0.75 - enemies.get(i).getHeight()))
                    {
                        gameStarted = false;
                        gameWon = false;
                        score.writeScoreToFile(this.filename, score.getScore());
                    }
                }
            }

            gc.setFont(Font.font(25));
            gc.setFill(Color.WHITE);
            int writtenHighScore;
            if(score.getScore() > score.getMaxScore())
            {
                writtenHighScore = score.getScore();
            }
            else
            {
                writtenHighScore = score.getMaxScore();
            }
            gc.fillText("Score: " + score.getScore(), 5,  25);
            gc.fillText("Highscore: " + writtenHighScore, 5, 50);
            gc.fillText("x " + enemies.size(), width * 0.7, 45);
            gc.setFont(Font.font(30));
            gc.fillText("LEVEL " + level, width * 0.35, 45);
        }
    }

    private void createPlayer(){
        double playerWidth = 50;
        double playerHeight = 40;
        double posX = (width / 2) - (playerWidth / 2);
        double posY = height - 75;
        double moveStep = 15;
        int playerScore = 0;
        if(gameWon)
        {
            level++;
            playerScore = savedScore;
        }
        int healthAmount = 3;
        Image image = new Image("file:Images/Player.png");
        player = new Player(posX, posY, playerWidth, playerHeight, moveStep, healthAmount, gc, image);
        score = new Score(playerScore, 0);
    }

    private void createEnemies(){
        // Grid
        int rowCount = 5;
        int colCount = 11;

        // Positions
        int posX = 250;
        int width = 40;
        int height = 30;
        int posY = divisionY + divisionHeight + height;
        int offset = 60;
        double moveStep = 0.1 * (1 + level);
        Image image = new Image("file:Images/Enemy.png");
        enemies = new ArrayList<>();
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
                Enemy enemy = new Enemy(posX, posY, width, height, moveStep, image, gc);
                enemies.add(enemy);
                posX += offset;
            }
            posX -= colCount * offset;
            posY += offset;
        }
    }

    private void createWalls(){
        int wallAmount = 4;

        int posX = 75;
        int width = 100;
        int height = 75;
        int posY = 500;
        int offset = 250;
        int healthAmount = 4;
        walls = new ArrayList<>();

        for(int i = 0; i < wallAmount; i++){
            Wall wall = new Wall(posX, posY, width, height, healthAmount, gc);
            walls.add(wall);
            posX += offset;
        }
    }

    private void shootPlayerBullet()
    {
        double width = 3;
        int height = 15;
        double posX = player.getX() + ((player.getWidth() / 2) - (width / 2));
        double posY = player.getY() - 10;
        double moveStep = 5;
        Color playerBulletColor = Color.WHITE;
        PlayerBullet playerBullet = new PlayerBullet(posX, posY, width, height, moveStep, playerBulletColor, gc);
        playerBullets.add(playerBullet);
    }

    public void shootEnemyBullet()
    {
        int n = rand.nextInt(enemies.size());
        int width = 3;
        int height = 30;
        double moveStep = 1;
        double posX = enemies.get(n).getX() + width;
        double posY = enemies.get(n).getY() + enemies.get(n).getHeight();
        Color enemyBulletColor = Color.WHITE;
        EnemyBullet enemyBullet = new EnemyBullet(posX, posY, width, height, moveStep, enemyBulletColor, gc);
        enemyBullets.add(enemyBullet);
    }

    private void destroyEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
    }

    private void destroyAllEnemies()
    {
        for(int i = 0; i < enemies.size();)
        {
            Enemy enemy = (Enemy)enemies.get(i);
            enemies.remove((enemy));
        }
    }

    private void destroyWall(Wall wall)
    {
        walls.remove(wall);
    }

    private void destroyPlayer()
    {
        player = null;
        score = null;
    }

    private void destroyAllEnemyBullets()
    {
        for(int i = 0; i < enemyBullets.size();)
        {
            EnemyBullet enemyBullet = (EnemyBullet)enemyBullets.get(i);
            enemyBullets.remove((enemyBullet));
        }
    }

    private void destroyAllPlayerBullets()
    {
        for(int i = 0; i < playerBullets.size();)
        {
            PlayerBullet playerBullet = (PlayerBullet)playerBullets.get(i);
            playerBullets.remove((playerBullet));
        }
    }

    private void destroyPlayerBullet(PlayerBullet playerBullet)
    {
        playerBullets.remove(playerBullet);
    }

    private void destroyEnemyBullet(EnemyBullet enemyBullet)
    {
        enemyBullets.remove(enemyBullet);
    }

    private void changeEnemiesDirection()
    {
        double value = 0.05;
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy enemy = (Enemy)enemies.get(i);
            enemy.setNewLocation();
            enemy.setMoveStep(value);
        }
        changeDirection = false;
    }
}