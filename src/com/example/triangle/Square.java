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
	int muMVPMatrixHandle;//�ܱ任��������
	int mProgram;
	int radio;
	float []mMVPMatrix;
	float[] mVMatrix = new float[16];
	static int one = 0x10000; 
	// ÿ�������������
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { 
		//����
		-0.5f,0.5f,-0.5f,//0
		-0.5f,0.5f,0.5f,//1
		0.5f,0.5f,0.5f,//2
		0.5f,0.5f,-0.5f,//3   0,1,2,0,2,1

		//����
		-0.5f,-0.5f,-0.5f,//4
		-0.5f,-0.5f,0.5f,//5
		0.5f,-0.5f,0.5f,//6
		0.5f,-0.5f,-0.5f,//7  4,5,6,4,6,7

		//����
		-0.5f,0.5f,-0.5f, //0
		-0.5f,-0.5f,-0.5f,//4
		
		-0.5f,-0.5f,0.5f,//5  
		-0.5f,0.5f,0.5f,//1   0,4,5,0,5,1
		

		//����
		0.5f,0.5f,-0.5f,//3
		0.5f,-0.5f,-0.5f,//7
		
		0.5f,-0.5f,0.5f,//6
		0.5f,0.5f,0.5f,//2 	3,7,6,3,7,2

		
		    

		//ǰ��
		-0.5f,0.5f,0.5f,//1
		-0.5f,-0.5f,0.5f,//5
		0.5f,-0.5f,0.5f,//6
		0.5f,0.5f,0.5f,//2		1,5,6,1,6,2
		
		//����
		
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
			0,4,7,0,7,3}; // ����Ļ���˳��


	float color[] = { 0.5f, 0.5f, 0.5f, 1.0f};


	String vertexShaderCode =
			"attribute vec4 vPosition;" +
					"uniform mat4 uMVPMatrix; "+//�ܱ任����
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
				// (������ * 4)
				squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		// Ϊ�����б��ʼ���ֽڻ���
		// (��Ӧ˳��������� * 2)short��2�ֽ�
		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);



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


	public void draw(float []mRotationMatrix) {
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
		 //��ȡ�������ܱ任��������id
		 muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
		 Matrix.setRotateM(mVMatrix, 0, 0, 0, 1, 0);
		 Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mRotationMatrix, 0); 
		 GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,mMVPMatrix,0);

		// ��������
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);

		// ����ָ�������εĶ�������
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
