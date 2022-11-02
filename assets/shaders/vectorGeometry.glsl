#version 330 core

layout (lines) in;
layout (line_strip, max_vertices = 4) out;

uniform mat4 transformationMatrix;
uniform mat4 targetTransformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    vec4 standardVec = vec4(0, 0, 0, 1.0);

    vec4 positionRelativeToCam = viewMatrix * transformationMatrix * standardVec;
    gl_Position = projectionMatrix * positionRelativeToCam;
    EmitVertex();

    positionRelativeToCam = viewMatrix * targetTransformationMatrix * standardVec;
    gl_Position = projectionMatrix * positionRelativeToCam;
    EmitVertex();

//    positionRelativeToCam = viewMatrix * targetTransformationMatrix * vec4(1, 1, 1, 0);
//    gl_Position = projectionMatrix * positionRelativeToCam;
//    EmitVertex();

    EndPrimitive();



//    gl_Position = position[0] + vec4(10, 10, 10, 0);
//    EmitVertex();
//
//    gl_Position = position[0];
//    EmitVertex();
//
//    gl_Position = position[1];
//    EmitVertex();


}


