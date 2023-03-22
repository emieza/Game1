package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.Gdx;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	OrthographicCamera camera;
	Texture img;
	float x = 0;
	float y = 0;
	float vx = 5.0f;
	float vy = 5.0f;
	float[] bgcolor = {1,1,0};
	String message = "hola";

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	protected void toggleBackground() {
		Random rand = new Random();
		bgcolor[0] = rand.nextFloat();
		bgcolor[1] = rand.nextFloat();
		bgcolor[2] = rand.nextFloat();
	}

	@Override
	public void render () {
		// CALCULA
		x = x + vx;
		y = y + vy;
		// límits
		if( y<=0 || y >= 480-img.getHeight() ) {
			vy = -1.0f * vy;

			// try http request
			HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
			Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("https://bytes.cat").build();
			Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
				@Override
				public void handleHttpResponse(Net.HttpResponse httpResponse) {
					toggleBackground();
					if( message == "hola" )
						message = Integer.toString(httpResponse.getStatus().getStatusCode());
					else
						message = "hola";
				}

				@Override
				public void failed(Throwable t) {

				}

				@Override
				public void cancelled() {

				}
			});

		}
		if( x<=0 || x >= 800-img.getWidth() ) {
			vx = -1.0f * vx;
		}

		// PINTA
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		ScreenUtils.clear(bgcolor[0], bgcolor[1], bgcolor[2], 1);
		batch.begin();
		batch.draw(img,x,y);
		font.draw(batch,message,50,50);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
