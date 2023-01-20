package com.totvs.world;

import com.totvs.entities.Player;
import com.totvs.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class World {
    public static Tile[] tiles;
    public static int WIDTH = 30, HEIGHT = 20;
    public static final int TILE_SIZE = 16;
    public static List<Player> players = new ArrayList<>();
    public static int playerCount = 0;
    public BufferedImage map;

    public World(String path) {
        try {
            map = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            players.add(Game.player1);
            players.add(Game.player2);

            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            // cria o mapa e coloca o jogador nele usando pixels coloridos (preto, branco, vermelho, azul)
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];

                    if (pixelAtual == 0xff000000) {
                        // chão / preto
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    } else if (pixelAtual == 0xffffffff) {
                        // parede / branco
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    } else if (pixelAtual == 0xff0000ff && players.size() > playerCount) {
                        // jogador / azul
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                        players.get(playerCount).setX(xx * 16);
                        players.get(playerCount).setY(yy * 16);
                        playerCount++;
                    } else {
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // verifica se ha colisões em blocos do mapa
    public static boolean isFree(int xNext, int yNext) {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles[x1 +(y1 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x2 +(y2 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x3 +(y3 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x4 +(y4 * World.WIDTH)] instanceof WallTile));
    }

    public void render(Graphics g) {
        for (int xx = 0; xx < WIDTH; xx++) {
            for (int yy = 0; yy < HEIGHT; yy++) {
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
}
