#version 330 core

layout (location=0) in vec3 aPos;
layout (location=0) in vec4 aColor;
layout (location=1) in vec2 texCord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec4 fColor;
out float structureValue;
out vec2 passTextureCords;

void main() {
    vec4 worldPosition = transformationMatrix
    * vec4(aPos.x, aPos.y, aPos.z, 1.0);

    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;

//    structureValue = fract(sin(dot(vec2(12,12), vec2(12.9898, 78.233)) * 43758.5453));

    structureValue = fract(sin(dot(aPos.xy*vec2(100,100), vec2(12.9898, 78.233))) * 43758.5453);

    passTextureCords=texCord;
}
