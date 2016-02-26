package com.example.main.util;

/**
 * Created by jan.babel on 11/09/2015.
 */
public class Util {

    /**Convert to the readable value the name, that can be read by text to speech engine
     *
     * @param exerciseName
     * @return clear string
     */
    public static String getNormalExerciseName(String exerciseName){
        return exerciseName.replaceAll("_"," ");
    }
}
