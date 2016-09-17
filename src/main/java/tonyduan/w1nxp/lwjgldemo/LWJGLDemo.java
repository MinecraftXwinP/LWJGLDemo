package tonyduan.w1nxp.lwjgldemo;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LWJGLDemo {

    // The window handle
    private long window;
    private Renderer renderer;

    public void run() {
        System.out.println("LWJGLDemo with LWJGL " + Version.getVersion() + "!");

        try {
            init();
            loop();

            // Free the window callbacks and destroy the window
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        int WIDTH = 300;
        int HEIGHT = 300;

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        });
        glfwSetWindowSizeCallback(window,(window,width,height) -> glViewport(0,0,width,height));

        glfwSetWindowCloseCallback(window, (w) -> glfwSetWindowShouldClose(w,true));

        adjustWindowPos(WIDTH,HEIGHT);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);



        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // shader program
        initProgram();

        // init renderer
        renderer = new TriangleRenderer();

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {


        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    private void adjustWindowPos(int width,int height) {
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
    }

    private Integer compileShader(int shaderType,String path) {
        Integer shader;
        CharBuffer buffer = null;
        try (InputStream in = getClass().getResourceAsStream(path);InputStreamReader sourceIn  = new InputStreamReader(in)) {
            buffer = CharBuffer.allocate(in.available());
            sourceIn.read(buffer);
            sourceIn.close();
            buffer.flip();
            shader = glCreateShader(shaderType);
            glShaderSource(shader,buffer);
            glCompileShader(shader);
            if (glGetShaderi(shader,GL_COMPILE_STATUS) != GL_TRUE) {
                // compile error
                System.err.println("Shader compile error");
                System.err.println(glGetShaderInfoLog(shader));
                return null;
            }
        } catch (IOException  e) {
            e.printStackTrace();
            System.err.println("Cannot load vertex shader from : " + path);
            if (buffer != null)
                System.err.println("Input shader code is :\n" + buffer);
            return null;
        }
        return shader;
    }

    private void initProgram() {
        // vertex shader
        Integer vshader = compileShader(GL_VERTEX_SHADER,"/shaders/vshader.vsh");
        Integer fshader = compileShader(GL_FRAGMENT_SHADER,"/shaders/fshader.fsh");
        int program;
        if (vshader != null || fshader != null) {
            program = glCreateProgram();
            if (vshader != null) {
                glAttachShader(program, vshader);
                glDeleteShader(vshader);
            }
            if (fshader != null) {
                glAttachShader(program, fshader);
                glDeleteShader(fshader);
            }
            glLinkProgram(program);
            if(glGetProgrami(program,GL_LINK_STATUS) == GL_TRUE)
                glUseProgram(program);
            else {
                System.err.println("Failed to compile program: \n" + glGetProgramInfoLog(program));
            }
        } else {
            System.out.println("No shader compiled, stop creating program");
        }
    }

    public void draw() {
        glColor3f(1,1,1);
        renderer.render();
    }

    public static void main(String[] args) {
        new LWJGLDemo().run();
    }

}