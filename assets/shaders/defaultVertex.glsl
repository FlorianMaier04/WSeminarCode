#version 330 core

layout (location=0) in vec3 aPos;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out float structureValue;

void main() {
    vec4 worldPosition = transformationMatrix
    * vec4(aPos.x, aPos.y, aPos.z, 1.0);

    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;

    //wird verwendet, um den Planeten Farbunterschiede in ihrer Kugelform zu geben (zur besseren Erkennung)
    structureValue = fract(sin(dot(aPos.xy*vec2(100,100), vec2(12.9898, 78.233))) * 43758.5453);
}
