package hanoi;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Created by Z on 7/27/2018
 *
 * This class represents an instance of a pane with the Tower of
 * Hanoi. It extends the Pane class in JavaFX. It includes the basic
 * UI of the Tower of Hanoi as well as the animated solution.
 */
public class TowerOfHanoi extends Pane {

    /**
     * This class represents a disk in the Tower of Hanoi. It
     * extends the Rectangle class in JavaFX and an additional field
     * called level to hold the level of the disk.
     */
    private static class Disk extends Rectangle {
        int level;
        Disk(int level, double x, double y, double width, double height) {
            super(x, y, width, height);
            this.level = level;
        }
    }

    /**
     * An array of Deques that serves as stacks to represent the
     * three towers in a Tower of Hanoi.
     */
    protected final Deque<Disk>[] towers;

    /**
     * The number of disks.
     */
    private int level;

    /**
     * The width and height of the disks that are also used to scale
     * other components of the tower. Default values are set to 30 and
     * 25.
     */
    private double diskWidth = 30;
    private double diskHeight = 25;

    /**
     * Constructs a default tower of hanoi with a level of 3.
     */
    public TowerOfHanoi() {
        this(3);
    }

    /**
     * Constructs a tower of hanoi with the given level and resets
     * the tower to its initial state.
     *
     * @param level the level
     */
    @SuppressWarnings("unchecked")
    public TowerOfHanoi(int level) {
        this.level = level;
        towers = (Deque<Disk>[]) new Deque[3];
        for (int i = 0; i < towers.length; i++)
            towers[i] = new ArrayDeque<>();

        reset();
    }

    /**
     * Returns the level of this tower.
     *
     * @return  the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of this tower and resets the UI and the
     * animation.
     *
     * @param level the new level
     */
    public void setLevel(int level) {
        this.level = level;
        reset();
    }

    /**
     * Returns the current disk width of this tower.
     *
     * @return  the current disk width
     */
    public double getDiskWidth() {
        return diskWidth;
    }

    /**
     * Sets the disk width of this tower and redraws the UI.
     *
     * @param diskWidth the new disk width
     */
    public void setDiskWidth(double diskWidth) {
        this.diskWidth = diskWidth;
        draw();
    }

    /**
     * Returns the current disk height of this tower.
     *
     * @return  the current disk height
     */
    public double getDiskHeight() {
        return diskHeight;
    }

    /**
     * Sets the disk height of this tower and redraws the UI.
     *
     * @param diskHeight    the new disk height
     */
    public void setDiskHeight(double diskHeight) {
        this.diskHeight = diskHeight;
        draw();
    }

    /**
     * Resets the UI to its original form; that is, moving all disks
     * to the first tower and resetting the animation to its starting
     * point.
     */
    public void reset() {
        getChildren().clear();
        Arrays.stream(towers).forEach(Deque::clear);
        animation.getChildren().clear();
        drawTowers();

        double baseWidth = (level + 1) * diskWidth;
        double padding = baseWidth / 2;

        setPadding(new Insets(0, padding, padding / 2,0));

        for (int i = level; i > 0; i--) {
            Disk r = new Disk(
                    i,
                    baseWidth - (i * diskWidth / 2),
                    baseWidth / 2 + i * diskHeight,
                    i * diskWidth,
                    diskHeight
            );
            r.setStroke(Color.BLACK);
            r.setFill(Color.WHITE);

            towers[0].push(r);
            getChildren().add(r);
        }
    }

    /**
     * Redraws the disks of this tower and pushes all disks to the
     * first deque.
     */
    private void draw() {
        getChildren().clear();
        drawTowers();

        double baseWidth = (level + 1) * diskWidth;
        for (int i = 0; i < 3; i++) {
            Deque<Disk> aux = new ArrayDeque<>();
            Deque<Disk> tower = towers[i];

            while (!tower.isEmpty()) {
                Disk oldDisk = towers[i].pop();
                Disk newDisk = new Disk(
                        oldDisk.level,
                        baseWidth - (oldDisk.level * diskWidth / 2),
                        baseWidth / 2 + oldDisk.level * diskHeight,
                        oldDisk.level * diskWidth,
                        diskHeight
                );
                newDisk.setFill(Color.WHITE);
                newDisk.setStroke(Color.BLACK);

                aux.push(newDisk);
                getChildren().add(newDisk);
            }

            while (!aux.isEmpty())
                tower.push(aux.pop());
        }
    }

