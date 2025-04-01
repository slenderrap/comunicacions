package io.github.slenderrap;
import static java.lang.Math.abs;

import com.badlogic.gdx.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;

import jdk.internal.org.jline.utils.Log;


public class GameScreen implements Screen {
    private Main main;
    private WebSocket socket;
    private String address = "oarribastaulats.ieti.site";
    private int port = 443;
    private float stateTime;
    private int posX, posY,posXantic, posYantic ;

    // Constructor recibe la clase principal
    public GameScreen(Main main) {
        this.main = main;

        String wsurl = WebSockets.toSecureWebSocketUrl(address, port);
        Gdx.app.log("info","intentant conectar-se a "+ wsurl);
        socket = WebSockets.newSocket(wsurl);
        socket.setSendGracefully(false);
        socket.addListener(new MyWSListener());
        socket.connect();
    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        // Lógica de detección de toques
        if (Gdx.input.isTouched()) {
            posX=Gdx.input.getX();
            posY=Gdx.input.getY();
            if (abs(posY - posYantic) > 1.0f||abs(posX - posXantic) > 1.0f) {
                posXantic = posX;
                posYantic = posY;
                Gdx.app.log("GameScreen", "Has tocat la pantalla!");
                socket.send("Has premut a la posició X: " + posX +" i Y: "+posY+" de la pantalla");
            }
        }


        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // COMUNICACIONS (rebuda de missatges)
    /////////////////////////////////////////////
    class MyWSListener implements WebSocketListener {

        @Override
        public boolean onOpen(WebSocket webSocket) {
            System.out.println("Opening...");
            return false;
        }

        @Override
        public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
            System.out.println("Closing...");
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, String packet) {
            System.out.println("Message:"+packet);
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, byte[] packet) {
            System.out.println("Message:"+packet);
            return false;
        }

        @Override
        public boolean onError(WebSocket webSocket, Throwable error) {
            System.out.println("ERROR:"+error.toString());
            return false;
        }
    }
    // Métodos requeridos por la interfaz Screen
    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        socket.close();
    }
}
