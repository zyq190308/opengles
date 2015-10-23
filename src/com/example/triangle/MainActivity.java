package com.example.triangle;



import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

/*
 * opengl es2.0 »æÖÆÈý½ÇÐÎ
 * 
 */
public class MainActivity extends Activity {
	GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		glSurfaceView = new GLSurfaceView(this);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(new MyRender());
		setContentView(glSurfaceView);
	}


}
