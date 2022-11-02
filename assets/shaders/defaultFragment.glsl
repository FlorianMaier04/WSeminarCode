#version 330 core


in vec4 fColor;
in float structureValue;
in vec2 passTextureCords;

out vec4 color;

uniform float brightness;
uniform vec3 starColor;
uniform sampler2D modelTexture;
uniform bool useTexture;

void main(){
    color = vec4(starColor * max(structureValue, 0.45) / vec3(255, 255, 255) * vec3(1.5,1.5,1.5), 1);

}