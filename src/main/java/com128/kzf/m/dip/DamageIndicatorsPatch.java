package com128.kzf.m.dip;

import net.minecraft.launchwrapper.ITweaker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class DamageIndicatorsPatch implements ITweaker {

    public static final String MODID  = "dip";

    public DamageIndicatorsPatch() {
        System.out.println("[DIP] Adding <init>(Minecraft p_i46445_1_, int field_71443_c, int field_71440_d) to avr | ScaledResolution.");
    }

    public List<String> arguments = new ArrayList<>();

    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        return;
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer(DamageIndicatorsPatchTransformer.class.getName());
    }

    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @SuppressWarnings("unchecked")
    @Override
    public String[] getLaunchArguments() {
        List<String> list = new ArrayList<String>();
        List<String> args = (List<String>) Launch.blackboard.get("ArgumentList");
        if(args == null)
            args = new ArrayList<String>();
        for(int i = 0; i < this.arguments.size(); i+=2){
            String s = this.arguments.get(i);
            if(s.startsWith("--")){
                if(!args.contains(s)){
                    list.add(s);
                    list.add(this.arguments.get(i+1));
                }
            }else{
                i--;
            }
        }
        return list.toArray(new String[0]);
    }
}
