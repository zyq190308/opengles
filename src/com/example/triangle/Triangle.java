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
	// ������ÿ�������������
	static final int COORDS_PER_VERTEX = 3;
	static float triangleCoords[] = { // ����ʱ�뷽��˳��:
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
	// ������ɫ���ֱ�Ϊred, green, blue ��alpha (opacity)
	float []color = {1.0f, 0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,  
			0.0f, 0.0f, 1.0f, 1.0f};


	String vertexShaderCode =
					"attribute vec3 vPosition;" +
					"attribute vec4 aColor;"+
					"varying  vec4 vColor;"+
					"uniform mat4 mVMatrix; "+//�ܱ任����
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


		ByteBuffer cbb = ByteBuffer.allocateDirect(
				// (������ * 4)floatռ���ֽ�
				color.length * 4);
		// �����豸�ı����ֽ���
		cbb.order(ByteOrder.nativeOrder());

		// ��ByteBuffer����һ�����㻺��
		colorBuffer = cbb.asFloatBuffer();
		// �������Ǽ���FloatBuffer��
		colorBuffer.put(color);
		// ����buffer���ӵ�һ�����꿪ʼ��
		colorBuffer.position(0);



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


		num+=5;
		// ��program����OpenGL ES������
		GLES20.glUseProgram(mProgram);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		// ��ȡָ��fragment shader�ĳ�ԱvColor��handle 
		int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);  

		// ׼�������ε���������
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
	
		
		// ��������
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

		// ����ָ�������εĶ�������
		//GLES20.glDisableVertexAttribArray(mPositionHandle);
	}



}
