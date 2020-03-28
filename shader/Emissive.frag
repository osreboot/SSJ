uniform sampler2D texture1;

void main(){
	float range = 8.0;
	
	vec4 sum = vec4(0.0, 0.0, 0.0, 0.0);
	
	float xStep = 1.0/1280.0;
	float yStep = 1.0/720.0;
	for(float displacement = -range; displacement <= range; displacement += 1.0){
		sum += texture2D(texture1, vec2(gl_TexCoord[0].x + (displacement * xStep), gl_TexCoord[0].y + (displacement * yStep))) * (range - abs(displacement));
		sum += texture2D(texture1, vec2(gl_TexCoord[0].x - (displacement * xStep), gl_TexCoord[0].y + (displacement * yStep))) * (range - abs(displacement));
		sum += texture2D(texture1, vec2(gl_TexCoord[0].x + (displacement * xStep), gl_TexCoord[0].y - (displacement * yStep))) * (range - abs(displacement));
		sum += texture2D(texture1, vec2(gl_TexCoord[0].x - (displacement * xStep), gl_TexCoord[0].y - (displacement * yStep))) * (range - abs(displacement));
	}

	gl_FragColor = vec4(sum.r / 4.0, sum.g / 4.0, sum.b / 4.0, sum.a / 96.0);
}
