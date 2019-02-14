package com.example.parklayoutsimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

class BodyManager {
    public static final short FIXED_ENTITY_BIT = 1;
    public static final short MOVING_ENTITY_BIT = 1 << 1;

    public static Body generateParkingSpace(int x, int y, int width, int height, World world, short entity_bit) {
        Sprite sprite = generateParkingSpaceSprite(width, height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);
        body.setUserData(sprite);
        updateSpritePosition(body);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = entity_bit;
        fixtureDef.filter.maskBits = MOVING_ENTITY_BIT | FIXED_ENTITY_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();    // Shape is the only disposable of the lot.
        return body;
    }

    public static Body generateWall(int x, int y, int width, int height, World world) {
        Sprite sprite = generateWallSprite(width, height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);
        body.setUserData(sprite);
        updateSpritePosition(body);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = FIXED_ENTITY_BIT;
        fixtureDef.filter.maskBits = MOVING_ENTITY_BIT | FIXED_ENTITY_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();    // Shape is the only disposable of the lot.
        return body;
    }

    public static void updateSpritePosition(Body body) {
        getSprite(body).setPosition(body.getPosition().x, body.getPosition().y);
    }

    public static Sprite getSprite(Body body) {
        return (Sprite) body.getUserData();
    }

    private static Sprite generateParkingSpaceSprite(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        pixmap.setColor(Color.YELLOW);
        pixmap.drawRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();   // Now, the pixmap is the textures responsibility.
        return new Sprite(texture);
    }

    private static Sprite generateWallSprite(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();   // Now, the pixmap is the textures responsibility.
        return new Sprite(texture);
    }
}
