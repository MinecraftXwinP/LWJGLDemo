package tonyduan.w1nxp.lwjgldemo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class TriangleRenderer implements Renderer {

    private int vertexBuffer;

    public static float[] vertexs = {
            0.0f,       0.5f,   0.0f,   1.0f,
            0.5f,    -0.366f,   0.0f,   1.0f,
            -0.5f,   -0.366f,   0.0f,   1.0f,
            1.0f,       0.0f,   0.0f,   1.0f,
            0.0f,       1.0f,   0.0f,   1.0f,
            0.0f,       0.0f,   1.0f,   1.0f
    };

    public TriangleRenderer() {
        vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER,vertexs,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    @Override
    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER,vertexBuffer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0,4,GL_FLOAT,false,0,0);
        glVertexAttribPointer(1,4,GL_FLOAT,false,0,48);
        glDrawArrays(GL_TRIANGLES,0,3);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }
}