    /**
     * Draws the three towers.
     */
    private void drawTowers() {
        double baseWidth = (level + 1) * diskWidth;
        double baseHeight = diskHeight * (2.0 / 3);
        double poleHeight = (level + 1) * diskHeight;
        double gap = baseWidth / 2 * 3;

        for (int i = 0; i < 3; i++) {
            Rectangle r = new Rectangle(
                    i * gap + (baseWidth / 2),
                    baseWidth / 2 + poleHeight,
                    baseWidth,
                    baseHeight
            );
            r.setFill(Color.BLACK);
            r.setStroke(Color.BLACK);

            Line pole = new Line(
                    i * gap + baseWidth,
                    baseWidth / 2,
                    i * gap + baseWidth,
                    baseWidth / 2 + poleHeight
            );
            pole.setStrokeWidth(3.0);

            getChildren().addAll(r, pole);
        }
    }

    /**
     * The list of animations to perform as the solution of this
     * tower.
     */
    private SequentialTransition animation = new SequentialTransition();

    /**
     * Solves this tower and plays the animation.
     */
    public void solve() {
        animation.setCycleCount(1);
        solve(level, 0, 1, 2);
        animation.play();
    }

    /**
     * Private helper method for solving this tower with recursion.
     *
     * @param level the current level
     * @param from  the current tower
     * @param to    the target tower
     * @param aux   the auxiliary tower
     */
    private void solve(int level, int from, int to, int aux) {
        if (level == 1) {
            animation.getChildren().add(moveDisk(from, to));
        } else {
            solve(level - 1, from, aux, to);
            animation.getChildren().add(moveDisk(from, to));
            solve(level - 1, aux, to, from);
        }
    }

    /**
     * Private helper method for moving a disk from one tower to
     * another and returns the list of animation for such.
     *
     * @param from  the current tower
     * @param to    the target tower
     * @return      the list of animations for such actions
     */
    private Animation moveDisk(int from, int to) {
        Disk disk = towers[from].pop();
        towers[to].push(disk);

        double baseWidth = (level + 1) * diskWidth;
        double poleHeight = (level + 1) * diskHeight;
        double padding = baseWidth / 2;

        Line up = new Line(
                from * 1.5 * baseWidth + baseWidth,
                padding + (poleHeight - (towers[from].size() + 1) * diskHeight) + diskHeight / 2,
                from * 1.5 * baseWidth + baseWidth,
                padding - diskHeight + diskHeight / 2
        );
        PathTransition moveUp = new PathTransition(Duration.millis(500), up, disk);

        Line side = new Line(
                up.getEndX(),
                up.getEndY(),
                to * 1.5 * baseWidth + baseWidth,
                up.getEndY()
        );
        PathTransition moveSide = new PathTransition(Duration.millis(500), side, disk);

        Line down = new Line(
                side.getEndX(),
                side.getEndY(),
                side.getEndX(),
                padding + (poleHeight - towers[to].size() * diskHeight) + diskHeight / 2
        );
        PathTransition moveDown = new PathTransition(Duration.millis(500), down, disk);

        return new SequentialTransition(disk, moveUp, moveSide, moveDown);
    }

    /**
     * Pauses the animation
     */
    public void pause() {
        animation.pause();
    }

    /**
     * Resumes the animation
     */
    public void play() {
        animation.play();
    }

    /**
     * Stops the animation and resets it to its starting point.
     */
    public void stop() {
        animation.stop();
    }

    /**
     * Sets the rate(speed) of the animation.
     *
     * @param rate the new rate
     */
    public void setRate(double rate) {
        animation.setRate(rate);
    }

    /**
     * Checks if the animation is running.
     *
     * @return  true if the animation is playing;
     *          false otherwise
     */
    public boolean isRunning() {
        return animation.getStatus() == Animation.Status.RUNNING;
    }

    /**
     * Checks if the animation is paused.
     *
     * @return  true if the animation is paused;
     *          false otherwise
     */
    public boolean isPaused() {
        return animation.getStatus() == Animation.Status.PAUSED;
    }

    /**
     * Checks if the animation is stopped, that is, the animation has
     * not yet started or has already ended.
     *
     * @return  true if the animation is stopped;
     *          false otherwise
     */
    public boolean isStopped() {
        return animation.getStatus() == Animation.Status.STOPPED;
    }
}
