package com.example.parklayoutsimulator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

class FileParser {
    private static ArrayList<Body> bodies = null;

    public static Vector2 getWorldDimensions(){
        // TODO: load from file
        return new Vector2(500, 250);
    }

    public static ArrayList<Body> getBodies(World world) {
        if (bodies != null)
            return bodies;
        bodies = new ArrayList<Body>();
        // TODO: load from file
        bodies.add(BodyManager.generateWall(0, 0, 250, 120, world));
        bodies.add(BodyManager.generateParkingSpace(250, 120, 100, 20, world, BodyManager.MOVING_ENTITY_BIT));
        bodies.add(BodyManager.generateParkingSpace(350, 140, 100, 20, world, BodyManager.FIXED_ENTITY_BIT));
        return bodies;
    }
}
