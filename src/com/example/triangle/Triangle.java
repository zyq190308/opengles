package com.example.triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Triangle {


	private FloatBuffer vertexBuffer;
	int mProgram;
	// ������ÿ�������������
	static final int COORDS_PER_VERTEX = 3;
	static float triangleCoords[] = { // ����ʱ�뷽��˳��:
		0.0f,  0.622008459f, 0.0f ,  // top
        -0.5f, -0.311004243f, 0.0f,  // bottom left
         0.5f, -0.311004243f, 0.0f    // bottom right
	};

	// ������ɫ���ֱ�Ϊred, green, blue ��alpha (opacity)
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

		// Ϊ�����״�����꣬��ʼ�������ֽڻ���
		ByteBuffer bb = ByteBuffer.allocateDirect(
				// (������ * 4)floatռ���ֽ�
				triangleCoords.length * 4);
		// �����豸�ı����ֽ���
		bb.order(ByteOrder.nativeOrder());

		// ��ByteBuffer����һ�����㻺��
		vertexBuffer = bb.asFloatBuffer();
		// �������Ǽ���FloatBuffer��
		vertexBuffer.put(triangleCoords);
		// ����buffer���ӵ�һ�����꿪ʼ��
		vertexBuffer.position(0);

		//��һ��������������ɫ��vertexShader
		int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		//�ڶ���,���ض�����ɫ��glsl����
		GLES20.glShaderSource(vertexShader, vertexShaderCode);
		GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
		//������,������ɫ��
		GLES20.glCompileShader(vertexShader);
		GLES20.glCompileShader(fragmentShader);

		//���Ĳ�, ����һ���յ�OpenGL ES Program
		mProgram = GLES20.glCreateProgram();  
		//���岽
		GLES20.glAttachShader(mProgram, vertexShader);   // ��vertex shader��ӵ�program
		GLES20.glAttachShader(mProgram, fragmentShader); // ��fragment shader��ӵ�program
		//�������� ������ִ�е� OpenGL ES program
		GLES20.glLinkProgram(mProgram);                  


	}

	public void draw() {
		// ��program����OpenGL ES������
		GLES20.glUseProgram(mProgram);

		// ��ȡָ��vertexshader�ĳ�ԱvPosition�� handle
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// ����һ��ָ�������εĶ��������handle
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// ׼�������ε���������
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				0, vertexBuffer);

		// ��ȡָ��fragment shader�ĳ�ԱvColor��handle 
		int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// ���������ε���ɫ
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		// ��������
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

		// ����ָ�������εĶ�������
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}



}
