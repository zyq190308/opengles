package com.example.triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;



import android.opengl.GLES20;
import android.opengl.Matrix;

public class Square {

	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;
	int muMVPMatrixHandle;//总变换矩阵引用
	int mProgram;
	int radio;
	float num = 0;
	float []mMVPMatrix = new float[16];
	float[] mVMatrix = new float[16];
	float[] mViewMatrix = new float[16];
	float[] mProjectionMatrix = new float[16];
	static int one = 0x10000; 
	FloatBuffer colorBuffer;
	// 每个顶点的坐标数
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { 
		//		//上面
		//		-0.5f,0.5f,-0.5f,//0
		//		-0.5f,0.5f,0.5f,//1
		//		0.5f,0.5f,0.5f,//2
		//		0.5f,0.5f,-0.5f,//3   0,1,2,0,2,1
		//
		//		//下面
		//		-0.5f,-0.5f,-0.5f,//4
		//		-0.5f,-0.5f,0.5f,//5
		//		0.5f,-0.5f,0.5f,//6
		//		0.5f,-0.5f,-0.5f,//7  4,5,6,4,6,7
		//
		//		//左面
		//		-0.5f,0.5f,-0.5f, //0
		//		-0.5f,-0.5f,-0.5f,//4
		//
		//		-0.5f,-0.5f,0.5f,//5  
		//		-0.5f,0.5f,0.5f,//1   0,4,5,0,5,1
		//
		//
		//		//右面
		//		0.5f,0.5f,-0.5f,//3
		//		0.5f,-0.5f,-0.5f,//7
		//
		//		0.5f,-0.5f,0.5f,//6
		//		0.5f,0.5f,0.5f,//2 	3,7,6,3,7,2
		//
		//
		//
		//
		//		//前面
		//		-0.5f,0.5f,0.5f,//1
		//		-0.5f,-0.5f,0.5f,//5
		//		0.5f,-0.5f,0.5f,//6
		//		0.5f,0.5f,0.5f,//2		1,5,6,1,6,2
		//
		//		//后面
		//
		//		-0.5f,0.5f,-0.5f,//0
		//		-0.5f,-0.5f,-0.5f,//4
		//		0.5f,-0.5f,-0.5f,//7
		//		0.5f,0.5f,-0.5f,//3		0,4,7,0,7,3

		// Front face
		-1.0f, 1.0f, 1.0f,                
		-1.0f, -1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 
		-1.0f, -1.0f, 1.0f,                 
		1.0f, -1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,

		// Right face
		1.0f, 1.0f, 1.0f,                
		1.0f, -1.0f, 1.0f,
		1.0f, 1.0f, -1.0f,
		1.0f, -1.0f, 1.0f,                
		1.0f, -1.0f, -1.0f,
		1.0f, 1.0f, -1.0f,

		// Back face
		1.0f, 1.0f, -1.0f,                
		1.0f, -1.0f, -1.0f,
		-1.0f, 1.0f, -1.0f,
		1.0f, -1.0f, -1.0f,                
		-1.0f, -1.0f, -1.0f,
		-1.0f, 1.0f, -1.0f,

		// Left face
		-1.0f, 1.0f, -1.0f,                
		-1.0f, -1.0f, -1.0f,
		-1.0f, 1.0f, 1.0f, 
		-1.0f, -1.0f, -1.0f,                
		-1.0f, -1.0f, 1.0f, 
		-1.0f, 1.0f, 1.0f, 

		// Top face
		-1.0f, 1.0f, -1.0f,                
		-1.0f, 1.0f, 1.0f, 
		1.0f, 1.0f, -1.0f, 
		-1.0f, 1.0f, 1.0f,                 
		1.0f, 1.0f, 1.0f, 
		1.0f, 1.0f, -1.0f,

		// Bottom face
		1.0f, -1.0f, -1.0f,                
		1.0f, -1.0f, 1.0f, 
		-1.0f, -1.0f, -1.0f,
		1.0f, -1.0f, 1.0f,                 
		-1.0f, -1.0f, 1.0f,
		-1.0f, -1.0f, -1.0f,  



	};




	short drawOrder[] = {  
			0,1,2,0,2,1,
			4,5,6,4,6,7,
			0,4,5,0,5,1,
			3,7,6,3,7,2,
			1,5,6,1,6,2,
			0,4,7,0,7,3}; // 顶点的绘制顺序


	float colors[] = { 
			//			0.5f, 0.5f, 0.5f, 1.0f,
			//			0.3f,0.8f,0.9f,1.0f,
			//			0.6f,0.7f,0.2f,1.0f,
			//			0.6f,0.8f,0.3f,1.0f,
			//			0.4f,0.3f,0.2f,1.0f,
			//			0.7f,0.7f,0.7f,1.0f
			// Front face (red)
			1.0f, 0.0f, 0.0f, 1.0f,                
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,                
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,

			  // Right face (green)
			0.0f, 1.0f, 0.0f, 1.0f,                
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,                
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,

			  // Back face (blue)
			0.0f, 0.0f, 1.0f, 1.0f,                
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,                
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,

			// Left face (yellow)
			1.0f, 1.0f, 0.0f, 1.0f,                
			1.0f, 1.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 0.0f, 1.0f,                
			1.0f, 1.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 0.0f, 1.0f,


			// Top face (cyan)
			0.0f, 1.0f, 1.0f, 1.0f,                
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,                
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,

			 // Bottom face (magenta)
			1.0f, 0.0f, 1.0f, 1.0f,                
			1.0f, 0.0f, 1.0f, 1.0f,
			1.0f, 0.0f, 1.0f, 1.0f,
			1.0f, 0.0f, 1.0f, 1.0f,                
			1.0f, 0.0f, 1.0f, 1.0f,
			1.0f, 0.0f, 1.0f, 1.0f  



	};


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

	public Square() {

		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
				// (坐标数 * 4)
				squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		// 为绘制列表初始化字节缓冲
		// (对应顺序的坐标数 * 2)short是2字节
		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		ByteBuffer cbb
		= ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
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

		num+=2;
		// 将program加入OpenGL ES环境中
		GLES20.glUseProgram(mProgram);


		// 获取指向vertexshader的成员vPosition的 handle
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		int maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		// 启用一个指向三角形的顶点数组的handle
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(maColorHandle);  
		// 准备三角形的坐标数据
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				0, vertexBuffer);

		//为画笔指定顶点着色数据
		GLES20.glVertexAttribPointer  
		(
				maColorHandle, 
				4, 
				GLES20.GL_FLOAT, 
				false,
				0,   
				colorBuffer
				); 




		//获取程序中总变换矩阵引用id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "mVMatrix"); 
		Matrix.setIdentityM(mVMatrix, 0);
		Matrix.scaleM(mVMatrix, 0, 0.4f, 0.3f, 0.4f);
		Matrix.rotateM(mVMatrix, 0, num, 1.0f, 1.0f, 0.0f);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1,false,mVMatrix,0);

		// 画三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);

		// 禁用指向三角形的顶点数组
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
