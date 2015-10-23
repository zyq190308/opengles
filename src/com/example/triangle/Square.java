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
	float []mMVPMatrix;
	float[] mVMatrix = new float[16];
	static int one = 0x10000; 
	// 每个顶点的坐标数
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { 
		//上面
		-0.5f,0.5f,-0.5f,//0
		-0.5f,0.5f,0.5f,//1
		0.5f,0.5f,0.5f,//2
		0.5f,0.5f,-0.5f,//3   0,1,2,0,2,1

		//下面
		-0.5f,-0.5f,-0.5f,//4
		-0.5f,-0.5f,0.5f,//5
		0.5f,-0.5f,0.5f,//6
		0.5f,-0.5f,-0.5f,//7  4,5,6,4,6,7

		//左面
		-0.5f,0.5f,-0.5f, //0
		-0.5f,-0.5f,-0.5f,//4
		
		-0.5f,-0.5f,0.5f,//5  
		-0.5f,0.5f,0.5f,//1   0,4,5,0,5,1
		

		//右面
		0.5f,0.5f,-0.5f,//3
		0.5f,-0.5f,-0.5f,//7
		
		0.5f,-0.5f,0.5f,//6
		0.5f,0.5f,0.5f,//2 	3,7,6,3,7,2

		
		    

		//前面
		-0.5f,0.5f,0.5f,//1
		-0.5f,-0.5f,0.5f,//5
		0.5f,-0.5f,0.5f,//6
		0.5f,0.5f,0.5f,//2		1,5,6,1,6,2
		
		//后面
		
		-0.5f,0.5f,-0.5f,//0
		-0.5f,-0.5f,-0.5f,//4
		0.5f,-0.5f,-0.5f,//7
		0.5f,0.5f,-0.5f,//3		0,4,7,0,7,3





	};




	short drawOrder[] = {  
			0,1,2,0,2,1,
			4,5,6,4,6,7,
			0,4,5,0,5,1,
			3,7,6,3,7,2,
			1,5,6,1,6,2,
			0,4,7,0,7,3}; // 顶点的绘制顺序


	float color[] = { 0.5f, 0.5f, 0.5f, 1.0f};


	String vertexShaderCode =
			"attribute vec4 vPosition;" +
					"uniform mat4 uMVPMatrix; "+//总变换矩阵
					"void main() {" +
					"  gl_Position = vPosition;" +
					"}";

	String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
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


	public void draw(float []mRotationMatrix) {
		// 将program加入OpenGL ES环境中
		GLES20.glUseProgram(mProgram);


		// 获取指向vertexshader的成员vPosition的 handle
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// 启用一个指向三角形的顶点数组的handle
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// 准备三角形的坐标数据
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				0, vertexBuffer);

		// 获取指向fragment shader的成员vColor的handle 
		int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// 设置三角形的颜色
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		 //获取程序中总变换矩阵引用id
		 muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
		 Matrix.setRotateM(mVMatrix, 0, 0, 0, 1, 0);
		 Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mRotationMatrix, 0); 
		 GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,mMVPMatrix,0);

		// 画三角形
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);

		// 禁用指向三角形的顶点数组
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
