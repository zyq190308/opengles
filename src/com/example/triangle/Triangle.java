package com.example.triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Triangle {


	private FloatBuffer vertexBuffer;
	FloatBuffer textureBuffer;
	FloatBuffer colorBuffer;
	int mProgram;
	float num = 0;
	// 数组中每个顶点的坐标数
	static final int COORDS_PER_VERTEX = 3;
	static float triangleCoords[] = { // 按逆时针方向顺序:
		0.0f,  0.5f, 0.0f ,  // top
		-0.5f, -0.5f, 0.0f,  // bottom left
		0.5f, -0.5f, 0.0f    // bottom right
	};
	//	float[] mVMatrix = new float[]{
	//		1.0f,0.0f,0.0f,(float) Math.sin(num),
	//		0.0f,1.0f,0.0f,0.0f,
	//		0.0f,0.0f,1.0f,0.0f,
	//		0.0f,0.0f,0.0f,1.0f
	//			
	//	}; 

	//	float[] mVMatrix = new float[]{
	//			
	//			
	//			(float) Math.cos(num),(float) -Math.sin(num),0,0,
	//			(float) Math.sin(num),(float) Math.cos(num),0,0,
	//			0,0,1,0,
	//			0,0,0,1
	//	};
	float[] mVMatrix = new float[16];
	// 设置颜色，分别为red, green, blue 和alpha (opacity)
	float []color = {1.0f, 0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,  
			0.0f, 0.0f, 1.0f, 1.0f};


	String vertexShaderCode =
					"attribute vec3 vPosition;" +
					"attribute vec4 aColor;"+
					"varying  vec4 vColor;"+
					"uniform mat4 mVMatrix; "+//总变换矩阵
					"void main() {" +
					"  gl_Position = mVMatrix*vec4(vPosition,1.0);" +
					"	vColor = aColor;"+
					"}";

	String fragmentShaderCode =
			"precision mediump float;" +
					"varying vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";



	public Triangle() {




		// 为存放形状的坐标，初始化顶点字节缓冲
		ByteBuffer bb = ByteBuffer.allocateDirect(
				// (坐标数 * 4)float占四字节
				triangleCoords.length * 4);
		// 设用设备的本点字节序
		bb.order(ByteOrder.nativeOrder());

		// 从ByteBuffer创建一个浮点缓冲
		vertexBuffer = bb.asFloatBuffer();
		// 把坐标们加入FloatBuffer中
		vertexBuffer.put(triangleCoords);
		// 设置buffer，从第一个坐标开始读
		vertexBuffer.position(0);


		ByteBuffer cbb = ByteBuffer.allocateDirect(
				// (坐标数 * 4)float占四字节
				color.length * 4);
		// 设用设备的本点字节序
		cbb.order(ByteOrder.nativeOrder());

		// 从ByteBuffer创建一个浮点缓冲
		colorBuffer = cbb.asFloatBuffer();
		// 把坐标们加入FloatBuffer中
		colorBuffer.put(color);
		// 设置buffer，从第一个坐标开始读
		colorBuffer.position(0);



		//第一步，创建顶点着色器vertexShader
		int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		//第二步,加载顶点着色器glsl代码
		GLES20.glShaderSource(vertexShader, vertexShaderCode);
		GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
		//第三步,编译着色器
		GLES20.glCompileShader(vertexShader);
		GLES20.glCompileShader(fragmentShader);

		//第四步, 创建一个空的OpenGL ES Program
		mProgram = GLES20.glCreateProgram();  
		//第五步
		GLES20.glAttachShader(mProgram, vertexShader);   // 将vertex shader添加到program
		GLES20.glAttachShader(mProgram, fragmentShader); // 将fragment shader添加到program
		//第六步， 创建可执行的 OpenGL ES program
		GLES20.glLinkProgram(mProgram);                  


	}

	public void draw() {


		num+=5;
		// 将program加入OpenGL ES环境中
		GLES20.glUseProgram(mProgram);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		// 获取指向fragment shader的成员vColor的handle 
		int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);  

		// 准备三角形的坐标数据
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				0, vertexBuffer);


		GLES20.glVertexAttribPointer  
		(
				mColorHandle, 
				4, 
				GLES20.GL_FLOAT, 
				false,
				0,   
				colorBuffer
				); 


	
		int mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "mVMatrix");
		Matrix.setIdentityM(mVMatrix, 0);
				Matrix.translateM(mVMatrix, 0, (float)Math.sin(num), 0.0f, 0.0f);
		//		Matrix.scaleM(mVMatrix, 0,(float)Math.sin(num), (float)Math.sin(num), (float)Math.sin(num));
		//		Matrix.setRotateM(mVMatrix, 0, 0.0f, 1.0f, 0.0f, 0.0f);
		//		Matrix.setRotateM(mVMatrix, 0, 0, 0, 1, 0);
		//		Matrix.rotateM(mVMatrix, 0, 60.0f, 0.0f, 0.0f, 1.0f);

		//Matrix.rotateM(mVMatrix, 0, num, 1.0f, 1.0f, 1.0f);

		GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mVMatrix, 0);
	
		
		// 画三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

		// 禁用指向三角形的顶点数组
		//GLES20.glDisableVertexAttribArray(mPositionHandle);
	}



}
