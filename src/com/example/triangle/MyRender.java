package com.example.triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;





import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class MyRender implements Renderer {




	Triangle mTriangle;
	Square mSquare;
	static int radio;
	float[] mRotationMatrix = new float[16];  
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置背景色
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		//打开背面剪裁   
		GLES20.glEnable(GLES20.GL_CULL_FACE);  
		MatrixState.setInitStack();
		// mTriangle = new Triangle();
		mSquare = new Square();

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// 此投影矩阵在onDrawFrame()中将应用到对象的坐标
		//Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

	}

	@Override
	public void onDrawFrame(GL10 gl) {

		// 重绘背景色
		GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		//Matrix.translateM(mRotationMatrix, 0, 5, 25, 5);
		Matrix.rotateM(mRotationMatrix, 0, 30, 0.0f, 1.0f, 0.0f);

		
		mSquare.draw(mRotationMatrix);
		


	}

}
