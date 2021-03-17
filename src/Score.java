import java.io.*;

public class Score{

    private int score;
    private int maxScore;

    public Score(int score, int maxScore)
    {
        this.score = score;
        this.maxScore = maxScore;
    }

    public int getScore()
    {
        return score;
    }

    public void addScore()
    {
        score++;
    }

    public int getMaxScore()
    {
        return maxScore;
    }

    public void setMaxScore(int number)
    {
        maxScore = number;
    }

    public void writeScoreToFile(String filename, int Score) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Integer.toString(Score));
            bw.newLine();
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public int getMaxScoreFromFile(String filename) {
        int scoreFromFile = 0;
        try {
            File file = new File(filename);
            if (file.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                String line;
                while((line = br.readLine()) != null) {
                    int scoreLine = Integer.parseInt(line);
                    if (scoreFromFile < scoreLine) {
                        scoreFromFile = scoreLine;
                    }
                }

                br.close();
                fr.close();
            } else {
                file.createNewFile();
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }
        return scoreFromFile;
    }
}
