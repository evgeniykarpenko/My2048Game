import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int score = 0;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped;

    @Override
    public void initialize() {
        isGameStopped = false;
        setScreenSize(SIDE, SIDE);

        createGame();
        drawScene();

    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }

    private Color getColorByValue(int value) {
        switch (value) {

            case 2:
                return Color.BLUE;
            case 4:
                return Color.LIGHTBLUE;
            case 8:
                return Color.GREEN;
            case 16:
                return Color.LIGHTGREEN;
            case 32:
                return Color.BISQUE;
            case 64:
                return Color.HOTPINK;
            case 128:
                return Color.PINK;
            case 256:
                return Color.LIGHTCORAL;
            case 512:
                return Color.RED;
            case 1024:
                return Color.ROSYBROWN;
            case 2048:
                return Color.BEIGE;
            default:
                return Color.AQUA;
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        if (value == 0)
            setCellValueEx(x, y, color, "");
        else
            setCellValueEx(x, y, color, String.valueOf(value));
    }

    private void createNewNumber() {

        int w = getMaxTileValue();
        if (w >= 2048) win();
        int cell1;
        int cell2;
        int percent = getRandomNumber(10);
        do {
            cell1 = getRandomNumber(SIDE);
            cell2 = getRandomNumber(SIDE);
        } while (gameField[cell2][cell1] != 0);


        if (percent < 9) {
            gameField[cell2][cell1] = 2;
        } else if (percent == 9) {
            gameField[cell2][cell1] = 4;
        }
    }

    private boolean compressRow(int[] row) {
        boolean b = false;
        for (int i = 0; i < row.length - 1; i++) {

            if ((row[i] == 0) && (row[i + 1] != 0)) {
                b = true;
                row[i] = row[i + 1];
                row[i + 1] = 0;
            }

            if (b)
                compressRow(row);
        }

        return b;
    }

    private boolean mergeRow(int[] row) {
        boolean b = false;
        int temp = 0;
        for (int i = 0; i < row.length - 1; i++) {

            if ((row[i] != 0) && (row[i + 1] == row[i])) {
                b = true;
                row[i] += row[i + 1];
                score+=row[i];
                row[i + 1] = 0;
                setScore(score);
            }


        }

        return b;
    }

    @Override
    public void onKeyPress(Key key) {

        if(isGameStopped){
            if(key ==Key.SPACE)
            {
                isGameStopped = false;
                score = 0;
                setScore(0);
                createGame();

                // showMessageDialog(null,null,null,0);
                drawScene();
            }
        }
        else {

            if (!canUserMove()) {
                gameOver();

            } else if (key == Key.LEFT) {
                moveLeft();
                drawScene();
            } else if (key == Key.RIGHT) {
                moveRight();
                drawScene();
            } else if (key == Key.UP) {
                moveUp();
                drawScene();
            } else if (key == Key.DOWN) {
                moveDown();
                drawScene();
            }
        }

    }

    private void moveLeft() {
        boolean b = false;
        for (int i = 0; i < gameField.length; i++) {
            if (compressRow(gameField[i]))
                b = true;
            if (mergeRow(gameField[i]))
                b = true;
            if (compressRow(gameField[i]))
                b = true;
        }
        if (b) createNewNumber();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();


    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int n = SIDE;

        int[][] tempArray = new int[SIDE][SIDE];

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {

                tempArray[i][j] = gameField[SIDE - 1 - j][i];
                ;
            }
        }

        gameField = tempArray;
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {

                if (gameField[i][j] > max)
                    max = gameField[i][j];

            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "WIN", Color.WHITE, 70);
    }

    private void gameOver()
    {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "LOOSE", Color.BLACK, 70);
    }

    private boolean canUserMove() {
        boolean b = false;
        int count = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) {
                    count++;
                }
            }
        }
        if (count > 0) {
            b = true;
        }


        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE-1; j++) {
                if (gameField[i][j] == gameField[i][j + 1]) {
                    b = true;
                }
            }
        }

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE - 1; j++) {
                if (gameField[j][i] == gameField[j + 1][i]) {
                    b = true;
                }
            }
        }


        return b;
    }
}
