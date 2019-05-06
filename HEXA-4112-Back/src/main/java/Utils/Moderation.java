/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Model.Demand;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author X
 */
public class Moderation {
    
    public static String checkObsceneWords(Demand demand) {
        String ligne = "";
        try{
            InputStream flux = new FileInputStream("./src/fichierTexte/grosOrdones.txt"); 
            InputStreamReader lecture=new InputStreamReader(flux);
            BufferedReader buff = new BufferedReader(lecture);
            
            String description = demand.getDescription().toLowerCase();
            String nameObject = demand.getNameObject().toLowerCase();
            boolean findWordObscene = false;
            while ((ligne=buff.readLine())!=null && !findWordObscene){
                    findWordObscene = nameObject.contains(ligne.toLowerCase()) || description.contains(ligne.toLowerCase());
            }
            buff.close(); 
        } catch (Exception e){
            System.out.println(e.toString());
        }
        return ligne;
    }
}
