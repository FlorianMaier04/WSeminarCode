#version 330 core

in float structureValue;

out vec4 color;

uniform vec3 starColor;

void main(){
    color = vec4(starColor * max(structureValue, 0.45) / vec3(255, 255, 255) * vec3(1.5,1.5,1.5), 1);

}