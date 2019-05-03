/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Utils;

import Services.Services;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
        
    public static void main(String[] args){
        Task t1 = new Task("delete_token");
        //Task te2=new TimerExample("Task2");
        Timer t=new Timer();
        t.scheduleAtFixedRate(t1, 0, 2*1000);
        //t.scheduleAtFixedRate(te2, 0,1000);
    }
}

class Task extends TimerTask{
    private String job;
    public Task(String job){
        this.job = job;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + job + " the task has executed successfully " + new Date());
        if("delete_token".equalsIgnoreCase(job)){
            Services s = new Services();
            s.deleteOldTokens(1*60*1000L);
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            
        }
    }
}