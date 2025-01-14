package Laevis;

import imgui.ImGui;
import imgui.type.ImFloat;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_Counter=0;
    private int U_ID=-1;// unique id
    public transient GameObject GameObject = null;

    public void StartComponent() {

    }

    public abstract void UpdateComponent(float DeltaTime);

    public void imgui(){
        try {
            Field[] fields=this.getClass().getDeclaredFields();
            for(Field field :fields){

                boolean isTransient=Modifier.isTransient(field.getModifiers());
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isTransient) continue;
                if(isPrivate){
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object value=field.get(this);
                String name=field.getName();

                if(type==int.class){
                    int val=(int)value;
                    int[]imInt={val};
                    if(ImGui.dragInt(name+": ",imInt)){
                        field.set(this,imInt[0]);
                    }
                } else if (type==float.class) {
                    float val=(float) value;
                    float[]imFloat={val};
                    if(ImGui.dragFloat(name+": ",imFloat)) {
                        field.set(this, imFloat[0]);
                     }
                } else if(type==boolean.class){
                    boolean val=(boolean) value;
                    if(ImGui.checkbox(name+": ",val)){
                        val=!val;
                        field.set(this,val);
                    }
                } else if (type== Vector3f.class) {
                    Vector3f val=(Vector3f) value;
                    float[]imVec={val.x,val.y,val.z};
                    if(ImGui.dragFloat3(name+": ",imVec)){
                        val.set(imVec[0],imVec[1],imVec[2]);
                    }
                } else if (type== Vector4f.class) {
                    Vector4f val=(Vector4f) value;
                    float[]imVec={val.x,val.y,val.z,val.w};
                    if(ImGui.dragFloat4(name+": ",imVec)){
                        val.set(imVec[0],imVec[1],imVec[2],imVec[4]);
                    }
                }


                if(isPrivate){
                    field.setAccessible(false);
                }

            }

        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
    public void generateID(){
        if(this.U_ID == -1){
            this.U_ID=ID_Counter++;

        }
    }

    public int getU_ID(){
        return this.U_ID;
    }
    public static void init(int maxID){
        ID_Counter=maxID;
    }
}
