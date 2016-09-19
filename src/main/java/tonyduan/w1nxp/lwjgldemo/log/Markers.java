package tonyduan.w1nxp.lwjgldemo.log;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Markers {
    public final static Marker PROGRAM_COMPILATION = MarkerManager.getMarker("Program Compilation");
    private final static Marker SHADER_LINKING_MARKER = MarkerManager.getMarker("Shader Linking").setParents(PROGRAM_COMPILATION);
    public final static Marker SHADER_LINKING_MARKER_VERTEX = MarkerManager.getMarker("Vertex Shader Linking").setParents(SHADER_LINKING_MARKER);
    public final static Marker SHADER_LINKING_MARKER_FRAGMENT = MarkerManager.getMarker("Fragment Shader Linking").setParents(SHADER_LINKING_MARKER);
}
