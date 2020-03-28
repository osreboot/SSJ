uniform sampler2D texture1;

void main(){
	float range = 1.5;
	
	float total = 0.0;
	vec4 sum = vec4(0.0, 0.0, 0.0, 0.0);
	
	float xStep = 0.5/1280.0;
	float yStep = 0.5/720.0;
	for(float displacementX = -range; displacementX <= range; displacementX += 1.0){
		for(float displacementY = -range; displacementY <= range; displacementY += 1.0){
			sum += texture2D(texture1, vec2(gl_TexCoord[0].x + (displacementX * xStep), gl_TexCoord[0].y + (displacementY * yStep)));
		}
	}
	
	sum /= 4.0;

	gl_FragColor = vec4(sum.r, sum.g, sum.b, sum.a);
}
