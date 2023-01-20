package com.totvs.entities;

import com.totvs.main.Game;
import com.totvs.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public boolean right, up, left, down;
    public final int downDir = 0, leftDir = 1, upDir = 2, rightDir = 3;
    public int dir = 0;
    public double speed = 2;
    public boolean isTagged = false, isTouching = false;

    private int frames = 0, index;
    private final int maxFrames = 6, maxIndex = 2;
    private boolean moved = false, canMove = true;

    private final BufferedImage[] rightPlayer;
    private final BufferedImage[] leftPlayer;
    private final BufferedImage[] upPlayer;
    private final BufferedImage[] downPlayer;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[3];
        leftPlayer = new BufferedImage[3];
        upPlayer = new BufferedImage[3];
        downPlayer = new BufferedImage[3];

        for (int i = 0; i < rightPlayer.length; i++) {
            rightPlayer[i] = sprite.getSubimage(i * 16, 39, 16, 26);
            leftPlayer[i] = sprite.getSubimage(i * 16, 103, 16, 26);
            upPlayer[i] = sprite.getSubimage(i * 16, 7, 16, 26);
            downPlayer[i] = sprite.getSubimage(i * 16, 69, 16, 26);
        }
    }

    @Override
    public void checkCollision() {
        for (int i = 0; i < Game.players.size(); i++) {
            if (!Game.players.get(i).equals(this)) {
                isTouching = this.getHitBox().intersects(Game.players.get(i).getHitBox()) && this.isTagged;
            }
        }
    }

    @Override
    public void tick() {
        this.updateHitbox(-1,  -3);
        moved = false;

        if (right && World.isFree((int) (this.getX() + speed), this.getY()) && this.canMove) {
            moved = true;
            dir = rightDir;
            x += speed;
        } else if (left && World.isFree((int) (this.getX() - speed), this.getY()) && this.canMove) {
            moved = true;
            dir = leftDir;
            x -= speed - 0.7;
        }

        if (up && World.isFree(this.getX(), (int) (this.getY() - speed)) && this.canMove) {
            moved = true;
            dir = upDir;
            y -= speed - 0.7;
        } else if (down && World.isFree(this.getX(), (int) (this.getY() + speed)) && this.canMove) {
            moved = true;
            dir = downDir;
            y += speed;
        }

        // verifica se pegou o outro jogador
        if (isTagged && !Game.tagIsOnCooldown) {
            checkCollision();
            for (int i = 0; i < Game.players.size(); i++) {
                if (!Game.players.get(i).equals(this)) {
                    if (isTouching) {
                        Game.tagIsOnCooldown = true;
                        Game.players.get(i).isTagged = true;
                        this.isTagged = false;
                        System.out.println("peguei vc " + (i + 1));
                    }
                }
            }
        }

        // animação de movimento do jogador
        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        } else {
            index = 0;
        }
    }

    public void render(Graphics g) {
        switch (dir) {
            case 0 -> g.drawImage(downPlayer[index], this.getX(), this.getY() - 10, null);
            case 1 -> g.drawImage(leftPlayer[index], this.getX(), this.getY() - 10, null);
            case 2 -> g.drawImage(upPlayer[index], this.getX(), this.getY() - 10, null);
            case 3 -> g.drawImage(rightPlayer[index], this.getX(), this.getY() - 10, null);
        }
    }
}
