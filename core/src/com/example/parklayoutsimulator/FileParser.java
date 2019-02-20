package com.example.parklayoutsimulator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

// This class should handle negative values and cases where the file is not in the expected state.
class FileParser {
    private static final String filename = "parameters.json";
    private static JsonValue parameters = new JsonReader().parse(new FileHandle(filename));
    private static Vector2 worldDimensions = null;
    private static ArrayList<Body> bodies = null;

    public static Vector2 getWorldDimensions() {
        if (worldDimensions != null)
            return worldDimensions;
        JsonValue world = parameters.get("world");
        int worldWidth = world.getInt("width");
        int worldHeight = world.getInt("height");
        worldDimensions = new Vector2(worldWidth, worldHeight);
        return worldDimensions;
    }

    public static ArrayList<Body> getBodies(World world) {
        if (bodies != null)
            return bodies;
        bodies = new ArrayList<Body>();
        JsonValue walls = parameters.get("walls");
        for (JsonValue wall : walls) {
            int x = wall.getInt("x");
            int y = wall.getInt("y");
            int width = wall.getInt("width");
            int height = wall.getInt("height");
            bodies.add(BodyManager.generateWall(x, y, width, height, world));
        }
        JsonValue fixedParkingSpaces = parameters.get("fixedParkingSpaces");
        for (JsonValue fixedParkingSpace : fixedParkingSpaces) {
            int x = fixedParkingSpace.getInt("x");
            int y = fixedParkingSpace.getInt("y");
            int width = fixedParkingSpace.getInt("width");
            int height = fixedParkingSpace.getInt("height");
            bodies.add(BodyManager.generateParkingSpace(x, y, width, height, world, BodyManager.FIXED_ENTITY_BIT));
        }
        JsonValue movableParkingSpaces = parameters.get("movableParkingSpaces");
        for (JsonValue movableParkingSpace : movableParkingSpaces) {
            int width = movableParkingSpace.getInt("width");
            int height = movableParkingSpace.getInt("height");
            int quantity = movableParkingSpace.getInt("quantity");
            for (int i = 0; i < quantity; i++)
                // Bodies shouldn't be placed always in the same place. What if there is a closed region where the body is placed? It cannot move outwards.
                bodies.add(BodyManager.generateParkingSpace(worldDimensions.x / 2, worldDimensions.y / 2, width, height, world, BodyManager.MOVABLE_ENTITY_BIT));
        }
        return bodies;
    }
}
