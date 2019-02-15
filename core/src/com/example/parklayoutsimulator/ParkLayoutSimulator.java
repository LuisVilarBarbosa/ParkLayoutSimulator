package com.example.parklayoutsimulator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class ParkLayoutSimulator implements ApplicationListener, GestureDetector.GestureListener {
    private static final boolean DEBUG_MODE = true;
    private Vector2 worldDimensions;
    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private World world;
    private ArrayList<Body> bodies;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        worldDimensions = FileParser.getWorldDimensions();
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        camera = new OrthographicCamera(worldDimensions.y * aspectRatio, worldDimensions.y);
        camera.position.set(worldDimensions.x / 2, worldDimensions.y / 2, 0);
        viewport = new FitViewport(worldDimensions.x, worldDimensions.y, camera);
        batch = new SpriteBatch();
        Vector2 gravity = new Vector2(0, 0);
        world = new World(gravity, true);
        bodies = FileParser.getBodies(world);
        setContactListener();
        generateScreenBoundaryBodies();
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }

    @Override
    public void render() {
        camera.update();
        world.step(Gdx.graphics.getDeltaTime(), 1, 1);
        for (Body body : bodies)
            BodyManager.updateSpritePosition(body);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);    // Gray
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Body body : bodies)
            BodyManager.getSprite(body).draw(batch);
        batch.end();
        if(DEBUG_MODE) {
            Matrix4 debugMatrix = batch.getProjectionMatrix().cpy();
            debugRenderer.render(world, debugMatrix);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        //camera.translate(deltaX, 0, 0);
        //camera.update();
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }

    private void setContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body body1 = contact.getFixtureA().getBody();
                Body body2 = contact.getFixtureB().getBody();
                if (body1.getType().equals(BodyDef.BodyType.DynamicBody))
                    body1.applyForceToCenter(randomValue(), randomValue(), true);
                else if (body2.getType().equals(BodyDef.BodyType.DynamicBody))
                    body2.applyForceToCenter(randomValue(), randomValue(), true);
                else
                    throw new IllegalStateException();
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

            private int randomValue() {
                return new BigInteger(256, new Random()).intValue();
            }
        });
    }

    private void generateScreenBoundaryBodies() {
        int width = MathUtils.ceil(worldDimensions.x);
        int height = MathUtils.ceil(worldDimensions.y);
        bodies.add(BodyManager.generateScreenBoundaryBody(0, -1, width, 1, world));
        bodies.add(BodyManager.generateScreenBoundaryBody(0, height, width, 1, world));
        bodies.add(BodyManager.generateScreenBoundaryBody(-1, 0, 1, height, world));
        bodies.add(BodyManager.generateScreenBoundaryBody(width, 0, 1, height, world));
    }
}
