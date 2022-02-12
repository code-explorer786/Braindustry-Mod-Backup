#define HIGHP
uniform sampler2D u_texture;
varying vec2 v_texCoords;
varying lowp vec4 v_color;
uniform vec2 iResolution, u_grow, u_bulletPosition, u_vecRot, u_bulletScreenPosition;
uniform vec2 u_cameraPosition;
uniform float u_time, u_length, u_scl, u_bulletRotation;
uniform vec3 u_offset;

//const float PI=3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450281027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989;

vec4 fromHsv(float hue, float saturation, float value){
    vec4 color=vec4(0.);
    float x = mod((hue / 60.0 + 6.), 6.);
    int i = int(x);
    float f = x - i;
    float p = value * (1. - saturation);
    float q = value * (1. - saturation * f);
    float t = value * (1. - saturation * (1. - f));
    if (i==0){
        color.r = value;
        color.g = t;
        color.b = p;
    } else if (i==1){
        color.r = q;
        color.g = value;
        color.b = p;
    } else if (i==2){
        color.r = p;
        color.g = value;
        color.b = t;
    } else if (i==3){
        color.r = p;
        color.g = q;
        color.b = value;
    } else if (i==4){
        color.r = t;
        color.g = p;
        color.b = value;
    } else if (i==5){
        color.r = value;
        color.g = p;
        color.b = q;
    }


    return normalize(color);
}
float dst(vec2  to, vec2 from){
    return distance(to, from);
}
bool isIn(vec2 to, vec2 i, float offset){
    if (to.x>i.x-offset && to.x<i.x+offset){

        if (to.y>i.y-offset && to.y<i.y+offset)return true;
    }
    return false;
}
vec2 rotVec2(vec2 vec, float deg){
    float rad=radians(deg);
    float nx=vec.x*cos(rad)-vec.y*sin(rad);
    float ny=vec.x*sin(rad)+vec.y*cos(rad);
    vec.x=nx;
    vec.y=ny;
    return vec;
}
vec4 getPix(vec2 fragCoord){
    vec4 color = texture2D(u_texture, v_texCoords);
    if (color.a==0.)return vec4(0);
    /* if (isIn(fragCoord,u_bulletScreenPosition.xy,10.)){
         return vec4(1.);
     }
    if (true) return vec4(0);*/
    float time=(u_time);
    vec2 pixel=fragCoord/u_scl+u_cameraPosition;
    pixel-=u_bulletPosition;
    pixel= rotVec2(pixel,-u_bulletRotation);
//    pixel-=(iResolution/2.0)/u_scl;
    pixel+=u_bulletPosition;
//    vec3 col = 0.50+0.50*cos(u_time+disVec.xyx+u_offset);
    vec4 col=fromHsv(pixel.x-u_time ,1.,1.);
//col=vec4(u_bulletRotation/360.);

    // Output to screen
    return vec4(col.rgb, color.a);
}
void main(){
    vec4 rainbow=vec4(getPix(vec2(gl_FragCoord.xy)));
    vec4 color = texture2D(u_texture, v_texCoords);

    gl_FragColor = vec4(rainbow.rgb*color.rgb, color.a*v_color.a);
}