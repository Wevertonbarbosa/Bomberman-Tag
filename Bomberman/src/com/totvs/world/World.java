package com.totvs.world;

import com.totvs.entities.Player;
import com.totvs.main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    public static Tile[] tiles;
    public static int WIDTH = 30, HEIGHT = 20;
    public static final int TILE_SIZE = 16;
    public static List<Player> players = new ArrayList<>();
    public static int playerCount = 0;

    public World() {
        tiles = new Tile[WIDTH * HEIGHT];

        players.add(Game.player1);
        players.add(Game.player2);

        for (int xx = 0; xx < WIDTH; xx++) {
            for (int yy = 0; yy < HEIGHT; yy++) {
                tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
            }
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
