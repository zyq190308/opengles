package com.example.triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Triangle {


	private FloatBuffer vertexBuffer;
	int mProgram;
	// 数组中每个顶点的坐标数
	static final int COORDS_PER_VERTEX = 3;
	static float triangleCoords[] = { // 按逆时针方向顺序:
		0.0f,  0.622008459f, 0.0f ,  // top
        -0.5f, -0.311004243f, 0.0f,  // bottom left
         0.5f, -0.311004243f, 0.0f    // bottom right
	};

	// 设置颜色，分别为red, green, blue 和alpha (opacity)
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };


	String vertexShaderCode =
			"attribute vec4 vPosition;" +
					"void main() {" +
					"  gl_Position = vPosition;" +
					"}";

	String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
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

		// 画三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

		// 禁用指向三角形的顶点数组
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}



}
