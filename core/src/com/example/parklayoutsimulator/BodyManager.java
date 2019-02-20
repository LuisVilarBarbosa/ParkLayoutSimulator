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
    public static final short MOVABLE_ENTITY_BIT = 1 << 1;

    public static Body generateParkingSpace(float x, float y, int width, int height, World world, short entity_bit) {
        Sprite sprite = generateParkingSpaceSprite(width, height);
        BodyDef bodyDef = new BodyDef();
        if (entity_bit == MOVABLE_ENTITY_BIT)
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        else if (entity_bit == FIXED_ENTITY_BIT)
            bodyDef.type = BodyDef.BodyType.StaticBody;
        else
            throw new IllegalStateException();
        bodyDef.position.set(x + width / 2f, y + height / 2f);
        Body body = world.createBody(bodyDef);
        body.setUserData(sprite);
        updateSpritePosition(body);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = entity_bit;
        fixtureDef.filter.maskBits = MOVABLE_ENTITY_BIT | FIXED_ENTITY_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();    // Shape is the only disposable of the lot.
        return body;
    }

    public static Body generateWall(float x, float y, int width, int height, World world) {
        Sprite sprite = generateWallSprite(width, height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width / 2f, y + height / 2f);
        Body body = world.createBody(bodyDef);
        body.setUserData(sprite);
        updateSpritePosition(body);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = FIXED_ENTITY_BIT;
        fixtureDef.filter.maskBits = MOVABLE_ENTITY_BIT | FIXED_ENTITY_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();    // Shape is the only disposable of the lot.
        return body;
    }

    public static Body generateScreenBoundaryBody(float x, float y, int width, int height, World world) {
        Sprite sprite = generateScreenBoundarySprite(width, height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width / 2f, y + height / 2f);
        Body body = world.createBody(bodyDef);
        body.setUserData(sprite);
        updateSpritePosition(body);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = FIXED_ENTITY_BIT;
        fixtureDef.filter.maskBits = MOVABLE_ENTITY_BIT | FIXED_ENTITY_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();    // Shape is the only disposable of the lot.
        return body;
    }

    public static void updateSpritePosition(Body body) {
        Sprite sprite = getSprite(body);
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
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

    private static Sprite generateScreenBoundarySprite(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();   // Now, the pixmap is the textures responsibility.
        return new Sprite(texture);
    }
}
